package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.HoaDonDAO2;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.MaGiamGiaDAO;
import com.example.louishotelmanagement.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuanLyThanhToanController {

    @FXML private Label lblSoKhachHang;
    @FXML private Label lblDangLuuTru;
    @FXML private Label lblCheckout;
    @FXML private Label lblKhachVIP;

    @FXML private TextField txtTimKiem;
    @FXML private ComboBox<String> cmbNgayLap;
    @FXML private Button btnLamMoi;
    @FXML private Button btnThanhToan;
    @FXML private TableView<HoaDon> tableViewKhachHang;
    @FXML private TableColumn<HoaDon, String> colMaHD;
    @FXML private TableColumn<HoaDon, LocalDate> colNgayLap;
    @FXML private TableColumn<HoaDon, String> colPhuongThuc;
    @FXML private TableColumn<HoaDon, String> colTrangThai;
    @FXML private TableColumn<HoaDon, BigDecimal> colTongTien;
    @FXML private TableColumn<HoaDon, String> colMaGiamGia;
    @FXML private TableColumn<HoaDon, String> colTenKH;

    private final HoaDonDAO2 hoaDonDAO2 = new HoaDonDAO2();
    private final ObservableList<HoaDon> masterList = FXCollections.observableArrayList();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @FXML
    public void initialize() {
        setupTableColumns();
        setupComboBox();
        setupListeners();

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

        colMaGiamGia.setCellValueFactory(data -> {
            String maGG = data.getValue().getMaGG();
            if (maGG == null || maGG.isEmpty()) return new SimpleStringProperty("");
            try {
                MaGiamGiaDAO maGiamGiaDAO = new MaGiamGiaDAO();
                MaGiamGia mgg = maGiamGiaDAO.layMaGiamGiaThepMa(maGG);
                return new SimpleStringProperty(mgg != null ? mgg.getCode() : "");
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

        tableViewKhachHang.setItems(masterList);
    }

    private void setupComboBox() {
        cmbNgayLap.setItems(FXCollections.observableArrayList("Tất cả", "1 ngày", "3 ngày", "1 tuần"));
        cmbNgayLap.getSelectionModel().selectFirst();
    }

    private void setupListeners() {
        txtTimKiem.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        cmbNgayLap.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        btnLamMoi.setOnAction(this::onLamMoi);
        btnThanhToan.setOnAction(this::onThanhToan);
    }

    private void loadHoaDonChuaThanhToan() throws SQLException {
        List<HoaDon> all = hoaDonDAO2.layDanhSachHoaDon(); // dùng DAO2
        List<HoaDon> chuaTT = all.stream()
                .filter(hd -> hd.getTrangThai() == TrangThaiHoaDon.CHUA_THANH_TOAN)
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
        lblSoKhachHang.setText(String.valueOf(visible.size()));

        LocalDate today = LocalDate.now();
        long countToday = visible.stream()
                .filter(hd -> hd.getNgayLap() != null && hd.getNgayLap().isEqual(today))
                .count();
        lblDangLuuTru.setText(String.valueOf(countToday));

        BigDecimal revenueToday = visible.stream()
                .filter(hd -> hd.getNgayLap() != null && hd.getNgayLap().isEqual(today))
                .map(HoaDon::getTongTien)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        lblCheckout.setText(String.format("%,.0f ₫", revenueToday.doubleValue()));

        long vipCount = visible.stream()
                .filter(hd -> hd.getKhachHang() != null && "Khách VIP".equalsIgnoreCase(hd.getKhachHang().getHangKhach()))
                .count();
        lblKhachVIP.setText(String.valueOf(vipCount));
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

    private void onThanhToan(ActionEvent event) {
        HoaDon selected = tableViewKhachHang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn hóa đơn", "Vui lòng chọn 1 hóa đơn trong bảng để thanh toán.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/thanh-toan-dialog.fxml"));
            Parent root = loader.load();

            ThanhToanDialogController dialogController = loader.getController();
            dialogController.setHoaDon(selected);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Thanh Toán - " + selected.getMaHD());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            loadHoaDonChuaThanhToan();
        } catch (IOException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở form thanh toán: " + e.getMessage());
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
}
