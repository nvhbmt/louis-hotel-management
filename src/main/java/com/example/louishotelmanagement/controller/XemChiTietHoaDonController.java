package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.HoaDonDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.model.HoaDon;
import com.example.louishotelmanagement.model.HoaDonChiTietItem;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.view.XemChiTietHoaDonView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class XemChiTietHoaDonController {

    @FXML private Label lblMaHD, lblNgayLap, lblNgayCheckout, lblTenKH;
    @FXML private Label lblPhuongThuc, lblTrangThai, lblTongTien, lblVAT;

    // Các nhãn chi tiết mới bổ sung
    @FXML private Label lblGiamGiaMa, lblGiamGiaHang, lblPhatNhanTre, lblPhatTraSom, lblPhatTraTre;

    @FXML private TableView<HoaDonChiTietItem> tableChiTiet;
    @FXML private TableColumn<HoaDonChiTietItem, String> colTenDV;
    @FXML private TableColumn<HoaDonChiTietItem, String> colSoLuong;
    @FXML private TableColumn<HoaDonChiTietItem, String> colDonGia;
    @FXML private TableColumn<HoaDonChiTietItem, String> colThanhTien;

    @FXML private ComboBox<String> cbFilter;
    @FXML private Button btnClose;

    private List<HoaDonChiTietItem> fullList;
    private final NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public XemChiTietHoaDonController(XemChiTietHoaDonView view) {
        this.lblMaHD = view.getLblMaHD();
        this.lblNgayLap = view.getLblNgayLap();
        this.lblNgayCheckout = view.getLblNgayCheckout();
        this.lblTenKH = view.getLblTenKH();
        this.lblPhuongThuc = view.getLblPhuongThuc();
        this.lblTrangThai = view.getLblTrangThai();
        this.lblGiamGiaMa = view.getLblGiamGiaMa();
        this.lblGiamGiaHang = view.getLblGiamGiaHang();
        this.lblVAT = view.getLblVAT();
        this.lblPhatNhanTre = view.getLblPhatNhanTre();
        this.lblPhatTraSom = view.getLblPhatTraSom();
        this.lblPhatTraTre = view.getLblPhatTraTre();
        this.lblTongTien = view.getLblTongTien();
        this.cbFilter = view.getCbFilter();
        this.tableChiTiet = view.getTableChiTiet();
        this.colTenDV = view.getColTenDV();
        this.colSoLuong = view.getColSoLuong();
        this.colDonGia = view.getColDonGia();
        this.colThanhTien = view.getColThanhTien();
        this.btnClose = view.getBtnClose();
        this.btnClose.setOnAction(event -> handleHuy());
    }

    public void loadData(String maHD) throws SQLException {
        HoaDon hd = hoaDonDAO.timHoaDonTheoMa(maHD);

        if (hd == null) {
            showAlert("Không tìm thấy hóa đơn mã: " + maHD);
            return;
        }

        // 1. Thông tin cơ bản
        lblMaHD.setText(hd.getMaHD());
        lblNgayLap.setText(hd.getNgayLap() != null ? hd.getNgayLap().format(dateFormat) : "N/A");
        lblNgayCheckout.setText(hd.getngayDi() != null ? hd.getngayDi().format(dateFormat) : "Chưa checkout");

        KhachHang kh = khachHangDAO.layKhachHangTheoMa(hd.getMaKH());
        lblTenKH.setText(kh != null ? kh.getHoTen() : "Khách vãng lai");

        lblPhuongThuc.setText(hd.getPhuongThuc() != null ? hd.getPhuongThuc().toString() : "Chưa chọn");
        lblTrangThai.setText(hd.getTrangThai() != null ? hd.getTrangThai().toString() : "N/A");

        // 2. Thông tin tiền tệ chi tiết
        lblTongTien.setText(formatMoney(hd.getTongTien()));
        lblVAT.setText(formatMoney(hd.getTongVAT()));

        // Hiển thị các khoản giảm giá
        lblGiamGiaMa.setText(formatMoney(hd.getGiamGiaTheoMa()));
        lblGiamGiaHang.setText(formatMoney(hd.getGiamGiaTheoHangKH()));

        // Hiển thị các khoản phạt
        lblPhatNhanTre.setText(formatMoney(hd.getPhatNhanPhongTre()));
        lblPhatTraSom.setText(formatMoney(hd.getPhatTraPhongSom()));
        lblPhatTraTre.setText(formatMoney(hd.getPhatTraPhongTre()));

        // 3. Load danh sách dịch vụ/phòng vào TableView
        fullList = hoaDonDAO.layChiTietHoaDon(maHD);

        colTenDV.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTenChiTiet()));

        colSoLuong.setCellValueFactory(c -> {
            HoaDonChiTietItem item = c.getValue();
            String unit = item.isPhong() ? " đêm" : " lần";
            return new javafx.beans.property.SimpleStringProperty(item.getSoLuong() + unit);
        });

        colDonGia.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDonGiaFormatted()));

        colThanhTien.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getThanhTienFormatted()));

        tableChiTiet.setItems(FXCollections.observableArrayList(fullList));

        // 4. Cấu hình Filter ComboBox
        if (cbFilter.getItems().isEmpty()) {
            cbFilter.getItems().addAll("Tất cả", "Phòng", "Dịch vụ");
            cbFilter.setValue("Tất cả");
        }
        cbFilter.setOnAction(e -> applyFilter());

        // 5. Nút đóng
        btnClose.setOnAction(e -> ((Stage) btnClose.getScene().getWindow()).close());
    }

    private void applyFilter() {
        String f = cbFilter.getValue();
        if (fullList == null) return;

        List<HoaDonChiTietItem> filtered;
        switch (f) {
            case "Phòng":
                filtered = fullList.stream().filter(HoaDonChiTietItem::isPhong).collect(Collectors.toList());
                break;
            case "Dịch vụ":
                filtered = fullList.stream().filter(i -> !i.isPhong()).collect(Collectors.toList());
                break;
            default:
                filtered = fullList;
        }
        tableChiTiet.setItems(FXCollections.observableArrayList(filtered));
    }

    private String formatMoney(BigDecimal v) {
        if (v == null || v.compareTo(BigDecimal.ZERO) == 0) return "0 ₫";
        return currency.format(v);
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.show();
    }
    @FXML
    private void handleHuy() {
        // 1. Lấy Stage (cửa sổ) hiện tại thông qua bất kỳ một Control nào đó trong View
        // Ở đây ta dùng nút btnClose là thuận tiện nhất
        Stage stage = (Stage) btnClose.getScene().getWindow();

        // 2. Thực hiện đóng cửa sổ
        stage.close();
    }
}