package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhuyenMaiDAO;
import com.example.louishotelmanagement.model.KhuyenMai;
import com.example.louishotelmanagement.ui.components.Badge;
import com.example.louishotelmanagement.ui.components.CustomButton;
import com.example.louishotelmanagement.ui.components.StatsCard;
import com.example.louishotelmanagement.ui.models.BadgeVariant;
import com.example.louishotelmanagement.ui.models.ButtonVariant;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.KhuyenMaiFormDialogView;
import com.example.louishotelmanagement.view.QuanLyKhuyenMaiView;
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

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QuanLyKhuyenMaiController {

    private StatsCard totalCouponsCard;
    private StatsCard activeCouponsCard;
    private StatsCard expiredCouponsCard;
    private StatsCard upcomingCouponsCard;
    private StatsCard disabledCouponsCard;

    private TextField txtTimKiem;
    private ComboBox<String> cbKieuGiamGia;
    private ComboBox<String> cbTrangThai;
    private TableView<KhuyenMai> tableViewKhuyenMai;
    private TableColumn<KhuyenMai, String> colMaKM;
    private TableColumn<KhuyenMai, String> colCode;
    private TableColumn<KhuyenMai, Double> colGiamGia;
    private TableColumn<KhuyenMai, String> colKieuGiamGia;
    private TableColumn<KhuyenMai, LocalDate> colNgayBatDau;
    private TableColumn<KhuyenMai, LocalDate> colNgayKetThuc;
    private TableColumn<KhuyenMai, Double> colTongTienToiThieu;
    private TableColumn<KhuyenMai, String> colTrangThai;
    private TableColumn<KhuyenMai, String> colMoTa;
    private TableColumn<KhuyenMai, Void> colThaoTac;

    private KhuyenMaiDAO khuyenMaiDAO;
    private ObservableList<KhuyenMai> masterList;
    private FilteredList<KhuyenMai> filteredKhuyenMaiList;


    public QuanLyKhuyenMaiController(QuanLyKhuyenMaiView view) {
        this.totalCouponsCard = view.getTotalCouponsCard();
        this.activeCouponsCard = view.getActiveCouponsCard();
        this.expiredCouponsCard = view.getExpiredCouponsCard();
        this.upcomingCouponsCard = view.getUpcomingCouponsCard();
        this.disabledCouponsCard = view.getDisabledCouponsCard();
        this.txtTimKiem = view.getTxtTimKiem();
        this.cbKieuGiamGia = view.getCbKieuGiamGia();
        this.cbTrangThai = view.getCbTrangThai();
        this.tableViewKhuyenMai = view.getTableViewKhuyenMai();
        this.colMaKM = view.getColMaKM();
        this.colCode = view.getColCode();
        this.colGiamGia = view.getColGiamGia();
        this.colKieuGiamGia = view.getColKieuGiamGia();
        this.colNgayBatDau = view.getColNgayBatDau();
        this.colNgayKetThuc = view.getColNgayKetThuc();
        this.colTongTienToiThieu = view.getColTongTienToiThieu();
        this.colTrangThai = view.getColTrangThai();
        this.colMoTa = view.getColMoTa();
        this.colThaoTac = view.getColThaoTac();
        setupController();
    }

    public void setupController() {
        masterList = FXCollections.observableArrayList();
        filteredKhuyenMaiList = new FilteredList<>(masterList, p -> true);
        tableViewKhuyenMai.setItems(filteredKhuyenMaiList);

        khuyenMaiDAO = new KhuyenMaiDAO();

        try {
            setupTableColumns();
            setupComboBox();
            cauHinhLoc();
            loadData();
        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cauHinhLoc() {
        filteredKhuyenMaiList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String timKiemText = txtTimKiem.getText();
            String kieuGiamGiaFilter = cbKieuGiamGia.getValue();
            String trangThaiFilter = cbTrangThai.getValue();

            return khuyenMai -> {
                // Filter theo tìm kiếm
                boolean timKiemMatch = timKiemText == null || timKiemText.trim().isEmpty() ||
                        (khuyenMai.getMaKM() != null && khuyenMai.getMaKM().toLowerCase().contains(timKiemText.toLowerCase())) ||
                        (khuyenMai.getCode() != null && khuyenMai.getCode().toLowerCase().contains(timKiemText.toLowerCase()));

                // Filter theo kiểu giảm giá
                boolean kieuMatch = kieuGiamGiaFilter == null || kieuGiamGiaFilter.equals("Tất cả kiểu giảm giá") ||
                        (khuyenMai.getKieuGiamGia() != null && khuyenMai.getKieuGiamGia().toString().equals(kieuGiamGiaFilter));

                // Filter theo trạng thái
                boolean trangThaiMatch = trangThaiFilter == null || trangThaiFilter.equals("Tất cả trạng thái") ||
                        checkTrangThaiFilter(khuyenMai, trangThaiFilter);

                return timKiemMatch && kieuMatch && trangThaiMatch;
            };
        }, txtTimKiem.textProperty(), cbKieuGiamGia.valueProperty(), cbTrangThai.valueProperty()));
    }

    private boolean checkTrangThaiFilter(KhuyenMai khuyenMai, String filter) {
        LocalDate today = LocalDate.now();
        LocalDate ngayBatDau = khuyenMai.getNgayBatDau();
        LocalDate ngayKetThuc = khuyenMai.getNgayKetThuc();
        boolean isActive = "Kích hoạt".equals(khuyenMai.getTrangThai());

        return switch (filter) {
            case "Đang sử dụng" -> isActive && ngayBatDau != null && ngayKetThuc != null &&
                                   !today.isBefore(ngayBatDau) && !today.isAfter(ngayKetThuc);
            case "Đã hết hạn" -> ngayKetThuc != null && today.isAfter(ngayKetThuc);
            case "Chưa bắt đầu" -> ngayBatDau != null && today.isBefore(ngayBatDau);
            case "Vô hiệu hóa" -> !isActive;
            default -> false;
        };
    }

    private void capNhatThongKe() throws SQLException {
        // Sử dụng filtered list để thống kê
        List<KhuyenMai> visibleKhuyenMai = tableViewKhuyenMai.getItems();

        int tongSoKhuyenMai = visibleKhuyenMai.size();
        int soMaDangSuDung = 0;
        int soMaHetHan = 0;
        int soMaChuaBatDau = 0;
        int soMaVoHieuHoa = 0;

        LocalDate today = LocalDate.now();
        for (KhuyenMai km : visibleKhuyenMai) {
            LocalDate ngayBatDau = km.getNgayBatDau();
            LocalDate ngayKetThuc = km.getNgayKetThuc();
            boolean isActive = "Kích hoạt".equals(km.getTrangThai());   

            if (!isActive) {
                soMaVoHieuHoa++;
            } else if (ngayKetThuc != null && today.isAfter(ngayKetThuc)) {
                soMaHetHan++;
            } else if (ngayBatDau != null && today.isBefore(ngayBatDau)) {
                soMaChuaBatDau++;
            } else if (ngayBatDau != null && ngayKetThuc != null &&
                      !today.isBefore(ngayBatDau) && !today.isAfter(ngayKetThuc)) {
                soMaDangSuDung++;
            }
        }

        totalCouponsCard.setValue(String.valueOf(tongSoKhuyenMai));
        activeCouponsCard.setValue(String.valueOf(soMaDangSuDung));
        expiredCouponsCard.setValue(String.valueOf(soMaHetHan));
        upcomingCouponsCard.setValue(String.valueOf(soMaChuaBatDau));
        disabledCouponsCard.setValue(String.valueOf(soMaVoHieuHoa));
    }

    private void setupTableColumns() {
        // Thiết lập các cột
        colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colGiamGia.setCellValueFactory(new PropertyValueFactory<>("giamGia"));
        colKieuGiamGia.setCellValueFactory(new PropertyValueFactory<>("kieuGiamGia"));
        colNgayBatDau.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colNgayKetThuc.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));
        colTongTienToiThieu.setCellValueFactory(new PropertyValueFactory<>("tongTienToiThieu"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));

        // Format ngày tháng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colNgayBatDau.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        colNgayKetThuc.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        // Format số tiền
        colGiamGia.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", item));
                }
            }
        });

        colTongTienToiThieu.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", item));
                }
            }
        });

        // Format trạng thái với màu sắc
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                } else {
                    switch (item.toLowerCase()) {
                        case "Đang sử dụng" -> setGraphic(Badge.createBadge(item, BadgeVariant.SUCCESS));
                        case "Đã hết hạn" -> setGraphic(Badge.createBadge(item, BadgeVariant.DANGER));
                        case "Chưa bắt đầu" -> setGraphic(Badge.createBadge(item, BadgeVariant.WARNING));
                        case "Vô hiệu hóa" -> setGraphic(Badge.createBadge(item, BadgeVariant.DEFAULT));
                    }
                }
            }
        });

        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = CustomButton.createButton("Sửa", ButtonVariant.INFO);
            private final Button btnDelete = CustomButton.createButton("Xóa", ButtonVariant.DANGER);

            {
                btnEdit.setOnAction(_ -> {
                    KhuyenMai khuyenMai = getTableView().getItems().get(getIndex());
                    handleSuaKhuyenMai(khuyenMai);
                });

                btnDelete.setOnAction(_ -> {
                    KhuyenMai khuyenMai = getTableView().getItems().get(getIndex());
                    handleXoaKhuyenMai(khuyenMai);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
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

        // Cho phép chọn nhiều dòng
        tableViewKhuyenMai.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void setupComboBox() {
        // Khởi tạo ComboBox kiểu giảm giá
        ObservableList<String> dsKieuGiamGia = FXCollections.observableArrayList(
                "Tất cả kiểu giảm giá",
                "Phần trăm (%)",
                "Số tiền (VNĐ)"
        );
        cbKieuGiamGia.setItems(dsKieuGiamGia);
        cbKieuGiamGia.setValue("Tất cả kiểu giảm giá");

        // Khởi tạo ComboBox trạng thái
        ObservableList<String> dsTrangThai = FXCollections.observableArrayList(
                "Tất cả trạng thái",
                "Đang sử dụng",
                "Đã hết hạn",
                "Chưa bắt đầu",
                "Vô hiệu hóa"
        );
        cbTrangThai.setItems(dsTrangThai);
        cbTrangThai.setValue("Tất cả trạng thái");
    }

    private void loadData() {
        try {
            // Lấy danh sách mã giảm giá từ database
            List<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.layDSKhuyenMai();
            masterList.setAll(dsKhuyenMai);
            capNhatThongKe();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu khuyến mãi: " + e.getMessage());
        }
    }

    public void handleLamMoi() {
        txtTimKiem.clear();
        cbKieuGiamGia.setValue("Tất cả kiểu giảm giá");
        cbTrangThai.setValue("Tất cả trạng thái");
        try {
            loadData();
        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể làm mới: " + e.getMessage());
        }
    }

    public void handleTimKiem() {
        // Filtering is now automatic via reactive binding
    }

    public void handleLocKieuGiamGia() {
        // Filtering is now automatic via reactive binding
    }

    public void handleLocTrangThai() {
        // Filtering is now automatic via reactive binding
    }

    public void handleThemKhuyenMai() {
        KhuyenMaiFormDialogView view = new KhuyenMaiFormDialogView();
        KhuyenMaiDialogController controller = new KhuyenMaiDialogController(view);
        Parent root = view.getRoot();
        Stage dialog = new Stage();
        dialog.setTitle("Thêm Khuyến Mãi Mới");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        controller.setMode("ADD");

        dialog.showAndWait();

        // Làm mới dữ liệu sau khi thêm
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSuaKhuyenMai(KhuyenMai khuyenMai) {
        try {
            // Load lại dữ liệu đầy đủ từ database
            KhuyenMai khuyenMaiDayDu = khuyenMaiDAO.layKhuyenMaiTheoMa(khuyenMai.getMaKM());
            KhuyenMaiFormDialogView view = new KhuyenMaiFormDialogView();
            KhuyenMaiDialogController controller = new KhuyenMaiDialogController(view);
            Parent root = view.getRoot();
            Stage dialog = new Stage();
            dialog.setTitle("Sửa Thông Tin Khuyến Mãi");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            controller.setMode("EDIT");

            // Set dữ liệu sau khi dialog hiển thị
            javafx.application.Platform.runLater(() -> controller.setKhuyenMai(khuyenMaiDayDu));
            dialog.showAndWait();

            // Làm mới dữ liệu sau khi sửa
            loadData();

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi load dữ liệu khuyến mãi: " + e.getMessage());
        }
    }

    private void handleXoaKhuyenMai(KhuyenMai khuyenMai) {
        // Xác nhận xóa
        String message = "Bạn có chắc chắn muốn xóa khuyến mãi này?\nMã: " + khuyenMai.getMaKM() + " - Code: " + khuyenMai.getCode();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận xóa", message)) {
            try {
                // Xóa khuyến mãi
                if (khuyenMaiDAO.xoaKhuyenMai(khuyenMai.getMaKM())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đã xóa khuyến mãi thành công!");
                    loadData();
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể xóa khuyến mãi!");
                }

            } catch (SQLException e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi xóa khuyến mãi: " + e.getMessage());
            }
        }
    }
}