package com.example.louishotelmanagement.view;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
public class MaQRView {
    private Button btnXacNhan;
    private Button btnHuy;
    private Parent root;

    public MaQRView() {
        AnchorPane anchorpane1 = new AnchorPane();
        anchorpane1.setPrefHeight(399.0);
        anchorpane1.setPrefWidth(420.0);
        ImageView imageview1 = new ImageView();
        imageview1.setFitHeight(286.0);
        imageview1.setFitWidth(298.0);
        imageview1.setPickOnBounds(true);
        imageview1.setPreserveRatio(true);
        imageview1.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/QR.jpg").toExternalForm()));
        imageview1.setLayoutX(72.0);
        imageview1.setLayoutY(36.0);
        Label label1 = new Label("Mã QR Thanh Toán");
        label1.setLayoutX(21.0);
        label1.setLayoutY(5.0);
        btnXacNhan = new Button("Xác nhận");
        btnXacNhan.setMnemonicParsing(false);
        btnXacNhan.setPrefHeight(38.0);
        btnXacNhan.setPrefWidth(76.0);
        btnXacNhan.setOnAction(e -> this.handleXacNhan(e));
        btnXacNhan.setLayoutX(65.0);
        btnXacNhan.setLayoutY(343.0);
        btnHuy = new Button("Hủy");
        btnHuy.setMnemonicParsing(false);
        btnHuy.setPrefHeight(38.0);
        btnHuy.setPrefWidth(65.0);
        btnHuy.setOnAction(e -> this.handleHuy(e));
        btnHuy.setLayoutX(296.0);
        btnHuy.setLayoutY(343.0);
        anchorpane1.getChildren().addAll(imageview1, label1, btnXacNhan, btnHuy);
        this.root = anchorpane1;
    }

    public Parent getRoot() {
        return root;
    }
    public Button getBtnXacNhan() {
        return btnXacNhan;
    }
    public Button getBtnHuy() {
        return btnHuy;
    }
    private void handleXacNhan(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleHuy(ActionEvent e) {
        // TODO: implement handler
    }
}
