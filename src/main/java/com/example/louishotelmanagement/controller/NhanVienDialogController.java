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
import java.util.StringTokenizer; // Cần cho định dạng họ tên

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
    private NhanVien nhanVienToEdit;
    private boolean isEditMode = false;

    public void initialize() {
        nhanVienDAO = new NhanVienDAO();
        cbChucVu.setItems(FXCollections.observableArrayList(
                "Quản lý", "Lễ tân", "Nhân viên"
        ));
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVienToEdit = nhanVien;
        isEditMode = (nhanVien != null);

        if (isEditMode) {

            lblTitle.setText("Sửa Thông Tin Nhân Viên");
            tfMaNV.setText(nhanVien.getMaNV());
            tfMaNV.setEditable(false); // Không cho sửa Mã NV khi sửa
            tfHoTen.setText(nhanVien.getHoTen());
            dpNgaySinh.setValue(nhanVien.getNgaySinh());
            tfSoDT.setText(nhanVien.getSoDT());
            tfDiaChi.setText(nhanVien.getDiaChi());
            cbChucVu.setValue(nhanVien.getChucVu());
        } else {

            lblTitle.setText("Thêm Nhân Viên Mới");
            tfMaNV.setEditable(false); // Mã NV tự sinh, không cho sửa
            // Lấy mã NV tiếp theo từ DAO và hiển thị
            try {
                String maNVTiepTheo = nhanVienDAO.layMaNVTiepTheo();
                tfMaNV.setText(maNVTiepTheo);
            } catch (SQLException e) {
                e.printStackTrace();
                hienThiLoi("Lỗi Lấy Mã NV", "Không thể lấy mã nhân viên tiếp theo: " + e.getMessage());
                tfMaNV.setText("Lỗi"); // Hiển thị lỗi nếu không lấy được mã
            }
        }
    }

    @FXML
    private void handleLuu(ActionEvent event) {
        if (validateInput()) {
            try {
                String maNV = tfMaNV.getText().trim();

                String hoTenRaw = tfHoTen.getText().trim();
                String hoTenFormatted = formatProperCase(hoTenRaw); // Gọi hàm định dạng

                LocalDate ngaySinh = dpNgaySinh.getValue();
                String soDT = tfSoDT.getText().trim();
                String diaChi = tfDiaChi.getText().trim();
                String chucVu = cbChucVu.getValue();


                NhanVien nv = new NhanVien(maNV, hoTenFormatted, soDT, diaChi, chucVu, ngaySinh);

                boolean success;
                if (isEditMode) {
                    success = nhanVienDAO.SuaNhanVien(nv);
                } else {
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

                if (!isEditMode && e.getMessage().contains("PRIMARY KEY constraint")) {
                    hienThiLoi("Lỗi Trùng Mã", "Mã nhân viên '" + tfMaNV.getText() + "' đã tồn tại!");
                } else {
                    hienThiLoi("Lỗi CSDL", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleHuy(ActionEvent event) {
        closeDialog();
    }

    // --- Hàm định dạng họ tên ---

    private String formatProperCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        //xóa space
        String cleanedInput = input.trim().replaceAll("\\s+", " ");

        StringBuilder properCase = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(cleanedInput, " "); // Tách chuỗi theo khoảng trắng

        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            // Viết hoa
            properCase.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase());
            if (tokenizer.hasMoreTokens()) {
                properCase.append(" "); // Thêm khoảng trắng giữa các từ
            }
        }
        return properCase.toString();
    }


    private boolean validateInput() {
        String errorMessage = "";
        LocalDate today = LocalDate.now();

        //kiểm tra
        if (tfMaNV.getText() == null || tfMaNV.getText().trim().isEmpty()) {
            errorMessage += "Mã nhân viên không được để trống!\n";
        }
        if (tfHoTen.getText() == null || tfHoTen.getText().trim().isEmpty()) {
            errorMessage += "Họ tên không được để trống!\n";
        }

        if (tfDiaChi.getText() == null || tfDiaChi.getText().trim().isEmpty()) {
            errorMessage += "Địa chỉ không được để trống!\n";
        }
        if (cbChucVu.getValue() == null || cbChucVu.getValue().isEmpty()) {
            errorMessage += "Chức vụ không được để trống!\n";
        }


        String soDT = tfSoDT.getText();
        if (soDT == null || soDT.trim().isEmpty()) {
            errorMessage += "Số điện thoại không được để trống!\n";
        } else if (!soDT.trim().matches("^\\d{11}$")) { // Thay đổi regex: ^ bắt đầu, \d{11} đúng 11 chữ số, $ kết thúc
            errorMessage += "Số điện thoại phải bao gồm đúng 11 chữ số!\n";
        }


        LocalDate ngaySinh = dpNgaySinh.getValue();
        if (ngaySinh == null) {
            errorMessage += "Ngày sinh không được để trống!\n";
        } else {
            if (ngaySinh.isAfter(today)) {
                errorMessage += "Ngày sinh không thể là một ngày trong tương lai!\n";
            } else {
                LocalDate ngayDu18Tuoi = ngaySinh.plusYears(18);
                if (ngayDu18Tuoi.isAfter(today)) {
                    errorMessage += "Nhân viên phải đủ 18 tuổi trở lên!\n";
                }
            }
        }
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            hienThiLoi("Dữ liệu không hợp lệ", errorMessage);
            return false;
        }
    }
    private void closeDialog() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }
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