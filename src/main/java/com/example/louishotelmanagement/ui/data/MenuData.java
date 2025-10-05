package com.example.louishotelmanagement.ui.data;

import com.example.louishotelmanagement.ui.models.MenuItemModel;
import javafx.scene.control.TreeItem;

import java.util.List;

public class MenuData {

    public static TreeItem<MenuItemModel> createMenuTree() {
        TreeItem<MenuItemModel> root = new TreeItem<>(new MenuItemModel("Root", "", null, true));
        root.setExpanded(true);

        // --- Quản lý đặt phòng ---
        TreeItem<MenuItemModel> bookingGroup = new TreeItem<>(
                new MenuItemModel("Quản lý đặt phòng", null, null, true)
        );
        bookingGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Đặt phòng trước", null,
                        "/com/example/louishotelmanagement/fxml/dat-phong-view.fxml", false)),
                new TreeItem<>(new MenuItemModel("Nhận phòng", null,
                        "/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml", false))
        ));

        // --- Dịch vụ ---
        TreeItem<MenuItemModel> serviceGroup = new TreeItem<>(
                new MenuItemModel("Dịch vụ", null, null, true)
        );
        serviceGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Thêm dịch vụ", null,
                        "/com/example/louishotelmanagement/fxml/them-dich-vu.fxml", false)),
                new TreeItem<>(new MenuItemModel("Hủy dịch vụ", null,
                        "/com/example/louishotelmanagement/fxml/huy-dich-vu.fxml", false))
        ));

        root.getChildren().addAll(List.of(bookingGroup, serviceGroup));
        return root;
    }
}
