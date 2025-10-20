package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.MaGiamGiaDAO;
import com.example.louishotelmanagement.model.KieuGiamGia;
import com.example.louishotelmanagement.model.MaGiamGia;
import com.example.louishotelmanagement.utils.UIUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class MaGiamGiaDialogController implements Initializable {

    @FXML
    private Label lblTieuDe;
    @FXML
    private VBox formContainer;
    @FXML
    private Button btnLuu;

    private MaGiamGiaDAO maGiamGiaDAO;
    private String mode = "ADD";

    // Form fields
    private StringField maGGField;
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
            khoiTaoDAO();
            String maGGTiepTheo = layMaGGTiepTheoNeuThemMoi();
            
            taoForm(maGGTiepTheo, "", 0.0, null, LocalDate.now(), LocalDate.now().plusDays(30), 0.0, "", "Hoạt động");
            hienThiForm();
            
        } catch (SQLException e) {
            UIUtils.hienThiThongBao("Lỗi", "Không thể khởi tạo form: " + e.getMessage());
        }
    }

    private void khoiTaoDAO() throws SQLException {
        maGiamGiaDAO = new MaGiamGiaDAO();
    }

    private String layMaGGTiepTheoNeuThemMoi() {
        if (!"ADD".equals(mode)) return "";
        
        try {
            // Tạo mã giảm giá tiếp theo dựa trên thời gian
            return "GG" + System.currentTimeMillis() % 100000;
        } catch (Exception e) {
            UIUtils.hienThiThongBao("Lỗi", "Không thể tạo mã giảm giá: " + e.getMessage());
            return "";
        }
    }

    private void taoForm(String maGG, String code, Double giamGia, KieuGiamGia kieuGiamGia, 
                        LocalDate ngayBatDau, LocalDate ngayKetThuc, Double tongTienToiThieu, 
                        String moTa, String trangThai) {
        // Tạo các field
        maGGField = taoFieldMaGG(maGG);
        codeField = taoFieldCode(code);
        giamGiaField = taoFieldGiamGia(giamGia);
        kieuGiamGiaField = taoFieldKieuGiamGia(kieuGiamGia);
        ngayBatDauField = taoFieldNgayBatDau(ngayBatDau);
        ngayKetThucField = taoFieldNgayKetThuc(ngayKetThuc);
        tongTienToiThieuField = taoFieldTongTienToiThieu(tongTienToiThieu);
        moTaField = taoFieldMoTa(moTa);
        trangThaiField = taoFieldTrangThai(trangThai);

        // Tạo form
        form = Form.of(Group.of(maGGField, codeField, giamGiaField, kieuGiamGiaField, 
                               ngayBatDauField, ngayKetThucField, tongTienToiThieuField, 
                               moTaField, trangThaiField))
                .title("Thông tin mã giảm giá");
    }

    private StringField taoFieldMaGG(String maGG) {
        StringField field = Field.ofStringType(maGG)
                .label("Mã Giảm Giá")
                .placeholder("VD: GG001")
                .validate(
                        StringLengthValidator.atLeast(3, "Mã giảm giá phải có ít nhất 3 ký tự"),
                        RegexValidator.forPattern("^GG\\d+$", "Mã giảm giá phải theo định dạng 'GGxxx'")
                )
                .required("Mã giảm giá không được để trống");
        
        if ("ADD".equals(mode)) {
            field.editable(false);
        }
        return field;
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
                .placeholder("Nhập mô tả mã giảm giá...");
    }

    private SingleSelectionField<String> taoFieldTrangThai(String trangThai) {
        return Field.ofSingleSelectionType(List.of("Hoạt động", "Hết hạn", "Chưa bắt đầu", "Tạm dừng"))
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
            lblTieuDe.setText("Sửa Thông Tin Mã Giảm Giá");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Mã Giảm Giá Mới");
            btnLuu.setText("Lưu");
            if (maGGField != null) {
                maGGField.editable(false);
            }
        }
    }

    public void setMaGiamGia(MaGiamGia maGiamGia) {
        if (maGiamGia == null) return;
        
        // Set values
        maGGField.valueProperty().set(maGiamGia.getMaGG());
        maGGField.editable(false);
        
        codeField.valueProperty().set(maGiamGia.getCode());
        giamGiaField.valueProperty().set(maGiamGia.getGiamGia());
        kieuGiamGiaField.selectionProperty().set(maGiamGia.getKieuGiamGia());
        ngayBatDauField.valueProperty().set(maGiamGia.getNgayBatDau());
        ngayKetThucField.valueProperty().set(maGiamGia.getNgayKetThuc());
        tongTienToiThieuField.valueProperty().set(maGiamGia.getTongTienToiThieu());
        moTaField.valueProperty().set(maGiamGia.getMoTa());
        trangThaiField.selectionProperty().set(maGiamGia.getTrangThai());
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            UIUtils.hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin mã giảm giá!");
            return;
        }
        
        try {
            MaGiamGia maGiamGia = taoMaGiamGiaTuForm();
            boolean thanhCong = luuMaGiamGia(maGiamGia);
            
            if (thanhCong) {
                hienThiThongBaoThanhCong();
                dongDialog();
            }
            
        } catch (SQLException e) {
            UIUtils.hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private MaGiamGia taoMaGiamGiaTuForm() {
        MaGiamGia maGiamGia = new MaGiamGia();
        maGiamGia.setMaGG(maGGField.valueProperty().get().trim());
        maGiamGia.setCode(codeField.valueProperty().get().trim());
        maGiamGia.setGiamGia(giamGiaField.valueProperty().get());
        maGiamGia.setKieuGiamGia(kieuGiamGiaField.getSelection());
        maGiamGia.setNgayBatDau(ngayBatDauField.valueProperty().get());
        maGiamGia.setNgayKetThuc(ngayKetThucField.valueProperty().get());
        maGiamGia.setTongTienToiThieu(tongTienToiThieuField.valueProperty().get());
        maGiamGia.setMoTa(moTaField.valueProperty().get().trim());
        maGiamGia.setTrangThai(trangThaiField.getSelection());
        
        // Set mã nhân viên (có thể lấy từ session hoặc hardcode)
        maGiamGia.setMaNhanVien("NV001"); // TODO: Lấy từ session thực tế
        
        return maGiamGia;
    }

    private boolean luuMaGiamGia(MaGiamGia maGiamGia) throws SQLException {
        boolean thanhCong;
        
        if ("ADD".equals(mode)) {
            thanhCong = maGiamGiaDAO.themMaGiamGia(maGiamGia);
        } else {
            thanhCong = maGiamGiaDAO.capNhatMaGiamGia(maGiamGia);
        }
        
        return thanhCong;
    }

    private void hienThiThongBaoThanhCong() {
        String thongBao = "ADD".equals(mode) ? "Đã thêm mã giảm giá thành công!" : "Đã cập nhật mã giảm giá thành công!";
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
