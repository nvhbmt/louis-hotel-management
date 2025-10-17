package com.example.louishotelmanagement.ui.data;

import com.example.louishotelmanagement.ui.models.MenuItemModel;
import javafx.scene.control.TreeItem;

import java.util.List;

public class MenuData {

    public static TreeItem<MenuItemModel> createMenuTree() {
        TreeItem<MenuItemModel> root = new TreeItem<>(new MenuItemModel("Root", null, null));
        root.setExpanded(true);

        TreeItem<MenuItemModel> trangChu = new TreeItem<>(
                new MenuItemModel("Trang chủ", "mdi2h-home",
                        "/com/example/louishotelmanagement/fxml/trang-chu-view.fxml")
        );

        TreeItem<MenuItemModel> phongGroup = new TreeItem<>(
                new MenuItemModel("Phòng", "mdi2b-bed",null)
        );
        phongGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Đặt phòng", "mdi2b-bed", "/com/example/louishotelmanagement/fxml/phong-view.fxml")),
                new TreeItem<>(new MenuItemModel("Quản lý phòng", "mdi2b-bed",
                        "/com/example/louishotelmanagement/fxml/quan-ly-phong-view.fxml")),
                new TreeItem<>(new MenuItemModel("Loại phòng", "mdi2t-tag",
                        "/com/example/louishotelmanagement/fxml/loai-phong-view.fxml"))
        ));

        // --- ☕ Dịch vụ ---
        TreeItem<MenuItemModel> dichVuGroup = new TreeItem<>(
                new MenuItemModel("Dịch vụ", "mdi2c-coffee", null)
        );
        dichVuGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Quản lý dịch vụ", "mdi2c-coffee",
                        "/com/example/louishotelmanagement/fxml/quan-ly-dich-vu-view.fxml")),
                new TreeItem<>(new MenuItemModel("Thêm dịch vụ", "mdi2p-plus-circle",
                        "/com/example/louishotelmanagement/fxml/them-dich-vu.fxml")),
                new TreeItem<>(new MenuItemModel("Cung cấp dịch vụ", "mdi2h-handshake",
                        "/com/example/louishotelmanagement/fxml/cung-cap-dich-vu.fxml")),
                new TreeItem<>(new MenuItemModel("Hủy dịch vụ", "mdi2c-close-circle",
                        "/com/example/louishotelmanagement/fxml/huy-dich-vu.fxml"))
        ));

        TreeItem<MenuItemModel> hoaDonGroup = new TreeItem<>(
                new MenuItemModel("Hóa đơn", "mdi2c-cash-multiple", null)
        );
        hoaDonGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Thanh toán", "mdi2c-cash",
                        "/com/example/louishotelmanagement/fxml/thanh-toan-view.fxml")),
                new TreeItem<>(new MenuItemModel("Giảm giá", "mdi2s-sale",
                        "/com/example/louishotelmanagement/fxml/giam-gia-view.fxml")),
                new TreeItem<>(new MenuItemModel("Hiển thị hóa đơn", "mdi2f-file-document",
                        "/com/example/louishotelmanagement/fxml/hoa-don-view.fxml"))
        ));

        TreeItem<MenuItemModel> thongKe = new TreeItem<>(
                new MenuItemModel("Thống kê", "mdi2c-chart-bar", "/com/example/louishotelmanagement/fxml/thong-ke-view.fxml")
        );

        TreeItem<MenuItemModel> nhanVienGroup = new TreeItem<>(
                new MenuItemModel("Nhân viên", "mdi2a-account-group",
                        "/com/example/louishotelmanagement/fxml/quan-ly-nhan-vien-view.fxml")
        );

        TreeItem<MenuItemModel> khachHangGroup = new TreeItem<>(
                new MenuItemModel("Khách hàng", "mdi2a-account",
                        "/com/example/louishotelmanagement/fxml/quan-ly-khach-hang-view.fxml")
        );


        root.getChildren().addAll(List.of(
                trangChu,
                phongGroup,
                dichVuGroup,
                hoaDonGroup,
                thongKe,
                nhanVienGroup,
                khachHangGroup
        ));

        return root;
    }
}
