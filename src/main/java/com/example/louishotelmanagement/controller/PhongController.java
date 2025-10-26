package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent; // Sửa import đúng
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal; // Sửa import đúng
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;


public class PhongController implements Initializable {

    // --- FXML Components ---
    @FXML private Label tieuDeLabel;
    @FXML private Button btnNhanPhong, btnDatTT, btnDoiPhong, btnHuyDat, btnDichVu, btnThanhToan;
    @FXML private ComboBox<String> cbxTang;
    @FXML private ComboBox<String> cbxTrangThai; // THÊM MỚI
    @FXML private TableView<Phong> tableViewPhong;
    @FXML private TableColumn<Phong, String> maPhong;
    @FXML private TableColumn<Phong, String> loaiPhong;
    @FXML private TableColumn<Phong, Double> donGia; // Sửa thành BigDecimal
    @FXML private TableColumn<Phong, TrangThaiPhong> trangThai;
    @FXML private Label lblTongPhong;
    @FXML private Label lblPhongTrong;
    @FXML private Label lblPhongSuDung;
    @FXML private Label lblPhongBaoTri;

    // --- Data and DAO ---
    private PhongDAO phongDAO;
    private ObservableList<Phong> phongObservableList; // Danh sách gốc (chứa tất cả)
    private FilteredList<Phong> filteredPhongList;   // Danh sách đã lọc (để hiển thị)

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        phongDAO = new PhongDAO();
        System.out.println("Phong page initialized");

