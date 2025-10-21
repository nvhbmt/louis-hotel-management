package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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
    private TableColumn<Phong, Double> donGia; // Cột này sẽ lấy đơn giá từ đối tượng LoaiPhong

    @FXML
    private TableColumn<Phong, TrangThaiPhong> trangThai;

    // --- Data and DAO ---
    private PhongDAO phongDAO;
    private ObservableList<Phong> phongObservableList;
    
    private ContentSwitcher switcher;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("Phong page initialized");
        //Tầng
        ObservableList<String> danhSachTang = FXCollections.observableArrayList();
        int soTangKhachSan = 4;
        for (int i = 1; i <= soTangKhachSan; i++) {
            danhSachTang.add("Tầng " + i);
        }
        cbxTang.setItems(danhSachTang);
        cbxTang.setValue("Tầng 1");

        //table

    }

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }
    @FXML
    private void moNhanPhong() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml");
    }
    
    @FXML
    private void moDatPhong() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/dat-phong-view.fxml");
    }
    
    @FXML
    private void moDatTT() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/dat-phong-truc-tiep-view.fxml");
    }
    
    @FXML
    private void moDoiPhong() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/doi-phong-view.fxml");
    }
    
    @FXML
    private void moHuy() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/huy-dat-phong-view.fxml");
    }
    
    @FXML
    private void moDichVu() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/dat-dich-vu-view.fxml");
    }
    
    @FXML
    private void moThanhToan() {
        ContentManager.safeSwitch(switcher, "/com/example/louishotelmanagement/fxml/thanh-toan-view.fxml");
    }
}