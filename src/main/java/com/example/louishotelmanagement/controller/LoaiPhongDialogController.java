package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoaiPhongDialogController implements Initializable {

    @FXML
    private Label lblTieuDe;
    @FXML
    private VBox formContainer;
    @FXML
    private Button btnLuu;

    private LoaiPhongDAO loaiPhongDAO;
    private String mode = "ADD"; // ADD hoặc EDIT

    // FormsFX form fields
    private StringField maLoaiPhongField;
    private StringField tenLoaiField;
    private DoubleField donGiaField;
    private StringField moTaField;

    // Form model
    private Form form;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loaiPhongDAO = new LoaiPhongDAO();

            // Initialize FormsFX form fields
            initializeFormFx("", "", 0.0, "");

            // Create form
            form = Form.of(
                    Group.of(maLoaiPhongField,
                            tenLoaiField,
                            donGiaField,
                            moTaField
                    )
            ).title("Thông tin loại phòng");

            // Render form and add to container
            FormRenderer formRenderer = new FormRenderer(form);
            formContainer.getChildren().add(formRenderer);

        } catch (Exception e) {
            hienThiThongBao("Lỗi", "Không thể khởi tạo form: " + e.getMessage());
        }
    }

    public void initializeFormFx(String maLoaiPhong, String tenLoai, Double donGia, String moTa) {
        // Initialize FormsFX form fields
        maLoaiPhongField = Field.ofStringType(maLoaiPhong)
                .label("Mã Loại Phòng")
                .placeholder("VD: LP001")
                .validate(
                        StringLengthValidator.atLeast(3, "Mã loại phòng phải có ít nhất 3 ký tự"),
                        RegexValidator.forPattern("^LP\\d+$", "Mã loại phòng phải theo định dạng 'LPxxx', trong đó 'x' là chữ số")
                )
                .required("Mã loại phòng không được để trống");

        tenLoaiField = Field.ofStringType(tenLoai)
                .label("Tên Loại Phòng")
                .placeholder("VD: Phòng Standard")
                .validate(
                        StringLengthValidator.atLeast(2, "Tên loại phòng phải có ít nhất 2 ký tự")
                )
                .required("Tên loại phòng không được để trống");

        donGiaField = Field.ofDoubleType(donGia)
                .label("Đơn Giá (VNĐ)")
                .placeholder("VD: 1000000")
                .validate(
                        com.dlsc.formsfx.model.validators.DoubleRangeValidator.atLeast(0, "Đơn giá phải lớn hơn hoặc bằng 0")
                )
                .required("Đơn giá không được để trống");

        moTaField = Field.ofStringType(moTa)
                .label("Mô tả")
                .placeholder("Nhập mô tả loại phòng...");
    }

    public void setMode(String mode) {
        this.mode = mode;
        if ("EDIT".equals(mode)) {
            lblTieuDe.setText("Sửa Thông Tin Loại Phòng");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Loại Phòng Mới");
            btnLuu.setText("Lưu");
        }
    }

    public void setLoaiPhong(LoaiPhong loaiPhong) {
        if (loaiPhong != null) {
            initializeFormFx(
                loaiPhong.getMaLoaiPhong(),
                loaiPhong.getTenLoai(),
                loaiPhong.getDonGia(),
                loaiPhong.getMoTa()
            );

            form = Form.of(
                    Group.of(maLoaiPhongField,
                            tenLoaiField,
                            donGiaField,
                            moTaField
                    )
            ).title("Thông tin loại phòng");
            
            // Không cho sửa mã loại phòng khi edit
            maLoaiPhongField.editable(false);

            formContainer.getChildren().clear();
            FormRenderer formRenderer = new FormRenderer(form);
            formContainer.getChildren().add(formRenderer);
        }
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin loại phòng!");
            return;
        }
        try {
            LoaiPhong loaiPhong = new LoaiPhong();
            loaiPhong.setMaLoaiPhong(maLoaiPhongField.valueProperty().get().trim());
            loaiPhong.setTenLoai(tenLoaiField.valueProperty().get().trim());
            loaiPhong.setDonGia(donGiaField.valueProperty().get());
            loaiPhong.setMoTa(moTaField.valueProperty().get().trim());

            System.out.println("Xử lý lưu loại phòng: " + loaiPhong);

            boolean thanhCong = false;

            if ("ADD".equals(mode)) {
                thanhCong = loaiPhongDAO.themLoaiPhong(loaiPhong);
                if (thanhCong) {
                    hienThiThongBao("Thành công", "Đã thêm loại phòng thành công!");
                } else {
                    hienThiThongBao("Lỗi", "Không thể thêm loại phòng! Có thể mã loại phòng đã tồn tại.");
                }
            } else if ("EDIT".equals(mode)) {
                thanhCong = loaiPhongDAO.capNhatLoaiPhong(loaiPhong);
                if (thanhCong) {
                    hienThiThongBao("Thành công", "Đã cập nhật loại phòng thành công!");
                } else {
                    hienThiThongBao("Lỗi", "Không thể cập nhật loại phòng!");
                }
            }

            if (thanhCong) {
                dongDialog();
            }

        } catch (SQLException e) {
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
