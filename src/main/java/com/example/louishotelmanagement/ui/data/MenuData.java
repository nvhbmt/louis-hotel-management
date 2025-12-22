package com.example.louishotelmanagement.ui.data;

import com.example.louishotelmanagement.controller.PhongController;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.ui.models.MenuItemModel;
import com.example.louishotelmanagement.view.PhongView;
import com.example.louishotelmanagement.view.QuanLyDichVuView;
import com.example.louishotelmanagement.view.QuanLyHoaDonView;
import com.example.louishotelmanagement.view.QuanLyKhachHangView;
import com.example.louishotelmanagement.view.QuanLyKhuyenMaiView;
import com.example.louishotelmanagement.view.QuanLyPhieuDatPhongView;
import com.example.louishotelmanagement.view.QuanLyNhanVienView;
import com.example.louishotelmanagement.view.ThongKeView;
import com.example.louishotelmanagement.view.DatDichVuView;
import com.example.louishotelmanagement.view.QuanLyLoaiPhongView;
import com.example.louishotelmanagement.view.QuanLyPhongView;
import com.example.louishotelmanagement.view.TrangChuView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class MenuData {

    private static Parent loadFxml(String fxmlPath) {
        if (fxmlPath == null) return null;
        try {
            FXMLLoader loader = new FXMLLoader(MenuData.class.getResource(fxmlPath));
            return loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TreeItem<MenuItemModel> createMenuTree() {
        TreeItem<MenuItemModel> root = new TreeItem<>(new MenuItemModel("Root", null, null));
        root.setExpanded(true);

        AuthService authService = AuthService.getInstance();
        String userRole = authService.getCurrentUserRole();

        TreeItem<MenuItemModel> trangChu = new TreeItem<>(
                new MenuItemModel("Trang chủ", "mdi2h-home",new TrangChuView().getRoot())
        );

        TreeItem<MenuItemModel> phongGroup = new TreeItem<>(
                new MenuItemModel("Phòng", "mdi2b-bed", null, null)
        );
        // Create PhongView and PhongController
        PhongView phongView = new PhongView();
        PhongController phongController = new PhongController();
        phongView.setupController(phongController);
        phongController.setupController();

        phongGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Đặt phòng", "mdi2b-bed", phongView.getRoot()))
        ));

        if ("Manager".equals(userRole)) {
                phongGroup.getChildren().addAll(List.of(
                        new TreeItem<>(new MenuItemModel("Quản lý phòng", "mdi2b-bed",
                                new QuanLyPhongView().getRoot())),
                        new TreeItem<>(new MenuItemModel("Loại phòng", "mdi2t-tag",
                                QuanLyLoaiPhongView.getInstance().getRoot()))
                ));
            }
        TreeItem<MenuItemModel> PhieuDatPhongGroup = new TreeItem<>(
                new MenuItemModel("Phiếu đặt phòng", "mdi2t-ticket-account", QuanLyPhieuDatPhongView.getInstance().getRoot())
        );
        TreeItem<MenuItemModel> dichVuGroup = new TreeItem<>(
                new MenuItemModel("Dịch vụ", "mdi2c-coffee", null, null)
        );
        dichVuGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Cung cấp dịch vụ", "mdi2h-handshake",
                        DatDichVuView.getInstance().getRoot()))
        ));

        if ("Manager".equals(userRole)) {
            // Get QuanLyDichVuView instance
            QuanLyDichVuView quanLyDichVuView = QuanLyDichVuView.getInstance();
                
            dichVuGroup.getChildren().addAll(List.of(
                new TreeItem<>(new MenuItemModel("Quản lý dịch vụ", "mdi2c-coffee",
                        quanLyDichVuView.getRoot()))
            ));
        }

        TreeItem<MenuItemModel> khuyenMai = new TreeItem<>(
                new MenuItemModel("Khuyến mãi", "mdi2c-chart-bar", QuanLyKhuyenMaiView.getInstance().getRoot())
        );

        TreeItem<MenuItemModel> hoaDonGroup = new TreeItem<>(
                new MenuItemModel("Hóa đơn", "mdi2c-cash-multiple", QuanLyHoaDonView.getInstance().getRoot())
        );

//        if ("Manager".equals(userRole)) {
//            hoaDonGroup.getChildren().addAll(List.of(
//                new TreeItem<>(new MenuItemModel("Quản lý hóa đơn", "mdi2f-file-document",
//                "/com/example/louishotelmanagement/fxml/hoa-don-view.fxml"))
//            ));
//        }

        TreeItem<MenuItemModel> thongKe = new TreeItem<>(
                new MenuItemModel("Thống kê", "mdi2c-chart-bar", ThongKeView.getInstance().getRoot())
        );

        TreeItem<MenuItemModel> nhanVienGroup = new TreeItem<>(
                new MenuItemModel("Nhân viên", "mdi2a-account-group", QuanLyNhanVienView.getInstance().getRoot())
        );

        TreeItem<MenuItemModel> khachHangGroup = new TreeItem<>(
                new MenuItemModel("Khách hàng", "mdi2a-account",
                        QuanLyKhachHangView.getInstance().getRoot())
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
