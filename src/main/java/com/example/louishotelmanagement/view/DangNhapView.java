package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.DangNhapController;
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
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setPrefHeight(1000.0);
        mainBorderPane.setPrefWidth(800.0);
        mainBorderPane.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/dang-nhap.css").toExternalForm());

        VBox centerVBox = new VBox(30.0);
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.getStyleClass().addAll("center-background");

        VBox welcomeVBox = new VBox(8.0);
        welcomeVBox.setAlignment(Pos.CENTER);
        Label welcomeTitleLabel = new Label("Chào mừng trở lại");
        welcomeTitleLabel.getStyleClass().addAll("welcome-title");
        Label welcomeSubtitleLabel = new Label("Vui lòng đăng nhập để tiếp tục");
        welcomeSubtitleLabel.getStyleClass().addAll("welcome-subtitle");
        welcomeVBox.getChildren().addAll(welcomeTitleLabel, welcomeSubtitleLabel);

        VBox loginCardVBox = new VBox(20.0);
        loginCardVBox.getStyleClass().addAll("login-card");

        VBox usernameGroupVBox = new VBox(8.0);
        usernameGroupVBox.getStyleClass().addAll("input-group");
        Label usernameLabel = new Label("Tên đăng nhập");
        usernameLabel.getStyleClass().addAll("input-label");

        HBox usernameInputHBox = new HBox(10.0);
        usernameInputHBox.setAlignment(Pos.CENTER_LEFT);
        usernameInputHBox.getStyleClass().addAll("input-container");
        ImageView usernameIcon = new ImageView();
        usernameIcon.setFitHeight(20.0);
        usernameIcon.setFitWidth(20.0);
        usernameIcon.setPreserveRatio(true);
        usernameIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/profile.png").toExternalForm()));
        tenDangNhapField = new TextField();
        tenDangNhapField.getStyleClass().addAll("modern-text-field");
        tenDangNhapField.setPromptText("Nhập tên đăng nhập");
        tenDangNhapField.setPrefWidth(280.0);
        usernameInputHBox.getChildren().addAll(usernameIcon, tenDangNhapField);
        usernameGroupVBox.getChildren().addAll(usernameLabel, usernameInputHBox);

        VBox passwordGroupVBox = new VBox(8.0);
        passwordGroupVBox.getStyleClass().addAll("input-group");
        Label passwordLabel = new Label("Mật khẩu");
        passwordLabel.getStyleClass().addAll("input-label");

        HBox passwordInputHBox = new HBox(10.0);
        passwordInputHBox.setAlignment(Pos.CENTER_LEFT);
        passwordInputHBox.getStyleClass().addAll("input-container");
        ImageView passwordIcon = new ImageView();
        passwordIcon.setFitHeight(20.0);
        passwordIcon.setFitWidth(20.0);
        passwordIcon.setPreserveRatio(true);
        passwordIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/hotel_key.png").toExternalForm()));
        matKhauField = new PasswordField();
        matKhauField.getStyleClass().addAll("modern-password-field");
        matKhauField.setPrefWidth(280.0);
        passwordInputHBox.getChildren().addAll(passwordIcon, matKhauField);
        passwordGroupVBox.getChildren().addAll(passwordLabel, passwordInputHBox);

        VBox buttonGroupVBox = new VBox(15.0);
        buttonGroupVBox.setAlignment(Pos.CENTER);
        dangNhapBtn = new Button("ĐĂNG NHẬP");
        dangNhapBtn.getStyleClass().addAll("modern-login-button");
        thongBaoLabel = new Label();
        thongBaoLabel.getStyleClass().addAll("error-message");
        buttonGroupVBox.getChildren().addAll(dangNhapBtn, thongBaoLabel);
        loginCardVBox.getChildren().addAll(usernameGroupVBox, passwordGroupVBox, buttonGroupVBox);
        centerVBox.getChildren().addAll(welcomeVBox, loginCardVBox);
        mainBorderPane.setCenter(centerVBox);
        BorderPane.setAlignment(centerVBox, Pos.CENTER);

        VBox headerVBox = new VBox();
        headerVBox.setAlignment(Pos.CENTER);
        headerVBox.setPrefHeight(200.0);
        headerVBox.getStyleClass().addAll("header-background");

        VBox logoGroupVBox = new VBox(15.0);
        logoGroupVBox.setAlignment(Pos.CENTER);
        ImageView logoImageView = new ImageView();
        logoImageView.setFitHeight(80.0);
        logoImageView.setFitWidth(80.0);
        logoImageView.setPreserveRatio(true);
        logoImageView.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/hotel.png").toExternalForm()));

        VBox titleGroupVBox = new VBox(5.0);
        titleGroupVBox.setAlignment(Pos.CENTER);
        Label brandTitleLabel = new Label("LOUIS HOTEL");
        brandTitleLabel.getStyleClass().addAll("brand-title");
        Label brandSubtitleLabel = new Label("MANAGEMENT SYSTEM");
        brandSubtitleLabel.getStyleClass().addAll("brand-subtitle");
        titleGroupVBox.getChildren().addAll(brandTitleLabel, brandSubtitleLabel);
        logoGroupVBox.getChildren().addAll(logoImageView, titleGroupVBox);
        headerVBox.getChildren().addAll(logoGroupVBox);
        mainBorderPane.setTop(headerVBox);
        BorderPane.setAlignment(headerVBox, Pos.CENTER);

        VBox footerVBox = new VBox(8.0);
        footerVBox.setAlignment(Pos.CENTER);
        footerVBox.getStyleClass().addAll("footer-background");
        Label copyrightLabel = new Label("© 2025 Louis Hotel Management System");
        copyrightLabel.getStyleClass().addAll("footer-text");
        Label versionLabel = new Label("Phiên bản 2.0");
        versionLabel.getStyleClass().addAll("footer-version");

        HBox footerLinksHBox = new HBox(20.0);
        footerLinksHBox.setAlignment(Pos.CENTER);
        Label securityLinkLabel = new Label("Bảo mật");
        securityLinkLabel.getStyleClass().addAll("footer-link");
        Label termsLinkLabel = new Label("Điều khoản");
        termsLinkLabel.getStyleClass().addAll("footer-link");
        Label supportLinkLabel = new Label("Hỗ trợ");
        supportLinkLabel.getStyleClass().addAll("footer-link");
        footerLinksHBox.getChildren().addAll(securityLinkLabel, termsLinkLabel, supportLinkLabel);
        footerVBox.getChildren().addAll(copyrightLabel, versionLabel, footerLinksHBox);
        mainBorderPane.setBottom(footerVBox);
        BorderPane.setAlignment(footerVBox, Pos.CENTER);
        this.root = mainBorderPane;

        new DangNhapController(this);
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
