package com.example.louishotelmanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.ContentManager;
import com.example.louishotelmanagement.util.MenuBuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LayoutController implements Initializable, ContentSwitcher {

    @FXML
    private VBox menuContainer;

    @FXML
    private BorderPane mainBorderPane;
    
    @FXML
    private Label userInfoLabel;
    
    @FXML
    private Button logoutBtn;
    
    private Button currentActiveButton;
    private Map<String, Button> fxmlPathToButton = new HashMap<>();
    private Map<Button, VBox> groupButtonToSubmenu = new HashMap<>();
    private AuthService authService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authService = AuthService.getInstance();
        setupUserInfo();
        setupMenuVBox();
        loadFXML("/com/example/louishotelmanagement/fxml/trang-chu-view.fxml");
        // Set trang chủ làm active mặc định
        setActiveButtonByPath("/com/example/louishotelmanagement/fxml/trang-chu-view.fxml");
    }
    
    private void setupUserInfo() {
        if (authService.isLoggedIn()) {
            String hoTen = authService.getCurrentUser().getNhanVien().getHoTen();
            String userRole = authService.getCurrentUserRole() == "manager" ? "Quản lý" : "Nhân viên";
            userInfoLabel.setText(hoTen + " (" + userRole + ")");
        }
        
        logoutBtn.setOnAction(_ -> handleLogout());
    }
    
    private void handleLogout() {
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận đăng xuất", "Bạn có chắc chắn muốn đăng xuất?")) {
            authService.logout();
            showLoginScreen();
        }
    }
    
    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/dang-nhap-view.fxml"));
            Scene scene = new Scene(loader.load(), 400, 500);
            
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Đăng nhập - Hệ thống quản lý khách sạn Louis");
            
            // Reset kích thước và thuộc tính Stage
            stage.setResizable(false);
            stage.setMinWidth(400);
            stage.setMinHeight(500);
            stage.setMaxWidth(400);
            stage.setMaxHeight(500);
            stage.setWidth(400);
            stage.setHeight(500);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Khởi tạo VBox menu và xử lý sự kiện */
    private void setupMenuVBox() {
        menuContainer.getChildren().clear();
        
        // Sử dụng MenuBuilder để xây dựng menu
        MenuBuilder.MenuBuildResult result = MenuBuilder.buildMenu(
            // Handler cho menu item click
            (fxmlPath, button) -> {
                loadFXML(fxmlPath);
                setActiveButton(button);
            },
            // Handler cho submenu toggle
            this::toggleSubmenu
        );
        
        // Lấy các components từ kết quả
        VBox builtMenu = result.getMenuContainer();
        fxmlPathToButton = result.getFxmlPathToButton();
        groupButtonToSubmenu = result.getGroupButtonToSubmenu();
        
        // Thêm menu vào container
        menuContainer.getChildren().addAll(builtMenu.getChildren());
    }
    
    /** Toggle submenu visibility */
    private void toggleSubmenu(Button groupHeader, VBox submenu) {
        boolean isExpanded = submenu.isVisible();
        
        // Close all other groups
        for (Map.Entry<Button, VBox> entry : groupButtonToSubmenu.entrySet()) {
            if (entry.getKey() != groupHeader) {
                entry.getValue().setVisible(false);
                entry.getValue().setManaged(false);
                entry.getKey().getStyleClass().remove("expanded");
                // Reset arrow rotation (arrow ở cuối cùng)
                MenuBuilder.resetArrowRotation(entry.getKey());
            }
        }
        
        // Toggle current group
        submenu.setVisible(!isExpanded);
        submenu.setManaged(!isExpanded);
        
        if (!isExpanded) {
            groupHeader.getStyleClass().add("expanded");
            // Rotate arrow (arrow ở cuối cùng)
            MenuBuilder.rotateArrow(groupHeader, 90);
        } else {
            groupHeader.getStyleClass().remove("expanded");
            // Reset arrow rotation
            MenuBuilder.rotateArrow(groupHeader, 0);
        }
    }
    
    
    /** Load FXML vào vùng trung tâm với caching */
    private void loadFXML(String path) {
        ContentManager.loadFXML(path, mainBorderPane, this);
    }
    
    /** Set button làm active */
    private void setActiveButton(Button button) {
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("active");
        }
        
        currentActiveButton = button;
        if (button != null) {
            button.getStyleClass().add("active");
        }
    }
    
    /** Set active button theo FXML path */
    private void setActiveButtonByPath(String fxmlPath) {
        Button button = fxmlPathToButton.get(fxmlPath);
        if (button != null) {
            setActiveButton(button);
            // Expand parent group if needed
            expandToButton(button);
        }
    }
    
    /** Expand group để hiển thị button được chọn */
    private void expandToButton(Button button) {
        // Find which group contains this button
        for (Map.Entry<Button, VBox> entry : groupButtonToSubmenu.entrySet()) {
            if (entry.getValue().getChildren().contains(button)) {
                // Expand this group
                VBox submenu = entry.getValue();
                if (!submenu.isVisible()) {
                    toggleSubmenu(entry.getKey(), submenu);
                }
                break;
            }
        }
    }

    @Override
    public void switchContent(String fxmlPath) {
        switchContent(fxmlPath, true);
    }
    
    @Override
    public void switchContent(String fxmlPath, boolean updateMenuActive) {
        loadFXML(fxmlPath);
        
        if (updateMenuActive) {
            setActiveButtonByPath(fxmlPath);
        }
    }
}
