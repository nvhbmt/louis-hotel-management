package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.TaiKhoanDAO;
import com.example.louishotelmanagement.model.TaiKhoan;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.PasswordUtil;
import com.example.louishotelmanagement.util.ThongBaoUtil;

import javafx.application.Platform;
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

        tenDangNhapField.setText("manager");
        matKhauField.setText("manager123");
        
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
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng nhập đầy đủ thông tin!");
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
                        // ThongBaoUtil.hienThiThongBao("Thành công", "Đăng nhập thành công!");
                        
                        // Chuyển đến màn hình chính
                        chuyenDenManHinhChinh();
                    } else {
                        ThongBaoUtil.hienThiLoi("Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng!");
                    }
                } else {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Tài khoản đã bị khóa!");
                }
            } else {
                ThongBaoUtil.hienThiLoi("Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        } catch (SQLException e) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Lỗi kết nối database: " + e.getMessage());
        } catch (Exception e) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Lỗi hệ thống: " + e.getMessage());
        }
    }
    
    private void chuyenDenManHinhChinh() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/layout-chinh.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            
            Stage stage = (Stage) dangNhapBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Hệ thống quản lý khách sạn Louis");
            
            // Reset thuộc tính Stage cho màn hình chính
            stage.setResizable(true);
            stage.setMinWidth(1000);
            stage.setMinHeight(600);
            stage.setMaxWidth(Double.MAX_VALUE);
            stage.setMaxHeight(Double.MAX_VALUE);
            
            // Đặt kích thước và maximize
            stage.setWidth(1200);
            stage.setHeight(800);
            stage.centerOnScreen();
            stage.show();
            
            // Maximize sau khi Stage đã được hiển thị
            Platform.runLater(() -> {
                stage.setMaximized(true);
            });
        } catch (IOException e) {   
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thể tải màn hình chính: " + e.getMessage());
            System.out.println("Không thể tải màn hình chính: " + e.getMessage());
        }
    }
}
