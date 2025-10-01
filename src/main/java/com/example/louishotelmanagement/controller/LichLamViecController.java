package com.example.louishotelmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LichLamViecController implements Initializable {

    @FXML
    private Label tieuDeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize lich lam viec page
        System.out.println("Lich lam viec page initialized");
    }
}
