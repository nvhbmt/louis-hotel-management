package com.example.louishotelmanagement.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class DangNhapView {
    private TextField tenDangNhapField;
    private PasswordField matKhauField;
    private Button dangNhapBtn;
    private Label thongBaoLabel;
    private Parent root;

    public DangNhapView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.setPrefHeight(1000.0);
        borderpane1.setPrefWidth(800.0);
        borderpane1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/dang-nhap.css").toExternalForm());

        VBox vbox1 = new VBox(30.0);
        vbox1.setAlignment(Pos.CENTER);
        vbox1.getStyleClass().addAll("center-background");

        VBox vbox2 = new VBox(8.0);
        vbox2.setAlignment(Pos.CENTER);
        Label label1 = new Label("Chào mừng trở lại");
        label1.getStyleClass().addAll("welcome-title");
        Label label2 = new Label("Vui lòng đăng nhập để tiếp tục");
        label2.getStyleClass().addAll("welcome-subtitle");
        vbox2.getChildren().addAll(label1, label2);

        VBox vbox3 = new VBox(20.0);
        vbox3.getStyleClass().addAll("login-card");

        VBox vbox4 = new VBox(8.0);
        vbox4.getStyleClass().addAll("input-group");
        Label label3 = new Label("Tên đăng nhập");
        label3.getStyleClass().addAll("input-label");

        HBox hbox1 = new HBox(10.0);
        hbox1.setAlignment(Pos.CENTER_LEFT);
        hbox1.getStyleClass().addAll("input-container");
        ImageView imageview1 = new ImageView();
        imageview1.setFitHeight(20.0);
        imageview1.setFitWidth(20.0);
        imageview1.setPreserveRatio(true);
        imageview1.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/profile.png").toExternalForm()));
        tenDangNhapField = new TextField();
        tenDangNhapField.getStyleClass().addAll("modern-text-field");
        tenDangNhapField.setPromptText("Nhập tên đăng nhập");
        tenDangNhapField.setPrefWidth(280.0);
        hbox1.getChildren().addAll(imageview1, tenDangNhapField);
        vbox4.getChildren().addAll(label3, hbox1);

        VBox vbox5 = new VBox(8.0);
        vbox5.getStyleClass().addAll("input-group");
        Label label4 = new Label("Mật khẩu");
        label4.getStyleClass().addAll("input-label");

        HBox hbox2 = new HBox(10.0);
        hbox2.setAlignment(Pos.CENTER_LEFT);
        hbox2.getStyleClass().addAll("input-container");
        ImageView imageview2 = new ImageView();
        imageview2.setFitHeight(20.0);
        imageview2.setFitWidth(20.0);
        imageview2.setPreserveRatio(true);
        imageview2.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/hotel_key.png").toExternalForm()));
        matKhauField = new PasswordField();
        matKhauField.getStyleClass().addAll("modern-password-field");
        matKhauField.setPrefWidth(280.0);
        hbox2.getChildren().addAll(imageview2, matKhauField);
        vbox5.getChildren().addAll(label4, hbox2);

        VBox vbox6 = new VBox(15.0);
        vbox6.setAlignment(Pos.CENTER);
        dangNhapBtn = new Button("ĐĂNG NHẬP");
        dangNhapBtn.getStyleClass().addAll("modern-login-button");
        thongBaoLabel = new Label();
        thongBaoLabel.getStyleClass().addAll("error-message");
        vbox6.getChildren().addAll(dangNhapBtn, thongBaoLabel);
        vbox3.getChildren().addAll(vbox4, vbox5, vbox6);
        vbox1.getChildren().addAll(vbox2, vbox3);
        borderpane1.setCenter(vbox1);
        BorderPane.setAlignment(vbox1, Pos.CENTER);

        VBox vbox7 = new VBox();
        vbox7.setAlignment(Pos.CENTER);
        vbox7.setPrefHeight(200.0);
        vbox7.getStyleClass().addAll("header-background");

        VBox vbox8 = new VBox(15.0);
        vbox8.setAlignment(Pos.CENTER);
        ImageView imageview3 = new ImageView();
        imageview3.setFitHeight(80.0);
        imageview3.setFitWidth(80.0);
        imageview3.setPreserveRatio(true);
        imageview3.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/hotel.png").toExternalForm()));

        VBox vbox9 = new VBox(5.0);
        vbox9.setAlignment(Pos.CENTER);
        Label label5 = new Label("LOUIS HOTEL");
        label5.getStyleClass().addAll("brand-title");
        Label label6 = new Label("MANAGEMENT SYSTEM");
        label6.getStyleClass().addAll("brand-subtitle");
        vbox9.getChildren().addAll(label5, label6);
        vbox8.getChildren().addAll(imageview3, vbox9);
        vbox7.getChildren().addAll(vbox8);
        borderpane1.setTop(vbox7);
        BorderPane.setAlignment(vbox7, Pos.CENTER);

        VBox vbox10 = new VBox(8.0);
        vbox10.setAlignment(Pos.CENTER);
        vbox10.getStyleClass().addAll("footer-background");
        Label label7 = new Label("© 2025 Louis Hotel Management System");
        label7.getStyleClass().addAll("footer-text");
        Label label8 = new Label("Phiên bản 2.0");
        label8.getStyleClass().addAll("footer-version");

        HBox hbox3 = new HBox(20.0);
        hbox3.setAlignment(Pos.CENTER);
        Label label9 = new Label("Bảo mật");
        label9.getStyleClass().addAll("footer-link");
        Label label10 = new Label("Điều khoản");
        label10.getStyleClass().addAll("footer-link");
        Label label11 = new Label("Hỗ trợ");
        label11.getStyleClass().addAll("footer-link");
        hbox3.getChildren().addAll(label9, label10, label11);
        vbox10.getChildren().addAll(label7, label8, hbox3);
        borderpane1.setBottom(vbox10);
        BorderPane.setAlignment(vbox10, Pos.CENTER);
        this.root = borderpane1;
    }

    public Parent getRoot() {
        return root;
    }

    public TextField getTenDangNhapField() {
        return tenDangNhapField;
    }

    public PasswordField getMatKhauField() {
        return matKhauField;
    }

    public Button getDangNhapBtn() {
        return dangNhapBtn;
    }

    public Label getThongBaoLabel() {
        return thongBaoLabel;
    }
}
