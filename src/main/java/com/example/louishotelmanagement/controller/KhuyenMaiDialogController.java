package com.example.louishotelmanagement.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import com.dlsc.formsfx.model.structure.DateField;
import com.dlsc.formsfx.model.structure.DoubleField;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.KhuyenMaiDAO;
import com.example.louishotelmanagement.model.KhuyenMai;
import com.example.louishotelmanagement.model.KieuGiamGia;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KhuyenMaiDialogController implements Initializable {

    @FXML
    private Label lblTieuDe;
    @FXML
    private VBox formContainer;
    @FXML
    private Button btnLuu;

    private KhuyenMaiDAO khuyenMaiDAO;
    private String mode = "ADD";

    // Form fields
    private StringField maKMField;
    private StringField codeField;
    private DoubleField giamGiaField;
    private SingleSelectionField<KieuGiamGia> kieuGiamGiaField;
    private DateField ngayBatDauField;
    private DateField ngayKetThucField;
    private DoubleField tongTienToiThieuField;
    private StringField moTaField;
    private SingleSelectionField<String> trangThaiField;
    private Form form;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            khuyenMaiDAO = new KhuyenMaiDAO();
            String maKMTiepTheo = khuyenMaiDAO.layMaKMTiepTheo();

            taoForm(new KhuyenMai(maKMTiepTheo, "", 0.0, null, LocalDate.now(), LocalDate.now().plusDays(30), 0.0, "", "Kích hoạt", ""));
            hienThiForm();

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể khởi tạo form: " + e.getMessage());
        }
    }

    private void taoForm(KhuyenMai khuyenMai) {
        // Tạo các field
        maKMField = taoFieldMaKM(khuyenMai.getMaKM());
        codeField = taoFieldCode(khuyenMai.getCode());
        giamGiaField = taoFieldGiamGia(khuyenMai.getGiamGia());
        kieuGiamGiaField = taoFieldKieuGiamGia(khuyenMai.getKieuGiamGia());
        ngayBatDauField = taoFieldNgayBatDau(khuyenMai.getNgayBatDau());
        ngayKetThucField = taoFieldNgayKetThuc(khuyenMai.getNgayKetThuc());
        tongTienToiThieuField = taoFieldTongTienToiThieu(khuyenMai.getTongTienToiThieu());
        moTaField = taoFieldMoTa(khuyenMai.getMoTa());
        trangThaiField = taoFieldTrangThai(khuyenMai.getTrangThai());

        // Tạo form
        form = Form
                .of(Group.of(maKMField,codeField, giamGiaField, kieuGiamGiaField, ngayBatDauField,
                        ngayKetThucField, tongTienToiThieuField, moTaField, trangThaiField))
                .title("Thông tin khuyến mãi");
    }
    
    private StringField taoFieldMaKM(String maKM) {
        return Field.ofStringType(maKM)
                .label("Mã Khuyến Mãi")
                .required("Mã khuyến mãi không được để trống")
                .editable(false);
    }

    private StringField taoFieldCode(String code) {
        return Field.ofStringType(code)
                .label("Code")
                .placeholder("VD: SUMMER2024")
                .validate(
                        StringLengthValidator.atLeast(3, "Code phải có ít nhất 3 ký tự"),
                        StringLengthValidator.upTo(20, "Code không được quá 20 ký tự")
                )
                .required("Code không được để trống");
    }

    private DoubleField taoFieldGiamGia(Double giamGia) {
        return Field.ofDoubleType(giamGia)
                .label("Giá Trị Giảm")
                .placeholder("Nhập giá trị giảm...")
                .validate(
                        com.dlsc.formsfx.model.validators.DoubleRangeValidator.atLeast(0.0, "Giá trị giảm phải lớn hơn hoặc bằng 0")
                )
                .required("Giá trị giảm không được để trống");
    }

    private SingleSelectionField<KieuGiamGia> taoFieldKieuGiamGia(KieuGiamGia kieuGiamGia) {
        return Field.ofSingleSelectionType(List.of(KieuGiamGia.PERCENT, KieuGiamGia.AMOUNT))
                .label("Kiểu Giảm Giá")
                .required("Vui lòng chọn kiểu giảm giá");
    }

    private DateField taoFieldNgayBatDau(LocalDate ngayBatDau) {
        return Field.ofDate(ngayBatDau)
                .label("Ngày Bắt Đầu")
                .required("Ngày bắt đầu không được để trống");
    }

    private DateField taoFieldNgayKetThuc(LocalDate ngayKetThuc) {
        return Field.ofDate(ngayKetThuc)
                .label("Ngày Kết Thúc")
                .required("Ngày kết thúc không được để trống");
    }

    private DoubleField taoFieldTongTienToiThieu(Double tongTienToiThieu) {
        return Field.ofDoubleType(tongTienToiThieu)
                .label("Tổng Tiền Tối Thiểu")
                .placeholder("Nhập tổng tiền tối thiểu...")
                .validate(
                        com.dlsc.formsfx.model.validators.DoubleRangeValidator.atLeast(0.0, "Tổng tiền tối thiểu phải lớn hơn hoặc bằng 0")
                )
                .required("Tổng tiền tối thiểu không được để trống");
    }

    private StringField taoFieldMoTa(String moTa) {
        return Field.ofStringType(moTa)
                .label("Mô Tả")
                .placeholder("Nhập mô tả khuyến mãi...");
    }

    private SingleSelectionField<String> taoFieldTrangThai(String trangThai) {
        return Field.ofSingleSelectionType(List.of("Kích hoạt", "Vô hiệu hóa"))
                .label("Trạng Thái")
                .required("Vui lòng chọn trạng thái");
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
            lblTieuDe.setText("Sửa Thông Tin Khuyến Mãi");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Khuyến Mãi Mới");
            btnLuu.setText("Lưu");
        }
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        if (khuyenMai == null) return;

        // Chuyển đổi trạng thái hiển thị thành trạng thái DB
        String trangThaiDB = khuyenMai.getTrangThai();
        if ("Đang sử dụng".equals(trangThaiDB) || "Đã hết hạn".equals(trangThaiDB) || "Chưa bắt đầu".equals(trangThaiDB)) {
            trangThaiDB = "Kích hoạt";
        }
        // Nếu là "Vô hiệu hóa" thì giữ nguyên

        KhuyenMai khuyenMaiForm = new KhuyenMai(
            khuyenMai.getMaKM(),
            khuyenMai.getCode(),
            khuyenMai.getGiamGia(),
            khuyenMai.getKieuGiamGia(),
            khuyenMai.getNgayBatDau(),
            khuyenMai.getNgayKetThuc(),
            khuyenMai.getTongTienToiThieu(),
            khuyenMai.getMoTa(),
            trangThaiDB,
            khuyenMai.getMaNhanVien()
        );

        formContainer.getChildren().clear();
        taoForm(khuyenMaiForm);
        kieuGiamGiaField.selectionProperty().set(khuyenMai.getKieuGiamGia());
        trangThaiField.selectionProperty().set(trangThaiDB);
        hienThiForm();
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin khuyến mãi!");
            return;
        }
        
        try {
            KhuyenMai khuyenMai = taoKhuyenMaiTuForm();
            boolean thanhCong = luuKhuyenMai(khuyenMai);

            if (thanhCong) {
                hienThiThongBaoThanhCong();
                dongDialog();
            }

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private KhuyenMai taoKhuyenMaiTuForm() {
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setMaKM(maKMField.valueProperty().get().trim());
        khuyenMai.setCode(codeField.valueProperty().get().trim());
        khuyenMai.setGiamGia(giamGiaField.valueProperty().get());
        khuyenMai.setKieuGiamGia(kieuGiamGiaField.getSelection());
        khuyenMai.setNgayBatDau(ngayBatDauField.valueProperty().get());
        khuyenMai.setNgayKetThuc(ngayKetThucField.valueProperty().get());
        khuyenMai.setTongTienToiThieu(tongTienToiThieuField.valueProperty().get());
        khuyenMai.setMoTa(moTaField.valueProperty().get().trim());
        khuyenMai.setTrangThai(trangThaiField.getSelection());
        return khuyenMai;
    }

    private boolean luuKhuyenMai(KhuyenMai khuyenMai) throws SQLException {
        boolean thanhCong;

        if ("ADD".equals(mode)) {
            thanhCong = khuyenMaiDAO.themKhuyenMai(khuyenMai);
        } else {
            thanhCong = khuyenMaiDAO.capNhatKhuyenMai(khuyenMai);
        }

        return thanhCong;
    }

    private void hienThiThongBaoThanhCong() {
        String thongBao = "ADD".equals(mode) ? "Đã thêm khuyến mãi thành công!" : "Đã cập nhật khuyến mãi thành công!";
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