package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PhongController implements Initializable {

    @FXML
    private Label tieuDeLabel;

    @FXML
    private Button btnNhanPhong, btnDatTT, btnDoiPhong, btnHuyDat, btnDichVu, btnThanhToan;

    @FXML
    private ComboBox<String> cbxTang;
    @FXML
    private TableView<Phong> tableViewPhong;

    @FXML
    private TableColumn<Phong, String> maPhong;

    @FXML
    private TableColumn<Phong, String> loaiPhong; // Cột này sẽ lấy tên từ đối tượng LoaiPhong

    @FXML
    private TableColumn<Phong, BigDecimal> donGia; // Cột này sẽ lấy đơn giá từ đối tượng LoaiPhong

    @FXML
    private TableColumn<Phong, TrangThaiPhong> trangThai;

    // --- Data and DAO ---
    private PhongDAO phongDAO;
    private ObservableList<Phong> phongObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        phongDAO = new PhongDAO();
        System.out.println("Phong page initialized");
        configureTable();
        loadTableData();
        setupComboBoxTang();



    }
    private void configureTable() {
        phongObservableList = FXCollections.observableArrayList();
        tableViewPhong.setItems(phongObservableList);
        maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        loaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong lp = cellData.getValue().getLoaiPhong();
            return new SimpleStringProperty(lp != null ? lp.getTenLoai() : "N/A");
        });

        donGia.setCellValueFactory(cellData -> {
            LoaiPhong lp = cellData.getValue().getLoaiPhong();
            return new SimpleObjectProperty<>(lp != null ? lp.getDonGia() : BigDecimal.ZERO);
        });
    }

    /**
     * Tải dữ liệu từ DAO và cập nhật TableView.
     */
    public void loadTableData() {
        try {
            ArrayList<Phong> dsPhong = phongDAO.layDSPhong();
            phongObservableList.clear();
            phongObservableList.addAll(dsPhong);
        } catch (SQLException e) {
            e.printStackTrace();
            // Cần có một thông báo lỗi cho người dùng ở đây
        }
    }

    /**
     * Khởi tạo dữ liệu cho ComboBox chọn tầng.
     */
    private void setupComboBoxTang() {
        ObservableList<String> danhSachTang = FXCollections.observableArrayList();
        int soTangKhachSan = 4;
        for (int i = 1; i <= soTangKhachSan; i++) {
            danhSachTang.add("Tầng " + i);
        }
        cbxTang.setItems(danhSachTang);
        if (!danhSachTang.isEmpty()) {
            cbxTang.setValue(danhSachTang.get(0)); // Chọn "Tầng 1" làm mặc định
        }
    }

    private ContentSwitcher switcher;

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }
    @FXML
    private void moNhanPhong(javafx.event.ActionEvent actionEvent) {
        if (switcher != null) {
            switcher.switchContent("/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml");
        }
    }
    @FXML
    private void moDatPhong(javafx.event.ActionEvent actionEvent) {
        if (switcher != null) {
            switcher.switchContent("/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml");
        }
    private void moDatTT(ActionEvent event) {
        // Giả định tên FXML cho Đặt tại quầy
        moTrang("dat-phong-truc-tiep-view.fxml", event);
    }
    @FXML
    private void moDatTT(javafx.event.ActionEvent actionEvent) {
        if (switcher != null) {
            switcher.switchContent("/com/example/louishotelmanagement/fxml/dat-phong-truc-tiep-view.fxml");
        }
    }
    @FXML
    private void moDoiPhong(javafx.event.ActionEvent actionEvent) {
        if (switcher != null) {
            switcher.switchContent("/com/example/louishotelmanagement/fxml/doi-phong-view.fxml");
        }
    }
    @FXML
    private void moHuy(javafx.event.ActionEvent actionEvent) {
        if (switcher != null) {
            switcher.switchContent("/com/example/louishotelmanagement/fxml/huy-phong-view.fxml");
        }
    }
    @FXML
    private void moDichVu(javafx.event.ActionEvent actionEvent) {
        if (switcher != null) {
            switcher.switchContent("/com/example/louishotelmanagement/fxml/dat-dich-vu-view.fxml");
        }
    }
    @FXML
    private void moThanhToan(javafx.event.ActionEvent actionEvent) {
        if (switcher != null) {
            switcher.switchContent("/com/example/louishotelmanagement/fxml/huy-phong-view.fxml");
        }
    }
}