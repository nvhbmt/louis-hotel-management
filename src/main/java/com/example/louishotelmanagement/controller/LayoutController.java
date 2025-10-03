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

public class LayoutController implements Initializable {

    // Flag to prevent recursive calls
    private boolean dangCapNhatThongKe = false;
    
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
        
        // Check if toggle buttons exist before setting toggle groups
        if (nutChuyenTuan != null && nutChuyenThang != null && nutChuyenNam != null) {
            nutChuyenTuan.setToggleGroup(nhomChuyenThoiGian);
            nutChuyenThang.setToggleGroup(nhomChuyenThoiGian);
            nutChuyenNam.setToggleGroup(nhomChuyenThoiGian);
            
            // Set default selections
            nutChuyenNam.setSelected(true);
        }
        
        if (nutChuyenVnd != null && nutChuyenUsd != null) {
            nutChuyenVnd.setToggleGroup(nhomChuyenTienTe);
            nutChuyenUsd.setToggleGroup(nhomChuyenTienTe);
            
            // Set default selections
            nutChuyenVnd.setSelected(true);
        }
        
        // Set default active menu - Thống kê
        setActiveMenu(nutThongKe);
        // Load default content - Thống kê
        loadContent("/com/example/louishotelmanagement/fxml/thong-ke-content.fxml");
    }

    // Method to refresh statistical data without reloading the FXML
    private void refreshStatisticalData() {
        // TODO: Refresh statistics data from database
        System.out.println("Refreshing statistical data...");
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
        // Prevent recursive calls
        if (dangCapNhatThongKe) {
            System.out.println("Already refreshing statistics, skipping...");
            return;
        }
        
        System.out.println("Thong ke clicked");
        dangCapNhatThongKe = true;
        
        try {
            setActiveMenu(nutThongKe);
            loadContent("/com/example/louishotelmanagement/fxml/thong-ke-content.fxml");
            refreshStatisticalData();
        } finally {
            // Reset flag after completion
            dangCapNhatThongKe = false;
        }
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
        setActiveMenu(nutQuanLyPhong);
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
        // Remove all menu buttons to default style
        resetMenuStyles();
        
        // Set active button style
        activeButton.setStyle("-fx-background-color: #e3f2fd; -fx-text-fill: #1976d2; -fx-font-size: 14px; -fx-cursor: hand; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12; -fx-border-color: #1976d2; -fx-border-width: 1;");
    }
    
    // Helper method to reset all menu button styles
    private void resetMenuStyles() {
        Button[] menuButtons = {
            nutThongKe, nutQuanLyPhong, nutQuanLyDichVu, nutQuanLyGiamGia,
            nutQuanLyNhanVien, nutQuanLyTaiKhoan, nutQuanLyKhachHang,
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
            
            // If this is thong-ke-content, setup toggle buttons
            if (fxmlPath.contains("thong-ke-content")) {
                setupThongKeToggleButtons(content);
            }
            
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
    
    // Setup toggle buttons for thong-ke content
    private void setupThongKeToggleButtons(javafx.scene.Parent content) {
        try {
            // Find toggle buttons in the loaded content
            ToggleButton tuanBtn = (ToggleButton) content.lookup("#nutChuyenTuan");
            ToggleButton thangBtn = (ToggleButton) content.lookup("#nutChuyenThang");
            ToggleButton namBtn = (ToggleButton) content.lookup("#nutChuyenNam");
            ToggleButton vndBtn = (ToggleButton) content.lookup("#nutChuyenVnd");
            ToggleButton usdBtn = (ToggleButton) content.lookup("#nutChuyenUsd");
            
            if (tuanBtn != null && thangBtn != null && namBtn != null) {
                tuanBtn.setToggleGroup(nhomChuyenThoiGian);
                thangBtn.setToggleGroup(nhomChuyenThoiGian);
                namBtn.setToggleGroup(nhomChuyenThoiGian);
                namBtn.setSelected(true);
            }
            
            if (vndBtn != null && usdBtn != null) {
                vndBtn.setToggleGroup(nhomChuyenTienTe);
                usdBtn.setToggleGroup(nhomChuyenTienTe);
                vndBtn.setSelected(true);
            }
        } catch (Exception e) {
            System.err.println("Error setting up toggle buttons: " + e.getMessage());
        }
    }
}
