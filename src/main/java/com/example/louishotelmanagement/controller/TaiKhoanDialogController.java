package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.example.louishotelmanagement.dao.NhanVienDAO;
import com.example.louishotelmanagement.dao.TaiKhoanDAO;
import com.example.louishotelmanagement.model.NhanVien;
import com.example.louishotelmanagement.model.TaiKhoan;
import com.example.louishotelmanagement.util.PasswordUtil;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class TaiKhoanDialogController implements Initializable {

    @FXML
    private Label lblTieuDe;
    @FXML
    private VBox formContainer;
    @FXML
    private Button btnLuu;

    private TaiKhoanDAO taiKhoanDAO;
    private NhanVienDAO nhanVienDAO;
    private String mode = "ADD";

    // Form fields
    private StringField maTKField;
    private SingleSelectionField<NhanVien> nhanVienField;
    private StringField tenDangNhapField;
    private StringField matKhauField;
    private SingleSelectionField<String> quyenField;
    private SingleSelectionField<String> trangThaiField;
    private Form form;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            khoiTaoDAO();
            String maTKTiepTheo = layMaTKTiepTheoNeuThemMoi();
            List<NhanVien> dsNhanVien = nhanVienDAO.layDSNhanVien();

            taoForm(maTKTiepTheo, null, "", "", "Staff", true, dsNhanVien);
            hienThiForm();

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể khởi tạo form: " + e.getMessage());
        }
    }

    private void khoiTaoDAO() throws SQLException {
        taiKhoanDAO = new TaiKhoanDAO();
        nhanVienDAO = new NhanVienDAO();
    }

    private String layMaTKTiepTheoNeuThemMoi() {
        if (!"ADD".equals(mode)) return "";

        try {
            return taiKhoanDAO.layMaTKTiepTheo();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể lấy mã tài khoản tiếp theo: " + e.getMessage());
            return "";
        }
    }

    private void taoForm(String maTK, NhanVien nhanVien, String tenDangNhap, String matKhau,
                        String quyen, boolean trangThai, List<NhanVien> dsNhanVien) {
        // Tạo các field
        maTKField = taoFieldMaTK(maTK);
        nhanVienField = taoFieldNhanVien(dsNhanVien, nhanVien);
        tenDangNhapField = taoFieldTenDangNhap(tenDangNhap);
        quyenField = taoFieldQuyen(quyen);
        trangThaiField = taoFieldTrangThai(trangThai);

        // Tạo form khác nhau cho ADD và EDIT
        if ("ADD".equals(mode)) {
            matKhauField = taoFieldMatKhau(matKhau);
            form = Form.of(Group.of(maTKField, nhanVienField, tenDangNhapField, matKhauField, quyenField, trangThaiField))
                    .title("Thông tin tài khoản");
        } else {
            // EDIT mode: không có field mật khẩu
            form = Form.of(Group.of(maTKField, nhanVienField, tenDangNhapField, quyenField, trangThaiField))
                    .title("Thông tin tài khoản");
        }
    }

    private StringField taoFieldMaTK(String maTK) {
        StringField field = Field.ofStringType(maTK)
                .label("Mã Tài Khoản")
                .placeholder("VD: TK001")
                .validate(
                        StringLengthValidator.exactly(5, "Mã tài khoản phải đúng 5 ký tự"),
                        RegexValidator.forPattern("^TK\\d{3}$", "Mã tài khoản phải theo định dạng 'TKxxx'")
                )
                .required("Mã tài khoản không được để trống");

        if ("ADD".equals(mode)) {
            field.editable(false);
        }
        return field;
    }

    private SingleSelectionField<NhanVien> taoFieldNhanVien(List<NhanVien> dsNhanVien, NhanVien selectedNhanVien) {
        SingleSelectionField<NhanVien> field = Field.ofSingleSelectionType(dsNhanVien)
                .label("Nhân viên")
                .required("Vui lòng chọn nhân viên");
        
        if (selectedNhanVien != null) {
            field.selectionProperty().set(selectedNhanVien);
        }
        
        return field;
    }

    private StringField taoFieldTenDangNhap(String tenDangNhap) {
        return Field.ofStringType(tenDangNhap)
                .label("Tên đăng nhập")
                .placeholder("Nhập tên đăng nhập...")
                .validate(
                        StringLengthValidator.atLeast(3, "Tên đăng nhập phải có ít nhất 3 ký tự"),
                        RegexValidator.forPattern("^\\S+$", "Tên đăng nhập không được chứa khoảng trắng")
                )
                .required("Tên đăng nhập không được để trống");
    }

    private StringField taoFieldMatKhau(String matKhau) {
        StringField field = Field.ofStringType(matKhau)
                .label("Mật khẩu")
                .placeholder("Nhập mật khẩu...")
                .validate(
                        StringLengthValidator.atLeast(6, "Mật khẩu phải có ít nhất 6 ký tự")
                );
        
        // Chỉ bắt buộc khi thêm mới
        if ("ADD".equals(mode)) {
            field.required("Mật khẩu không được để trống");
        }
        
        return field;
    }

    private SingleSelectionField<String> taoFieldQuyen(String quyen) {
        SingleSelectionField<String> field = Field.ofSingleSelectionType(List.of("Manager", "Staff"))
                .label("Quyền")
                .required("Vui lòng chọn quyền");
        
        if (quyen != null && !quyen.isEmpty()) {
            field.selectionProperty().set(quyen);
        }
        
        return field;
    }

    private SingleSelectionField<String> taoFieldTrangThai(boolean trangThai) {
        SingleSelectionField<String> field = Field.ofSingleSelectionType(List.of("Hoạt động", "Khóa"))
                .label("Trạng thái")
                .required("Vui lòng chọn trạng thái");
        
        if (trangThai) {
            field.selectionProperty().set("Hoạt động");
        } else {
            field.selectionProperty().set("Khóa");
        }
        
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
        if ("EDIT".equals(mode)) {
            lblTieuDe.setText("Sửa Thông Tin Tài Khoản");
            btnLuu.setText("Cập nhật");
        } else {
            lblTieuDe.setText("Thêm Tài Khoản Mới");
            btnLuu.setText("Lưu");
            if (maTKField != null) {
                maTKField.editable(false);
            }
        }
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        if (taiKhoan == null) return;

        try {
            // Load lại form với data của tài khoản
            List<NhanVien> dsNhanVien = nhanVienDAO.layDSNhanVien();
            
            // Tìm nhân viên đúng trong danh sách
            NhanVien nhanVienToSet = null;
            if (taiKhoan.getNhanVien() != null && taiKhoan.getNhanVien().getMaNV() != null) {
                nhanVienToSet = dsNhanVien.stream()
                    .filter(nv -> nv.getMaNV().equals(taiKhoan.getNhanVien().getMaNV()))
                    .findFirst()
                    .orElse(null);
            }
            
            // Clear form container
            formContainer.getChildren().clear();
            
            // Tạo lại form với data của tài khoản
            taoForm(
                taiKhoan.getMaTK(),
                nhanVienToSet, // Sử dụng nhân viên đã tìm được
                taiKhoan.getTenDangNhap(),
                "", // Không hiển thị mật khẩu cũ
                taiKhoan.getQuyen(),
                taiKhoan.getTrangThai(),
                dsNhanVien
            );
            hienThiForm();
            
            // Set mã TK không được edit
            maTKField.editable(false);
            
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể load data tài khoản: " + e.getMessage());
        }
    }

    @FXML
    private void handleLuu() {
        if (!form.isValid()) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Vui lòng kiểm tra lại thông tin tài khoản!");
            return;
        }

        try {
            TaiKhoan taiKhoan = taoTaiKhoanTuForm();
            boolean thanhCong = luuTaiKhoan(taiKhoan);

            if (thanhCong) {
                hienThiThongBaoThanhCong();
                dongDialog();
            }

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private TaiKhoan taoTaiKhoanTuForm() {
        TaiKhoan taiKhoan = new TaiKhoan("", null, "", "", "", false);
        taiKhoan.setMaTK(maTKField.valueProperty().get().trim());
        
        NhanVien nhanVien = nhanVienField.getSelection();
        if (nhanVien != null) {
            taiKhoan.setNhanVien(nhanVien);
        }
        
        taiKhoan.setTenDangNhap(tenDangNhapField.valueProperty().get().trim());
        
        // Hash mật khẩu
        if (matKhauField != null) {
            String matKhauPlain = matKhauField.valueProperty().get();
            if (matKhauPlain != null && !matKhauPlain.trim().isEmpty()) {
                String matKhauHash = PasswordUtil.hashPassword(matKhauPlain);
                taiKhoan.setMatKhauHash(matKhauHash);
            }
        }
        
        taiKhoan.setQuyen(quyenField.getSelection());
        
        String trangThaiStr = trangThaiField.getSelection();
        taiKhoan.setTrangThai("Hoạt động".equals(trangThaiStr));

        return taiKhoan;
    }

    private boolean luuTaiKhoan(TaiKhoan taiKhoan) throws SQLException {
        boolean thanhCong;

        if ("ADD".equals(mode)) {
            thanhCong = taiKhoanDAO.themTaiKhoan(
                taiKhoan.getMaTK(),
                taiKhoan.getNhanVien(),
                taiKhoan.getTenDangNhap(),
                taiKhoan.getMatKhauHash(),
                taiKhoan.getQuyen(),
                taiKhoan.getTrangThai()
            );
        } else {
            // Khi edit, nếu mật khẩu trống thì giữ nguyên mật khẩu cũ
            String matKhauHash = taiKhoan.getMatKhauHash();
            if (matKhauHash == null || matKhauHash.isEmpty()) {
                // Lấy mật khẩu cũ từ database
                try {
                    TaiKhoan taiKhoanCu = taiKhoanDAO.timTaiKhoanTheoMa(taiKhoan.getMaTK());
                    if (taiKhoanCu != null) {
                        matKhauHash = taiKhoanCu.getMatKhauHash();
                    }
                } catch (SQLException e) {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể lấy mật khẩu cũ: " + e.getMessage());
                    return false;
                }
            }
            
            thanhCong = taiKhoanDAO.capNhatTaiKhoan(
                taiKhoan.getMaTK(),
                taiKhoan.getNhanVien(),
                taiKhoan.getTenDangNhap(),
                matKhauHash,
                taiKhoan.getQuyen(),
                taiKhoan.getTrangThai()
            );
        }

        return thanhCong;
    }

    private void hienThiThongBaoThanhCong() {
        String thongBao = "ADD".equals(mode) ? "Đã thêm tài khoản thành công!" : "Đã cập nhật tài khoản thành công!";
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
