package com.example.louishotelmanagement.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label vanBanChaoMung;

    @FXML
    protected void khiNhanNutChao() {
        vanBanChaoMung.setText("Welcome to JavaFX Application!");
    }
}