package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
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