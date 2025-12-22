package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.NhanVienDAO;
import com.example.louishotelmanagement.dao.TaiKhoanDAO;
import com.example.louishotelmanagement.model.NhanVien;
import com.example.louishotelmanagement.model.TaiKhoan;
import com.example.louishotelmanagement.util.PasswordUtil;
import com.example.louishotelmanagement.view.NhanVienDialogView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.StringTokenizer;

public class NhanVienDialogController {

    @FXML private Label lblTitle;
    @FXML private TextField tfMaNV, tfHoTen, tfSoDT, tfDiaChi, tfTenDangNhap;
    @FXML private PasswordField pfMatKhau;
    @FXML private DatePicker dpNgaySinh;
    @FXML private ComboBox<String> cbChucVu, cbQuyen;
    @FXML private CheckBox ckTrangThai;
    @FXML private Button btnLuu, btnHuy;

    private NhanVienDAO nhanVienDAO;
    private TaiKhoanDAO taiKhoanDAO;
    private NhanVien nhanVienToEdit;
    private TaiKhoan taiKhoanToEdit;
    private boolean isEditMode = false;

    public NhanVienDialogController(NhanVienDialogView view) {
        this.lblTitle = view.getLblTitle();
        this.tfMaNV = view.getTfMaNV();
        this.tfHoTen = view.getTfHoTen();
        this.dpNgaySinh = view.getDpNgaySinh();
        this.tfSoDT = view.getTfSoDT();
        this.tfDiaChi = view.getTfDiaChi();
        this.cbChucVu = view.getCbChucVu();
        this.tfTenDangNhap = view.getTfTenDangNhap();
        this.pfMatKhau = view.getPfMatKhau();
        this.cbQuyen = view.getCbQuyen();
        this.ckTrangThai = view.getCkTrangThai();
        this.btnLuu = view.getBtnLuu();
        this.btnHuy = view.getBtnHuy();
        this.btnLuu.setOnAction(event -> handleLuu());
        this.btnHuy.setOnAction(event -> handleHuy());
        initialize();
    }

    public void initialize() {
        nhanVienDAO = new NhanVienDAO();
        taiKhoanDAO = new TaiKhoanDAO();

        cbChucVu.setItems(FXCollections.observableArrayList("Quản lý", "Lễ tân"));
        cbQuyen.setItems(FXCollections.observableArrayList("Manager", "Staff"));
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVienToEdit = nhanVien;
        isEditMode = (nhanVien != null);

        if (isEditMode) {
            lblTitle.setText("Sửa Thông Tin Nhân Viên & Tài Khoản");
            loadDataToForm();
        } else {
            lblTitle.setText("Thêm Nhân Viên & Tài Khoản Mới");
            try {
                tfMaNV.setText(nhanVienDAO.layMaNVTiepTheo());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadDataToForm() {
        // Load thông tin NV
        tfMaNV.setText(nhanVienToEdit.getMaNV());
        tfHoTen.setText(nhanVienToEdit.getHoTen());
        dpNgaySinh.setValue(nhanVienToEdit.getNgaySinh());
        tfSoDT.setText(nhanVienToEdit.getSoDT());
        tfDiaChi.setText(nhanVienToEdit.getDiaChi());
        cbChucVu.setValue(nhanVienToEdit.getChucVu());

        // Load thông tin TK liên quan
        try {
            taiKhoanToEdit = taiKhoanDAO.timTaiKhoanTheoMaNV(nhanVienToEdit.getMaNV());
            if (taiKhoanToEdit != null) {
                tfTenDangNhap.setText(taiKhoanToEdit.getTenDangNhap());
                cbQuyen.setValue(taiKhoanToEdit.getQuyen());
                ckTrangThai.setSelected(taiKhoanToEdit.getTrangThai());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLuu() {
        if (!validateInput()) return;

        try {
            // 1. Chuẩn bị dữ liệu Nhân Viên
            String hoTenFormatted = formatProperCase(tfHoTen.getText());
            NhanVien nv = new NhanVien(tfMaNV.getText(), hoTenFormatted, tfSoDT.getText(),
                    tfDiaChi.getText(), cbChucVu.getValue(), dpNgaySinh.getValue());

            boolean successNV;
            if (isEditMode) {
                successNV = nhanVienDAO.SuaNhanVien(nv);
            } else {
                successNV = nhanVienDAO.ThemNhanVien(nv);
            }

            // 2. Chuẩn bị dữ liệu Tài Khoản (chỉ thực hiện nếu NV lưu thành công)
            if (successNV) {
                luuTaiKhoanLogic(nv);
                hienThiThongBao("Thành Công", "Đã lưu thông tin nhân viên và tài khoản.");
                closeDialog();
            }

        } catch (SQLException e) {
            hienThiLoi("Lỗi CSDL", "Chi tiết: " + e.getMessage());
        }
    }

    private void luuTaiKhoanLogic(NhanVien nv) throws SQLException {
        String tenDN = tfTenDangNhap.getText().trim();
        String mkRaw = pfMatKhau.getText();
        String quyen = cbQuyen.getValue();
        boolean trangThai = ckTrangThai.isSelected();

        if (isEditMode && taiKhoanToEdit != null) {
            // Cập nhật tài khoản hiện có
            String mkHash = (mkRaw.isEmpty()) ? taiKhoanToEdit.getMatKhauHash() : PasswordUtil.hashPassword(mkRaw);
            taiKhoanDAO.capNhatTaiKhoan(taiKhoanToEdit.getMaTK(), nv, tenDN, mkHash, quyen, trangThai);
        } else {
            // Thêm mới tài khoản (nếu chưa có)
            String maTK = taiKhoanDAO.layMaTKTiepTheo();
            String mkHash = PasswordUtil.hashPassword(mkRaw.isEmpty() ? "123456" : mkRaw);
            taiKhoanDAO.themTaiKhoan(maTK, nv, tenDN, mkHash, quyen, trangThai);
        }
    }

    private boolean validateInput() {
        StringBuilder sb = new StringBuilder();
        if (tfHoTen.getText().isEmpty()) sb.append("- Họ tên không được để trống!\n");
        if (tfTenDangNhap.getText().isEmpty()) sb.append("- Tên đăng nhập không được để trống!\n");
        if (!isEditMode && pfMatKhau.getText().isEmpty()) sb.append("- Mật khẩu không được để trống khi tạo mới!\n");
        if (cbChucVu.getValue() == null) sb.append("- Chưa chọn chức vụ!\n");
        if (cbQuyen.getValue() == null) sb.append("- Chưa chọn quyền tài khoản!\n");

        if (sb.length() > 0) {
            hienThiLoi("Dữ liệu thiếu", sb.toString());
            return false;
        }
        return true;
    }

    private String formatProperCase(String input) {
        if (input == null || input.isEmpty()) return "";
        String cleaned = input.trim().replaceAll("\\s+", " ");
        StringBuilder result = new StringBuilder();
        StringTokenizer st = new StringTokenizer(cleaned, " ");
        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase()).append(" ");
        }
        return result.toString().trim();
    }

    @FXML private void handleHuy() { closeDialog(); }
    private void closeDialog() { ((Stage) btnHuy.getScene().getWindow()).close(); }
    private void hienThiThongBao(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setContentText(m); a.showAndWait();
    }
    private void hienThiLoi(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR); a.setTitle(t); a.setContentText(m); a.showAndWait();
    }
}