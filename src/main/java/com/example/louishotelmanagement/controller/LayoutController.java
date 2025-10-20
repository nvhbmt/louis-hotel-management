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

public class LayoutController implements Initializable,ContentSwitcher {

    @FXML
    private TreeView<MenuItemModel> menuTree;

    @FXML
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMenuTree();
        loadFXML("/com/example/louishotelmanagement/fxml/thong-ke-content.fxml");
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
                } else {
                    setText(item.getTitle());
                    setGraphic(item.getIconNode());
                }
            }
        });

        // Click item con → load FXML
        menuTree.getSelectionModel().selectedItemProperty().addListener((_, _, selected) -> {
            if (selected != null && selected.getValue().getFxmlPath() != null) {
                loadFXML(selected.getValue().getFxmlPath());
            }
        });

        // Click 1 lần vào menu cha → mở/đóng
        menuTree.setOnMouseClicked(event -> {
            TreeItem<MenuItemModel> selected = menuTree.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.isLeaf()) {
                boolean newState = !selected.isExpanded();
                // Đóng các nhóm khác
                for (TreeItem<MenuItemModel> item : menuTree.getRoot().getChildren()) {
                    if (item != selected) item.setExpanded(false);
                }
                selected.setExpanded(newState);
            }
        });
    }

    /** Load FXML vào vùng trung tâm */
    private void loadFXML(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent view = loader.load();

            Object controller = loader.getController();

            if (controller instanceof PhongController) {
                PhongController phongController = (PhongController) controller;
                // Truyền LayoutController (là ContentSwitcher) cho PhongController
                phongController.setContentSwitcher(this);
            }

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void switchContent(String fxmlPath) {
        loadFXML(fxmlPath);
    }
}
