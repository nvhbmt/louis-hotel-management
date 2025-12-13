package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.HoaDonDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.model.HoaDon;
import com.example.louishotelmanagement.model.HoaDonChiTietItem;
import com.example.louishotelmanagement.model.KhachHang;
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
    @FXML private Label lblPhuongThuc, lblTrangThai, lblTongTien, lblTongGiamGia, lblVAT, lblTienPhat;

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

    // ============================================
    // LOAD DATA
    // ============================================
    public void loadData(String maHD) throws SQLException {
        HoaDon hd = hoaDonDAO.timHoaDonTheoMa(maHD);

        if (hd == null) {
            showAlert("Không tìm thấy hóa đơn!");
            return;
        }

        lblMaHD.setText(hd.getMaHD());
        lblNgayLap.setText(hd.getNgayLap() != null ? hd.getNgayLap().format(dateFormat) : "");
        lblNgayCheckout.setText(hd.getNgayCheckOut() != null ? hd.getNgayCheckOut().format(dateFormat) : "");

        KhachHang kh = khachHangDAO.layKhachHangTheoMa(hd.getMaKH());
        lblTenKH.setText(kh != null ? kh.getHoTen() : "Không có");

        lblPhuongThuc.setText(hd.getPhuongThuc() != null ? hd.getPhuongThuc().toString() : "");
        lblTrangThai.setText(hd.getTrangThai().toString());

        lblTongTien.setText(formatMoney(hd.getTongTien()));
        lblTongGiamGia.setText(formatMoney(hd.getTongGiamGia()));
        lblVAT.setText(formatMoney(hd.getTongVAT()));
        lblTienPhat.setText(formatMoney(hd.getTienPhat()));

        // Load chi tiết
        fullList = hoaDonDAO.layChiTietHoaDon(maHD);

        // Mapping TableColumn
        colTenDV.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTenChiTiet()));

        colSoLuong.setCellValueFactory(c -> {
            HoaDonChiTietItem item = c.getValue();

            String suffix = item.isPhong() ? " ngày" : " lần";
            String result = item.getSoLuong() + suffix;

            return new javafx.beans.property.SimpleStringProperty(result);
        });

        colDonGia.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDonGiaFormatted()));

        colThanhTien.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getThanhTienFormatted()));

        tableChiTiet.setItems(FXCollections.observableArrayList(fullList));

        // ComboBox filter
        cbFilter.getItems().addAll("Tất cả", "Phòng", "Dịch vụ");
        cbFilter.setValue("Tất cả");

        cbFilter.setOnAction(e -> applyFilter());

        // Nút đóng
        btnClose.setOnAction(e -> ((Stage) btnClose.getScene().getWindow()).close());
    }

    // ============================================
    // FILTER
    // ============================================
    private void applyFilter() {
        String f = cbFilter.getValue();

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

    // ============================================
    private String formatMoney(BigDecimal v) {
        return v == null ? "0" : currency.format(v);
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.show();
    }
}
