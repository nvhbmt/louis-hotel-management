package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoaiPhongController implements Initializable {

    @FXML
    private Label lblTongSoLoaiPhong;
    @FXML
    private Label lblGiaThapNhat;
    @FXML
    private Label lblGiaCaoNhat;
    @FXML
    private Label lblGiaTrungBinh;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private ComboBox<String> cbLocGia;
    @FXML
    private TableView<LoaiPhong> tableViewLoaiPhong;
    @FXML
    private TableColumn<LoaiPhong, String> colMaLoaiPhong;
    @FXML
    private TableColumn<LoaiPhong, String> colTenLoai;
    @FXML
    private TableColumn<LoaiPhong, Double> colDonGia;
    @FXML
    private TableColumn<LoaiPhong, String> colMoTa;
    @FXML
    private TableColumn<LoaiPhong, Void> colThaoTac;
    @FXML
    private Label lblTrangThai;
    @FXML
    private Label lblSoLuong;
    @FXML
    private Button btnThemLoaiPhong;
    @FXML
    private Button btnLamMoi;

    private LoaiPhongDAO loaiPhongDAO;
    private ObservableList<LoaiPhong> danhSachLoaiPhong;
    private ObservableList<LoaiPhong> danhSachLoaiPhongFiltered;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loaiPhongDAO = new LoaiPhongDAO();

            // Khởi tạo dữ liệu
            khoiTaoDuLieu();
            khoiTaoTableView();
            khoiTaoComboBox();

            // Load dữ liệu
            taiDuLieu();

        } catch (Exception e) {
            hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void capNhatThongKe() throws SQLException {
        List<LoaiPhong> dsLoaiPhong = loaiPhongDAO.layDSLoaiPhong();
        
        int tongSoLoaiPhong = dsLoaiPhong.size();
        lblTongSoLoaiPhong.setText(String.valueOf(tongSoLoaiPhong));

        if (tongSoLoaiPhong > 0) {
            Double giaThapNhat = dsLoaiPhong.stream()
                    .map(LoaiPhong::getDonGia)
                    .min(Double::compareTo)
                    .orElse(0.0);
            
            Double giaCaoNhat = dsLoaiPhong.stream()
                    .map(LoaiPhong::getDonGia)
                    .max(Double::compareTo)
                    .orElse(0.0);
            
            Double giaTrungBinh = dsLoaiPhong.stream()
                    .map(LoaiPhong::getDonGia)
                    .reduce(0.0, Double::sum) / tongSoLoaiPhong;

            lblGiaThapNhat.setText(formatCurrency(giaThapNhat));
            lblGiaCaoNhat.setText(formatCurrency(giaCaoNhat));
            lblGiaTrungBinh.setText(formatCurrency(giaTrungBinh));
        } else {
            lblGiaThapNhat.setText("0 VNĐ");
            lblGiaCaoNhat.setText("0 VNĐ");
            lblGiaTrungBinh.setText("0 VNĐ");
        }
    }

    private String formatCurrency(Double amount) {
        if (amount == null) return "0 VNĐ";
        return String.format("%,.0f VNĐ", amount);
    }

    private void khoiTaoDuLieu() {
        danhSachLoaiPhong = FXCollections.observableArrayList();
        danhSachLoaiPhongFiltered = FXCollections.observableArrayList();
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaLoaiPhong.setCellValueFactory(new PropertyValueFactory<>("maLoaiPhong"));
        colTenLoai.setCellValueFactory(new PropertyValueFactory<>("tenLoai"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        // Format cột đơn giá
        colDonGia.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });

        // Cột thao tác
        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");

            {
                btnEdit.getStyleClass().addAll("btn", "btn-xs", "btn-info");
                btnEdit.setStyle("-fx-padding: 2 4 2 4; -fx-min-width: 40px; -fx-pref-width: 40px;");

                btnDelete.getStyleClass().addAll("btn", "btn-xs", "btn-danger");
                btnDelete.setStyle("-fx-padding: 2 4 2 4; -fx-min-width: 40px; -fx-pref-width: 40px;");

                btnEdit.setOnAction(_ -> {
                    LoaiPhong loaiPhong = getTableView().getItems().get(getIndex());
                    handleSuaLoaiPhong(loaiPhong);
                });

                btnDelete.setOnAction(_ -> {
                    LoaiPhong loaiPhong = getTableView().getItems().get(getIndex());
                    handleXoaLoaiPhong(loaiPhong);
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
        tableViewLoaiPhong.setItems(danhSachLoaiPhongFiltered);

        // Cho phép chọn một dòng
        tableViewLoaiPhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox lọc giá
        List<String> danhSachLocGia = List.of(
            "Dưới 1 triệu",
            "1-2 triệu", 
            "2-5 triệu",
            "5-10 triệu",
            "Trên 10 triệu"
        );
        cbLocGia.setItems(FXCollections.observableArrayList(danhSachLocGia));
        cbLocGia.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn khoảng giá");
                } else {
                    setText(item);
                }
            }
        });
        cbLocGia.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn khoảng giá");
                } else {
                    setText(item);
                }
            }
        });
    }

    private void taiDuLieu() {
        try {
            // Lấy danh sách loại phòng từ database
            List<LoaiPhong> dsLoaiPhong = loaiPhongDAO.layDSLoaiPhong();

            danhSachLoaiPhong.clear();
            danhSachLoaiPhong.addAll(dsLoaiPhong);
            capNhatThongKe();

            // Áp dụng filter hiện tại
            apDungFilter();

            capNhatTrangThai("Đã tải " + dsLoaiPhong.size() + " loại phòng");

        } catch (SQLException e) {
            hienThiThongBao("Lỗi", "Không thể tải dữ liệu loại phòng: " + e.getMessage());
        }
    }

    private void apDungFilter() {
        danhSachLoaiPhongFiltered.clear();

        List<LoaiPhong> filtered = danhSachLoaiPhong.stream()
                .filter(loaiPhong -> {
                    // Filter theo tìm kiếm
                    String timKiem = txtTimKiem.getText().toLowerCase();
                    if (!timKiem.isEmpty()) {
                        boolean matchMa = loaiPhong.getMaLoaiPhong().toLowerCase().contains(timKiem);
                        boolean matchTen = loaiPhong.getTenLoai().toLowerCase().contains(timKiem);
                        if (!matchMa && !matchTen) {
                            return false;
                        }
                    }

                    // Filter theo giá
                    String giaFilter = cbLocGia.getValue();
                    if (giaFilter != null) {
                        Double donGia = loaiPhong.getDonGia();
                        if (donGia == null) return false;
                        
                        switch (giaFilter) {
                            case "Dưới 1 triệu":
                                return donGia < 1000000.0;
                            case "1-2 triệu":
                                return donGia >= 1000000.0 && donGia <= 2000000.0;
                            case "2-5 triệu":
                                return donGia > 2000000.0 && donGia <= 5000000.0;
                            case "5-10 triệu":
                                return donGia > 5000000.0 && donGia <= 10000000.0;
                            case "Trên 10 triệu":
                                return donGia > 10000000.0;
                        }
                    }

                    return true;
                })
                .toList();

        danhSachLoaiPhongFiltered.addAll(filtered);
        lblSoLuong.setText("Tổng số loại phòng: " + danhSachLoaiPhongFiltered.size());
    }

    @FXML
    private void handleThemLoaiPhong() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/loai-phong-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Thêm Loại Phòng Mới");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            // Thiết lập controller và dữ liệu
            LoaiPhongDialogController controller = loader.getController();
            controller.setMode("ADD");

            dialog.showAndWait();

            // Làm mới dữ liệu sau khi thêm
            taiDuLieu();

        } catch (IOException e) {
            hienThiThongBao("Lỗi", "Không thể mở form thêm loại phòng: " + e.getMessage());
        }
    }

    @FXML
    private void handleSuaLoaiPhong(LoaiPhong loaiPhong) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/loai-phong-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Sửa Thông Tin Loại Phòng");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            // Thiết lập controller và dữ liệu
            LoaiPhongDialogController controller = loader.getController();
            controller.setMode("EDIT");
            controller.setLoaiPhong(loaiPhong);
            dialog.showAndWait();

            // Làm mới dữ liệu sau khi sửa
            taiDuLieu();

        } catch (IOException e) {
            hienThiThongBao("Lỗi", "Không thể mở form sửa loại phòng: " + e.getMessage());
        }
    }

    @FXML
    private void handleXoaLoaiPhong(LoaiPhong loaiPhong) {
        // Xác nhận xóa
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa loại phòng này?");
        alert.setContentText("Loại phòng: " + loaiPhong.getTenLoai() + " - Mã: " + loaiPhong.getMaLoaiPhong());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Kiểm tra xem loại phòng có đang được sử dụng không
                    if (loaiPhongDAO.kiemTraLoaiPhongDuocSuDung(loaiPhong.getMaLoaiPhong())) {
                        hienThiThongBao("Lỗi", "Không thể xóa loại phòng này vì đang được sử dụng!");
                        return;
                    }

                    // Xóa loại phòng
                    if (loaiPhongDAO.xoaLoaiPhong(loaiPhong.getMaLoaiPhong())) {
                        hienThiThongBao("Thành công", "Đã xóa loại phòng thành công!");
                        taiDuLieu();
                    } else {
                        hienThiThongBao("Lỗi", "Không thể xóa loại phòng!");
                    }

                } catch (SQLException e) {
                    hienThiThongBao("Lỗi", "Lỗi khi xóa loại phòng: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbLocGia.setValue(null);
    }

    @FXML
    private void handleTimKiem() {
        apDungFilter();
    }

    @FXML
    private void handleLocGia() {
        apDungFilter();
    }

    private void capNhatTrangThai(String message) {
        lblTrangThai.setText(message);
    }

    private void hienThiThongBao(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.showAndWait();
    }
}