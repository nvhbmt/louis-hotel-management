package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ContentManager;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.MenuBuilder;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.LayoutView;
import com.example.louishotelmanagement.view.QuanLyDichVuView;
import com.example.louishotelmanagement.view.QuanLyHoaDonView;
import com.example.louishotelmanagement.view.QuanLyKhachHangView;
import com.example.louishotelmanagement.view.QuanLyKhuyenMaiView;
import com.example.louishotelmanagement.view.QuanLyNhanVienView;
import com.example.louishotelmanagement.view.QuanLyPhieuDatPhongView;
import com.example.louishotelmanagement.view.ThongKeView;
import com.example.louishotelmanagement.view.QuanLyPhongView;
import com.example.louishotelmanagement.view.DangNhapView;
import com.example.louishotelmanagement.view.DatDichVuView;
import com.example.louishotelmanagement.view.QuanLyLoaiPhongView;
import com.example.louishotelmanagement.view.TrangChuView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LayoutController implements ContentSwitcher, Initializable {

    // Static reference để các controller khác có thể access
    private static LayoutController instance;

    private LayoutView view;

    // UI Components from view
    private VBox menuContainer;
    private BorderPane mainBorderPane;
    private Label userInfoLabel;
    private Button logoutBtn;

    private Button currentActiveButton;
    private Map<Parent, Button> rootToButton = new HashMap<>();
    private Map<String, Button> fxmlPathToButton = new HashMap<>();
    private Map<Button, VBox> groupButtonToSubmenu = new HashMap<>();
    private AuthService authService;

    public LayoutController(LayoutView view) {
        this.view = view;

        // Get UI components from view
        this.menuContainer = view.getMenuContainer();
        this.mainBorderPane = view.getMainBorderPane();
        this.userInfoLabel = view.getUserInfoLabel();
        this.logoutBtn = view.getLogoutBtn();

        setupController();
    }

    public void setupController() {
        instance = this; // Set static instance
        authService = AuthService.getInstance();
        setupUserInfo();
        setupMenuVBox();
        // Load TrangChuView programmatically instead of FXML
        loadParent(new TrangChuView().getRoot());
        // Note: TrangChuController sẽ tự setup ContentSwitcher trong initialize() method của nó
    }

    /**
     * Get current LayoutController instance
     */
    public static LayoutController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Method kept for compatibility but logic moved to setupController()
        // This method may be called by FXML loader if still using FXML
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
        Scene scene = new Scene(new DangNhapView().getRoot(), 400, 500);

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
        stage.show();
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
                if (currentActiveButton != null) {
                    currentActiveButton.getStyleClass().remove("active");
                    currentActiveButton = null;
                }
            }
        }
    }

    /**
     * Khởi tạo VBox menu và xử lý sự kiện
     */
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

    /**
     * Toggle submenu visibility
     */
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


    /**
     * Load FXML vào vùng trung tâm với caching
     */
    private void loadFXML(String path) {
        ContentManager.loadFXML(path, mainBorderPane, this);
    }

    /**
     * Load Parent vào vùng trung tâm
     */
    private void loadParent(Parent root) {
        mainBorderPane.setCenter(root);
    }

    /**
     * Set button làm active
     */
    private void setActiveButton(Button button) {
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("active");
        }

        currentActiveButton = button;
        if (button != null) {
            button.getStyleClass().add("active");
        }
    }


    /**
     * Expand group để hiển thị button được chọn
     */
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
        // Handle QuanLyDichVuView identifier
        if (QuanLyDichVuView.getIdentifier().equals(fxmlPath)) {
            QuanLyDichVuView quanLyDichVuView = QuanLyDichVuView.getInstance();
            switchContent(quanLyDichVuView.getRoot());
            return;
        }

        // Handle QuanLyLoaiPhongView identifier
        if (QuanLyLoaiPhongView.getIdentifier().equals(fxmlPath)) {
            QuanLyLoaiPhongView quanLyLoaiPhongView = QuanLyLoaiPhongView.getInstance();
            switchContent(quanLyLoaiPhongView.getRoot());
            return;
        }

        // Handle QuanLyHoaDonView identifier
        if (QuanLyHoaDonView.getIdentifier().equals(fxmlPath)) {
            QuanLyHoaDonView quanLyHoaDonView = QuanLyHoaDonView.getInstance();
            switchContent(quanLyHoaDonView.getRoot());
            return;
        }

        // Handle QuanLyKhachHangView identifier
        if (QuanLyKhachHangView.getIdentifier().equals(fxmlPath)) {
            QuanLyKhachHangView quanLyKhachHangView = QuanLyKhachHangView.getInstance();
            switchContent(quanLyKhachHangView.getRoot());
            return;
        }

        // Handle QuanLyKhuyenMaiView identifier
        if (QuanLyKhuyenMaiView.getIdentifier().equals(fxmlPath)) {
            QuanLyKhuyenMaiView quanLyKhuyenMaiView = QuanLyKhuyenMaiView.getInstance();
            switchContent(quanLyKhuyenMaiView.getRoot());
            return;
        }

        // Handle QuanLyNhanVienView identifier
        if (QuanLyNhanVienView.getIdentifier().equals(fxmlPath)) {
            QuanLyNhanVienView quanLyNhanVienView = QuanLyNhanVienView.getInstance();
            switchContent(quanLyNhanVienView.getRoot());
            return;
        }

        // Handle QuanLyPhieuDatPhongView identifier
        if (QuanLyPhieuDatPhongView.getIdentifier().equals(fxmlPath)) {
            QuanLyPhieuDatPhongView quanLyPhieuDatPhongView = QuanLyPhieuDatPhongView.getInstance();
            switchContent(quanLyPhieuDatPhongView.getRoot());
            return;
        }

        // Handle ThongKeView identifier
        if (ThongKeView.getIdentifier().equals(fxmlPath)) {
            ThongKeView thongKeView = ThongKeView.getInstance();
            switchContent(thongKeView.getRoot());
            return;
        }

        // Handle DatDichVuView identifier
        if (DatDichVuView.getIdentifier().equals(fxmlPath)) {
            DatDichVuView datDichVuView = DatDichVuView.getInstance();
            switchContent(datDichVuView.getRoot());
            return;
        }

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
     * This mapping is used when ContentSwitcher.switchContent(String fxmlPath) is called
     */
    private void setupFxmlPathToButtonMapping() {
        // Clear any existing mappings
        fxmlPathToButton.clear();

 
        fxmlPathToButton.put(QuanLyPhongView.getIdentifier(),
                           findButtonByTitle("Quản lý phòng"));
        fxmlPathToButton.put(QuanLyLoaiPhongView.getIdentifier(),
                           findButtonByTitle("Loại phòng"));

        // ============================================
        // BOOKING MANAGEMENT
        // ============================================
        fxmlPathToButton.put(QuanLyPhieuDatPhongView.getIdentifier(),
                           findButtonByTitle("Phiếu đặt phòng"));

        // ============================================
        // SERVICE MANAGEMENT
        // ============================================
        fxmlPathToButton.put(DatDichVuView.getIdentifier(),
                           findButtonByTitle("Cung cấp dịch vụ"));
        fxmlPathToButton.put(QuanLyDichVuView.getIdentifier(),
                           findButtonByTitle("Quản lý dịch vụ"));

        // ============================================
        // PROMOTION & BILLING
        // ============================================
        fxmlPathToButton.put(QuanLyKhuyenMaiView.getIdentifier(),
                           findButtonByTitle("Khuyến mãi"));
        fxmlPathToButton.put(QuanLyHoaDonView.getIdentifier(),
                           findButtonByTitle("Hóa đơn"));

        // ============================================
        // REPORTING & ANALYTICS
        // ============================================
        fxmlPathToButton.put(ThongKeView.getIdentifier(),
                           findButtonByTitle("Thống kê"));

        // ============================================
        // USER MANAGEMENT
        // ============================================
        fxmlPathToButton.put(QuanLyNhanVienView.getIdentifier(),
                           findButtonByTitle("Nhân viên"));
        fxmlPathToButton.put(QuanLyKhachHangView.getIdentifier(),
                           findButtonByTitle("Khách hàng"));
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
