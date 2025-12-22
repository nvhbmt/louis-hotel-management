package com.example.louishotelmanagement.view;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
public class PhongFormDialogView {
    private Label lblTieuDe;
    private VBox formContainer;
    private Button btnLuu;
    private Parent root;
    private Button btnDong;

    public PhongFormDialogView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.setPrefHeight(325.0);
        borderpane1.setPrefWidth(674.0);
        borderpane1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/formsfx.css").toExternalForm());

        VBox vbox1 = new VBox(20.0);
        vbox1.setPrefHeight(343.0);
        vbox1.setPrefWidth(610.0);
        vbox1.getStyleClass().addAll("dialog-container");

        VBox vbox2 = new VBox();
        vbox2.getStyleClass().addAll("dialog-header");
        lblTieuDe = new Label("Thêm Phòng Mới");
        lblTieuDe.getStyleClass().addAll("dialog-title");
        Label label1 = new Label("Nhập thông tin phòng");
        label1.getStyleClass().addAll("dialog-subtitle");
        vbox2.getChildren().addAll(lblTieuDe, label1);

        formContainer = new VBox();

        HBox hbox1 = new HBox();
        hbox1.getStyleClass().addAll("dialog-buttons");
        this.btnDong = new Button("Hủy");
        btnDong.getStyleClass().addAll("btn-cancel");
        btnDong.setOnAction(e -> this.handleHuy(e));
        btnLuu = new Button("Lưu");
        btnLuu.getStyleClass().addAll("btn-save");
        btnLuu.setOnAction(e -> this.handleLuu(e));
        hbox1.getChildren().addAll(btnDong, btnLuu);
        vbox1.getChildren().addAll(vbox2, formContainer, hbox1);
        borderpane1.setCenter(vbox1);
        borderpane1.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));
        this.root = borderpane1;
    }

    public Parent getRoot() {
        return root;
    }
    public Label getLblTieuDe() {
        return lblTieuDe;
    }
    public VBox getFormContainer() {
        return formContainer;
    }
    public Button getBtnLuu() {
        return btnLuu;
    }
    public Button getBtnDong() {
        return btnDong;
    }
    private void handleHuy(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleLuu(ActionEvent e) {
        // TODO: implement handler
    }
}
