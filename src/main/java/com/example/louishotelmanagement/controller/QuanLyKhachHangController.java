package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.TrangThaiKhachHang;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QuanLyKhachHangController implements Initializable {

    private KhachHangDAO khachHangDAO;

    @FXML
    private ComboBox<String> cmbHang;
    @FXML
    private ComboBox<TrangThaiKhachHang> cmbTrangThai;
    @FXML
    private TableView<KhachHang> tableViewKhachHang;
    @FXML
    private TableColumn<KhachHang, String> colMaKH;
    @FXML
    private TableColumn<KhachHang, String> colHoTen;
    @FXML
    private TableColumn<KhachHang, String> colSoDT;
    @FXML
    private TableColumn<KhachHang, String> colEmail;
    @FXML
    private TableColumn<KhachHang, String> colHangKhach;
    @FXML
    private TableColumn<KhachHang, String> colGhiChu;
    @FXML
    private TableColumn<KhachHang, String> colTrangThai;
    @FXML
    private TableColumn<KhachHang, Void> colThaoTac;

    private ArrayList<KhachHang> dsKhachHang;

    @FXML
    private Label lblSoKhachHang;
    @FXML
    private Label lblCheckout;
    @FXML
    private Label lblDangLuuTru;
    @FXML
    private Label lblKhachVIP;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private Button btnLamMoi;
    @FXML
    private Button btnThemKhachHang;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            khachHangDAO = new KhachHangDAO();

            //Khởi tạo dữ liệu
            setupComboBox();
            setupTableColumns();
            setupListener();
            loadData();

        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------ Khởi tạo UI ------------
    private void setupComboBox() {
        // Hạng khách
        ObservableList<String> dsHang = FXCollections.observableArrayList(
                "Tất cả hạng",
                "Khách VIP",
                "Khách quen",
                "Khách doanh nghiệp"
        );
        cmbHang.setItems(dsHang);
        cmbHang.setValue("Tất cả hạng");

        // Trạng thái khách hàng (dùng enum)
        ObservableList<TrangThaiKhachHang> dsTrangThai = FXCollections.observableArrayList(TrangThaiKhachHang.values());
        cmbTrangThai.setItems(dsTrangThai);
        cmbTrangThai.getItems().add(0, null); // "Tất cả trạng thái"
        cmbTrangThai.setConverter(new StringConverter<>() {
            @Override
            public String toString(TrangThaiKhachHang object) {
                return object == null ? "Tất cả trạng thái" : object.getTenHienThi();
            }

            @Override
            public TrangThaiKhachHang fromString(String string) {
                return null; // không cần
            }
        });
        cmbTrangThai.getSelectionModel().select(0);
    }

    private void setupTableColumns() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colSoDT.setCellValueFactory(new PropertyValueFactory<>("soDT"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colHangKhach.setCellValueFactory(new PropertyValueFactory<>("hangKhach"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colTrangThai.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrangThai().getTenHienThi())
        );

        colThaoTac.setCellFactory(_ -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");

            {
                btnEdit.getStyleClass().addAll("btn", "btn-xs", "btn-info", "btn-table-edit");
                btnDelete.getStyleClass().addAll("btn", "btn-xs", "btn-danger", "btn-table-delete");

                btnEdit.setOnAction(_ -> {
                    KhachHang khachHang = getTableView().getItems().get(getIndex());
                    handleSuaKhachHang(khachHang);
                });

                btnDelete.setOnAction(_ -> {
                    KhachHang khachHang = getTableView().getItems().get(getIndex());
                    handleXoaKhachHang(khachHang);
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
    }

    private void setupListener() {
        cmbHang.valueProperty().addListener((obs, oldV, newV) -> apDungFilterKhachHang());
        cmbTrangThai.valueProperty().addListener((obs, oldV, newV) -> apDungFilterKhachHang());
        txtTimKiem.textProperty().addListener((obs, oldV, newV) -> apDungFilterKhachHang());
        btnLamMoi.setOnAction(e -> lamMoiDanhSachKhachHang());
        btnThemKhachHang.setOnAction(e -> handleThemKhachHang());
    }

    // ------------ Load dữ liệu ------------
    private void loadData() {
        try {
            dsKhachHang = khachHangDAO.layDSKhachHang();
            ObservableList<KhachHang> danhSach = FXCollections.observableArrayList(dsKhachHang);
            tableViewKhachHang.setItems(danhSach);
            capNhatThongKe();
        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi lấy dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------ Logic ------------
    void capNhatThongKe() {
        int tongSoKhach = dsKhachHang.size();

        int soKhachVip = (int) dsKhachHang.stream()
                .filter(kh -> "Khách VIP".equalsIgnoreCase(kh.getHangKhach()))
                .count();

        int soKhachDangLuuTru = (int) dsKhachHang.stream()
                .filter(kh -> "Đang lưu trú".equalsIgnoreCase(kh.getTrangThai().getTenHienThi()))
                .count();

        int soKhachCheckout = (int) dsKhachHang.stream()
                .filter(kh -> "Check-out".equalsIgnoreCase(kh.getTrangThai().getTenHienThi()))
                .count();

        lblSoKhachHang.setText(String.valueOf(tongSoKhach));
        lblDangLuuTru.setText(String.valueOf(soKhachDangLuuTru));
        lblKhachVIP.setText(String.valueOf(soKhachVip));
        lblCheckout.setText(String.valueOf(soKhachCheckout));
    }

    @FXML
    private void handleThemKhachHang() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/khach-hang-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Thêm Khách Hàng Mới");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            KhachHangDialogController controller = loader.getController();
            controller.setMode("ADD");

            dialog.showAndWait();
            loadData();
        } catch (Exception e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", e.getMessage());
            e.printStackTrace();
        }
    }

    private void lamMoiDanhSachKhachHang() {
        txtTimKiem.setText("");
        cmbHang.setValue("Tất cả hạng");
        cmbTrangThai.getSelectionModel().select(0);

        tableViewKhachHang.setItems(FXCollections.observableArrayList(dsKhachHang));
        capNhatThongKe();
        tableViewKhachHang.refresh();
    }

    private void apDungFilterKhachHang() {
        String timKiem = txtTimKiem.getText() != null ? txtTimKiem.getText().toLowerCase().trim() : "";
        String hangFilter = cmbHang.getValue();
        TrangThaiKhachHang trangThaiFilter = cmbTrangThai.getValue();

        ObservableList<KhachHang> filteredList = FXCollections.observableArrayList();

        for (KhachHang kh : dsKhachHang) {
            boolean matchSearch = timKiem.isEmpty() ||
                    (kh.getHoTen() != null && kh.getHoTen().toLowerCase().contains(timKiem)) ||
                    (kh.getSoDT() != null && kh.getSoDT().toLowerCase().contains(timKiem)) ||
                    (kh.getEmail() != null && kh.getEmail().toLowerCase().contains(timKiem));

            boolean matchHang = hangFilter == null || "Tất cả hạng".equals(hangFilter) ||
                    (kh.getHangKhach() != null && kh.getHangKhach().equalsIgnoreCase(hangFilter));

            boolean matchTrangThai = trangThaiFilter == null || kh.getTrangThai() == trangThaiFilter;

            if (matchSearch && matchHang && matchTrangThai) {
                filteredList.add(kh);
            }
        }

        tableViewKhachHang.setItems(filteredList);
        tableViewKhachHang.refresh();
    }

    @FXML
    private void handleSuaKhachHang(KhachHang khachHang) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/khach-hang-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Sửa Thông Tin Khách Hàng");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            KhachHangDialogController controller = loader.getController();
            controller.setMode("EDIT");
            controller.setKhachHang(khachHang);

            dialog.showAndWait();
            loadData();

        } catch (IOException e) {
            ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể mở form sửa khách hàng: " + e.getMessage());
        }
    }

    @FXML
    private void handleXoaKhachHang(KhachHang khachHang) {
        String message = "Bạn có chắc chắn muốn xóa khách hàng này?\nMã: " + khachHang.getMaKH() + " - Họ tên: " + khachHang.getHoTen();
        if (ThongBaoUtil.hienThiXacNhan("Xác nhận xóa", message)) {
            try {
                if (khachHangDAO.xoaKhachHang(khachHang.getMaKH())) {
                    ThongBaoUtil.hienThiThongBao("Thành công", "Đã xóa khách hàng thành công!");
                    loadData();
                } else {
                    ThongBaoUtil.hienThiThongBao("Lỗi", "Không thể xóa khách hàng!");
                }

            } catch (SQLException e) {
                ThongBaoUtil.hienThiThongBao("Lỗi", "Lỗi khi xóa khách hàng: " + e.getMessage());
            }
        }
    }
}
