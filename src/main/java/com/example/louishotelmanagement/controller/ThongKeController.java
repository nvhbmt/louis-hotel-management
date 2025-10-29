package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.dao.ThongKeDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

public class ThongKeController implements Initializable {

    @FXML
    public BarChart thongkeBarChart;
    @FXML
    private VBox thongKeContent;

    // Statistics Cards
    @FXML
    private Label lblTongSoPhong;
    @FXML
    private Label lblPhongDangSuDung;
    @FXML
    private Label lblTiLeSuDung;
    @FXML
    private Label lblTongKhachHang;
    @FXML
    private Label lblTongLoaiPhong;

    // Room Status Details
    @FXML
    private Label lblPhongTrong;
    @FXML
    private Label lblPhongDaDat;
    @FXML
    private Label lblPhongDangSuDungChiTiet;
    @FXML
    private Label lblPhongBaoTri;

    // Bar Charts
    @FXML
    private Rectangle barPhongTrong;
    @FXML
    private Rectangle barPhongDaDat;
    @FXML
    private Rectangle barPhongDangSuDung;
    @FXML
    private Rectangle barPhongBaoTri;

    // Percentages
    @FXML
    private Label lblTiLeTrong;
    @FXML
    private Label lblTiLeDaDat;
    @FXML
    private Label lblTiLeDangSuDung;
    @FXML
    private Label lblTiLeBaoTri;

    // Room Types
    @FXML
    private VBox vboxLoaiPhong;
    @FXML
    private Label lblGiaTrungBinh;
    @FXML
    private Label lblGiaCaoNhat;
    @FXML
    private Label lblGiaThapNhat;

    // Revenue Chart Controls
    @FXML
    private ToggleButton btnTheoNgay;
    @FXML
    private ToggleButton btnTheoTuan;
    @FXML
    private ToggleButton btnTheoThang;
    @FXML
    private HBox hboxChonNgay;
    @FXML
    private HBox hboxChonNam;
    @FXML
    private HBox vboxSelectionControls;
    @FXML
    private DatePicker dpTuNgay;
    @FXML
    private DatePicker dpDenNgay;
    @FXML
    private ComboBox<Integer> cbNam;
    @FXML
    private VBox vboxChart;
    @FXML
    private Label lblTongDoanhThu;

    // Refresh
    @FXML
    private Label lblCapNhatCuoi;

    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private KhachHangDAO khachHangDAO;
    private ThongKeDAO thongKeDAO;
    private SimpleDateFormat dateFormat;
    private String currentChartType = "THANG"; // NGAY, TUAN, THANG

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            phongDAO = new PhongDAO();
            loaiPhongDAO = new LoaiPhongDAO();
            khachHangDAO = new KhachHangDAO();
            thongKeDAO = new ThongKeDAO();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            // Khởi tạo controls
            khoiTaoControls();

            // Load dữ liệu thống kê
            taiDuLieuThongKe();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void taiDuLieuThongKe() {
        try {
            // Lấy dữ liệu từ database
            List<Phong> dsPhong = phongDAO.layDSPhong();
            List<LoaiPhong> dsLoaiPhong = loaiPhongDAO.layDSLoaiPhong();
            int tongKhachHang = khachHangDAO.layDSKhachHang().size();

            // Cập nhật thống kê tổng quan
            capNhatThongKeTongQuan(dsPhong, dsLoaiPhong, tongKhachHang);

            // Cập nhật trạng thái phòng
            capNhatTrangThaiPhong(dsPhong);

            // Cập nhật loại phòng
            capNhatLoaiPhong(dsLoaiPhong);

            // Cập nhật thời gian
            lblCapNhatCuoi.setText("Cập nhật lần cuối: " + dateFormat.format(new Date()));

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu thống kê: " + e.getMessage());
        }
    }

    private void capNhatThongKeTongQuan(List<Phong> dsPhong, List<LoaiPhong> dsLoaiPhong, int tongKhachHang) {
        int tongSoPhong = dsPhong.size();
        int phongDangSuDung = (int) dsPhong.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.DANG_SU_DUNG)
                .count();

        double tiLeSuDung = tongSoPhong > 0 ? (double) phongDangSuDung / tongSoPhong * 100 : 0;

