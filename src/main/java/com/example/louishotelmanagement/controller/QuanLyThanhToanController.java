package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.HoaDonDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.KhuyenMaiDAO;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.ui.components.StatsCard;
import com.example.louishotelmanagement.util.Refreshable;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.QuanLyHoaDonView;
import com.example.louishotelmanagement.view.XemChiTietHoaDonView;
import com.example.louishotelmanagement.view.XemHoaDonTxtView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuanLyThanhToanController implements Refreshable {

    private StatsCard unpaidInvoicesCard;
    private StatsCard paidInvoicesCard;
    private StatsCard dailyRevenueCard;
    private StatsCard promotionalInvoicesCard;

    private TextField txtTimKiem;
    private ComboBox<String> cmbNgayLap;
    private Button btnLamMoi;
    private TableView<HoaDon> tableViewKhachHang;
    private TableColumn<HoaDon, String> colMaHD;
    private TableColumn<HoaDon, LocalDate> colNgayLap;
    private TableColumn<HoaDon, Void> colThaoTac;
    private TableColumn<HoaDon, String> colPhuongThuc;
    private TableColumn<HoaDon, String> colTrangThai;
    private TableColumn<HoaDon, BigDecimal> colTongTien;
    private TableColumn<HoaDon, String> colMaKhuyenMai;
    private TableColumn<HoaDon, String> colTenKH;

    private HoaDonDAO hoaDonDAO;
    private ObservableList<HoaDon> masterList;
    private FilteredList<HoaDon> filteredHoaDonList;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public QuanLyThanhToanController(QuanLyHoaDonView view) {
        this.unpaidInvoicesCard = view.getUnpaidInvoicesCard();
        this.paidInvoicesCard = view.getPaidInvoicesCard();
        this.dailyRevenueCard = view.getDailyRevenueCard();
        this.promotionalInvoicesCard = view.getPromotionalInvoicesCard();
        this.txtTimKiem = view.getTxtTimKiem();
        this.cmbNgayLap = view.getCmbNgayLap();
        this.btnLamMoi = view.getBtnLamMoi();
        this.tableViewKhachHang = view.getTableViewKhachHang();
        this.colMaHD = view.getColMaHD();
        this.colNgayLap = view.getColNgayLap();
        this.colPhuongThuc = view.getColPhuongThuc();
        this.colTrangThai = view.getColTrangThai();
        this.colTongTien = view.getColTongTien();
        this.colMaKhuyenMai = view.getColMaKhuyenMai();
        this.colTenKH = view.getColTenKH();
        this.colThaoTac = view.getColThaoTac();
        initialize();
    }

    public void initialize() {
        masterList = FXCollections.observableArrayList();
        filteredHoaDonList = new FilteredList<>(masterList, p -> true);
        tableViewKhachHang.setItems(filteredHoaDonList);

        hoaDonDAO = new HoaDonDAO();

        // Load data trước
        try {
            loadHoaDonChuaThanhToan();
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách hóa đơn: " + ex.getMessage());
            ex.printStackTrace();
        }

        // Sau đó setup UI
        setupTableColumns();
        setupComboBox();
        cauHinhLoc();
    }

    private void cauHinhLoc() {
        filteredHoaDonList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String timKiemText = txtTimKiem.getText();
            String ngayLapFilter = cmbNgayLap.getValue();

            return hoaDon -> {
                // Filter theo tìm kiếm
                boolean timKiemMatch = timKiemText == null || timKiemText.trim().isEmpty() ||
                        (hoaDon.getMaHD() != null && hoaDon.getMaHD().toLowerCase().contains(timKiemText.toLowerCase())) ||
                        (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getHoTen() != null &&
                         hoaDon.getKhachHang().getHoTen().toLowerCase().contains(timKiemText.toLowerCase()));

                // Filter theo ngày lập
                boolean ngayMatch = ngayLapFilter == null || ngayLapFilter.equals("Tất cả ngày") ||
                        checkDateFilter(hoaDon.getNgayLap(), ngayLapFilter);

                return timKiemMatch && ngayMatch;
            };
        }, txtTimKiem.textProperty(), cmbNgayLap.valueProperty()));
    }

    private void setupTableColumns() {

        colMaHD.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        colNgayLap.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getNgayLap()));
        colPhuongThuc.setCellValueFactory(data -> {
            PhuongThucThanhToan p = data.getValue().getPhuongThuc();
            return new SimpleStringProperty(p != null ? p.toString() : "");
        });
        colTrangThai.setCellValueFactory(data -> {
            TrangThaiHoaDon t = data.getValue().getTrangThai();
            return new SimpleStringProperty(t != null ? t.toString() : "");
        });
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        colMaKhuyenMai.setCellValueFactory(data -> {
            String maKM = data.getValue().getMaKM();
            if (maKM == null || maKM.isEmpty()) return new SimpleStringProperty("");
            try {
                KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
                KhuyenMai km = khuyenMaiDAO.layKhuyenMaiTheoMa(maKM);
                return new SimpleStringProperty(km != null ? km.getCode() : "");
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("");
            }
        });

        colTenKH.setCellValueFactory(data -> {
            KhachHang kh = data.getValue().getKhachHang();
            String tenKH = kh != null ? kh.getHoTen() : "";
            return new SimpleStringProperty(tenKH);
        });

        colTongTien.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? "" : String.format("%,.0f ₫", value.doubleValue()));
            }
        });

        colNgayLap.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? "" : dateFormatter.format(date));
            }
        });

        colThaoTac.setCellFactory(param -> new TableCell<>() {
            private final Button btnView = new Button("Xem");
            private final Button btnIn = new Button("In");
            private final HBox pane = new HBox(10,btnIn, btnView);
            {
                pane.setAlignment(Pos.CENTER);
                double buttonWidth = 70;
                btnView.setPrefWidth(buttonWidth);
                btnView.setStyle("-fx-background-color: #f0f0f0;-fx-border-radius: 10");
                btnView.setOnAction(event -> handleXemChiTiet(getTableView().getItems().get(getIndex())));
                btnIn.setPrefWidth(buttonWidth);
                btnIn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-border-radius: 10;");
                btnIn.setOnAction(event -> handleInChiTiet(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        // Table items are already set in initialize() with filteredHoaDonList
    }

    private void XemChiTiet(HoaDon hoaDon){
        try {
            XemChiTietHoaDonView view = new XemChiTietHoaDonView();
            XemChiTietHoaDonController controller = new XemChiTietHoaDonController(view);
            Parent root = view.getRoot();
            controller.loadData(hoaDon.getMaHD());

            Stage st = new Stage();
            st.setTitle("Chi tiết hóa đơn");
            st.setScene(new Scene(root));
            st.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void handleXemChiTiet(HoaDon hoaDon) {
        if (hoaDon == null) return;
        if(hoaDon.getTrangThai().equals(TrangThaiHoaDon.CHUA_THANH_TOAN)){
            if(ThongBaoUtil.hienThiXacNhan("Xác nhận","Bạn có thể sẽ không thấy đầy đủ số liệu nếu chưa trả phòng")){
                XemChiTiet(hoaDon);
            }
        }else{
            XemChiTiet(hoaDon);
        }


    }

    private void handleInChiTiet(HoaDon hoaDon) {

        // 1. Kiểm tra trạng thái hóa đơn (Giữ nguyên logic kiểm tra)
        if (!hoaDon.getTrangThai().equals(TrangThaiHoaDon.DA_THANH_TOAN)){
            // ThongBaoUtil.hienThiLoi phải là một dependency đã được import
            ThongBaoUtil.hienThiLoi("Lỗi","Vui lòng trả phòng và thanh toán hóa đơn trước khi xem");
            return;
        }

        try {
            // Lấy Mã Hóa Đơn từ đối tượng được chọn trên TableView
            String maHD = hoaDon.getMaHD();

            // 2. *** FIX QUAN TRỌNG ***: Tải lại hóa đơn HOÀN CHỈNH từ database
            // Sử dụng phương thức DAO đã được chứng minh là tải đầy đủ dữ liệu (TongGiamGia, TongVAT, v.v.)
            // GIẢ ĐỊNH: Lớp Controller có thể truy cập 'this.hoaDonDAO'
            HoaDon hoaDonHoanChinh = this.hoaDonDAO.timHoaDonTheoMa(maHD);

            if (hoaDonHoanChinh == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi Dữ Liệu");
                alert.setHeaderText("Không thể tìm thấy hóa đơn chi tiết.");
                alert.setContentText("Không tìm thấy dữ liệu hóa đơn hoàn chỉnh cho mã: " + maHD);
                alert.showAndWait();
                return;
            }

            // 3. Khởi tạo Generator và tạo nội dung TXT
            // Đảm bảo HoaDonTxtGenerator.taoNoiDungHoaDon nhận đối tượng HoaDon
            com.example.louishotelmanagement.util.HoaDonTxtGenerator generator = new com.example.louishotelmanagement.util.HoaDonTxtGenerator();
            String noiDungHoaDon = generator.taoNoiDungHoaDon(hoaDonHoanChinh); // <-- DÙNG đối tượng HOÀN CHỈNH

            // 4. Load giao diện và hiển thị (Giữ nguyên logic FXML)
            XemHoaDonTxtView view = new XemHoaDonTxtView();
            XemHoaDonTxtController controller = new XemHoaDonTxtController(view);
            Parent root = view.getRoot();
            controller.initData(noiDungHoaDon);

            Stage stage = new Stage();
            stage.setTitle("Chi Tiết Hóa Đơn (Xem file TXT): " + maHD);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Truy Vấn Dữ Liệu");
            alert.setHeaderText("Không thể tải chi tiết hóa đơn.");
            alert.setContentText("Lỗi SQL: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void setupComboBox() {
        cmbNgayLap.setItems(FXCollections.observableArrayList("Tất cả ngày", "1 ngày", "3 ngày", "1 tuần"));
        cmbNgayLap.setValue("Tất cả ngày");
        txtTimKiem.clear();
    }


    private void loadHoaDonChuaThanhToan() throws SQLException {
        List<HoaDon> all = hoaDonDAO.layDanhSachHoaDon();
        List<HoaDon> chuaTT = all.stream()
                .collect(Collectors.toList());
        masterList.setAll(chuaTT);
        capNhatThongKe();
    }


    private void capNhatThongKe() {
        List<HoaDon> visible = tableViewKhachHang.getItems();
        long countKHChuaThanhToan = visible.stream()
                        .filter(hd->hd.getTrangThai()==TrangThaiHoaDon.CHUA_THANH_TOAN)
                        .count();
        unpaidInvoicesCard.setValue(String.valueOf(countKHChuaThanhToan));

        long countKHDaThanhToan = visible.stream()
                .filter(hd->hd.getTrangThai()==TrangThaiHoaDon.DA_THANH_TOAN)
                .count();
        paidInvoicesCard.setValue(String.valueOf(countKHDaThanhToan));

        LocalDate today = LocalDate.now();
        BigDecimal revenueToday = visible.stream()
                .filter(hd -> hd.getNgayLap() != null && hd.getNgayLap().isEqual(today) && hd.getTrangThai().equals(TrangThaiHoaDon.DA_THANH_TOAN))
                .map(HoaDon::getTongTien)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dailyRevenueCard.setValue(String.format("%,.0f ₫", revenueToday.doubleValue()));

        long KhuyenMaicount = visible.stream()
                .filter(hd -> hd.getMaKM() != null)
                .count();
        promotionalInvoicesCard.setValue(String.valueOf(KhuyenMaicount));
    }

    private void onLamMoi(ActionEvent event) {
        txtTimKiem.clear();
        cmbNgayLap.getSelectionModel().selectFirst();
        try {
            loadHoaDonChuaThanhToan();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể làm mới: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    @Override
    public void refreshData() throws SQLException, Exception {
        setupTableColumns();
        setupComboBox();
        loadHoaDonChuaThanhToan();
    }

    public void handleLamMoi() {
        txtTimKiem.clear();
        cmbNgayLap.setValue("Tất cả ngày");
        try {
            loadHoaDonChuaThanhToan();
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách hóa đơn: " + ex.getMessage());
        }
    }

    public void handleTimKiem() {
    }



    private boolean checkDateFilter(LocalDate hoaDonDate, String filter) {
        if (hoaDonDate == null) return false;

        LocalDate today = LocalDate.now();
        return switch (filter) {
            case "1 ngày" -> hoaDonDate.isEqual(today);
            case "3 ngày" -> {
                LocalDate threeDaysAgo = today.minusDays(2); // Hôm nay + 2 ngày trước = 3 ngày
                yield !hoaDonDate.isBefore(threeDaysAgo);
            }
            case "1 tuần" -> {
                LocalDate oneWeekAgo = today.minusWeeks(1).plusDays(1); // Hôm nay + 6 ngày trước = 1 tuần
                yield !hoaDonDate.isBefore(oneWeekAgo);
            }
            default -> false;
        };
    }
}
