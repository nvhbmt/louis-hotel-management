package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.LoaiPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
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
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QuanLyPhongController implements Initializable {
    @FXML
    public Label lblsoPhongTrong;
    @FXML
    public Label lblSoPhongTrong;
    @FXML
    public Label lblSoPhongSuDung;
    @FXML
    public Label lblSoPhongBaoTri;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private ComboBox<Integer> cbTang;
    @FXML
    private ComboBox<String> cbTrangThai;
    @FXML
    private ComboBox<LoaiPhong> cbLocLoaiPhong;

    @FXML
    private TableView<Phong> tableViewPhong;
    @FXML
    private TableColumn<Phong, String> colMaPhong;
    @FXML
    private TableColumn<Phong, Integer> colTang;
    @FXML
    private TableColumn<Phong, TrangThaiPhong> colTrangThai;
    @FXML
    private TableColumn<Phong, String> colMoTa;
    @FXML
    private TableColumn<Phong, String> colTenLoaiPhong;
    @FXML
    private TableColumn<Phong, BigDecimal> colDonGia;
    @FXML
    public TableColumn<Phong, Void> colThaoTac;

    @FXML
    private Label lblTrangThai;
    @FXML
    private Label lblSoLuong;

    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private ObservableList<Phong> danhSachPhong;
    private ObservableList<Phong> danhSachPhongFiltered;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            phongDAO = new PhongDAO();
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
        int tongSoPhong = phongDAO.layDSPhong().size();
        int soPhongTrong = phongDAO.layDSPhongTheoTrangThai(TrangThaiPhong.TRONG).size();
        int soPhongSuDung = phongDAO.layDSPhongTheoTrangThai(TrangThaiPhong.DANG_SU_DUNG).size();
        int soPhongBaoTri = phongDAO.layDSPhongTheoTrangThai(TrangThaiPhong.BAO_TRI).size();

        lblsoPhongTrong.setText(String.valueOf(tongSoPhong));
        lblSoPhongTrong.setText(String.valueOf(soPhongTrong));
        lblSoPhongSuDung.setText(String.valueOf(soPhongSuDung));
        lblSoPhongBaoTri.setText(String.valueOf(soPhongBaoTri));
    }

    private void khoiTaoDuLieu() {
        danhSachPhong = FXCollections.observableArrayList();
        danhSachPhongFiltered = FXCollections.observableArrayList();
    }

    private void khoiTaoTableView() {
        // Thiết lập các cột
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colTang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));

        colTrangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    switch (item) {
                        case TrangThaiPhong.TRONG ->
                                setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;"); // Xanh lá
                        case TrangThaiPhong.DA_DAT ->
                                setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404;"); // Vàng
                        case TrangThaiPhong.DANG_SU_DUNG ->
                                setStyle("-fx-background-color: #cce5ff; -fx-text-fill: #004085;"); // Xanh dương
                        case TrangThaiPhong.BAO_TRI ->
                                setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;"); // Đỏ
                        default -> setStyle("");
                    }
                }
            }
        });

        // Cột tên loại phòng và đơn giá sẽ được thiết lập trong taiDuLieu()
        colTenLoaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createStringBinding(loaiPhong::getTenLoai) :
                    javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });

        colDonGia.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createObjectBinding(loaiPhong::getDonGia) :
                    javafx.beans.binding.Bindings.createObjectBinding(() -> BigDecimal.ZERO);
        });

        colThaoTac.setCellFactory(_ -> new TableCell<>() {

            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");

            {
                btnEdit.getStyleClass().addAll("btn", "btn-xs", "btn-info");
                btnEdit.setStyle("-fx-padding: 2 4 2 4; -fx-min-width: 40px; -fx-pref-width: 40px;");


                btnDelete.getStyleClass().addAll("btn", "btn-xs", "btn-danger");
                btnDelete.setStyle("-fx-padding: 2 4 2 4; -fx-min-width: 40px; -fx-pref-width: 40px;");

                btnEdit.setOnAction(_ -> {
                    Phong phong = getTableView().getItems().get(getIndex());
                    handleSuaPhong(phong);
                });

                btnDelete.setOnAction(_ -> {
                    Phong phong = getTableView().getItems().get(getIndex());
                    handleXoaPhong(phong);
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
        tableViewPhong.setItems(danhSachPhongFiltered);

        // Cho phép chọn nhiều dòng
        tableViewPhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void khoiTaoComboBox() {
        // Khởi tạo ComboBox tầng
        List<Integer> danhSachTang = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            danhSachTang.add(i);
        }
        cbTang.setItems(FXCollections.observableArrayList(danhSachTang));
        cbTang.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn tầng");
                } else {
                    setText("Tầng " + item);
                }
            }
        });
        cbTang.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn tầng");
                } else {
                    setText("Tầng " + item);
                }
            }
        });

        // Khởi tạo ComboBox trạng thái với các trạng thái phù hợp
        List<String> danhSachTrangThai = List.of("Trống", "Đã đặt", "Đang sử dụng", "Bảo trì");
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

        // Khởi tạo ComboBox loại phòng để filter
        khoiTaoComboBoxLoaiPhong();
    }

    private void khoiTaoComboBoxLoaiPhong() {
        try {
            List<LoaiPhong> danhSachLoaiPhong = loaiPhongDAO.layDSLoaiPhong();

            // Thiết lập ComboBox để hiển thị tên loại phòng
            cbLocLoaiPhong.setCellFactory(_ -> new ListCell<>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Chọn loại phòng");
                    } else {
                        setText(item.getTenLoai());
                    }
                }
            });

            cbLocLoaiPhong.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(LoaiPhong item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Chọn loại phòng");
                    } else {
                        setText(item.getTenLoai());
                    }
                }
            });

            cbLocLoaiPhong.setItems(FXCollections.observableArrayList(danhSachLoaiPhong));

        } catch (SQLException e) {
            hienThiThongBao("Lỗi", "Không thể tải danh sách loại phòng: " + e.getMessage());
        }
    }

    private void taiDuLieu() {
        try {
            // Lấy danh sách phòng từ database
            List<Phong> dsPhong = phongDAO.layDSPhong();

            danhSachPhong.clear();
            danhSachPhong.addAll(dsPhong);
            capNhatThongKe();

            // Áp dụng filter hiện tại
            apDungFilter();

            capNhatTrangThai("Đã tải " + dsPhong.size() + " phòng");

        } catch (SQLException e) {
            hienThiThongBao("Lỗi", "Không thể tải dữ liệu phòng: " + e.getMessage());
        }
    }

    private void apDungFilter() {
        danhSachPhongFiltered.clear();

        List<Phong> filtered = danhSachPhong.stream()
                .filter(phong -> {
                    // Filter theo tìm kiếm
                    String timKiem = txtTimKiem.getText().toLowerCase();
                    if (!timKiem.isEmpty()) {
                        boolean matchMaPhong = phong.getMaPhong().toLowerCase().contains(timKiem);
                        boolean matchTang = phong.getTang() != null &&
                                phong.getTang().toString().contains(timKiem);
                        if (!matchMaPhong && !matchTang) {
                            return false;
                        }
                    }

                    // Filter theo tầng
                    Integer tangFilter = cbTang.getValue();
                    if (tangFilter != null && (phong.getTang() == null || !phong.getTang().equals(tangFilter))) {
                        return false;
                    }

                    // Filter theo trạng thái
                    String trangThaiFilter = cbTrangThai.getValue();
                    if (trangThaiFilter != null && (phong.getTrangThai() == null ||
                            !phong.getTrangThai().toString().equalsIgnoreCase(trangThaiFilter))) {
                        return false;
                    }

                    // Filter theo loại phòng
                    LoaiPhong loaiPhongFilter = cbLocLoaiPhong.getValue();
                    return loaiPhongFilter == null || (phong.getLoaiPhong() != null &&
                            phong.getLoaiPhong().getMaLoaiPhong().equals(loaiPhongFilter.getMaLoaiPhong()));
                })
                .toList();

        danhSachPhongFiltered.addAll(filtered);
        lblSoLuong.setText("Tổng số phòng: " + danhSachPhongFiltered.size());
    }

    @FXML
    private void handleThemPhong() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/phong-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Thêm Phòng Mới");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            // Thiết lập controller và dữ liệu
            PhongDialogController controller = loader.getController();
            controller.setMode("ADD");

            dialog.showAndWait();

            // Làm mới dữ liệu sau khi thêm
            taiDuLieu();

        } catch (IOException e) {
            hienThiThongBao("Lỗi", "Không thể mở form thêm phòng: " + e.getMessage());
        }
    }

    @FXML
    private void handleSuaPhong(Phong phong) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/phong-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Sửa Thông Tin Phòng");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            // Thiết lập controller và dữ liệu
            PhongDialogController controller = loader.getController();
            controller.setMode("EDIT");
            controller.setPhong(phong);
            dialog.showAndWait();

            // Làm mới dữ liệu sau khi sửa
            taiDuLieu();

        } catch (IOException e) {
            hienThiThongBao("Lỗi", "Không thể mở form sửa phòng: " + e.getMessage());
        }
    }

    @FXML
    private void handleXoaPhong(Phong phong) {

        // Xác nhận xóa
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa phòng này?");
        alert.setContentText("Phòng: " + phong.getMaPhong() + " - Tầng: " + phong.getTang());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Kiểm tra xem phòng có đang được sử dụng không
                    if (phongDAO.kiemTraPhongDuocSuDung(phong.getMaPhong())) {
                        hienThiThongBao("Lỗi", "Không thể xóa phòng này vì đang được sử dụng!");
                        return;
                    }

                    // Xóa phòng
                    if (phongDAO.xoaPhong(phong.getMaPhong())) {
                        hienThiThongBao("Thành công", "Đã xóa phòng thành công!");
                        taiDuLieu();
                    } else {
                        hienThiThongBao("Lỗi", "Không thể xóa phòng!");
                    }

                } catch (SQLException e) {
                    hienThiThongBao("Lỗi", "Lỗi khi xóa phòng: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleLamMoi() {
        taiDuLieu();
        txtTimKiem.clear();
        cbTang.setValue(null);
        cbTrangThai.setValue(null);
        cbLocLoaiPhong.setValue(null);
    }

    @FXML
    private void handleTimKiem() {
        apDungFilter();
    }

    @FXML
    private void handleLocTang() {
        apDungFilter();
    }

    @FXML
    private void handleLocTrangThai() {
        apDungFilter();
    }

    @FXML
    private void handleLocLoaiPhong() {
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
