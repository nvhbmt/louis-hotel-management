package com.example.louishotelmanagement.ui.data;

import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.ui.models.MenuItemModel;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class MenuData {

    public static TreeItem<MenuItemModel> createMenuTree() {
        TreeItem<MenuItemModel> root = new TreeItem<>(new MenuItemModel("Root", null, null));
        root.setExpanded(true);

        AuthService authService = AuthService.getInstance();
        String userRole = authService.getCurrentUserRole();

        TreeItem<MenuItemModel> trangChu = new TreeItem<>(
                new MenuItemModel("Trang chủ", "mdi2h-home",
                        "/com/example/louishotelmanagement/fxml/trang-chu-view.fxml")
        );

        TreeItem<MenuItemModel> phongGroup = new TreeItem<>(
                new MenuItemModel("Phòng", "mdi2b-bed",null)
        );
        phongGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Đặt phòng", "mdi2b-bed", "/com/example/louishotelmanagement/fxml/phong-view.fxml"))
        ));

        if ("Manager".equals(userRole)) {
                phongGroup.getChildren().addAll(List.of(
                        new TreeItem<>(new MenuItemModel("Quản lý phòng", "mdi2b-bed",
                                "/com/example/louishotelmanagement/fxml/quan-ly-phong-view.fxml")),
                        new TreeItem<>(new MenuItemModel("Loại phòng", "mdi2t-tag",
                                "/com/example/louishotelmanagement/fxml/quan-ly-loai-phong-view.fxml"))
                ));
            }
        TreeItem<MenuItemModel> PhieuDatPhongGroup = new TreeItem<>(
                new MenuItemModel("Phiếu đặt phòng", "mdi2t-ticket-account", "/com/example/louishotelmanagement/fxml/quan-ly-phieu-dat-phong.fxml")
        );
        TreeItem<MenuItemModel> dichVuGroup = new TreeItem<>(
                new MenuItemModel("Dịch vụ", "mdi2c-coffee", null)
        );
        dichVuGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Cung cấp dịch vụ", "mdi2h-handshake",
                        "/com/example/louishotelmanagement/fxml/dat-dich-vu-view.fxml"))
        ));

        if ("Manager".equals(userRole)) {
            dichVuGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Quản lý dịch vụ", "mdi2c-coffee",
                "/com/example/louishotelmanagement/fxml/quan-ly-dich-vu-view.fxml"))
            ));
        }

        TreeItem<MenuItemModel> khuyenMai = new TreeItem<>(
                new MenuItemModel("Khuyến mãi", "mdi2c-chart-bar", "/com/example/louishotelmanagement/fxml/quan-ly-khuyen-mai-view.fxml")
        );

        TreeItem<MenuItemModel> hoaDonGroup = new TreeItem<>(
                new MenuItemModel("Hóa đơn", "mdi2c-cash-multiple", "/com/example/louishotelmanagement/fxml/quan-ly-hoa-don-view.fxml")
        );

//        if ("Manager".equals(userRole)) {
//            hoaDonGroup.getChildren().addAll(List.of(
//                new TreeItem<>(new MenuItemModel("Quản lý hóa đơn", "mdi2f-file-document",
//                "/com/example/louishotelmanagement/fxml/hoa-don-view.fxml"))
//            ));
//        }

        TreeItem<MenuItemModel> thongKe = new TreeItem<>(
                new MenuItemModel("Thống kê", "mdi2c-chart-bar", "/com/example/louishotelmanagement/fxml/thong-ke-view.fxml")
        );

        TreeItem<MenuItemModel> nhanVienGroup = new TreeItem<>(
                new MenuItemModel("Nhân viên", "mdi2a-account-group", "/com/example/louishotelmanagement/fxml/quan-ly-nhan-vien-view.fxml")
        );

        TreeItem<MenuItemModel> khachHangGroup = new TreeItem<>(
                new MenuItemModel("Khách hàng", "mdi2a-account",
                        "/com/example/louishotelmanagement/fxml/quan-ly-khach-hang-view.fxml")
        );

        // Phân quyền menu theo role
        List<TreeItem<MenuItemModel>> menuItems = new ArrayList<>();
        menuItems.add(trangChu);
        menuItems.add(phongGroup);
        menuItems.add(PhieuDatPhongGroup);
        menuItems.add(dichVuGroup);
        menuItems.add(khuyenMai);
        menuItems.add(hoaDonGroup);

        // Chỉ Manager mới có quyền xem thống kê và quản lý nhân viên
        if ("Manager".equals(userRole)) {
            menuItems.add(thongKe);
            menuItems.add(nhanVienGroup);
        }

        menuItems.add(khachHangGroup);

        root.getChildren().addAll(menuItems);

        return root;
    }
}
