package com.example.louishotelmanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;

import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.ContentManager;
import com.example.louishotelmanagement.util.MenuBuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LayoutController implements Initializable, ContentSwitcher {

    // Static reference để các controller khác có thể access
    private static LayoutController instance;

    @FXML
    private VBox menuContainer;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label userInfoLabel;

    @FXML
    private Button logoutBtn;

    private Button currentActiveButton;
    private Map<Parent, Button> rootToButton = new HashMap<>();
    private Map<String, Button> fxmlPathToButton = new HashMap<>();
    private Map<Button, VBox> groupButtonToSubmenu = new HashMap<>();
    private AuthService authService;

    /**
     * Get current LayoutController instance
     */
    public static LayoutController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this; // Set static instance
        authService = AuthService.getInstance();
        setupUserInfo();
        setupMenuVBox();
        loadFXML("/com/example/louishotelmanagement/fxml/trang-chu-view.fxml");
        // Note: TrangChuController sẽ tự setup ContentSwitcher trong initialize() method của nó
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
    @Override
    public void switchContent(Parent root) {
        if (root != null) {
            // Load Parent vào vùng trung tâm
            loadParent(root);

            // Tìm button tương ứng và set active
            Button correspondingButton = rootToButton.get(root);
            if (correspondingButton != null) {
                setActiveButton(correspondingButton);
            } else {
                // Nếu không tìm thấy button tương ứng, xóa trạng thái active
                if (currentActiveButton != null) {
                    currentActiveButton.getStyleClass().remove("active");
                    currentActiveButton = null;
                }
            }
        }
    }

    /** Khởi tạo VBox menu và xử lý sự kiện */
    private void setupMenuVBox() {
        menuContainer.getChildren().clear();

        // Sử dụng MenuBuilder để xây dựng menu
        MenuBuilder.MenuBuildResult result = MenuBuilder.buildMenu(
            // Handler cho menu item click
            (root, button) -> {
                loadParent(root);
                setActiveButton(button);
            },
            // Handler cho submenu toggle
            this::toggleSubmenu
        );
        
        // Lấy các components từ kết quả
        VBox builtMenu = result.getMenuContainer();
        rootToButton = result.getRootToButton();
        groupButtonToSubmenu = result.getGroupButtonToSubmenu();

        // Setup fxmlPath to button mapping for string-based navigation
        setupFxmlPathToButtonMapping();
        
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

    /** Load Parent vào vùng trung tâm */
    private void loadParent(Parent root) {
        mainBorderPane.setCenter(root);
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
        // Load FXML to get Parent, then delegate to Parent-based method
        try {
            Parent root = loadFXMLToParent(fxmlPath);
            switchContent(root);

            // Try to find and set active button if this fxmlPath corresponds to a menu item
            Button correspondingButton = findButtonForFxmlPath(fxmlPath);
            if (correspondingButton != null) {
                setActiveButtonForNavigation(correspondingButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to direct FXML loading
            loadFXML(fxmlPath);
        }
    }

    /**
     * Setup mapping from fxmlPath to Button for string-based navigation
     */
    private void setupFxmlPathToButtonMapping() {
        // Create mapping based on known fxmlPath patterns
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/trang-chu-view.fxml", findButtonByTitle("Trang chủ"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/phong-view.fxml", findButtonByTitle("Đặt phòng"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/quan-ly-phong-view.fxml", findButtonByTitle("Quản lý phòng"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/quan-ly-loai-phong-view.fxml", findButtonByTitle("Loại phòng"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/quan-ly-phieu-dat-phong.fxml", findButtonByTitle("Phiếu đặt phòng"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/dat-dich-vu-view.fxml", findButtonByTitle("Cung cấp dịch vụ"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/quan-ly-dich-vu-view.fxml", findButtonByTitle("Quản lý dịch vụ"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/quan-ly-khuyen-mai-view.fxml", findButtonByTitle("Khuyến mãi"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/quan-ly-hoa-don-view.fxml", findButtonByTitle("Hóa đơn"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/thong-ke-view.fxml", findButtonByTitle("Thống kê"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/quan-ly-nhan-vien-view.fxml", findButtonByTitle("Nhân viên"));
        fxmlPathToButton.put("/com/example/louishotelmanagement/fxml/quan-ly-khach-hang-view.fxml", findButtonByTitle("Khách hàng"));
    }

    /**
     * Find button by its title text
     */
    private Button findButtonByTitle(String title) {
        for (Button button : rootToButton.values()) {
            if (title.equals(button.getText())) {
                return button;
            }
        }
        return null;
    }

    /**
     * Set active button for navigation, handling submenu expansion if needed
     */
    private void setActiveButtonForNavigation(Button button) {
        // Check if this button is in a submenu (not a direct menu button)
        Button parentGroupButton = findParentGroupButton(button);

        if (parentGroupButton != null) {
            // This is a submenu button, expand parent group
            expandToButton(button); // expandToButton expects the submenu button, not the group button
            // Set active only for the submenu button
            setActiveButton(button);
        } else {
            // This is a direct menu button
            setActiveButton(button);
        }
    }

    /**
     * Find the parent group button for a submenu button
     */
    private Button findParentGroupButton(Button submenuButton) {
        for (Map.Entry<Button, VBox> entry : groupButtonToSubmenu.entrySet()) {
            Button groupButton = entry.getKey();
            VBox submenu = entry.getValue();

            // Check if the submenu contains this button
            if (submenu.getChildren().contains(submenuButton)) {
                return groupButton;
            }
        }
        return null; // Not a submenu button
    }

    /**
     * Find button corresponding to fxmlPath using the fxmlPathToButton mapping
     */
    private Button findButtonForFxmlPath(String fxmlPath) {
        return fxmlPathToButton.get(fxmlPath);
    }

    /**
     * Load FXML and return Parent instead of setting it directly to container
     */
    private Parent loadFXMLToParent(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Set up the controller with ContentSwitcher if needed
        Object controller = loader.getController();
        if (controller != null) {
            ContentManager.updateControllerSwitcher(controller, this);
        }

        return root;
    }
}
