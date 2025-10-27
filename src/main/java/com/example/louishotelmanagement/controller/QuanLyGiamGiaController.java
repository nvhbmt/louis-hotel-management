package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.MaGiamGiaDAO;
import com.example.louishotelmanagement.model.KieuGiamGia;
import com.example.louishotelmanagement.model.MaGiamGia;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class QuanLyGiamGiaController implements Initializable {
    @FXML
    public Label lblTongSoMaGiamGia;
    @FXML
    public Label lblSoMaDangHoatDong;
    @FXML
    public Label lblSoMaHetHan;
    @FXML
    public Label lblSoMaChuaBatDau;
    @FXML
    public BorderPane rootPane;
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

    private void capNhatThongKe() throws SQLException {
        List<MaGiamGia> dsMaGiamGia = maGiamGiaDAO.layDSMaGiamGia();
        List<MaGiamGia> dsMaDangHoatDong = maGiamGiaDAO.layMaGiamGiaDangHoatDong();

        int tongSoMaGiamGia = dsMaGiamGia.size();
        int soMaDangHoatDong = dsMaDangHoatDong.size();
        int soMaHetHan = 0;
        int soMaChuaBatDau = 0;

        LocalDate ngayHienTai = LocalDate.now();

        for (MaGiamGia mg : dsMaGiamGia) {
            if (mg.getNgayKetThuc().isBefore(ngayHienTai)) {
                soMaHetHan++;
            } else if (mg.getNgayBatDau().isAfter(ngayHienTai)) {
                soMaChuaBatDau++;
            }
        }

        lblTongSoMaGiamGia.setText(String.valueOf(tongSoMaGiamGia));
        lblSoMaDangHoatDong.setText(String.valueOf(soMaDangHoatDong));
        lblSoMaHetHan.setText(String.valueOf(soMaHetHan));
        lblSoMaChuaBatDau.setText(String.valueOf(soMaChuaBatDau));
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

        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");

            {
                btnEdit.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-edit");
                btnDelete.getStyleClass().addAll("btn", "btn-xs", "btn-danger", "btn-table-delete");

                btnEdit.setOnAction(_ -> {
                    MaGiamGia maGiamGia = getTableView().getItems().get(getIndex());
                    handleSuaMaGiamGia(maGiamGia);
                });

                btnDelete.setOnAction(_ -> {
                    MaGiamGia maGiamGia = getTableView().getItems().get(getIndex());
                    handleXoaMaGiamGia(maGiamGia);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

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
            capNhatThongKe();

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
    private void handleThemMaGiamGia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/ma-giam-gia-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Thêm Mã Giảm Giá Mới");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            // Thiết lập controller và dữ liệu
            MaGiamGiaDialogController controller = loader.getController();
            controller.setMode("ADD");

            dialog.showAndWait();

            // Làm mới dữ liệu sau khi thêm
            taiDuLieu();

        } catch (IOException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể mở form thêm mã giảm giá: " + e.getMessage());
        }
    }

    @FXML
    private void handleSuaMaGiamGia(MaGiamGia maGiamGia) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/ma-giam-gia-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Sửa Thông Tin Mã Giảm Giá");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            // Thiết lập controller và dữ liệu
            MaGiamGiaDialogController controller = loader.getController();
            controller.setMode("EDIT");
            controller.setMaGiamGia(maGiamGia);
            dialog.showAndWait();

            // Làm mới dữ liệu sau khi sửa
            taiDuLieu();

        } catch (IOException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể mở form sửa mã giảm giá: " + e.getMessage());
        }
    }

    @FXML
    private void handleXoaMaGiamGia(MaGiamGia maGiamGia) {
        // Xác nhận xóa
        String message = "Bạn có chắc chắn muốn xóa mã giảm giá này?\nMã: " + maGiamGia.getMaGG() + " - Code: " + maGiamGia.getCode();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận xóa", message)) {
            try {
                // Xóa mã giảm giá
                if (maGiamGiaDAO.xoaMaGiamGia(maGiamGia.getMaGG())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đã xóa mã giảm giá thành công!");
                    taiDuLieu();
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể xóa mã giảm giá!");
                }

            } catch (SQLException e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi xóa mã giảm giá: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbKieuGiamGia.setValue(null);
        cbTrangThai.setValue(null);
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
}