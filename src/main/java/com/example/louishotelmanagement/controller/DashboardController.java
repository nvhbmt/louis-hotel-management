package com.example.louishotelmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Button nutTongQuan;
    
    @FXML
    private Button nutQuanLyPhong;
    
    @FXML
    private Button nutQuanLyDichVu;
    
    @FXML
    private Button nutQuanLyGiamGia;
    
    @FXML
    private Button nutQuanLyNhanVien;
    
    @FXML
    private Button nutQuanLyTaiKhoan;
    
    @FXML
    private Button nutQuanLyKhachHang;
    
    @FXML
    private Button nutThongKe;
    
    @FXML
    private ToggleButton nutChuyenTuan;
    
    @FXML
    private ToggleButton nutChuyenThang;
    
    @FXML
    private ToggleButton nutChuyenNam;
    
    @FXML
    private ToggleButton nutChuyenVnd;
    
    @FXML
    private ToggleButton nutChuyenUsd;
    
    @FXML
    private ToggleGroup nhomChuyenThoiGian;
    
    @FXML
    private ToggleGroup nhomChuyenTienTe;
    
    // Submenu buttons
    @FXML
    private Button nutDanhSachPhong;
    
    @FXML
    private Button nutLoaiPhong;
    
    @FXML
    private Button nutTrangThaiPhong;
    
    @FXML
    private Button nutDanhSachDichVu;
    
    @FXML
    private Button nutDanhMucDichVu;
    
    @FXML
    private Button nutDatDichVu;
    
    @FXML
    private Button nutDanhSachNhanVien;
    
    @FXML
    private Button nutLichLamViec;
    
    @FXML
    private Button nutLuongThuong;
    
    // Expandable menu components
    @FXML
    private VBox menuConPhong;
    
    @FXML
    private VBox menuConDichVu;
    
    @FXML
    private VBox menuConNhanVien;
    
    // Main content area for navigation
    @FXML
    private BorderPane mainBorderPane;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize toggle groups
        nhomChuyenThoiGian = new ToggleGroup();
        nhomChuyenTienTe = new ToggleGroup();
        
        nutChuyenTuan.setToggleGroup(nhomChuyenThoiGian);
        nutChuyenThang.setToggleGroup(nhomChuyenThoiGian);
        nutChuyenNam.setToggleGroup(nhomChuyenThoiGian);
        
        nutChuyenVnd.setToggleGroup(nhomChuyenTienTe);
        nutChuyenUsd.setToggleGroup(nhomChuyenTienTe);
        
        // Set default selections
        nutChuyenNam.setSelected(true);
        nutChuyenVnd.setSelected(true);
        
        // Set default active menu
        setActiveMenu(nutTongQuan);
    }

    @FXML
    private void khiNhanTongQuan() {
        System.out.println("Tong quan clicked");
        setActiveMenu(nutTongQuan);
        // Load dashboard content (default view)
        loadContent("/com/example/louishotelmanagement/fxml/dashboard-view.fxml");
    }

    @FXML
    private void khiChuyenQuanLyPhong() {
        System.out.println("Quan ly phong toggle clicked");
        chuyenMenuCon(menuConPhong);
    }

    @FXML
    private void khiChuyenQuanLyDichVu() {
        System.out.println("Quan ly dich vu toggle clicked");
        chuyenMenuCon(menuConDichVu);
    }

    @FXML
    private void khiNhanQuanLyGiamGia() {
        System.out.println("Quan ly giam gia clicked");
        setActiveMenu(nutQuanLyGiamGia);
        loadContent("/com/example/louishotelmanagement/fxml/quan-ly-giam-gia-view.fxml");
    }

    @FXML
    private void khiChuyenQuanLyNhanVien() {
        System.out.println("Quan ly nhan vien toggle clicked");
        chuyenMenuCon(menuConNhanVien);
    }

    @FXML
    private void khiNhanQuanLyTaiKhoan() {
        System.out.println("Quan ly tai khoan clicked");
        setActiveMenu(nutQuanLyTaiKhoan);
        loadContent("/com/example/louishotelmanagement/fxml/quan-ly-tai-khoan-view.fxml");
    }

    @FXML
    private void khiNhanQuanLyKhachHang() {
        System.out.println("Quan ly khach hang clicked");
        setActiveMenu(nutQuanLyKhachHang);
        loadContent("/com/example/louishotelmanagement/fxml/quan-ly-khach-hang-view.fxml");
    }

    @FXML
    private void khiNhanThongKe() {
        System.out.println("Thong ke clicked");
        setActiveMenu(nutThongKe);
        loadContent("/com/example/louishotelmanagement/fxml/thong-ke-view.fxml");
    }

    @FXML
    private void khiChuyenTuan() {
        System.out.println("Tuan toggle selected");
        // TODO: Update chart to show weekly data
    }

    @FXML
    private void khiChuyenThang() {
        System.out.println("Thang toggle selected");
        // TODO: Update chart to show monthly data
    }

    @FXML
    private void khiChuyenNam() {
        System.out.println("Nam toggle selected");
        // TODO: Update chart to show yearly data
    }

    @FXML
    private void khiChuyenVnd() {
        System.out.println("VND toggle selected");
        // TODO: Update chart currency to VND
    }

    @FXML
    private void khiChuyenUsd() {
        System.out.println("USD toggle selected");
        // TODO: Update chart currency to USD
    }
    
    // Submenu event handlers
    @FXML
    private void khiNhanDanhSachPhong() {
        System.out.println("Danh sach phong clicked");
        setActiveMenu(nutDanhSachPhong);
        loadContent("/com/example/louishotelmanagement/fxml/danh-sach-phong-view.fxml");
    }
    
    @FXML
    private void khiNhanLoaiPhong() {
        System.out.println("Loai phong clicked");
        setActiveMenu(nutLoaiPhong);
        loadContent("/com/example/louishotelmanagement/fxml/loai-phong-view.fxml");
    }
    
    @FXML
    private void khiNhanTrangThaiPhong() {
        System.out.println("Trang thai phong clicked");
        setActiveMenu(nutTrangThaiPhong);
        loadContent("/com/example/louishotelmanagement/fxml/trang-thai-phong-view.fxml");
    }
    
    @FXML
    private void khiNhanDanhSachDichVu() {
        System.out.println("Danh sach dich vu clicked");
        setActiveMenu(nutDanhSachDichVu);
        loadContent("/com/example/louishotelmanagement/fxml/danh-sach-dich-vu-view.fxml");
    }
    
    @FXML
    private void khiNhanDanhMucDichVu() {
        System.out.println("Danh muc dich vu clicked");
        setActiveMenu(nutDanhMucDichVu);
        loadContent("/com/example/louishotelmanagement/fxml/danh-muc-dich-vu-view.fxml");
    }
    
    @FXML
    private void khiNhanDatDichVu() {
        System.out.println("Dat dich vu clicked");
        setActiveMenu(nutDatDichVu);
        loadContent("/com/example/louishotelmanagement/fxml/dat-dich-vu-view.fxml");
    }
    
    @FXML
    private void khiNhanDanhSachNhanVien() {
        System.out.println("Danh sach nhan vien clicked");
        setActiveMenu(nutDanhSachNhanVien);
        loadContent("/com/example/louishotelmanagement/fxml/danh-sach-nhan-vien-view.fxml");
    }
    
    @FXML
    private void khiNhanLichLamViec() {
        System.out.println("Lich lam viec clicked");
        setActiveMenu(nutLichLamViec);
        loadContent("/com/example/louishotelmanagement/fxml/lich-lam-viec-view.fxml");
    }
    
    @FXML
    private void khiNhanLuongThuong() {
        System.out.println("Luong thuong clicked");
        setActiveMenu(nutLuongThuong);
        loadContent("/com/example/louishotelmanagement/fxml/luong-thuong-view.fxml");
    }
    
    // Helper method to toggle submenu visibility
    private void chuyenMenuCon(VBox menuCon) {
        if (menuCon.isVisible()) {
            menuCon.setVisible(false);
            menuCon.setManaged(false);
        } else {
            menuCon.setVisible(true);
            menuCon.setManaged(true);
        }
    }
    
    // Helper method to set active menu styling
    private void setActiveMenu(Button activeButton) {
        // Reset all menu buttons to default style
        resetMenuStyles();
        
        // Set active button style
        activeButton.setStyle("-fx-background-color: #e3f2fd; -fx-text-fill: #1976d2; -fx-font-size: 14px; -fx-cursor: hand; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12; -fx-border-color: #1976d2; -fx-border-width: 1;");
    }
    
    // Helper method to reset all menu button styles
    private void resetMenuStyles() {
        Button[] menuButtons = {
            nutTongQuan, nutQuanLyPhong, nutQuanLyDichVu, nutQuanLyGiamGia,
            nutQuanLyNhanVien, nutQuanLyTaiKhoan, nutQuanLyKhachHang, nutThongKe,
            nutDanhSachPhong, nutLoaiPhong, nutTrangThaiPhong, nutDanhSachDichVu,
            nutDanhMucDichVu, nutDatDichVu, nutDanhSachNhanVien, nutLichLamViec, nutLuongThuong
        };
        
        for (Button button : menuButtons) {
            if (button != null) {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: #333333; -fx-font-size: 14px; -fx-cursor: hand; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12;");
            }
        }
    }
    
    // Helper method to load content from FXML
    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            javafx.scene.Parent content = loader.load();
            
            // Replace the center content
            mainBorderPane.setCenter(content);
            
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
