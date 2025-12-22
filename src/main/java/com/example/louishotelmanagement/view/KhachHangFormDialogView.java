package com.example.louishotelmanagement.view;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;

public class KhachHangFormDialogView {
    private Label lblTieuDe;
    private VBox formContainer;
    private Button btnLuu;
    private Button btnHuy; // Thêm khai báo nút hủy
    private Parent root;

    public KhachHangFormDialogView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.setPrefHeight(566.0);
        borderpane1.setPrefWidth(700.0);
        borderpane1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/formsfx.css").toExternalForm());

        VBox vbox1 = new VBox(20.0);
        vbox1.getStyleClass().addAll("dialog-container");

        VBox vbox2 = new VBox();
        vbox2.getStyleClass().addAll("dialog-header");
        lblTieuDe = new Label("Thêm Khách Hàng Mới");
        lblTieuDe.getStyleClass().addAll("dialog-title");
        Label label1 = new Label("Nhập thông tin khách hàng");
        vbox2.getChildren().addAll(lblTieuDe, label1);

        formContainer = new VBox();
        // Gợi ý: formContainer sẽ chứa các TextField tên, số điện thoại, CCCD...

        HBox hbox1 = new HBox();
        hbox1.getStyleClass().addAll("dialog-buttons");

        btnHuy = new Button("Hủy");
        btnHuy.getStyleClass().addAll("btn-cancel");

        btnLuu = new Button("Lưu");
        btnLuu.getStyleClass().addAll("btn-save");

        hbox1.getChildren().addAll(btnHuy, btnLuu);
        vbox1.getChildren().addAll(vbox2, formContainer, hbox1);
        borderpane1.setCenter(vbox1);
        borderpane1.setPadding(new Insets(32.0));
        this.root = borderpane1;
    }

    public Parent getRoot() { return root; }
    public Label getLblTieuDe() { return lblTieuDe; }
    public VBox getFormContainer() { return formContainer; }
    public Button getBtnLuu() { return btnLuu; }
    public Button getBtnHuy() { return btnHuy; } // Getter cho nút hủy
}