package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.MaGiamGiaDAO;
import com.example.louishotelmanagement.model.KieuGiamGia;
import com.example.louishotelmanagement.model.MaGiamGia;
import com.example.louishotelmanagement.util.Refreshable;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ChonKhuyenMaiController implements Initializable, Refreshable {

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
    private TableView<MaGiamGia> tableViewMaGiamGia;
    @FXML
    private TableColumn<MaGiamGia, String> colMaGG;
    @FXML
    private TableColumn<MaGiamGia, String> colCode;
    @FXML
    private TableColumn<MaGiamGia, Double> colGiamGia;
    @FXML
    private TableColumn<MaGiamGia, KieuGiamGia> colKieuGiamGia;
    @FXML
    private TableColumn<MaGiamGia, LocalDate> colNgayBatDau;
    @FXML
    private TableColumn<MaGiamGia, LocalDate> colNgayKetThuc;
    @FXML
    private TableColumn<MaGiamGia, Double> colTongTienToiThieu;
    @FXML
    private TableColumn<MaGiamGia, String> colTrangThai;
    @FXML
    private TableColumn<MaGiamGia, String> colMoTa;
    @FXML
    public TableColumn<MaGiamGia, Void> colThaoTac;

    private MaGiamGiaDAO maGiamGiaDAO;
    private ObservableList<MaGiamGia> danhSachMaGiamGia;
    private ObservableList<MaGiamGia> danhSachMaGiamGiaFiltered;
    private MaGiamGia maGiamGiaDuocChon;
    @Override
    public void refreshData() throws SQLException, Exception {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
             maGiamGiaDAO = new MaGiamGiaDAO();

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
        danhSachMaGiamGia = FXCollections.observableArrayList();
        danhSachMaGiamGiaFiltered = FXCollections.observableArrayList();
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaGG.setCellValueFactory(new PropertyValueFactory<>("maGG"));
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
        tableViewMaGiamGia.setItems(danhSachMaGiamGiaFiltered);

        // Cho phép chọn nhiều dòng
        tableViewMaGiamGia.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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
            List<MaGiamGia> dsMaGiamGia = maGiamGiaDAO.layDSMaGiamGia();

            LocalDate ngayHienTai = LocalDate.now();

            for (MaGiamGia mg : dsMaGiamGia) {
                LocalDate batDau = mg.getNgayBatDau();
                LocalDate ketThuc = mg.getNgayKetThuc();

                if (ketThuc.isBefore(ngayHienTai)) {
                    mg.setTrangThai("Hết hạn");
                }
                else if (batDau.isAfter(ngayHienTai)) {
                    mg.setTrangThai("Chưa diễn ra");
                }
                else if ((batDau.isBefore(ngayHienTai) || batDau.isEqual(ngayHienTai)) &&
                        (ketThuc.isAfter(ngayHienTai) || ketThuc.isEqual(ngayHienTai))) {
                    mg.setTrangThai("Đang diễn ra");
                }

                // Cập nhật xuống DB nếu thay đổi
                maGiamGiaDAO.capNhatMaGiamGia(mg);
            }

            danhSachMaGiamGia.clear();
            danhSachMaGiamGia.addAll(dsMaGiamGia);
            // Áp dụng filter hiện tại
            apDungFilter();
        } catch (SQLException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể tải dữ liệu mã giảm giá: " + e.getMessage());
        }
    }

    private void apDungFilter() {
        danhSachMaGiamGiaFiltered.clear();

        List<MaGiamGia> filtered = danhSachMaGiamGia.stream()
                .filter(maGiamGia -> {
                    // Filter theo tìm kiếm
                    String timKiem = txtTimKiem.getText().toLowerCase();
                    if (!timKiem.isEmpty()) {
                        boolean matchMaGG = maGiamGia.getMaGG().toLowerCase().contains(timKiem);
                        boolean matchCode = maGiamGia.getCode().toLowerCase().contains(timKiem);
                        boolean matchMoTa = maGiamGia.getMoTa() != null &&
                                maGiamGia.getMoTa().toLowerCase().contains(timKiem);
                        if (!matchMaGG && !matchCode && !matchMoTa) {
                            return false;
                        }
                    }

                    // Filter theo kiểu giảm giá
                    KieuGiamGia kieuGiamGiaFilter = cbKieuGiamGia.getValue();
                    if (kieuGiamGiaFilter != null && !maGiamGia.getKieuGiamGia().equals(kieuGiamGiaFilter)) {
                        return false;
                    }

                    // Filter theo trạng thái
                    String trangThaiFilter = cbTrangThai.getValue();
                    if (trangThaiFilter != null && (maGiamGia.getTrangThai() == null ||
                            !maGiamGia.getTrangThai().equalsIgnoreCase(trangThaiFilter))) {
                        return false;
                    }

                    return true;
                })
                .toList();

        danhSachMaGiamGiaFiltered.addAll(filtered);
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

    public MaGiamGia getMaGiamGiaDuocChon() {
        return maGiamGiaDuocChon;
    }
// ...

    @FXML
    private void handleChon(){
        MaGiamGia selectedMaGG = tableViewMaGiamGia.getSelectionModel().getSelectedItem();

        if (selectedMaGG != null) {
            maGiamGiaDuocChon = selectedMaGG;
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } else {
            ThongBaoUtil.hienThiThongBao("Thông báo", "Vui lòng chọn một mã giảm giá để sử dụng.");
        }
    }
    @FXML
    private void handleHuy(){
        maGiamGiaDuocChon = null;
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

}

