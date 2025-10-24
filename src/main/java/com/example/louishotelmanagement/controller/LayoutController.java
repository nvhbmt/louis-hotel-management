package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.ui.models.MenuItemModel;
import com.example.louishotelmanagement.ui.data.MenuData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {

    @FXML
    private TreeView<MenuItemModel> menuTree;

    @FXML
    private BorderPane mainBorderPane;
    
    private TreeItem<MenuItemModel> currentActiveItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMenuTree();
        loadFXML("/com/example/louishotelmanagement/fxml/trang-chu-view.fxml");
        // Set trang chủ làm active mặc định
        setActiveItem(findMenuItemByPath("/com/example/louishotelmanagement/fxml/trang-chu-view.fxml"));
    }

    /** Khởi tạo TreeView và xử lý sự kiện */
    private void setupMenuTree() {
        TreeItem<MenuItemModel> root = MenuData.createMenuTree();
        menuTree.setRoot(root);
        menuTree.setShowRoot(false);

        // Custom hiển thị từng dòng menu
        menuTree.setCellFactory(_ -> new TreeCell<>() {
            @Override
            protected void updateItem(MenuItemModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    getStyleClass().remove("active");
                } else {
                    setText(item.getTitle());
                    setGraphic(item.getIconNode());
                    
                    // Thêm/xóa class active dựa trên trạng thái
                    if (currentActiveItem != null && currentActiveItem.getValue().equals(item)) {
                        getStyleClass().add("active");
                    } else {
                        getStyleClass().remove("active");
                    }
                }
            }
        });

        // Click item con → load FXML
        menuTree.getSelectionModel().selectedItemProperty().addListener((_, _, selected) -> {
            if (selected != null && selected.getValue().getFxmlPath() != null) {
                loadFXML(selected.getValue().getFxmlPath());
                setActiveItem(selected);
            }
        });

        // Click 1 lần vào menu cha → mở/đóng (vì đã ẩn mũi tên)
        menuTree.setOnMouseClicked(event -> {
            TreeItem<MenuItemModel> selected = menuTree.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (!selected.isLeaf()) {
                    // Menu cha - mở/đóng
                    boolean newState = !selected.isExpanded();
                    // Đóng các nhóm khác
                    for (TreeItem<MenuItemModel> item : menuTree.getRoot().getChildren()) {
                        if (item != selected) item.setExpanded(false);
                    }
                    selected.setExpanded(newState);
                } else if (selected.getValue().getFxmlPath() != null) {
                    // Menu con - load FXML và set active
                    loadFXML(selected.getValue().getFxmlPath());
                    setActiveItem(selected);
                }
            }
        });
    }

    /** Load FXML vào vùng trung tâm */
    private void loadFXML(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent view = loader.load();
//chuyen trang
            Object controller = loader.getController();
            if (controller instanceof PhongController) {
                PhongController phongController = (PhongController) controller;
                phongController.setContentSwitcher(this::loadFXML);
            }
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** Set item làm active */
    private void setActiveItem(TreeItem<MenuItemModel> item) {
        if (currentActiveItem != null) {
            // Refresh cell cũ để bỏ active state
            refreshTreeCell(currentActiveItem);
        }
        
        currentActiveItem = item;
        if (item != null) {
            // Refresh cell mới để thêm active state
            refreshTreeCell(item);
        }
    }
    
    /** Refresh tree cell để cập nhật style */
    private void refreshTreeCell(TreeItem<MenuItemModel> item) {
        // Force refresh của tree view để cập nhật cell styling
        menuTree.refresh();
    }
    
    /** Tìm menu item theo FXML path */
    private TreeItem<MenuItemModel> findMenuItemByPath(String fxmlPath) {
        return findMenuItemByPathRecursive(menuTree.getRoot(), fxmlPath);
    }
    
    /** Tìm menu item theo FXML path (recursive) */
    private TreeItem<MenuItemModel> findMenuItemByPathRecursive(TreeItem<MenuItemModel> parent, String fxmlPath) {
        if (parent == null) return null;
        
        // Kiểm tra item hiện tại
        if (fxmlPath.equals(parent.getValue().getFxmlPath())) {
            return parent;
        }
        
        // Tìm trong children
        for (TreeItem<MenuItemModel> child : parent.getChildren()) {
            TreeItem<MenuItemModel> found = findMenuItemByPathRecursive(child, fxmlPath);
            if (found != null) {
                return found;
            }
        }
        
        return null;
    }
}
