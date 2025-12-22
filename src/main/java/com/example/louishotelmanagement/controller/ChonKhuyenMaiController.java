package com.example.louishotelmanagement.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import com.example.louishotelmanagement.dao.KhuyenMaiDAO;
import com.example.louishotelmanagement.model.KhuyenMai;
import com.example.louishotelmanagement.model.KieuGiamGia;
import com.example.louishotelmanagement.util.Refreshable;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.view.ChonKhuyenMaiDiaLogView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ChonKhuyenMaiController implements Refreshable {

    @FXML
    public BorderPane rootPane;
    @FXML
    public Button btnLamMoi;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private ComboBox<KieuGiamGia> cbKieuGiamGia;
    @FXML
    private ComboBox<String> cbTrangThai;

    @FXML
    private TableView<KhuyenMai> tableViewKhuyenMai;
    @FXML
    private TableColumn<KhuyenMai, String> colMaKM;
    @FXML
    private TableColumn<KhuyenMai, String> colCode;
    @FXML
    private TableColumn<KhuyenMai, Double> colGiamGia;
    @FXML
    private TableColumn<KhuyenMai, KieuGiamGia> colKieuGiamGia;
    @FXML
    private TableColumn<KhuyenMai, LocalDate> colNgayBatDau;
    @FXML
    private TableColumn<KhuyenMai, LocalDate> colNgayKetThuc;
    @FXML
    private TableColumn<KhuyenMai, Double> colTongTienToiThieu;
    @FXML
    private TableColumn<KhuyenMai, String> colTrangThai;
    @FXML
    private TableColumn<KhuyenMai, String> colMoTa;
    @FXML
    public TableColumn<KhuyenMai, Void> colThaoTac;
    @FXML
    private Button btnHuy;
    @FXML
    private Button btnChon;


    private KhuyenMaiDAO khuyenMaiDAO;
    private ObservableList<KhuyenMai> danhSachKhuyenMai;
    private ObservableList<KhuyenMai> danhSachKhuyenMaiFiltered;
    private KhuyenMai khuyenMaiDuocChon;
    @Override
    public void refreshData() throws SQLException, Exception {
        khoiTaoDuLieu();
        khoiTaoTableView();
        khoiTaoComboBox();
    }
    public ChonKhuyenMaiController(ChonKhuyenMaiDiaLogView view) {
        this.rootPane = view.getRootPane();
        this.txtTimKiem = view.getTxtTimKiem();
        this.cbKieuGiamGia = view.getCbKieuGiamGia();
        this.cbTrangThai = view.getCbTrangThai();
        this.btnLamMoi = view.getBtnLamMoi();
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
        this.btnLamMoi.setOnAction(event -> handleLamMoi());
        this.btnHuy = view.getBtnHuy();
        this.btnChon = view.getBtnChon();
        this.btnHuy.setOnAction(event -> handleHuy());
        this.btnChon.setOnAction(event -> handleChon());
        initialize();
    }
    public void initialize() {
        try {
             khuyenMaiDAO = new KhuyenMaiDAO();

            // Khởi tạo dữ liệu
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();

            // Load dữ liệu
            taiDuLieu();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void khoiTaoDuLieu() {
        danhSachKhuyenMai = FXCollections.observableArrayList();
        danhSachKhuyenMaiFiltered = FXCollections.observableArrayList();
    }

    private void khoiTaoTableView() {
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
                super.updateItem(item, empty);
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
                super.updateItem(item, empty);
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
                super.updateItem(item, empty);
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
                super.updateItem(item, empty);
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
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().clear();
                } else {
                    setText(item);
                    getStyleClass().clear();
                    switch (item.toLowerCase()) {
                        case "hoạt động" -> getStyleClass().add("status-hoat-dong");
                        case "hết hạn" -> getStyleClass().add("status-het-han");
                        case "chưa bắt đầu" -> getStyleClass().add("status-chua-bat-dau");
                        case "tạm dừng" -> getStyleClass().add("status-tam-dung");
                        default -> {
                            // Không thêm style class nào
                        }
                    }
                }
            }
        });

        // Thiết lập TableView
        tableViewKhuyenMai.setItems(danhSachKhuyenMaiFiltered);

        // Cho phép chọn nhiều dòng
        tableViewKhuyenMai.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox kiểu giảm giá
        List<KieuGiamGia> danhSachKieuGiamGia = List.of(KieuGiamGia.PERCENT, KieuGiamGia.AMOUNT);
        cbKieuGiamGia.setItems(FXCollections.observableArrayList(danhSachKieuGiamGia));
        cbKieuGiamGia.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(KieuGiamGia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn kiểu giảm giá");
                } else {
                    setText(item == KieuGiamGia.PERCENT ? "Phần trăm (%)" : "Số tiền (VNĐ)");
                }
            }
        });
        cbKieuGiamGia.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(KieuGiamGia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn kiểu giảm giá");
                } else {
                    setText(item == KieuGiamGia.PERCENT ? "Phần trăm (%)" : "Số tiền (VNĐ)");
                }
            }
        });

        // Khởi tạo ComboBox trạng thái
        List<String> danhSachTrangThai = List.of("Đang diễn ra", "Hết hạn", "Chưa diễn ra", "Tạm dừng");
        cbTrangThai.setItems(FXCollections.observableArrayList(danhSachTrangThai));
        cbTrangThai.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn trạng thái");
                } else {
                    setText(item);
                }
            }
        });
        cbTrangThai.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn trạng thái");
                } else {
                    setText(item);
                }
            }
        });
    }

    private void taiDuLieu() {
        try {
            // Lấy danh sách mã giảm giá từ database
            List<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.layDSKhuyenMai();

            LocalDate ngayHienTai = LocalDate.now();

            for (KhuyenMai km : dsKhuyenMai) {
                LocalDate batDau = km.getNgayBatDau();
                LocalDate ketThuc = km.getNgayKetThuc();

                if (ketThuc.isBefore(ngayHienTai)) {
                    km.setTrangThai("Hết hạn");
                }
                else if (batDau.isAfter(ngayHienTai)) {
                    km.setTrangThai("Chưa diễn ra");
                }
                else if ((batDau.isBefore(ngayHienTai) || batDau.isEqual(ngayHienTai)) &&
                        (ketThuc.isAfter(ngayHienTai) || ketThuc.isEqual(ngayHienTai))) {
                    km.setTrangThai("Đang diễn ra");
                }

                // Cập nhật xuống DB nếu thay đổi
                khuyenMaiDAO.capNhatKhuyenMai(km);
            }

            danhSachKhuyenMai.clear();
            danhSachKhuyenMai.addAll(dsKhuyenMai);
            // Áp dụng filter hiện tại
            apDungFilter();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu mã giảm giá: " + e.getMessage());
        }
    }

    private void apDungFilter() {
        danhSachKhuyenMaiFiltered.clear();

        List<KhuyenMai> filtered = danhSachKhuyenMai.stream()
                .filter(khuyenMai -> {
                    // Filter theo tìm kiếm
                    String timKiem = txtTimKiem.getText().toLowerCase();
                    if (!timKiem.isEmpty()) {
                        boolean matchMaKM = khuyenMai.getMaKM().toLowerCase().contains(timKiem);
                        boolean matchCode = khuyenMai.getCode().toLowerCase().contains(timKiem);
                        boolean matchMoTa = khuyenMai.getMoTa() != null &&
                                khuyenMai.getMoTa().toLowerCase().contains(timKiem);
                        if (!matchMaKM && !matchCode && !matchMoTa) {
                            return false;
                        }
                    }

                    // Filter theo kiểu giảm giá
                    KieuGiamGia kieuGiamGiaFilter = cbKieuGiamGia.getValue();
                    if (kieuGiamGiaFilter != null && !khuyenMai.getKieuGiamGia().equals(kieuGiamGiaFilter)) {
                        return false;
                    }

                    // Filter theo trạng thái
                    String trangThaiFilter = cbTrangThai.getValue();
                    if (trangThaiFilter != null && (khuyenMai.getTrangThai() == null ||
                            !khuyenMai.getTrangThai().equalsIgnoreCase(trangThaiFilter))) {
                        return false;
                    }

                    return true;
                })
                .toList();

        danhSachKhuyenMaiFiltered.addAll(filtered);
    }

    @FXML
    private void handleTimKiem() {
        apDungFilter();
    }

    @FXML
    private void handleLocKieuGiamGia() {
        apDungFilter();
    }

    @FXML
    private void handleLocTrangThai() {
        apDungFilter();
    }
    @FXML
    private void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbKieuGiamGia.setValue(null);
        cbTrangThai.setValue(null);
    }

    public KhuyenMai getKhuyenMaiDuocChon() {
        return khuyenMaiDuocChon;
    }
// ...

    @FXML
    private void handleChon(){
        KhuyenMai selectedMaKM = tableViewKhuyenMai.getSelectionModel().getSelectedItem();

        if (selectedMaKM != null) {
            khuyenMaiDuocChon = selectedMaKM;
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } else {
            ThongBaoUtil.hienThiThongBao("Thông báo", "Vui lòng chọn một mã giảm giá để sử dụng.");
        }
    }
    @FXML
    private void handleHuy(){
        khuyenMaiDuocChon = null;
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

}