        configureTable();
        setupComboBoxes();
        setupFiltering(); // Thiết lập bộ lọc tự động
        loadTableData();
    }

    /**
     * Cấu hình cột, định dạng ô, và gán danh sách đã lọc cho TableView.
     */
    private void configureTable() {
        // 1. Khởi tạo danh sách gốc
        phongObservableList = FXCollections.observableArrayList();
        // 2. Tạo FilteredList để "gói" danh sách gốc
        filteredPhongList = new FilteredList<>(phongObservableList, p -> true);
        // 3. Gán FilteredList cho TableView
        tableViewPhong.setItems(filteredPhongList);

        // 4. Cấu hình các cột
        maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));

        loaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong lp = cellData.getValue().getLoaiPhong();
            return new SimpleStringProperty(lp != null ? lp.getTenLoai() : "N/A");
        });

        // Cột Đơn Giá (Sửa thành BigDecimal)
        donGia.setCellValueFactory(cellData -> {
            LoaiPhong lp = cellData.getValue().getLoaiPhong();
            Double gia = (lp != null) ? lp.getDonGia() : 0.0;
            return new SimpleObjectProperty<>(gia);
        });

        // Định dạng tiền tệ cho cột Đơn Giá (Sửa thành BigDecimal)
        donGia.setCellFactory(column -> new TableCell<Phong, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        // Cột Trạng Thái (Lấy dữ liệu và tô màu)
        trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        trangThai.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    String backgroundColor = switch (item) {
                        case DANG_SU_DUNG -> "#ffcdd2"; // Đỏ nhạt
                        case TRONG -> "#c8e6c9";        // Lục nhạt
                        case BAO_TRI -> "#bbdefb";      // Xanh nhạt
                        case DA_DAT -> "#fff9c4";       // Vàng nhạt
                        default -> "transparent";
                    };
                    setStyle("-fx-background-color: " + backgroundColor + "; -fx-alignment: CENTER;");
                }
            }
        });
    }

    /**
     * Tải dữ liệu từ CSDL và cập nhật thống kê.
     */
    public void loadTableData() {
        try {
            ArrayList<Phong> dsPhong = phongDAO.layDSPhong();
            phongObservableList.clear();
            phongObservableList.addAll(dsPhong);
            capNhatThongKe();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Khởi tạo dữ liệu cho cả hai ComboBox (Tầng và Trạng Thái).
     */
    private void setupComboBoxes() {
        // --- Setup ComboBox Tầng ---
        ObservableList<String> danhSachTang = FXCollections.observableArrayList();
        danhSachTang.add("Tất cả các tầng"); // Thêm tùy chọn "Tất cả"
        int soTangKhachSan = 4;
        for (int i = 1; i <= soTangKhachSan; i++) {
            danhSachTang.add("Tầng " + i);
        }
        cbxTang.setItems(danhSachTang);
        cbxTang.setValue("Tất cả các tầng"); // Đặt làm mặc định

        // --- Setup ComboBox Trạng Thái ---
        ObservableList<String> danhSachTrangThai = FXCollections.observableArrayList();
        danhSachTrangThai.add("Tất cả trạng thái"); // Thêm tùy chọn "Tất cả"
        // Thêm các trạng thái từ Enum
        danhSachTrangThai.add(TrangThaiPhong.TRONG.toString());
        danhSachTrangThai.add(TrangThaiPhong.DANG_SU_DUNG.toString());
        danhSachTrangThai.add(TrangThaiPhong.DA_DAT.toString());
        danhSachTrangThai.add(TrangThaiPhong.BAO_TRI.toString());

        cbxTrangThai.setItems(danhSachTrangThai);
        cbxTrangThai.setValue("Tất cả trạng thái"); // Đặt làm mặc định
    }

    /**
     * Thiết lập bộ lọc tự động. Bảng sẽ tự lọc lại
     * mỗi khi giá trị trong cbxTang hoặc cbxTrangThai thay đổi.
     */
    private void setupFiltering() {
        filteredPhongList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String tangDaChon = cbxTang.getValue();
            String trangThaiDaChon = cbxTrangThai.getValue();

            return phong -> {
                // --- Điều kiện 1: Lọc theo tầng ---
                boolean tangMatch = false;
                if (tangDaChon == null || tangDaChon.equals("Tất cả các tầng")) {
                    tangMatch = true;
                } else {
                    try {
                        int soTang = Integer.parseInt(tangDaChon.split("\\s+")[1]);
                        tangMatch = (phong.getTang() != null && phong.getTang() == soTang);
                    } catch (Exception e) {
                        tangMatch = false; // Lỗi parse thì không khớp
                    }
                }

                // --- Điều kiện 2: Lọc theo trạng thái ---
                boolean trangThaiMatch = false;
                if (trangThaiDaChon == null || trangThaiDaChon.equals("Tất cả trạng thái")) {
                    trangThaiMatch = true;
                } else {
                    trangThaiMatch = (phong.getTrangThai() != null && phong.getTrangThai().toString().equals(trangThaiDaChon));
                }

                // Chỉ hiển thị khi CẢ HAI điều kiện đều đúng
                return tangMatch && trangThaiMatch;
            };
        }, cbxTang.valueProperty(), cbxTrangThai.valueProperty())); // Kích hoạt khi 1 trong 2 thay đổi
    }


    /**
     * Cập nhật các Label thống kê dựa trên danh sách gốc.
     */
    private void capNhatThongKe() {
        long tongSo = phongObservableList.size();
        long soTrong = phongObservableList.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.TRONG)
                .count();
        long soSuDung = phongObservableList.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.DANG_SU_DUNG)
                .count();
        long soBaoTri = phongObservableList.stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.BAO_TRI)
                .count();

        // Cập nhật giao diện
        lblTongPhong.setText(String.valueOf(tongSo));
        lblPhongTrong.setText(String.valueOf(soTrong));
        lblPhongSuDung.setText(String.valueOf(soSuDung));
        lblPhongBaoTri.setText(String.valueOf(soBaoTri));
    }

    // --- Các phương thức chuyển trang (giữ nguyên) ---
    private ContentSwitcher switcher;

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML private void moNhanPhong(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml");
    }
    @FXML private void moDatPhong(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/dat-phong-view.fxml");
    }
    @FXML private void moDatTT(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/dat-phong-truc-tiep-view.fxml");
    }
    @FXML private void moDoiPhong(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/doi-phong-view.fxml");
    }
    @FXML private void moHuy(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/huy-dat-phong-view.fxml");
    }
    @FXML private void moDichVu(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/dat-dich-vu-view.fxml");
    }
    @FXML private void moThanhToan(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/thanh-toan-view.fxml");
    }
    @FXML private void moTraPhong(ActionEvent actionEvent) {
        if (switcher != null) switcher.switchContent("/com/example/louishotelmanagement/fxml/huy-phong-view.fxml");
    }
}