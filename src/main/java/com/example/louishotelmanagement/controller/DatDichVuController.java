package com.example.louishotelmanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DatDichVuController implements Initializable {


    public ComboBox cboMaPhong;
    public TextField txtNgayLap;
    public TextField txtMaPhieuTam;
    public TableView tblChiTietTam;
    public TableColumn colDVMa;
    public TableColumn colDVTen;
    public TableColumn colDVSL;
    public TableColumn colDVGia;
    public ComboBox cboDichVu;
    public TextField txtSoLuong;
    public Button btnThemDV;
    public Button btnXoaDV;
    public TextArea txtGhiChu;
    public VBox dsDichVuDaDat;
    public Label lblTongTienTam;
    public Button btnXacNhanLapPhieu;
    @FXML
    private Label tieuDeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize dat dich vu page
        System.out.println("Dat dich vu page initialized");
    }



    public void handleXacNhanLapPhieu(ActionEvent actionEvent) {
    }

    public void handleThemDichVu(ActionEvent actionEvent) {
    }

    public void handleXoaDichVu(ActionEvent actionEvent) {
    }
}
