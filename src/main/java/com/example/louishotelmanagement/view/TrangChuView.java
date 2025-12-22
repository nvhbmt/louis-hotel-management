package com.example.louishotelmanagement.view;
// Generated Java code from FXML

import com.example.louishotelmanagement.controller.LayoutController;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.view.QuanLyDichVuView;
import com.example.louishotelmanagement.view.QuanLyKhachHangView;
import com.example.louishotelmanagement.view.ThongKeView;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TrangChuView {

    private Parent root;
    private ContentSwitcher switcher;

    public TrangChuView() {
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setPrefHeight(864.0);
        mainBorderPane.setPrefWidth(1014.4);
        mainBorderPane.getStylesheets()
                .addAll(getClass()
                        .getResource("/com/example/louishotelmanagement/css/trang-chu.css")
                        .toExternalForm());

        VBox mainContentVBox = new VBox(40.0);
        mainContentVBox.setAlignment(Pos.CENTER);
        mainContentVBox.getStyleClass().addAll("center-background");

        VBox welcomeSectionVBox = new VBox(15.0);
        welcomeSectionVBox.setAlignment(Pos.CENTER);
        welcomeSectionVBox.getStyleClass().addAll("welcome-padding");
        Label welcomeMainTitleLabel = new Label("Chào mừng đến với hệ thống");
        welcomeMainTitleLabel.getStyleClass().addAll("welcome-main-title");
        Label welcomeSubtitleLabel = new Label("Quản lý khách sạn thông minh và hiệu quả");
        welcomeSubtitleLabel.getStyleClass().addAll("welcome-subtitle");
        Label welcomeDescriptionLabel = new Label(
                "Tối ưu hóa quy trình vận hành, nâng cao trải nghiệm khách hàng và theo dõi hiệu suất theo thời gian thực.");
        welcomeDescriptionLabel.getStyleClass().addAll("welcome-description");
        welcomeDescriptionLabel.setMaxWidth(600);
        welcomeSectionVBox.getChildren().addAll(welcomeMainTitleLabel, welcomeSubtitleLabel, welcomeDescriptionLabel);

        GridPane featuresGridPane = new GridPane();
        featuresGridPane.setHgap(30);
        featuresGridPane.setVgap(30);
        featuresGridPane.setAlignment(Pos.CENTER);
        featuresGridPane.getStyleClass().addAll("feature-grid-padding");

        VBox roomManagementCardVBox = new VBox();
        roomManagementCardVBox.getStyleClass().addAll("feature-card");

        VBox roomManagementContentVBox = new VBox(15.0);
        roomManagementContentVBox.setAlignment(Pos.CENTER);
        roomManagementContentVBox.getStyleClass().addAll("card-content");
        ImageView roomManagementImageView = new ImageView();
        roomManagementImageView.setFitHeight(60.0);
        roomManagementImageView.setFitWidth(60.0);
        roomManagementImageView.setPreserveRatio(true);
        roomManagementImageView.setImage(new Image(
                getClass().getResource("/com/example/louishotelmanagement/image/hotel.png")
                        .toExternalForm()));

        VBox roomManagementTextVBox = new VBox(8.0);
        roomManagementTextVBox.setAlignment(Pos.CENTER);
        Label roomManagementTitleLabel = new Label("Quản Lý Phòng");
        roomManagementTitleLabel.getStyleClass().addAll("card-title");
        Label roomManagementDescriptionLabel = new Label("Đặt phòng, check-in/out, quản lý trạng thái phòng");
        roomManagementDescriptionLabel.getStyleClass().addAll("card-description");
        roomManagementTextVBox.getChildren().addAll(roomManagementTitleLabel, roomManagementDescriptionLabel);
        Button roomManagementButton = new Button("Truy cập");
        roomManagementButton.getStyleClass().addAll("card-button");
        roomManagementButton.setOnAction(e -> this.moQuanLyPhong(e));
        roomManagementContentVBox.getChildren().addAll(roomManagementImageView, roomManagementTextVBox, roomManagementButton);
        roomManagementCardVBox.getChildren().addAll(roomManagementContentVBox);
        GridPane.setColumnIndex(roomManagementCardVBox, 0);
        GridPane.setRowIndex(roomManagementCardVBox, 0);

        VBox customerManagementCardVBox = new VBox();
        customerManagementCardVBox.getStyleClass().addAll("feature-card");

        VBox customerManagementContentVBox = new VBox(15.0);
        customerManagementContentVBox.setAlignment(Pos.CENTER);
        customerManagementContentVBox.getStyleClass().addAll("card-content");
        ImageView customerManagementImageView = new ImageView();
        customerManagementImageView.setFitHeight(60.0);
        customerManagementImageView.setFitWidth(60.0);
        customerManagementImageView.setPreserveRatio(true);
        customerManagementImageView.setImage(new Image(
                getClass().getResource("/com/example/louishotelmanagement/image/people.png")
                        .toExternalForm()));

        VBox customerManagementTextVBox = new VBox(8.0);
        customerManagementTextVBox.setAlignment(Pos.CENTER);
        Label customerManagementTitleLabel = new Label("Khách Hàng");
        customerManagementTitleLabel.getStyleClass().addAll("card-title");
        Label customerManagementDescriptionLabel = new Label("Quản lý thông tin khách hàng, lịch sử đặt phòng");
        customerManagementDescriptionLabel.getStyleClass().addAll("card-description");
        customerManagementTextVBox.getChildren().addAll(customerManagementTitleLabel, customerManagementDescriptionLabel);
        Button customerManagementButton = new Button("Truy cập");
        customerManagementButton.getStyleClass().addAll("card-button");
        customerManagementButton.setOnAction(e -> this.moQuanLyKhachHang(e));
        customerManagementContentVBox.getChildren().addAll(customerManagementImageView, customerManagementTextVBox, customerManagementButton);
        customerManagementCardVBox.getChildren().addAll(customerManagementContentVBox);
        GridPane.setColumnIndex(customerManagementCardVBox, 1);
        GridPane.setRowIndex(customerManagementCardVBox, 0);

        VBox serviceManagementCardVBox = new VBox();
        serviceManagementCardVBox.getStyleClass().addAll("feature-card");

        VBox serviceManagementContentVBox = new VBox(15.0);
        serviceManagementContentVBox.setAlignment(Pos.CENTER);
        serviceManagementContentVBox.getStyleClass().addAll("card-content");
        ImageView serviceManagementImageView = new ImageView();
        serviceManagementImageView.setFitHeight(60.0);
        serviceManagementImageView.setFitWidth(60.0);
        serviceManagementImageView.setPreserveRatio(true);
        serviceManagementImageView.setImage(new Image(
                getClass().getResource("/com/example/louishotelmanagement/image/dining-table.png")
                        .toExternalForm()));

        VBox serviceManagementTextVBox = new VBox(8.0);
        serviceManagementTextVBox.setAlignment(Pos.CENTER);
        Label serviceManagementTitleLabel = new Label("Dịch Vụ");
        serviceManagementTitleLabel.getStyleClass().addAll("card-title");
        Label serviceManagementDescriptionLabel = new Label("Quản lý dịch vụ, đặt dịch vụ cho khách hàng");
        serviceManagementDescriptionLabel.getStyleClass().addAll("card-description");
        serviceManagementTextVBox.getChildren().addAll(serviceManagementTitleLabel, serviceManagementDescriptionLabel);
        Button serviceManagementButton = new Button("Truy cập");
        serviceManagementButton.getStyleClass().addAll("card-button");
        serviceManagementButton.setOnAction(e -> this.moQuanLyDichVu(e));
        serviceManagementContentVBox.getChildren().addAll(serviceManagementImageView, serviceManagementTextVBox, serviceManagementButton);
        serviceManagementCardVBox.getChildren().addAll(serviceManagementContentVBox);
        GridPane.setColumnIndex(serviceManagementCardVBox, 0);
        GridPane.setRowIndex(serviceManagementCardVBox, 1);

        VBox statisticsCardVBox = new VBox();
        statisticsCardVBox.getStyleClass().addAll("feature-card");

        VBox statisticsContentVBox = new VBox(15.0);
        statisticsContentVBox.setAlignment(Pos.CENTER);
        statisticsContentVBox.getStyleClass().addAll("card-content");
        ImageView statisticsImageView = new ImageView();
        statisticsImageView.setFitHeight(60.0);
        statisticsImageView.setFitWidth(60.0);
        statisticsImageView.setPreserveRatio(true);
        statisticsImageView.setImage(new Image(
                getClass().getResource("/com/example/louishotelmanagement/image/bar-chart.png")
                        .toExternalForm()));

        VBox statisticsTextVBox = new VBox(8.0);
        statisticsTextVBox.setAlignment(Pos.CENTER);
        Label statisticsTitleLabel = new Label("Thống Kê");
        statisticsTitleLabel.getStyleClass().addAll("card-title");
        Label statisticsDescriptionLabel = new Label("Báo cáo doanh thu, hiệu suất và xu hướng");
        statisticsDescriptionLabel.getStyleClass().addAll("card-description");
        statisticsTextVBox.getChildren().addAll(statisticsTitleLabel, statisticsDescriptionLabel);
        Button statisticsButton = new Button("Truy cập");
        statisticsButton.getStyleClass().addAll("card-button");
        statisticsButton.setOnAction(e -> this.moThongKe(e));
        statisticsContentVBox.getChildren().addAll(statisticsImageView, statisticsTextVBox, statisticsButton);
        statisticsCardVBox.getChildren().addAll(statisticsContentVBox);
        GridPane.setColumnIndex(statisticsCardVBox, 1);
        GridPane.setRowIndex(statisticsCardVBox, 1);
        featuresGridPane.getChildren().addAll(roomManagementCardVBox, customerManagementCardVBox, serviceManagementCardVBox, statisticsCardVBox);

        VBox quickActionsVBox = new VBox(15.0);
        quickActionsVBox.setAlignment(Pos.CENTER);
        quickActionsVBox.getStyleClass().addAll("quick-actions-padding");
        Label quickActionsTitleLabel = new Label("Hành động nhanh");
        quickActionsTitleLabel.getStyleClass().addAll("quick-actions-title");

        HBox quickActionsHBox = new HBox(20.0);
        quickActionsHBox.setAlignment(Pos.CENTER);
        Button bookRoomButton = new Button("Đặt phòng mới");
        bookRoomButton.getStyleClass().addAll("quick-action-button");
        bookRoomButton.setOnAction(e -> this.moDatPhong(e));
        Button checkInButton = new Button("Check-in");
        checkInButton.getStyleClass().addAll("quick-action-button");
        checkInButton.setOnAction(e -> this.moNhanPhong(e));
        Button checkOutButton = new Button("Check-out");
        checkOutButton.getStyleClass().addAll("quick-action-button");
        checkOutButton.setOnAction(e -> this.moTraPhong(e));
        quickActionsHBox.getChildren().addAll(bookRoomButton, checkInButton, checkOutButton);
        quickActionsVBox.getChildren().addAll(quickActionsTitleLabel, quickActionsHBox);
        mainContentVBox.getChildren().addAll(welcomeSectionVBox, featuresGridPane, quickActionsVBox);
        mainBorderPane.setCenter(mainContentVBox);
        BorderPane.setAlignment(mainContentVBox, Pos.CENTER);

        VBox headerVBox = new VBox();
        headerVBox.setAlignment(Pos.CENTER);
        headerVBox.setPrefHeight(120.0);
        headerVBox.getStyleClass().addAll("header-gradient");

        VBox headerContentVBox = new VBox(8.0);
        headerContentVBox.setAlignment(Pos.CENTER);
        Label brandTitleLabel = new Label("LOUIS HOTEL");
        brandTitleLabel.getStyleClass().addAll("brand-title");
        Label brandSubtitleLabel = new Label("MANAGEMENT SYSTEM");
        brandSubtitleLabel.getStyleClass().addAll("brand-subtitle");
        headerContentVBox.getChildren().addAll(brandTitleLabel, brandSubtitleLabel);
        headerVBox.getChildren().addAll(headerContentVBox);
        mainBorderPane.setTop(headerVBox);
        BorderPane.setAlignment(headerVBox, Pos.CENTER);

        VBox footerVBox = new VBox(10.0);
        footerVBox.setAlignment(Pos.CENTER);
        footerVBox.getStyleClass().addAll("footer-background");
        Label footerCopyrightLabel = new Label("© 2025 Louis Hotel Management System");
        footerCopyrightLabel.getStyleClass().addAll("footer-text");
        Label footerVersionLabel =
                new Label("Phiên bản 2.0 - Được thiết kế để tối ưu hóa trải nghiệm người dùng");
        footerVersionLabel.getStyleClass().addAll("footer-version");
        footerVBox.getChildren().addAll(footerCopyrightLabel, footerVersionLabel);
        mainBorderPane.setBottom(footerVBox);
        BorderPane.setAlignment(footerVBox, Pos.CENTER);
        this.root = mainBorderPane;

        ContentSwitcher switcher = LayoutController.getInstance();
        if (switcher != null) {
            setContentSwitcher(switcher);
        }
    }
    
    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }

    public Parent getRoot() {
        return root;
    }

    private void moQuanLyPhong(ActionEvent e) {
        switcher.switchContent(new QuanLyPhongView().getRoot());
    }

    private void moQuanLyKhachHang(ActionEvent e) {
        QuanLyKhachHangView quanLyKhachHangView = QuanLyKhachHangView.getInstance();
        switcher.switchContent(quanLyKhachHangView.getRoot());
    }

    private void moQuanLyDichVu(ActionEvent e) {
        QuanLyDichVuView quanLyDichVuView = QuanLyDichVuView.getInstance();
        switcher.switchContent(quanLyDichVuView.getRoot());
    }

    private void moThongKe(ActionEvent e) {
        switcher.switchContent(ThongKeView.getInstance().getRoot());
    }

    private void moDatPhong(ActionEvent e) {
        switcher.switchContent("/com/example/louishotelmanagement/fxml/dat-phong-truc-tiep-view.fxml");
    }

    private void moNhanPhong(ActionEvent e) {
        switcher.switchContent("/com/example/louishotelmanagement/fxml/nhan-phong-view.fxml");
    }

    private void moTraPhong(ActionEvent e) {
        switcher.switchContent("/com/example/louishotelmanagement/fxml/tra-phong-view.fxml");
    }
}