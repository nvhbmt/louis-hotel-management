package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.TaiKhoanDAO;
import com.example.louishotelmanagement.model.TaiKhoan;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.PasswordUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DangNhapController implements Initializable {

    @FXML
    private TextField tenDangNhapField;
    
    @FXML
    private PasswordField matKhauField;
    
    @FXML
    private Button dangNhapBtn;
    
    @FXML
    private Label thongBaoLabel;
    
    private TaiKhoanDAO taiKhoanDAO;
    private AuthService authService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taiKhoanDAO = new TaiKhoanDAO();
        authService = AuthService.getInstance();
        
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        dangNhapBtn.setOnAction(_ -> handleDangNhap());
        
        // Enter key để đăng nhập
        matKhauField.setOnAction(_ -> handleDangNhap());
        tenDangNhapField.setOnAction(_ -> handleDangNhap());
    }
    
    private void handleDangNhap() {
        String tenDangNhap = tenDangNhapField.getText().trim();
        String matKhau = matKhauField.getText();
        
        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            showThongBao("Vui lòng nhập đầy đủ thông tin!", true);
            return;
        }
        
        try {
            // Tìm tài khoản theo tên đăng nhập trước
            TaiKhoan taiKhoan = taiKhoanDAO.timTaiKhoanTheoTenDangNhap(tenDangNhap);
            
            if (taiKhoan != null) {
                if (taiKhoan.getTrangThai()) {
                    // Verify mật khẩu với hash đã lưu trong database
                    if (PasswordUtil.verifyPassword(matKhau, taiKhoan.getMatKhauHash())) {
                        // Đăng nhập thành công
                        authService.setCurrentUser(taiKhoan);
                        showThongBao("Đăng nhập thành công!", false);
                        
                        // Chuyển đến màn hình chính
                        chuyenDenManHinhChinh();
                    } else {
                        showThongBao("Tên đăng nhập hoặc mật khẩu không đúng!", true);
                    }
                } else {
                    showThongBao("Tài khoản đã bị khóa!", true);
                }
            } else {
                showThongBao("Tên đăng nhập hoặc mật khẩu không đúng!", true);
            }
        } catch (SQLException e) {
            showThongBao("Lỗi kết nối database: " + e.getMessage(), true);
        } catch (Exception e) {
            showThongBao("Lỗi hệ thống: " + e.getMessage(), true);
        }
    }
    
    private void chuyenDenManHinhChinh() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/layout-chinh.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            
            Stage stage = (Stage) dangNhapBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Hệ thống quản lý khách sạn Louis");
            stage.setMinWidth(1000);
            stage.setMinHeight(600);

            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showThongBao("Không thể tải màn hình chính: " + e.getMessage(), true);
            System.out.println("Không thể tải màn hình chính: " + e.getMessage());
        }
    }
    
    private void showThongBao(String message, boolean isError) {
        thongBaoLabel.setText(message);
        thongBaoLabel.setStyle(isError ? "-fx-text-fill: #dc3545;" : "-fx-text-fill: #28a745;");
        thongBaoLabel.setVisible(true);
    }
}
