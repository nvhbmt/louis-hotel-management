package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class PhongDialogController implements Initializable {

    @FXML
    private Label lblTieuDe;
    @FXML
    private VBox formContainer;
    @FXML
    private Button btnHuy;
    @FXML
    private Button btnLuu;

    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private String mode = "ADD"; // ADD hoặc EDIT

    // FormsFX form fields
    private StringField maPhongField;
    private IntegerField tangField;
    private SingleSelectionField<TrangThaiPhong> trangThaiField;
    private SingleSelectionField<LoaiPhong> loaiPhongField;
    private StringField moTaField;

    // Form model
    private Form form;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            phongDAO = new PhongDAO();
            loaiPhongDAO = new LoaiPhongDAO();
            List<LoaiPhong> dsLoaiPhong = loaiPhongDAO.layDSLoaiPhong();


            // Initialize FormsFX form fields
            initializeFormFx("", 1, null, dsLoaiPhong, "");

            // Create form
            form = Form.of(
                    Group.of(maPhongField,
                            tangField,
                            trangThaiField,
                            loaiPhongField,
                            moTaField
                    )
            ).title("Thông tin phòng");

            // Render form and add to container
            FormRenderer formRenderer = new FormRenderer(form);
            formContainer.getChildren().add(formRenderer);

        } catch (SQLException e) {
            hienThiThongBao("Lỗi", "Không thể tải danh sách loại phòng: " + e.getMessage());
        }
    }

    public void initializeFormFx(String maPhong, Integer tang, TrangThaiPhong trangThai, List<LoaiPhong> dsLoaiPhong, String moTa) {
        // Initialize FormsFX form fields
        maPhongField = Field.ofStringType(maPhong)
                .label("Mã Phòng")
                .placeholder("VD: P101")
                .validate(
                        StringLengthValidator.exactly(4, "Mã phòng phải đúng 4 ký tự"),
                        RegexValidator.forPattern("^P\\d{3}$", "Mã phòng phải theo định dạng 'Pxxx', trong đó 'x' là chữ số")
                )
                .required("Mã phòng không được để trống");

        tangField = Field.ofIntegerType(tang)
                .label("Tầng")
                .required("Tầng không được để trống");

        trangThaiField = Field.ofSingleSelectionType(List.of(TrangThaiPhong.TRONG, TrangThaiPhong.DA_DAT, TrangThaiPhong.DANG_SU_DUNG, TrangThaiPhong.BAO_TRI))
                .label("Trạng thái")
                .required("Vui lòng chọn trạng thái phòng");

        loaiPhongField = Field.ofSingleSelectionType(dsLoaiPhong)
                .label("Loại phòng")
                .required("Vui lòng chọn loại phòng");

        moTaField = Field.ofStringType(moTa)
                .label("Mô tả")
                .placeholder("Nhập mô tả phòng...");
    }

    public void setMode(String mode) {
        this.mode = mode;
        if ("EDIT".equals(mode)) {
            lblTieuDe.setText("Sửa Thông Tin Phòng");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Phòng Mới");
            btnLuu.setText("Lưu");
        }
    }

    public void setPhong(Phong phong) {
        if (phong != null) {
            maPhongField.valueProperty().set(phong.getMaPhong());
            maPhongField.editable(false); // Không cho sửa mã phòng khi edit

            if (phong.getTang() != null) {
                tangField.valueProperty().set(phong.getTang());
            }

            trangThaiField.selectionProperty().set(phong.getTrangThai());
            moTaField.valueProperty().set(phong.getMoTa());
            loaiPhongField.selectionProperty().set(phong.getLoaiPhong()); // Clear selection first
            // Tải lại dữ liệu loại phòng để đảm bảo danh sách cập nhật
            taiDuLieuLoaiPhong();
        }
    }

    private void taiDuLieuLoaiPhong() {
        try {
            List<LoaiPhong> danhSachLoaiPhong = loaiPhongDAO.layDSLoaiPhong();

            // Convert to string list for display
            List<String> loaiPhongStrings = danhSachLoaiPhong.stream()
                    .map(lp -> lp.getTenLoai() + " (" + lp.getMaLoaiPhong() + ")")
                    .toList();

            // Lưu lại giá trị hiện tại để set lại sau
            LoaiPhong currentLoaiPhongValue = loaiPhongField.getSelection();
            TrangThaiPhong currentTrangThaiValue = trangThaiField.getSelection();

            // Create new field instances to avoid "Cannot change a control's field once set" error
            initializeFormFx(
                    maPhongField.valueProperty().get(),
                    tangField.valueProperty().get(),
                    trangThaiField.getSelection(),
                    danhSachLoaiPhong,
                    moTaField.valueProperty().get()
            );

            // Recreate form with new field instances
            form = Form.of(
                    Group.of(
                            maPhongField,
                            tangField,
                            trangThaiField,
                            loaiPhongField,
                            moTaField)

            ).title("Thông tin phòng");

            // Clear and re-render form
            formContainer.getChildren().clear();
            FormRenderer formRenderer = new FormRenderer(form);
            formContainer.getChildren().add(formRenderer);

            // Set lại giá trị loại phòng và trạng thái nếu có
            if (currentLoaiPhongValue != null) {
                loaiPhongField.selectionProperty().set(currentLoaiPhongValue);
            }
            if (currentTrangThaiValue != null && !currentTrangThaiValue.toString().trim().isEmpty()) {
                trangThaiField.selectionProperty().set(currentTrangThaiValue);
            }

        } catch (SQLException e) {
            hienThiThongBao("Lỗi", "Không thể tải danh sách loại phòng: " + e.getMessage());
        }
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin phòng!");
           return;
        }
        try {
            Phong phong = new Phong();
            phong.setMaPhong(maPhongField.valueProperty().get().trim());
            phong.setTang(tangField.valueProperty().get());
            phong.setTrangThai(trangThaiField.getSelection());
            phong.setMoTa(moTaField.valueProperty().get().trim());

            // Extract maLoaiPhong from the selected string
            LoaiPhong loaiPhong = loaiPhongField.getSelection();

            if (loaiPhong != null) {
                phong.setLoaiPhong(loaiPhong);
            }

            System.out.println("Xử lý lưu phòng: " + phong);

            boolean thanhCong = false;

            if ("ADD".equals(mode)) {
                thanhCong = phongDAO.themPhong(phong);
                if (thanhCong) {
                    hienThiThongBao("Thành công", "Đã thêm phòng thành công!");
                } else {
                    hienThiThongBao("Lỗi", "Không thể thêm phòng! Có thể mã phòng đã tồn tại.");
                }
            } else if ("EDIT".equals(mode)) {
                thanhCong = phongDAO.capNhatPhong(phong);
                if (thanhCong) {
                    hienThiThongBao("Thành công", "Đã cập nhật phòng thành công!");
                } else {
                    hienThiThongBao("Lỗi", "Không thể cập nhật phòng!");
                }
            }

            if (thanhCong) {
                dongDialog();
            }

        } catch (SQLException e) {
            System.out.println(e);
            hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());

        }
    }

    @FXML
    private void handleHuy() {
        dongDialog();
    }


    private void dongDialog() {
        Stage stage = (Stage) btnLuu.getScene().getWindow();
        stage.close();
    }

    private void hienThiThongBao(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }
}
