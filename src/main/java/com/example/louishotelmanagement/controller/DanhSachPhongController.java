package com.example.louishotelmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DanhSachPhongController implements Initializable {

    @FXML
    private Label tieuDeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize danh sach phong page
        System.out.println("Danh sach phong page initialized");
    }
}
