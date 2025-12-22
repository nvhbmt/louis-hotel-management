package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.HoaDonDAO;
import com.example.louishotelmanagement.dao.HoaDonDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.KhuyenMaiDAO;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.Refreshable;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuanLyThanhToanController implements Refreshable {


    @FXML private Label lblSoKhachHang;
    @FXML private Label lblDaThanhToan;
    @FXML private Label lblCheckout;
    @FXML private Label lblKhuyenMai;

    @FXML private TextField txtTimKiem;
    @FXML private ComboBox<String> cmbNgayLap;
    @FXML private Button btnLamMoi;
    @FXML private Button btnThanhToan;
    @FXML private TableView<HoaDon> tableViewKhachHang;
    @FXML private TableColumn<HoaDon, String> colMaHD;
    @FXML private TableColumn<HoaDon, LocalDate> colNgayLap;
    @FXML private TableColumn<HoaDon, Void> colThaoTac;
    @FXML private TableColumn<HoaDon, String> colPhuongThuc;
    @FXML private TableColumn<HoaDon, String> colTrangThai;
    @FXML private TableColumn<HoaDon, BigDecimal> colTongTien;
    @FXML private TableColumn<HoaDon, String> colMaKhuyenMai;
    @FXML private TableColumn<HoaDon, String> colTenKH;
    HoaDonDAO hoadonDAO;

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ObservableList<HoaDon> masterList = FXCollections.observableArrayList();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        setupTableColumns();
        setupComboBox();
        setupListeners();
        hoadonDAO = new HoaDonDAO();
        try {

            loadHoaDonChuaThanhToan();
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách hóa đơn: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void setupTableColumns() {
        KhachHangDAO khachHangDAO = new KhachHangDAO();

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
            String maKH = data.getValue().getMaKH();
            String tenKH = "";
            if (maKH != null && !maKH.isEmpty()) {
                try {
                    KhachHang kh = khachHangDAO.layKhachHangTheoMa(maKH);
                    if (kh != null) tenKH = kh.getHoTen();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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

        tableViewKhachHang.setItems(masterList);
    }

    private void XemChiTiet(HoaDon hoaDon){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/louishotelmanagement/fxml/xem-chi-tiet-hoa-don.fxml")
            );

            Parent ui = loader.load();
            XemChiTietHoaDonController ctr = loader.getController();
            ctr.loadData(hoaDon.getMaHD());

            Stage st = new Stage();
            st.setTitle("Chi tiết hóa đơn");
            st.setScene(new Scene(ui));
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
            HoaDon hoaDonHoanChinh = this.hoadonDAO.timHoaDonTheoMa(maHD);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/xem-hoa-don-txt.fxml"));
            Parent root = loader.load();

            XemHoaDonTxtController controller = loader.getController();
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
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Giao Diện");
            alert.setHeaderText("Không thể mở màn hình xem hóa đơn.");
            alert.setContentText("Lỗi FXML (kiểm tra lại đường dẫn): " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void setupComboBox() {
        cmbNgayLap.setItems(FXCollections.observableArrayList("Tất cả", "1 ngày", "3 ngày", "1 tuần"));
        cmbNgayLap.getSelectionModel().selectFirst();
    }

    private void setupListeners() {
        txtTimKiem.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        cmbNgayLap.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        btnLamMoi.setOnAction(this::onLamMoi);
    }

    private void loadHoaDonChuaThanhToan() throws SQLException {
        List<HoaDon> all = hoaDonDAO.layDanhSachHoaDon();
        List<HoaDon> chuaTT = all.stream()
                .collect(Collectors.toList());
        masterList.setAll(chuaTT);
        tableViewKhachHang.setItems(FXCollections.observableArrayList(masterList));
        capNhatThongKe();
    }

    private void applyFilters() {
        String keyword = txtTimKiem.getText() != null ? txtTimKiem.getText().trim().toLowerCase() : "";
        String ngayOption = cmbNgayLap.getValue();

        List<HoaDon> filtered = masterList.stream()
                .filter(hd -> {
                    boolean matchesKeyword = keyword.isEmpty()
                            || (hd.getMaHD() != null && hd.getMaHD().toLowerCase().contains(keyword))
                            || (hd.getSoPhong() != null && hd.getSoPhong().toLowerCase().contains(keyword))
                            || (hd.getKhachHang() != null && hd.getKhachHang().getHoTen() != null
                            && hd.getKhachHang().getHoTen().toLowerCase().contains(keyword))
                            || (hd.getMaKH() != null && hd.getMaKH().toLowerCase().contains(keyword));
                    if (!matchesKeyword) return false;

                    if (ngayOption == null || "Tất cả".equals(ngayOption)) return true;
                    int days = switch (ngayOption) {
                        case "1 ngày" -> 1;
                        case "3 ngày" -> 3;
                        case "1 tuần" -> 7;
                        default -> 0;
                    };
                    LocalDate cutoff = LocalDate.now().minusDays(days - 1);
                    return hd.getNgayLap() != null && !hd.getNgayLap().isBefore(cutoff);
                })
                .collect(Collectors.toList());

        tableViewKhachHang.setItems(FXCollections.observableArrayList(filtered));
        capNhatThongKe();
    }

    private void capNhatThongKe() {
        List<HoaDon> visible = tableViewKhachHang.getItems();
        long countKHChuaThanhToan = visible.stream()
                        .filter(hd->hd.getTrangThai()==TrangThaiHoaDon.CHUA_THANH_TOAN)
                        .count();
        lblSoKhachHang.setText(String.valueOf(countKHChuaThanhToan));


        long countKHDaThanhToan = visible.stream()
                .filter(hd->hd.getTrangThai()==TrangThaiHoaDon.DA_THANH_TOAN)
                .count();
        lblDaThanhToan.setText(String.valueOf(countKHDaThanhToan));
        LocalDate today = LocalDate.now();
        BigDecimal revenueToday = visible.stream()
                .filter(hd -> hd.getNgayLap() != null && hd.getNgayLap().isEqual(today) && hd.getTrangThai().equals(TrangThaiHoaDon.DA_THANH_TOAN))
                .map(HoaDon::getTongTien)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        lblCheckout.setText(String.format("%,.0f ₫", revenueToday.doubleValue()));

        long KhuyenMaicount = visible.stream()
                .filter(hd -> hd.getMaKM() != null)
                .count();
        lblKhuyenMai.setText(String.valueOf(KhuyenMaicount));
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
        setupListeners();
        loadHoaDonChuaThanhToan();
    }
}
