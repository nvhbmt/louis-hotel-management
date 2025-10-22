package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private String mode = "ADD";

    // Form fields
    private StringField maLoaiPhongField;
    private StringField tenLoaiField;
    private DoubleField donGiaField;
    private StringField moTaField;
    private Form form;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            khoiTaoDAO();
            String maLoaiPhongTiepTheo = layMaLoaiPhongTiepTheoNeuThemMoi();

            taoForm(maLoaiPhongTiepTheo, "", 0.0, "");
            hienThiForm();

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể khởi tạo form: " + e.getMessage());
        }
    }

    private void khoiTaoDAO() throws SQLException {
        loaiPhongDAO = new LoaiPhongDAO();
    }

    private String layMaLoaiPhongTiepTheoNeuThemMoi() {
        if (!"ADD".equals(mode)) return "";

        try {
            return loaiPhongDAO.layMaLoaiPhongTiepTheo();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể lấy mã loại phòng tiếp theo: " + e.getMessage());
            return "";
        }
    }

    private void taoForm(String maLoaiPhong, String tenLoai, Double donGia, String moTa) {
        // Tạo các field
        maLoaiPhongField = taoFieldMaLoaiPhong(maLoaiPhong);
        tenLoaiField = taoFieldTenLoai(tenLoai);
        donGiaField = taoFieldDonGia(donGia);
        moTaField = taoFieldMoTa(moTa);

        // Tạo form
        form = Form.of(Group.of(maLoaiPhongField, tenLoaiField, donGiaField, moTaField))
                .title("Thông tin loại phòng");
    }

    private StringField taoFieldMaLoaiPhong(String maLoaiPhong) {
        StringField field = Field.ofStringType(maLoaiPhong)
                .label("Mã Loại Phòng")
                .placeholder("VD: LP001")
                .validate(
                        StringLengthValidator.atLeast(3, "Mã loại phòng phải có ít nhất 3 ký tự"),
                        RegexValidator.forPattern("^LP\\d{3}$", "Mã loại phòng phải theo định dạng 'LPxxx'")
                )
                .required("Mã loại phòng không được để trống");

        if ("ADD".equals(mode)) {
            field.editable(false);
        }
        return field;
    }

    private StringField taoFieldTenLoai(String tenLoai) {
        return Field.ofStringType(tenLoai)
                .label("Tên Loại Phòng")
                .placeholder("VD: Phòng Standard")
                .validate(StringLengthValidator.atLeast(2, "Tên loại phòng phải có ít nhất 2 ký tự"))
                .required("Tên loại phòng không được để trống");
    }

    private DoubleField taoFieldDonGia(Double donGia) {
        return Field.ofDoubleType(donGia)
                .label("Đơn Giá (VNĐ)")
                .placeholder("VD: 1000000")
                .validate(com.dlsc.formsfx.model.validators.DoubleRangeValidator.atLeast(0, "Đơn giá phải lớn hơn hoặc bằng 0"))
                .required("Đơn giá không được để trống");
    }

    private StringField taoFieldMoTa(String moTa) {
        return Field.ofStringType(moTa)
                .label("Mô tả")
                .placeholder("Nhập mô tả loại phòng...");
    }

    private void hienThiForm() {
        FormRenderer formRenderer = new FormRenderer(form);
        formContainer.getChildren().add(formRenderer);
        formContainer.getStyleClass().add("formsfx-form-container");
    }

    public void setMode(String mode) {
        this.mode = mode;
        capNhatUIVaoCheDo();
    }

    private void capNhatUIVaoCheDo() {
        if ("EDIT".equals(mode)) {
            lblTieuDe.setText("Sửa Thông Tin Loại Phòng");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Loại Phòng Mới");
            btnLuu.setText("Lưu");
            if (maLoaiPhongField != null) {
                maLoaiPhongField.editable(false);
            }
        }
    }

    public void setLoaiPhong(LoaiPhong loaiPhong) {
        if (loaiPhong == null) return;

        // Set values
        maLoaiPhongField.valueProperty().set(loaiPhong.getMaLoaiPhong());
        maLoaiPhongField.editable(false);
        tenLoaiField.valueProperty().set(loaiPhong.getTenLoai());
        donGiaField.valueProperty().set(loaiPhong.getDonGia());
        moTaField.valueProperty().set(loaiPhong.getMoTa());
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin loại phòng!");
            return;
        }

        try {
            LoaiPhong loaiPhong = taoLoaiPhongTuForm();
            boolean thanhCong = luuLoaiPhong(loaiPhong);

            if (thanhCong) {
                hienThiThongBaoThanhCong();
                dongDialog();
            }

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private LoaiPhong taoLoaiPhongTuForm() {
        LoaiPhong loaiPhong = new LoaiPhong();
        loaiPhong.setMaLoaiPhong(maLoaiPhongField.valueProperty().get().trim());
        loaiPhong.setTenLoai(tenLoaiField.valueProperty().get().trim());
        loaiPhong.setDonGia(donGiaField.valueProperty().get());
        loaiPhong.setMoTa(moTaField.valueProperty().get().trim());

        return loaiPhong;
    }

    private boolean luuLoaiPhong(LoaiPhong loaiPhong) throws SQLException {
        boolean thanhCong;

        if ("ADD".equals(mode)) {
            thanhCong = loaiPhongDAO.themLoaiPhong(loaiPhong);
        } else {
            thanhCong = loaiPhongDAO.capNhatLoaiPhong(loaiPhong);
        }

        return thanhCong;
    }

    private void hienThiThongBaoThanhCong() {
        String thongBao = "ADD".equals(mode) ? "Đã thêm loại phòng thành công!" : "Đã cập nhật loại phòng thành công!";
        ThongBaoUtil.hienThiThongBao("Thành công", thongBao);
    }

    @FXML
    private void handleHuy() {
        dongDialog();
    }

    private void dongDialog() {
        Stage stage = (Stage) btnLuu.getScene().getWindow();
        stage.close();
    }
}
