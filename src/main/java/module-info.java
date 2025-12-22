module com.example.louishotelmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.swing;
    requires itextpdf;


    exports com.example.louishotelmanagement.app;
    opens com.example.louishotelmanagement.app to javafx.fxml;
    exports com.example.louishotelmanagement.controller;
    opens com.example.louishotelmanagement.controller to javafx.fxml;
    exports com.example.louishotelmanagement.model;
    opens com.example.louishotelmanagement.model to javafx.fxml;
    exports com.example.louishotelmanagement.dao;
    opens com.example.louishotelmanagement.dao to javafx.fxml;
    exports com.example.louishotelmanagement.config;
    opens com.example.louishotelmanagement.config to javafx.fxml;
    exports com.example.louishotelmanagement.ui.models;
    exports com.example.louishotelmanagement.ui.components;
}