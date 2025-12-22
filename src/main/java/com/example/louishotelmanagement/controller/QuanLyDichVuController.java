package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.DichVuDAO;
import com.example.louishotelmanagement.model.DichVu;
import com.example.louishotelmanagement.ui.components.Badge;
import com.example.louishotelmanagement.ui.components.CustomButton;
import com.example.louishotelmanagement.ui.components.StatsCard;
import com.example.louishotelmanagement.ui.models.BadgeVariant;
import com.example.louishotelmanagement.ui.models.ButtonVariant;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.DichVuFormDialogView;
import com.example.louishotelmanagement.view.QuanLyDichVuView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
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

import java.util.List;

public class QuanLyDichVuController {
    private StatsCard totalServicesCard;
    private StatsCard activeServicesCard;
    private StatsCard dailyRevenueCard;
    private StatsCard monthlyRevenueCard;
    private TextField txtTimKiem;
    private ComboBox<String> cbTrangThaiKinhDoanh;

    private TableView<DichVu> tableViewDichVu;
    private TableColumn<DichVu, String> colMaDV;
    private TableColumn<DichVu, String> colTenDV;
    private TableColumn<DichVu, Integer> colSoLuong;
    private TableColumn<DichVu, Double> colDonGia;
    private TableColumn<DichVu, String> colMoTa;
    private TableColumn<DichVu, Boolean> colTrangThai;
    public TableColumn<DichVu, Void> colThaoTac;

    private DichVuDAO dichVuDAO;
    private ObservableList<DichVu> danhSachDichVu;
    private ObservableList<DichVu> danhSachDichVuFiltered;
    private FilteredList<DichVu> filteredDichVuList;
    private List<DichVu> dsDichVu;

    public QuanLyDichVuController(QuanLyDichVuView view) {
        this.totalServicesCard = view.getTotalServicesCard();
        this.activeServicesCard = view.getActiveServicesCard();
        this.dailyRevenueCard = view.getDailyRevenueCard();
        this.monthlyRevenueCard = view.getMonthlyRevenueCard();
        this.cbTrangThaiKinhDoanh = view.getCbTrangThaiKinhDoanh();
        this.txtTimKiem = view.getTxtTimKiem();
        this.tableViewDichVu = view.getTableViewDichVu();
        this.colMaDV = view.getColMaDV();
        this.colTenDV = view.getColTenDV();
        this.colSoLuong = view.getColSoLuong();
        this.colDonGia = view.getColDonGia();
        this.colMoTa = view.getColMoTa();
        this.colTrangThai = view.getColTrangThai();
        this.colThaoTac = view.getColThaoTac();
        setupController();
    }

