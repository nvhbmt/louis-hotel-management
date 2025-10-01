package com.example.louishotelmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class QuanLyKhachHangController implements Initializable {

    @FXML
    private Label tieuDeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize quan ly khach hang page
        System.out.println("Quan ly khach hang page initialized");
    }
}
