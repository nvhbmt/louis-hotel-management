package com.example.louishotelmanagement.ui.data;

import com.example.louishotelmanagement.ui.models.MenuItemModel;
import javafx.scene.control.TreeItem;

import java.util.List;

public class MenuData {

    public static TreeItem<MenuItemModel> createMenuTree() {
        TreeItem<MenuItemModel> root = new TreeItem<>(new MenuItemModel("Root", null, null));
        root.setExpanded(true);

        TreeItem<MenuItemModel> home = new TreeItem<>(
                new MenuItemModel("Trang chủ", "mdi2h-home",
                        "/com/example/louishotelmanagement/fxml/thong-ke-content.fxml")
        );

        TreeItem<MenuItemModel> bookingGroup = new TreeItem<>(
                new MenuItemModel("Đặt phòng", "mdi2b-bed", null)
        );
        bookingGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Đặt phòng trước", "mdi2c-calendar-plus",
                        "/com/example/louishotelmanagement/fxml/dat-phong-view.fxml")),
                new TreeItem<>(new MenuItemModel("Nhận phòng", "mdi2l-login",
                        "/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml")),
                new TreeItem<>(new MenuItemModel("Đổi phòng", "mdi2s-swap-horizontal",
                        "/com/example/louishotelmanagement/fxml/doi-phong-view.fxml")),
                new TreeItem<>(new MenuItemModel("Trả phòng", "mdi2l-logout",
                        "/com/example/louishotelmanagement/fxml/huy-phong-view.fxml"))
        ));

        // --- ☕ Dịch vụ ---
        TreeItem<MenuItemModel> serviceGroup = new TreeItem<>(
                new MenuItemModel("Dịch vụ", "mdi2c-coffee", null)
        );
        serviceGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Thêm dịch vụ", "mdi2p-plus-circle",
                        "/com/example/louishotelmanagement/fxml/them-dich-vu.fxml")),
                new TreeItem<>(new MenuItemModel("Cung cấp dịch vụ", "mdi2h-handshake",
                        "/com/example/louishotelmanagement/fxml/cung-cap-dich-vu.fxml")),
                new TreeItem<>(new MenuItemModel("Hủy dịch vụ", "mdi2c-close-circle",
                        "/com/example/louishotelmanagement/fxml/huy-dich-vu.fxml"))
        ));

        // --- 💰 Hóa đơn ---
        TreeItem<MenuItemModel> invoiceGroup = new TreeItem<>(
                new MenuItemModel("Hóa đơn", "mdi2c-cash-multiple", null)
        );
        invoiceGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Thanh toán", "mdi2c-cash",
                        "/com/example/louishotelmanagement/fxml/thanh-toan-view.fxml")),
                new TreeItem<>(new MenuItemModel("Giảm giá", "mdi2s-sale",
                        "/com/example/louishotelmanagement/fxml/giam-gia-view.fxml")),
                new TreeItem<>(new MenuItemModel("Hiển thị hóa đơn", "mdi2f-file-document",
                        "/com/example/louishotelmanagement/fxml/hoa-don-view.fxml"))
        ));

        // --- 📊 Thống kê ---
        TreeItem<MenuItemModel> reportGroup = new TreeItem<>(
                new MenuItemModel("Thống kê", "mdi2c-chart-bar", null)
        );
        reportGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Phòng đặt nhiều nhất", "mdi2f-file",
                        "/com/example/louishotelmanagement/fxml/thong-ke-phong-view.fxml")),
                new TreeItem<>(new MenuItemModel("Doanh thu", "mdi2c-chart-line",
                        "/com/example/louishotelmanagement/fxml/thong-ke-doanh-thu-view.fxml"))
        ));

        TreeItem<MenuItemModel> staff = new TreeItem<>(
                new MenuItemModel("Nhân viên", "mdi2a-account-group",
                        "/com/example/louishotelmanagement/fxml/nhan-vien-view.fxml")
        );

        // --- 🏨 Quản lý phòng ---
        TreeItem<MenuItemModel> roomGroup = new TreeItem<>(
                new MenuItemModel("Phòng", "mdi2b-bed-outline", null)
        );
        roomGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Quản lý phòng", "mdi2b-bed",
                        "/com/example/louishotelmanagement/fxml/quan-ly-phong-view.fxml")),
                new TreeItem<>(new MenuItemModel("Loại phòng", "mdi2t-tag",
                        "/com/example/louishotelmanagement/fxml/loai-phong-view.fxml"))
        ));

        // --- 🙋 Khách hàng ---
        TreeItem<MenuItemModel> customer = new TreeItem<>(
                new MenuItemModel("Khách hàng", "mdi2a-account",
                        "/com/example/louishotelmanagement/fxml/quan-ly-khach-hang-view.fxml")
        );

        // --- ⚙️ Hệ thống ---
        TreeItem<MenuItemModel> system = new TreeItem<>(
                new MenuItemModel("Hệ thống", "mdi2c-cog",
                        "/com/example/louishotelmanagement/fxml/he-thong-view.fxml")
        );

        root.getChildren().addAll(List.of(
                home,
                bookingGroup,
                serviceGroup,
                invoiceGroup,
                reportGroup,
                staff,
                roomGroup,
                customer,
                system
        ));

        return root;
    }
}
