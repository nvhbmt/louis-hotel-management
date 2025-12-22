package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.model.HangKhach;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.TrangThaiKhachHang;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.KhachHangFormDialogView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class KhachHangDialogController {

    @FXML
    private Label lblTieuDe;
    @FXML
    private VBox formContainer;
    @FXML
    private Button btnLuu;
    @FXML
    private Button btnHuy;

    private KhachHangDAO khachHangDAO;
    private String mode = "ADD";

    // Form fields
    private StringField maKHField;
    private StringField hoTenField;
    private StringField soDTField;
    private StringField emailField;
    private StringField diaChiField;
    private DateField ngaySinhField;
    private StringField ghiChuField;
    private StringField cccdField;
    private SingleSelectionField<String> hangKhachField;
    private SingleSelectionField<String> trangThaiField;

    private Form form;

    public KhachHangDialogController(KhachHangFormDialogView view) {
        this.lblTieuDe = view.getLblTieuDe();
        this.formContainer = view.getFormContainer();
        this.btnLuu = view.getBtnLuu();
        this.btnHuy = view.getBtnHuy();
        this.btnHuy.setOnAction(event -> handleHuy());
        this.btnLuu.setOnAction(event -> handleLuu());
        initialize();
    }

    public void initialize() {
            khachHangDAO = new KhachHangDAO();
            String maKHTiepTheo = layMaKhachHangNeuThemMoi();

            taoForm(maKHTiepTheo, "", "", "", "", LocalDate.now(), "", "", "Khách quen", "Đang lưu trú");
            hienThiForm();
    }

    private String layMaKhachHangNeuThemMoi() {
        if (!"ADD".equals(mode)) return "";
        return "KH" + (System.currentTimeMillis() % 1000);
    }

    private void taoForm(String maKH, String hoTen, String soDT, String email, String diaChi,
                         LocalDate ngaySinh, String ghiChu, String cccd,
                         String hangKhach, String trangThai) {

        maKHField = Field.ofStringType(maKH)
                .label("Mã khách hàng")
                .placeholder("VD: KH001")
                .validate(
                        StringLengthValidator.atLeast(5, "Mã khách hàng phải có ít nhất 5 ký tự"),
                        RegexValidator.forPattern("^KH\\d+$", "Mã khách hàng phải theo định dạng 'KHxxx'")
                )
                .required("Mã khách hàng không được để trống");
        if ("ADD".equals(mode)) maKHField.editable(false);

        hoTenField = Field.ofStringType(hoTen)
                .label("Họ và tên")
                .placeholder("VD: Nguyễn Văn A")
                .validate(
                        StringLengthValidator.atLeast(5, "Họ tên phải có ít nhất 5 ký tự"),
                        RegexValidator.forPattern("[A-ZÀ-Ỹ][a-zà-ỹ]+( [A-ZÀ-Ỹ][a-zà-ỹ]+)+", "Chữ cái đầu mỗi từ phải viết hoa")
                )
                .required("Họ tên không được để trống");

        soDTField = Field.ofStringType(soDT)
                .label("Số điện thoại")
                .placeholder("VD: 0123456789")
                .validate(
                        RegexValidator.forPattern("^\\d{10}$", "Số điện thoại phải 10 chữ số")
                )
                .required("Số điện thoại không được để trống");

        emailField = Field.ofStringType(email)
                .label("Email")
                .placeholder("VD: example@gmail.com")
                .validate(
                        RegexValidator.forPattern("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", "Email không hợp lệ")
                )
                .required("Email không được để trống");

        diaChiField = Field.ofStringType(diaChi)
                .label("Địa chỉ")
                .placeholder("VD: 123 Đường ABC, Quận 1")
                .required("Địa chỉ không được để trống");

        ngaySinhField = Field.ofDate(ngaySinh)
                .label("Ngày sinh")
                .required("Ngày sinh không được để trống");

        ghiChuField = Field.ofStringType(ghiChu)
                .label("Ghi chú")
                .placeholder("Nhập ghi chú nếu có");

        cccdField = Field.ofStringType(cccd)
                .label("CCCD")
                .placeholder("VD: 012345678901")
                .validate(
                        RegexValidator.forPattern("\\d{12}", "CCCD phải có 12 chữ số")
                )
                .required("CCCD không được để trống");

        ObservableList<String> dsHangKhach = FXCollections.observableArrayList("Khách VIP", "Khách quen", "Khách doanh nghiệp");
        hangKhachField = Field.ofSingleSelectionType(dsHangKhach)
                .label("Hạng khách")
                .required("Vui lòng chọn hạng khách");
        hangKhachField.selectionProperty().set(hangKhach);

        ObservableList<String> dsTrangThai = FXCollections.observableArrayList("Đang lưu trú", "Đã đặt", "Check-out");
        trangThaiField = Field.ofSingleSelectionType(dsTrangThai)
                .label("Trạng thái")
                .required("Vui lòng chọn trạng thái");
        trangThaiField.selectionProperty().set(trangThai);

        form = Form.of(Group.of(maKHField, hoTenField, soDTField, emailField, diaChiField,
                        ngaySinhField, ghiChuField, cccdField, hangKhachField, trangThaiField))
                .title("Thông tin Khách Hàng");
    }

    private void hienThiForm() {
        FormRenderer renderer = new FormRenderer(form);
        formContainer.getChildren().add(renderer);
        formContainer.getStyleClass().add("formsfx-form-container");
    }

    public void setMode(String mode) {
        this.mode = mode;
        capNhatUIVaoCheDo();
    }

    private void capNhatUIVaoCheDo() {
        if ("EDIT".equals(mode)) {
            lblTieuDe.setText("Sửa Thông Tin Khách Hàng");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Khách Hàng Mới");
            btnLuu.setText("Lưu");
            if (maKHField != null) maKHField.editable(false);
        }
    }

    public void setKhachHang(KhachHang kh) {
        if (kh == null) return;

        formContainer.getChildren().clear();

        taoForm(
                kh.getMaKH(),
                kh.getHoTen(),
                kh.getSoDT(),
                kh.getEmail(),
                kh.getDiaChi(),
                kh.getNgaySinh(),
                kh.getGhiChu(),
                kh.getCCCD(),
                kh.getHangKhach().toString(),
                kh.getTrangThai().getTenHienThi()
        );

        hienThiForm();
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin khách hàng!");
            return;
        }

        try {
            KhachHang kh = taoKhachHangTuForm();
            boolean thanhCong;
            if ("ADD".equals(mode)) {
                thanhCong = khachHangDAO.themKhachHang(kh);
            } else {
                thanhCong = khachHangDAO.capNhatKhachHang(kh);
            }

            if (thanhCong) {
                ThongBaoUtil.hienThiThongBao("Thành công", "Lưu thông tin khách hàng thành công!");
                dongDialog();
            }

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private KhachHang taoKhachHangTuForm() {
        KhachHang kh = new KhachHang();
        kh.setMaKH(maKHField.valueProperty().get());
        kh.setHoTen(hoTenField.valueProperty().get());
        kh.setSoDT(soDTField.valueProperty().get());
        kh.setEmail(emailField.valueProperty().get());
        kh.setDiaChi(diaChiField.valueProperty().get());
        kh.setNgaySinh(ngaySinhField.valueProperty().get());
        kh.setGhiChu(ghiChuField.valueProperty().get());
        kh.setCCCD(cccdField.valueProperty().get());
        kh.setHangKhach(HangKhach.fromString(hangKhachField.selectionProperty().get()));
        kh.setTrangThai(TrangThaiKhachHang.fromString(trangThaiField.selectionProperty().get()));

        return kh;
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