    public void setupController() {
        try {
            dichVuDAO = new DichVuDAO();

            // Khởi tạo dữ liệu
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();
            cauHinhLoc();

            // Load dữ liệu
            taiDuLieu();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void capNhatThongKe() {
        if (dsDichVu == null) return;

        int tongSo = dsDichVu.size();
        int conKinhDoanh = 0;
        int ngungKinhDoanh = 0;
        double tongGiaTri = 0;

        for (DichVu dv : dsDichVu) {
            if (dv.isConKinhDoanh()) {
                conKinhDoanh++;
            } else {
                ngungKinhDoanh++;
            }
            tongGiaTri += dv.getDonGia() * dv.getSoLuong();
        }

        // Update StatsCard values
        totalServicesCard.setValue(String.valueOf(tongSo));
        activeServicesCard.setValue(String.valueOf(conKinhDoanh));
        dailyRevenueCard.setValue(String.format("%.0f VNĐ", tongGiaTri));
        monthlyRevenueCard.setValue(String.format("%.0f VNĐ", tongGiaTri));
    }


    private void khoiTaoDuLieu() {
        danhSachDichVu = FXCollections.observableArrayList();
        danhSachDichVuFiltered = FXCollections.observableArrayList();
        filteredDichVuList = new FilteredList<>(danhSachDichVu, p -> true);
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaDV.setCellValueFactory(new PropertyValueFactory<>("maDV"));
        colTenDV.setCellValueFactory(new PropertyValueFactory<>("tenDV"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));

        // Cột trạng thái hiển thị văn bản dựa trên conKinhDoanh
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("conKinhDoanh"));

        // Cột đơn giá với định dạng tiền tệ
        colDonGia.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f VNĐ", item));
                }
            }
        });

        // Cột trạng thái kinh doanh
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item) {
                        setGraphic(Badge.createBadge("Đang kinh doanh", BadgeVariant.SUCCESS));
                    } else {
                        setGraphic(Badge.createBadge("Ngừng kinh doanh", BadgeVariant.DANGER));
                    }
                }
            }
        });

        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = CustomButton.createButton("Sửa", ButtonVariant.INFO);
            private final Button btnDelete = CustomButton.createButton("Xóa", ButtonVariant.DANGER);

            {
                btnEdit.setOnAction(_ -> {
                    DichVu dichVu = getTableView().getItems().get(getIndex());
                    handleSuaDichVu(dichVu);
                });

                btnDelete.setOnAction(_ -> {
                    DichVu dichVu = getTableView().getItems().get(getIndex());
                    handleXoaDichVu(dichVu);
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

        // Thiết lập TableView với FilteredList
        tableViewDichVu.setItems(filteredDichVuList);

        // Cho phép chọn nhiều dòng
        tableViewDichVu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox trạng thái kinh doanh
        List<String> danhSachTrangThai = List.of("Đang kinh doanh", "Ngừng kinh doanh");
        cbTrangThaiKinhDoanh.setItems(FXCollections.observableArrayList(danhSachTrangThai));
        cbTrangThaiKinhDoanh.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Tất cả trạng thái");
                } else {
                    setText(item);
                }
            }
        });
        cbTrangThaiKinhDoanh.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Tất cả trạng thái");
                } else {
                    setText(item);
                }
            }
        });

        // Set default values
        cbTrangThaiKinhDoanh.setValue("Tất cả trạng thái");

        // Thiết lập reactive filtering
        cauHinhLoc();
    }

    private void cauHinhLoc() {
        filteredDichVuList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String timKiemText = txtTimKiem.getText();
            String trangThaiDaChon = cbTrangThaiKinhDoanh.getValue();

            return dichVu -> {
                // Filter theo tìm kiếm
                boolean timKiemMatch = timKiemText == null || timKiemText.trim().isEmpty() ||
                        (dichVu.getTenDV() != null && dichVu.getTenDV().toLowerCase().contains(timKiemText.toLowerCase())) ||
                        (dichVu.getMaDV() != null && dichVu.getMaDV().toLowerCase().contains(timKiemText.toLowerCase()));

                // Filter theo trạng thái
                boolean trangThaiMatch = (trangThaiDaChon == null || trangThaiDaChon.equals("Tất cả trạng thái")) ||
                        (trangThaiDaChon.equals("Đang kinh doanh") && dichVu.isConKinhDoanh()) ||
                        (trangThaiDaChon.equals("Ngừng kinh doanh") && !dichVu.isConKinhDoanh());

                return timKiemMatch && trangThaiMatch;
            };
        }, txtTimKiem.textProperty(), cbTrangThaiKinhDoanh.valueProperty()));
    }

    private void taiDuLieu() {
        try {
            // Lấy danh sách dịch vụ từ database
            dsDichVu = dichVuDAO.layTatCaDichVu(true);

            danhSachDichVu.clear();
            danhSachDichVu.addAll(dsDichVu);
            capNhatThongKe();

            // FilteredList will automatically apply filters

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu dịch vụ: " + e.getMessage());
        }
    }


    public void handleThemDichVu() {
        DichVuFormDialogView view = new DichVuFormDialogView();
        DichVuDialogController controller = new DichVuDialogController(view);
        Parent root = view.getRoot();
        Stage dialog = new Stage();
        dialog.setTitle("Thêm Dịch Vụ Mới");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        controller.setMode("ADD");

        dialog.showAndWait();

        // Làm mới dữ liệu sau khi thêm
        taiDuLieu();

    }

    public void handleSuaDichVu(DichVu dichVu) {
        DichVuFormDialogView view = new DichVuFormDialogView();
        DichVuDialogController controller = new DichVuDialogController(view);
        Parent root = view.getRoot();
        Stage dialog = new Stage();
        dialog.setTitle("Sửa Thông Tin Dịch Vụ");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));

        // Thiết lập controller và dữ liệu
        controller.setMode("EDIT");
        controller.setDichVu(dichVu);
        dialog.showAndWait();

        // Làm mới dữ liệu sau khi sửa
        taiDuLieu();

    }

    public void handleXoaDichVu(DichVu dichVu) {
        // Xác nhận xóa
        String message = "Bạn có chắc chắn muốn ngừng kinh doanh dịch vụ này?\nDịch vụ: " + dichVu.getMaDV() + " - " + dichVu.getTenDV();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận ngừng kinh doanh", message)) {
            try {
                // Ngừng kinh doanh dịch vụ
                if (dichVuDAO.xoaDichVu(dichVu.getMaDV())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đã xóa dịch vụ thành công!");
                    taiDuLieu();
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể ngừng kinh doanh dịch vụ!");
                }

            } catch (Exception e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi ngừng kinh doanh dịch vụ: " + e.getMessage());
            }
        }
    }

    public void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbTrangThaiKinhDoanh.setValue("Tất cả trạng thái");
    }

    public void handleTimKiem() {
        // Filtering is now automatic via reactive binding
    }


//    private void capNhatTrangThai(String message) {
//        lblTrangThai.setText(message);
//    }
}

