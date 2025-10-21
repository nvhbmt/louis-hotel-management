package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.utils.UIUtils;
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

import javax.swing.*;
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
    private ComboBox<String> cmbTrangThai;
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
    private TableColumn<KhachHang, Void> colThaoTac;
    private ArrayList<KhachHang> dsKhachHang;
    @FXML
    private Label lblSoKhachHang;
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
            setupListner();
            loadData();

        }catch (Exception e){
            UIUtils.hienThiThongBao("Lỗi", "Không thể kết nối cơ sở dữ liệu" + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------ Khởi tạo UI ------------
    //Khởi tạo combobox
    private void setupComboBox() {
        ObservableList<String> dsHang = FXCollections.observableArrayList(
                "Tất cả hạng",
                "Khách VIP",
                "Khách quen",
                "Khách doanh nghiệp"
        );
        cmbHang.setItems(dsHang);
        cmbHang.setValue("Tất cả hạng");

        ObservableList<String> dsTrangThai = FXCollections.observableArrayList(
                "Tất cả trạng thái",
                "Đang lưu trú",
                "Check-out",
                "Đã đặt"
        );
        cmbTrangThai.setItems(dsTrangThai);
        cmbTrangThai.setValue("Tất cả trạng thái");
    }

    //Nạp dữ liệu vào table
    private void setupTableColumns() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colSoDT.setCellValueFactory(new PropertyValueFactory<>("soDT"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colHangKhach.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

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

    //Đăng ký sự kiện
    private void setupListner() {
        cmbHang.valueProperty().addListener((observable, oldValue, newValue) -> {
            apDungFilterKhachHang();
        });

        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
            apDungFilterKhachHang();
        });

        btnLamMoi.setOnAction(event -> lamMoiDanhSachKhachHang());

        btnThemKhachHang.setOnAction(event -> handleThemKhachHang());
    }

    // ------------ Load dữ lệu ------------
    //Load dữ liệu, cập nhật thống kê
    private  void loadData() {
        try {
            dsKhachHang = khachHangDAO.layDSKhachHang();
            ObservableList<KhachHang> danhSach =  FXCollections.observableArrayList(dsKhachHang);

            tableViewKhachHang.setItems(danhSach);

            capNhatThongKe();
        }catch (Exception e){
            UIUtils.hienThiThongBao("Lỗi","Lỗi lấy dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------ Xử lý logic ------------
    //Cập nhật các ô thống kê
    void capNhatThongKe() {
        try {
            int tongSoKhach = dsKhachHang.size();
            int soKhachVip = 0;

            for (KhachHang kh : dsKhachHang) {
                if("Khách VIP".equals(kh.getGhiChu())){
                    soKhachVip++;
                }
            }

            lblSoKhachHang.setText(String.valueOf(tongSoKhach));
            lblKhachVIP.setText(String.valueOf(soKhachVip));

        } catch (Exception e) {
            UIUtils.hienThiThongBao("Lỗi", "Không thể cập nhật thống kê: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Thêm khách hàng
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
        }catch (Exception e){
            UIUtils.hienThiThongBao("Lỗi", e.getMessage());
            e.printStackTrace();
        }
    }

    private void lamMoiDanhSachKhachHang() {
        txtTimKiem.setText("");
        cmbHang.setValue("Tất cả hạng");
        cmbTrangThai.setValue("Tất cả trạng thái");

        ObservableList<KhachHang> danhSach = FXCollections.observableArrayList(dsKhachHang);
        tableViewKhachHang.setItems(danhSach);

        capNhatThongKe();
    }

    // Tìm kiếm + combobox lọc
    private void apDungFilterKhachHang() {
        String timKiem = (txtTimKiem.getText() != null) ? txtTimKiem.getText().toLowerCase().trim() : "";
        String hangFilter = cmbHang.getValue();

        ObservableList<KhachHang> danhSach = FXCollections.observableArrayList();

        for (KhachHang kh: dsKhachHang) {
            boolean equalTimKiem = timKiem.isEmpty() ||
                    kh.getHoTen() != null && kh.getHoTen().toLowerCase().contains(timKiem) ||
                    kh.getSoDT() != null && kh.getSoDT().toLowerCase().contains(timKiem) ||
                    kh.getEmail() != null && kh.getEmail().toLowerCase().contains(timKiem);

            boolean equalHang = hangFilter == null ||
                    hangFilter.equals("Tất cả hạng") ||
                    kh.getGhiChu() != null && kh.getGhiChu().equalsIgnoreCase(hangFilter);

            if (equalTimKiem && equalHang) {
                danhSach.add(kh);
            }
        }

        tableViewKhachHang.setItems(danhSach);
    }

    @FXML
    private void handleSuaKhachHang(KhachHang KhachHang) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/khach-hang-form-dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("Sửa Thông Tin Khách Hàng");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(loader.load()));

            // Thiết lập controller và dữ liệu
            KhachHangDialogController controller = loader.getController();
            controller.setMode("EDIT");
            controller.setKhachHang(KhachHang);
            dialog.showAndWait();

            // Làm mới dữ liệu sau khi sửa
            loadData();

        } catch (IOException e) {
            UIUtils.hienThiThongBao("Lỗi", "Không thể mở form sửa khách hàng: " + e.getMessage());
        }
    }

    @FXML
    private void handleXoaKhachHang(KhachHang KhachHang) {
        // Xác nhận xóa
        String message = "Bạn có chắc chắn muốn xóa khách hàng này?\nMã: " + KhachHang.getMaKH() + " - Họ tên: " + KhachHang.getHoTen();
        if (UIUtils.hienThiXacNhan("Xác nhận xóa", message)) {
            try {
                // Xóa mã giảm giá
                if (khachHangDAO.xoaKhachHang(KhachHang.getMaKH())) {
                    UIUtils.hienThiThongBao("Thành công", "Đã xóa khách hàng thành công!");
                    loadData();
                } else {
                    UIUtils.hienThiThongBao("Lỗi", "Không thể xóa khách hàng!");
                }

            } catch (SQLException e) {
                UIUtils.hienThiThongBao("Lỗi", "Lỗi khi xóa khách hàng: " + e.getMessage());
            }
        }
    }
}
