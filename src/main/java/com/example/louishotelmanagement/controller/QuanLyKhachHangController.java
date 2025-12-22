package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.model.HangKhach;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.ui.components.CustomButton;
import com.example.louishotelmanagement.ui.components.StatsCard;
import com.example.louishotelmanagement.ui.models.ButtonVariant;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.KhachHangFormDialogView;
import com.example.louishotelmanagement.view.QuanLyKhachHangView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class QuanLyKhachHangController {

    private StatsCard totalCustomersCard;
    private StatsCard stayingCustomersCard;
    private StatsCard checkedOutCustomersCard;
    private StatsCard vipCustomersCard;

    private ComboBox<String> cmbHang;
    private ComboBox<String> cmbTrangThai;
    private TableView<KhachHang> tableViewKhachHang;
    private TableColumn<KhachHang, String> colMaKH;
    private TableColumn<KhachHang, String> colHoTen;
    private TableColumn<KhachHang, String> colSoDT;
    private TableColumn<KhachHang, String> colEmail;

    private KhachHangDAO khachHangDAO;
    private ObservableList<KhachHang> masterList;
    private FilteredList<KhachHang> filteredKhachHangList;

    public QuanLyKhachHangController(QuanLyKhachHangView view) {
        this.totalCustomersCard = view.getTotalCustomersCard();
        this.stayingCustomersCard = view.getStayingCustomersCard();
        this.checkedOutCustomersCard = view.getCheckedOutCustomersCard();
        this.vipCustomersCard = view.getVipCustomersCard();
        this.cmbHang = view.getCmbHang();
        this.cmbTrangThai = view.getCmbTrangThai();
        this.tableViewKhachHang = view.getTableViewKhachHang();
        this.colMaKH = view.getColMaKH();
        this.colHoTen = view.getColHoTen();
        this.colSoDT = view.getColSoDT();
        this.colEmail = view.getColEmail();
        this.colHangKhach = view.getColHangKhach();
        this.colGhiChu = view.getColGhiChu();
        this.colTrangThai = view.getColTrangThai();
        this.colThaoTac = view.getColThaoTac();
        this.txtTimKiem = view.getTxtTimKiem();
        setupController();
    }

    private TableColumn<KhachHang, String> colHangKhach;
    private TableColumn<KhachHang, String> colGhiChu;
    private TableColumn<KhachHang, String> colTrangThai;
    private TableColumn<KhachHang, Void> colThaoTac;

    private TextField txtTimKiem;


    public void setupController() {
        masterList = FXCollections.observableArrayList();
        filteredKhachHangList = new FilteredList<>(masterList, p -> true);
        tableViewKhachHang.setItems(filteredKhachHangList);

        khachHangDAO = new KhachHangDAO();

        try {
            //Khởi tạo dữ liệu
            setupComboBox();
            setupTableColumns();
            cauHinhLoc();
            loadData();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cauHinhLoc() {
        filteredKhachHangList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String timKiemText = txtTimKiem.getText();
            String trangThaiFilter = cmbTrangThai.getValue();
            String hangFilter = cmbHang.getValue();
            
            return khachHang -> {
                // Filter theo tìm kiếm
                boolean timKiemMatch = timKiemText == null || timKiemText.trim().isEmpty() ||
                        (khachHang.getMaKH() != null && khachHang.getMaKH().toLowerCase().contains(timKiemText.toLowerCase())) ||
                        (khachHang.getHoTen() != null && khachHang.getHoTen().toLowerCase().contains(timKiemText.toLowerCase())) ||
                        (khachHang.getSoDT() != null && khachHang.getSoDT().contains(timKiemText)) ||
                        (khachHang.getEmail() != null && khachHang.getEmail().toLowerCase().contains(timKiemText.toLowerCase()));

                // Filter theo hạng khách
                boolean hangMatch = hangFilter == null || hangFilter.equals("Tất cả hạng") ||
                        (khachHang.getHangKhach() != null && khachHang.getHangKhach().toString().equals(hangFilter));

                // Filter theo trạng thái
                boolean trangThaiMatch = trangThaiFilter == null || trangThaiFilter.equals("Tất cả trạng thái") ||
                        (khachHang.getTrangThai() != null && khachHang.getTrangThai().toString().equals(trangThaiFilter));

                return timKiemMatch && hangMatch && trangThaiMatch;
            };
        }, txtTimKiem.textProperty(), cmbHang.valueProperty(), cmbTrangThai.valueProperty()));
    }

    // ------------ Khởi tạo UI ------------
    private void setupComboBox() {
        // Hạng khách
        ObservableList<String> dsHang = FXCollections.observableArrayList(
                "Tất cả hạng",
                HangKhach.KHACH_THUONG.toString(),
                HangKhach.KHACH_QUEN.toString(),
                HangKhach.KHACH_VIP.toString(),
                HangKhach.KHACH_DOANH_NGHIEP.toString()
        );
        cmbHang.setItems(dsHang);
        cmbHang.setValue("Tất cả hạng");

        // Trạng thái khách hàng (string values)
        ObservableList<String> dsTrangThai = FXCollections.observableArrayList(
                "Tất cả trạng thái",
                "Đang lưu trú",
                "Check-out",
                "Đã đặt"
        );
        cmbTrangThai.setItems(dsTrangThai);
        cmbTrangThai.setValue("Tất cả trạng thái");
    }

    private void setupTableColumns() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colSoDT.setCellValueFactory(new PropertyValueFactory<>("soDT"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colHangKhach.setCellValueFactory(new PropertyValueFactory<>("hangKhach"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colTrangThai.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrangThai().getTenHienThi())
        );

        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = CustomButton.createButton("Sửa", ButtonVariant.INFO);
            private final Button btnDelete = CustomButton.createButton("Xóa", ButtonVariant.DANGER);

            {
                btnEdit.setOnAction(_ -> {
                    KhachHang khachHang = getTableView().getItems().get(getIndex());
                    handleSuaKhachHang(khachHang);
                });

                btnDelete.setOnAction(_ -> {
                    KhachHang khachHang = getTableView().getItems().get(getIndex());
                    handleXoaKhachHang(khachHang);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(8, btnEdit, btnDelete);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        colThaoTac.setCellValueFactory(_ -> new ReadOnlyObjectWrapper<>(null));
    }


    // ------------ Load dữ liệu ------------
    private void loadData() {
        try {
            ArrayList<KhachHang> dsKhachHang = khachHangDAO.layDSKhachHang();
            masterList.setAll(dsKhachHang);
            capNhatThongKe();
        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi lấy dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------ Logic ------------
    void capNhatThongKe() {
        // Sử dụng filtered list để thống kê
        java.util.List<KhachHang> visibleKhachHang = tableViewKhachHang.getItems();

        int tongSoKhach = visibleKhachHang.size();

        int soKhachVip = (int) visibleKhachHang.stream()
                .filter(kh -> kh.getHangKhach() != null && "Khách VIP".equalsIgnoreCase(kh.getHangKhach().toString()))
                .count();

        int soKhachDangLuuTru = (int) visibleKhachHang.stream()
                .filter(kh -> kh.getTrangThai() != null && "Đang lưu trú".equalsIgnoreCase(kh.getTrangThai().getTenHienThi()))
                .count();

        int soKhachCheckout = (int) visibleKhachHang.stream()
                .filter(kh -> kh.getTrangThai() != null && "Check-out".equalsIgnoreCase(kh.getTrangThai().getTenHienThi()))
                .count();

        totalCustomersCard.setValue(String.valueOf(tongSoKhach));
        stayingCustomersCard.setValue(String.valueOf(soKhachDangLuuTru));
        vipCustomersCard.setValue(String.valueOf(soKhachVip));
        checkedOutCustomersCard.setValue(String.valueOf(soKhachCheckout));
    }

    public void handleLamMoi() {
        txtTimKiem.clear();
        cmbHang.setValue("Tất cả hạng");
        cmbTrangThai.setValue("Tất cả trạng thái");
        try {
            loadData();
        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể làm mới: " + e.getMessage());
        }
    }

    public void handleTimKiem() {
        // Filtering is now automatic via reactive binding
    }

    public void handleThemKhachHang() {
        try {
            KhachHangFormDialogView view = new KhachHangFormDialogView();
            KhachHangDialogController controller = new KhachHangDialogController(view);
            Parent root = view.getRoot();
            Stage dialog = new Stage();
            dialog.setTitle("Thêm Khách Hàng Mới");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            controller.setMode("ADD");

            dialog.showAndWait();
            loadData();
        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleSuaKhachHang(KhachHang khachHang) {
        KhachHangFormDialogView view = new KhachHangFormDialogView();
        KhachHangDialogController controller = new KhachHangDialogController(view);
        Parent root = view.getRoot();
        Stage dialog = new Stage();
        dialog.setTitle("Sửa Thông Tin Khách Hàng");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        controller.setMode("EDIT");
        controller.setKhachHang(khachHang);

        dialog.showAndWait();
        loadData();

    }

    private void handleXoaKhachHang(KhachHang khachHang) {
        String message = "Bạn có chắc chắn muốn xóa khách hàng này?\nMã: " + khachHang.getMaKH() + " - Họ tên: " + khachHang.getHoTen();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận xóa", message)) {
            try {
                if (khachHangDAO.xoaKhachHang(khachHang.getMaKH())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đã xóa khách hàng thành công!");
                    loadData();
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể xóa khách hàng!");
                }

            } catch (SQLException e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi xóa khách hàng: " + e.getMessage());
            }
        }
    }
}
