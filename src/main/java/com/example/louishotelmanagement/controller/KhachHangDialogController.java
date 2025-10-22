package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class KhachHangDialogController implements Initializable {

    @FXML
    private VBox formContainer;

    private KhachHangDAO khachHangDAO;
    @FXML
    private ComboBox<String> cmbHang;
    private String mode = "ADD";

    private StringField maKHField;
    private StringField hoTenField;
    private StringField soDTField;
    private StringField emailField;
    private StringField diaChiField;
    private DateField ngaySinhField;
    private SingleSelectionField<String> ghiChuField;
    private StringField CCCDField;
    private Form form;
    public Label lblTieuDe;
    public Button btnLuu;

    public void initialize(URL location, ResourceBundle resources) {
        khachHangDAO = new KhachHangDAO();
        String maKhachHangTiepTheo = layMaKhachHangNeuMoi();

        taoForm(maKhachHangTiepTheo, "", "", "", "", LocalDate.now(), "", "");
        hienThiForm();

    }

    private String layMaKhachHangNeuMoi() {
        if (!mode.equals("ADD")) {
            return "";
        }

        try {
            return "KH" + System.currentTimeMillis() % 100000;
        } catch (Exception e) {
            UIUtils.hienThiThongBao("Lỗi", e.getMessage());
            return "";
        }
    }

    private void taoForm(String maKH, String hoTen, String soDT, String email, String diaChi, LocalDate ngaySinh, String ghiChu, String CCCD) {
        maKHField = taoFeildMaKH(maKH);
        hoTenField = taoFieldHoTen(hoTen);
        soDTField = taoFieldSoDT(soDT);
        emailField = taoFieldEmail(email);
        diaChiField = taoFieldDiaChi(diaChi);
        ngaySinhField = taoFieldNgaySinh(ngaySinh);
        ghiChuField = taoFieldGhiChu(ghiChu);
        CCCDField = taoFieldCCCD(CCCD);

        form = Form.of(Group.of(maKHField, hoTenField, soDTField, emailField, diaChiField, ngaySinhField, ghiChuField, CCCDField))
                .title("Thông tin Khách Hàng");
    }

    private StringField taoFeildMaKH(String maKH) {
        StringField field = Field.ofStringType(maKH)
                .label("Mã khách hàng:")
                .placeholder("VD: KH001")
                .validate(
                        StringLengthValidator.atLeast(5,"Mã khách hàng cần phải có ít nhất 5 ký tự"),
                        RegexValidator.forPattern("KH\\d{3}", "Mã khách hàng phải có định dạnh 'KHxxx'")
                )
                .required("Mã khách hàng không được để trống");

        if (!mode.equals("ADD")) {
            field.editable(false);
        }
        return field;
    }

    private StringField taoFieldHoTen(String hoTen) {
        StringField field = Field.ofStringType(hoTen)
                .label("Họ và tên:")
                .placeholder("VD: George Floyd")
                .validate(
                        StringLengthValidator.atLeast(5, "Họ và tên khách hàng phải có ít nhất 5 ký tự"),
                        RegexValidator.forPattern("^\\p{Lu}\\p{Ll}+( \\p{Lu}\\p{Ll}*)*$", "Họ tên khách hàng chữ cái đầu mỗi từ phải viết hoa")
                )
                .required("Họ và tên khách hàng không được để trống");
        return field;
    }

    private StringField taoFieldSoDT(String soDT) {
        StringField field = Field.ofStringType(soDT)
                .label("Số điện thoại:")
                .placeholder("VD: 012345678")
                .validate(
                        StringLengthValidator.atLeast(9, "Số điện thoại phải có ít nhất 9 ký tự"),
                        RegexValidator.forPattern("[0-9]+", "Số điện thoại chỉ được nhập số")
                )
                .required("Số điện thoại không được để trống");
        return field;
    }

    private StringField taoFieldEmail(String email) {
        StringField field = Field.ofStringType(email)
                .label("E-mail:")
                .placeholder("Icantbreath@gmail.com")
                .validate(
                        RegexValidator.forPattern("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", "Vui lòng nhập đúng định dạng email")
                )
                .required("Email không được để trống");
        return field;
    }

    private StringField taoFieldDiaChi(String diaChi) {
        StringField field = Field.ofStringType(diaChi)
                .label("Địa chỉ:")
                .placeholder("VD: 123 Đường ABC, Quận 1")
                .required("Địa chỉ không được để trống");
        return field;
    }

    private DateField taoFieldNgaySinh(LocalDate ngaySinh) {
        DateField field = Field.ofDate(ngaySinh)
                .label("Ngày sinh:")
                .required("Ngày sinh không được để trống");
        return field;
    }

    private SingleSelectionField<String> taoFieldGhiChu(String ghiChu) {
        ObservableList<String> dsGhiChu = FXCollections.observableArrayList(
                        "Khách quen",
                        "Khách VIP",
                        "Khách doanh nghiệp"
                );

        SingleSelectionField<String> field = Field.ofSingleSelectionType(dsGhiChu)
                .label("Ghi chú:")
                .placeholder("Chọn hàng khách")
                .required(true);

        // Nếu ở mode sửa và có ghi chú sẵn
        if (ghiChu != null && dsGhiChu.contains(ghiChu)) {
            int index = dsGhiChu.indexOf(ghiChu);
            if (index >= 0) {
                field.select(index);
            }
        }
        return field;
    }

    private StringField taoFieldCCCD(String CCCD) {
        StringField field = Field.ofStringType(CCCD)
                .label("CCCD:")
                .placeholder("VD: 012345678901")
                .validate(
                        StringLengthValidator.atLeast(12, "CCCD phải có ít nhất 12 ký tự"),
                        RegexValidator.forPattern("[0-9]+", "CCCD chỉ được nhập số")
                )
                .required("CCCD không được để trống");
        return field;
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
        if (mode.equals("EDIT")) {
            lblTieuDe.setText("Sửa Thông Tin Khách Hàng");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Khách Hàng Mới");
            btnLuu.setText("Lưu");
        }
    }

    public void setKhachHang(KhachHang khachHang) {
        if (khachHang == null) {
            return;
        }

        formContainer.getChildren().clear();

        // Tạo lại form với dữ liệu khách hàng
        taoForm(
                khachHang.getMaKH(),
                khachHang.getHoTen(),
                khachHang.getSoDT(),
                khachHang.getEmail(),
                khachHang.getDiaChi(),
                khachHang.getNgaySinh(),
                khachHang.getGhiChu(),
                khachHang.getCCCD()
        );

        hienThiForm();
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            UIUtils.hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin khách hàng!");
            return;
        }

        try {
            KhachHang khachHang = taoKhachHangTuForm();
            boolean thanhCong = luuKhachHang(khachHang);

            if (thanhCong) {
                hienThiThongBaoThanhCong();
                dongDialog();
            }
        }catch (Exception e) {
            UIUtils.hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private KhachHang taoKhachHangTuForm() {
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKH(maKHField.valueProperty().getValue());
        khachHang.setHoTen(hoTenField.valueProperty().getValue());
        khachHang.setSoDT(soDTField.valueProperty().getValue());
        khachHang.setEmail(emailField.valueProperty().getValue());
        khachHang.setDiaChi(diaChiField.valueProperty().getValue());
        khachHang.setNgaySinh(ngaySinhField.valueProperty().getValue());
        khachHang.setGhiChu(ghiChuField.getSelection());
        khachHang.setCCCD(CCCDField.valueProperty().getValue());

        return khachHang;
    }

    private boolean luuKhachHang(KhachHang khachHang) {
        boolean thanhCong = false;
        try {
            if (mode.equals("ADD")) {
                thanhCong = khachHangDAO.themKhachHang(khachHang);
            } else {
                thanhCong = khachHangDAO.capNhatKhachHang(khachHang);
            }
        } catch (Exception e) {
            UIUtils.hienThiThongBao("Lỗi", e.getMessage());
        }
        return thanhCong;
    }

    private void hienThiThongBaoThanhCong() {
        String thongBao = "ADD".equals(mode) ? "Đã thêm nhân viên thành công!" : "Đã cập nhật nhân viên thành công!";
        UIUtils.hienThiThongBao("Thành công", thongBao);
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
