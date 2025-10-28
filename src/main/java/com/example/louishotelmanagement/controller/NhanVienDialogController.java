package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.NhanVienDAO;
import com.example.louishotelmanagement.model.NhanVien;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class NhanVienDialogController {

    @FXML private Label lblTitle;
    @FXML private TextField tfMaNV;
    @FXML private TextField tfHoTen;
    @FXML private DatePicker dpNgaySinh;
    @FXML private TextField tfSoDT;
    @FXML private TextField tfDiaChi;
    @FXML private ComboBox<String> cbChucVu;
    @FXML private Button btnLuu;
    @FXML private Button btnHuy;

    private NhanVienDAO nhanVienDAO;
    private NhanVien nhanVienToEdit; // Nhân viên cần sửa (null nếu là thêm mới)
    private boolean isEditMode = false;

    public void initialize() {
        nhanVienDAO = new NhanVienDAO();
        // Khởi tạo ComboBox chức vụ (giống như trong QuanLyNhanVienController)
        cbChucVu.setItems(FXCollections.observableArrayList(
                "Quản lý", "Lễ tân", "Nhân viên" // Thêm các chức vụ khác nếu cần
        ));
    }

    /**
     * Phương thức này được gọi từ QuanLyNhanVienController để truyền dữ liệu vào.
     * @param nhanVien Nhân viên cần sửa (null nếu là thêm mới).
     */
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVienToEdit = nhanVien;
        isEditMode = (nhanVien != null);

        if (isEditMode) {
            lblTitle.setText("Sửa Thông Tin Nhân Viên");
            tfMaNV.setText(nhanVien.getMaNV());
            tfMaNV.setEditable(false); // Không cho sửa Mã NV
            tfHoTen.setText(nhanVien.getHoTen());
            dpNgaySinh.setValue(nhanVien.getNgaySinh());
            tfSoDT.setText(nhanVien.getSoDT());
            tfDiaChi.setText(nhanVien.getDiaChi());
            cbChucVu.setValue(nhanVien.getChucVu());
        } else {
            lblTitle.setText("Thêm Nhân Viên Mới");
            tfMaNV.setEditable(true);
            // Có thể thêm logic tự sinh mã NV ở đây nếu muốn
        }
    }

    /**
     * Xử lý sự kiện khi nhấn nút Lưu.
     */
    @FXML
    private void handleLuu(ActionEvent event) {
        if (validateInput()) {
            try {
                String maNV = tfMaNV.getText().trim();
                String hoTen = tfHoTen.getText().trim();
                LocalDate ngaySinh = dpNgaySinh.getValue();
                String soDT = tfSoDT.getText().trim();
                String diaChi = tfDiaChi.getText().trim();
                String chucVu = cbChucVu.getValue();

                NhanVien nv = new NhanVien(maNV, hoTen, soDT, diaChi, chucVu, ngaySinh);

                boolean success;
                if (isEditMode) {
                    // Gọi DAO để cập nhật
                    success = nhanVienDAO.SuaNhanVien(nv);
                } else {
                    // Gọi DAO để thêm mới
                    success = nhanVienDAO.ThemNhanVien(nv);
                }

                if (success) {
                    hienThiThongBao("Thành Công", (isEditMode ? "Cập nhật" : "Thêm") + " nhân viên thành công.");
                    closeDialog();
                } else {
                    hienThiLoi("Thất Bại", "Không thể lưu thông tin nhân viên.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                hienThiLoi("Lỗi CSDL", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
            }
        }
    }

    /**
     * Xử lý sự kiện khi nhấn nút Hủy.
     */
    @FXML
    private void handleHuy(ActionEvent event) {
        closeDialog();
    }

    /**
     * Kiểm tra tính hợp lệ của dữ liệu nhập.
     * @return true nếu hợp lệ, false nếu không.
     */
    private boolean validateInput() {
        String errorMessage = "";

        if (tfMaNV.getText() == null || tfMaNV.getText().trim().isEmpty()) {
            errorMessage += "Mã nhân viên không được để trống!\n";
        }
        if (tfHoTen.getText() == null || tfHoTen.getText().trim().isEmpty()) {
            errorMessage += "Họ tên không được để trống!\n";
        }
        if (dpNgaySinh.getValue() == null) {
            errorMessage += "Ngày sinh không được để trống!\n";
        } else if (dpNgaySinh.getValue().isAfter(LocalDate.now())) {
            errorMessage += "Ngày sinh không hợp lệ!\n";
        }
        if (tfSoDT.getText() == null || tfSoDT.getText().trim().isEmpty()) {
            errorMessage += "Số điện thoại không được để trống!\n";
        } else if (!tfSoDT.getText().trim().matches("\\d{10,11}")) { // Ví dụ: kiểm tra 10-11 chữ số
            errorMessage += "Số điện thoại không hợp lệ!\n";
        }
        if (tfDiaChi.getText() == null || tfDiaChi.getText().trim().isEmpty()) {
            errorMessage += "Địa chỉ không được để trống!\n";
        }
        if (cbChucVu.getValue() == null || cbChucVu.getValue().isEmpty()) {
            errorMessage += "Chức vụ không được để trống!\n";
        }


        if (errorMessage.isEmpty()) {
            return true;
        } else {
            // Hiển thị lỗi validation
            hienThiLoi("Dữ liệu không hợp lệ", errorMessage);
            return false;
        }
    }

    /**
     * Đóng cửa sổ dialog hiện tại.
     */
    private void closeDialog() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    // --- Các hàm tiện ích hiển thị thông báo (Giống như trong QuanLyNhanVienController) ---
    private void hienThiThongBao(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void hienThiLoi(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}