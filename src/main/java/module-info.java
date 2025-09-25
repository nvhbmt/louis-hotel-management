module com.example.louishotelmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.example.louishotelmanagement to javafx.fxml;
    exports com.example.louishotelmanagement.app;
    opens com.example.louishotelmanagement.app to javafx.fxml;
    exports com.example.louishotelmanagement.controller;
    opens com.example.louishotelmanagement.controller to javafx.fxml;
}