        lblTongSoPhong.setText(String.valueOf(tongSoPhong));
        lblPhongDangSuDung.setText(String.valueOf(phongDangSuDung));
        lblTiLeSuDung.setText(String.format("%.1f%%", tiLeSuDung));
        lblTongKhachHang.setText(String.valueOf(tongKhachHang));
        lblTongLoaiPhong.setText(String.valueOf(dsLoaiPhong.size()));
    }

    private void capNhatTrangThaiPhong(List<Phong> dsPhong) {
        int tongSoPhong = dsPhong.size();

        int phongTrong = (int) dsPhong.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.TRONG)
                .count();

        int phongDaDat = (int) dsPhong.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.DA_DAT)
                .count();

        int phongDangSuDung = (int) dsPhong.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.DANG_SU_DUNG)
                .count();

        int phongBaoTri = (int) dsPhong.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.BAO_TRI)
                .count();

        // Cập nhật labels
        lblPhongTrong.setText("Phòng trống: " + phongTrong);
        lblPhongDaDat.setText("Phòng đã đặt: " + phongDaDat);
        lblPhongDangSuDungChiTiet.setText("Phòng đang sử dụng: " + phongDangSuDung);
        lblPhongBaoTri.setText("Phòng bảo trì: " + phongBaoTri);

        // Cập nhật tỷ lệ phần trăm
        double tiLeTrong = tongSoPhong > 0 ? (double) phongTrong / tongSoPhong * 100 : 0;
        double tiLeDaDat = tongSoPhong > 0 ? (double) phongDaDat / tongSoPhong * 100 : 0;
        double tiLeDangSuDung = tongSoPhong > 0 ? (double) phongDangSuDung / tongSoPhong * 100 : 0;
        double tiLeBaoTri = tongSoPhong > 0 ? (double) phongBaoTri / tongSoPhong * 100 : 0;

        lblTiLeTrong.setText(String.format("%.1f%%", tiLeTrong));
        lblTiLeDaDat.setText(String.format("%.1f%%", tiLeDaDat));
        lblTiLeDangSuDung.setText(String.format("%.1f%%", tiLeDangSuDung));
        lblTiLeBaoTri.setText(String.format("%.1f%%", tiLeBaoTri));

        // Cập nhật bar charts (width tương đối với tổng width 200)
        double maxWidth = 200.0;
        barPhongTrong.setWidth(tiLeTrong * maxWidth / 100);
        barPhongDaDat.setWidth(tiLeDaDat * maxWidth / 100);
        barPhongDangSuDung.setWidth(tiLeDangSuDung * maxWidth / 100);
        barPhongBaoTri.setWidth(tiLeBaoTri * maxWidth / 100);
    }

    private void capNhatLoaiPhong(List<LoaiPhong> dsLoaiPhong) {
        // Clear existing content
        vboxLoaiPhong.getChildren().clear();

        // Add header
        Label headerLabel = new Label("Danh sách loại phòng");
        headerLabel.getStyleClass().add("room-type-header");
        vboxLoaiPhong.getChildren().add(headerLabel);

        // Add room types
        for (LoaiPhong loaiPhong : dsLoaiPhong) {
            HBox roomTypeBox = new HBox(10);
            roomTypeBox.setPadding(new Insets(5, 0, 5, 0));

            Label nameLabel = new Label(loaiPhong.getTenLoai());
            nameLabel.getStyleClass().add("room-type-item");
            nameLabel.setPrefWidth(120);

            Label priceLabel = new Label(String.format("%,.0f VNĐ", loaiPhong.getDonGia()));
            priceLabel.getStyleClass().add("room-type-price");

            roomTypeBox.getChildren().addAll(nameLabel, priceLabel);
            vboxLoaiPhong.getChildren().add(roomTypeBox);
        }

        // Cập nhật thống kê giá
        if (!dsLoaiPhong.isEmpty()) {
            double giaTrungBinh = dsLoaiPhong.stream()
                    .mapToDouble(LoaiPhong::getDonGia)
                    .average()
                    .orElse(0.0);

            double giaCaoNhat = dsLoaiPhong.stream()
                    .mapToDouble(LoaiPhong::getDonGia)
                    .max()
                    .orElse(0.0);

            double giaThapNhat = dsLoaiPhong.stream()
                    .mapToDouble(LoaiPhong::getDonGia)
                    .min()
                    .orElse(0.0);

            lblGiaTrungBinh.setText(String.format("Giá trung bình: %,.0f VNĐ", giaTrungBinh));
            lblGiaCaoNhat.setText(String.format("Giá cao nhất: %,.0f VNĐ", giaCaoNhat));
            lblGiaThapNhat.setText(String.format("Giá thấp nhất: %,.0f VNĐ", giaThapNhat));
        } else {
            lblGiaTrungBinh.setText("Giá trung bình: 0 VNĐ");
            lblGiaCaoNhat.setText("Giá cao nhất: 0 VNĐ");
            lblGiaThapNhat.setText("Giá thấp nhất: 0 VNĐ");
        }
    }

    @FXML
    private void handleLamMoi() {
        taiDuLieuThongKe();
        taiDuLieuChart();
    }

    private void khoiTaoControls() {
        // Khởi tạo ComboBox năm
        List<Integer> danhSachNam = new ArrayList<>();
        int namHienTai = Year.now().getValue();
        for (int i = namHienTai - 5; i <= namHienTai + 1; i++) {
            danhSachNam.add(i);
        }
        cbNam.setItems(FXCollections.observableArrayList(danhSachNam));
        cbNam.setValue(namHienTai);

        // Khởi tạo DatePicker với giá trị mặc định
        dpTuNgay.setValue(LocalDate.now().minusDays(30));
        dpDenNgay.setValue(LocalDate.now());

        // Thiết lập ToggleGroup cho các button
        ToggleGroup toggleGroup = new ToggleGroup();
        btnTheoNgay.setToggleGroup(toggleGroup);
        btnTheoTuan.setToggleGroup(toggleGroup);
        btnTheoThang.setToggleGroup(toggleGroup);
        btnTheoThang.setSelected(true);

        // Load dữ liệu chart ban đầu
        taiDuLieuChart();
    }

    @FXML
    private void handleChonTheoNgay() {
        currentChartType = "NGAY";
        hboxChonNgay.setVisible(true);
        hboxChonNam.setVisible(false);
        taiDuLieuChart();
    }

    @FXML
    private void handleChonTheoTuan() {
        currentChartType = "TUAN";
        hboxChonNgay.setVisible(true);
        hboxChonNam.setVisible(true);
        taiDuLieuChart();
    }

    @FXML
    private void handleChonTheoThang() {
        currentChartType = "THANG";
        hboxChonNgay.setVisible(false);
        hboxChonNam.setVisible(true);
        taiDuLieuChart();
    }

    @FXML
    private void handleChonNgay() {
        taiDuLieuChart();
    }

    @FXML
    private void handleChonNam() {
        taiDuLieuChart();
    }

    @FXML
    private void handleCapNhatChart() {
        taiDuLieuChart();
    }

    private void taiDuLieuChart() {
        try {
            Map<String, Double> doanhThuData = new HashMap<>();
            Map<String, Integer> soLuongData = new HashMap<>();
            double tongDoanhThu = 0;

            switch (currentChartType) {
                case "NGAY":
                    if (dpTuNgay.getValue() != null && dpDenNgay.getValue() != null) {
                        doanhThuData = thongKeDAO.layDoanhThuTheoNgay(dpTuNgay.getValue(), dpDenNgay.getValue());
                        tongDoanhThu = thongKeDAO.layTongDoanhThu(dpTuNgay.getValue(), dpDenNgay.getValue());
                    }
                    break;
                case "TUAN":
                    if (cbNam.getValue() != null) {
                        doanhThuData = thongKeDAO.layDoanhThuTheoTuan(cbNam.getValue());
                        soLuongData = thongKeDAO.laySoLuongDatPhongTheoThang(cbNam.getValue());
                        tongDoanhThu = doanhThuData.values().stream().mapToDouble(Double::doubleValue).sum();
                    }
                    break;
                case "THANG":
                    if (cbNam.getValue() != null) {
                        doanhThuData = thongKeDAO.layDoanhThuTheoThang(cbNam.getValue());
                        soLuongData = thongKeDAO.laySoLuongDatPhongTheoThang(cbNam.getValue());
                        tongDoanhThu = doanhThuData.values().stream().mapToDouble(Double::doubleValue).sum();
                    }
                    break;
            }

            // Hiển thị chart
            hienThiChart(doanhThuData, soLuongData, tongDoanhThu);

        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu chart: " + e.getMessage());
        }
    }

    private void hienThiChart(Map<String, Double> doanhThuData, Map<String, Integer> soLuongData, double tongDoanhThu) {
        // Clear existing chart data
        thongkeBarChart.getData().clear();

        if (doanhThuData.isEmpty()) {
            lblTongDoanhThu.setText("Tổng doanh thu: 0 VNĐ");
            return;
        }

        // Tạo series cho doanh thu
        XYChart.Series<String, Number> doanhThuSeries = new XYChart.Series<>();
        doanhThuSeries.setName("Doanh thu (VNĐ)");

        // Tạo series cho số lượng (nếu có)
        XYChart.Series<String, Number> soLuongSeries = new XYChart.Series<>();
        if (!soLuongData.isEmpty()) {
            soLuongSeries.setName("Số lượng đặt phòng");
        }

        // Sắp xếp keys để hiển thị theo thứ tự
        List<String> sortedKeys = new ArrayList<>(doanhThuData.keySet());
        Collections.sort(sortedKeys);

        // Thêm dữ liệu vào series
        for (String key : sortedKeys) {
            Double doanhThu = doanhThuData.getOrDefault(key, 0.0);
            Integer soLuong = soLuongData.getOrDefault(key, 0);

            doanhThuSeries.getData().add(new XYChart.Data<>(key, doanhThu));
            if (!soLuongData.isEmpty()) {
                soLuongSeries.getData().add(new XYChart.Data<>(key, soLuong));
            }
        }

        // Thêm series vào chart
        thongkeBarChart.getData().add(doanhThuSeries);
        if (!soLuongData.isEmpty()) {
            thongkeBarChart.getData().add(soLuongSeries);
        }

        // Cập nhật tổng doanh thu
        lblTongDoanhThu.setText(String.format("Tổng doanh thu: %,.0f VNĐ", tongDoanhThu));
    }

}