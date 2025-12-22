package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.model.PhieuDatPhong;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class NhanPhongView {
    private ComboBox<String> dsKhachHang;
    private TextField soDT;
    private TextField CCCD;
    private ComboBox<String> dsPhong;
    private DatePicker ngayDat;
    private Button btnCheck;

    private TextField maPhieu;
    private TextField maPhong;
    private TextField tang;
    private TextField hoTen;
    private DatePicker ngayDen;
    private DatePicker ngayDi;

    private Button btnNhanPhong;
    private Parent root;

    public NhanPhongView() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1014.4, 864.0);
        borderPane.setPadding(new Insets(32.0));

        // Nạp CSS
        try {
            borderPane.getStylesheets().add(getClass().getResource("/com/example/louishotelmanagement/css/nhan-phong.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Không tìm thấy file CSS nhan-phong.css");
        }

        // --- TOP: HEADER ---
        HBox header = new HBox();
        header.getStyleClass().add("header-section");

        VBox iconContainer = new VBox();
        iconContainer.getStyleClass().add("header-icon-container");
        ImageView headerIcon = new ImageView();
        try {
            headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/suitcases.png").toExternalForm()));
            headerIcon.getStyleClass().add("header-icon");
        } catch (Exception e) {
            System.err.println("Không tìm thấy icon suitcases.png");
        }
        iconContainer.getChildren().add(headerIcon);

        VBox titleSection = new VBox();
        titleSection.getStyleClass().add("header-title-section");
        Label lblTitle = new Label("Nhận phòng");
        lblTitle.getStyleClass().add("header-title");
        Label lblSubTitle = new Label("Thực hiện chức năng nhận phòng cho khách");
        lblSubTitle.getStyleClass().add("header-subtitle");
        titleSection.getChildren().addAll(lblTitle, lblSubTitle);

        header.getChildren().addAll(iconContainer, titleSection);
        borderPane.setTop(header);

        // --- CENTER: INFO SECTION ---
        VBox centerBox = new VBox();
        centerBox.getStyleClass().add("info-section");
        BorderPane.setAlignment(centerBox, Pos.CENTER);

        Label lblHeading1 = new Label("Thông tin nhận phòng");
        lblHeading1.getStyleClass().add("section-heading");

        // Hàng nhập liệu
        dsKhachHang = new ComboBox<>();
        dsKhachHang.setPrefSize(526.0, 27.0);
        dsKhachHang.getStyleClass().add("combo-box");

        soDT = new TextField();
        soDT.setEditable(false);
        soDT.getStyleClass().add("text-field");

        CCCD = new TextField();
        CCCD.setEditable(false);
        CCCD.getStyleClass().add("text-field");

        dsPhong = new ComboBox<>();
        dsPhong.getStyleClass().add("combo-box");

        ngayDat = new DatePicker();
        ngayDat.getStyleClass().add("date-picker");

        btnCheck = new Button("Check");
        btnCheck.getStyleClass().add("button-base");
        HBox checkBtnBox = new HBox(btnCheck);
        checkBtnBox.setAlignment(Pos.CENTER);
        checkBtnBox.setPrefHeight(49.0);
        checkBtnBox.setPrefWidth(880.0);

        centerBox.getChildren().addAll(
                lblHeading1,
                createInputRow("Khách hàng", dsKhachHang),
                createInputRow("Số điện thoại", soDT),
                createInputRow("CCCD", CCCD),
                createInputRow("Phòng", dsPhong),
                createInputRow("Ngày đặt", ngayDat),
                checkBtnBox
        );

        // --- CHI TIẾT PHÒNG ---
        Label lblHeading2 = new Label("Thông tin phòng");
        lblHeading2.getStyleClass().add("section-heading");

        VBox detailsContainer = new VBox();
        detailsContainer.setAlignment(Pos.CENTER);
        detailsContainer.setFillWidth(false);
        detailsContainer.setPrefHeight(270.0);
        detailsContainer.setPrefWidth(880.0);

        VBox detailsBox = new VBox();
        detailsBox.getStyleClass().add("room-details-box");

        maPhieu = new TextField(); maPhieu.setEditable(false); maPhieu.getStyleClass().add("text-field");
        maPhong = new TextField(); maPhong.setEditable(false); maPhong.getStyleClass().add("text-field");
        tang = new TextField(); tang.setEditable(false); tang.getStyleClass().add("text-field");
        hoTen = new TextField(); hoTen.setEditable(false); hoTen.getStyleClass().add("text-field");
        ngayDen = new DatePicker(); ngayDen.getStyleClass().add("date-picker");
        ngayDi = new DatePicker(); ngayDi.getStyleClass().add("date-picker");

        detailsBox.getChildren().addAll(
                createDetailRow("Mã phiếu", maPhieu),
                createDetailRow("Mã phòng", maPhong),
                createDetailRow("Tầng", tang),
                createDetailRow("Họ và tên", hoTen),
                createDetailRow("Ngày đến", ngayDen),
                createDetailRow("Ngày đi", ngayDi)
        );
        detailsContainer.getChildren().add(detailsBox);

        centerBox.getChildren().addAll(lblHeading2, detailsContainer);
        borderPane.setCenter(centerBox);

        // --- BOTTOM: ACTIONS ---
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPrefHeight(108.0);
        bottomBox.setPrefWidth(1014.0);

        btnNhanPhong = new Button("Nhận phòng");
        btnNhanPhong.getStyleClass().add("button-base");
        btnCheck.setId("btnCheck"); // Thêm dòng này
        btnNhanPhong.setId("btnNhanPhong"); // Thêm dòng này
        bottomBox.getChildren().add(btnNhanPhong);
        borderPane.setBottom(bottomBox);

        this.root = borderPane;
    }

    private HBox createInputRow(String labelText, javafx.scene.Node inputField) {
        HBox row = new HBox();
        row.getStyleClass().add("input-row");
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("input-label");
        row.getChildren().addAll(lbl, inputField);
        return row;
    }

    private HBox createDetailRow(String labelText, javafx.scene.Node inputField) {
        HBox row = new HBox();
        Label lbl = new Label(labelText);
        row.getChildren().addAll(lbl, inputField);
        return row;
    }

    // --- GETTERS ---
    public Parent getRoot() { return root; }
    public ComboBox<String> getDsKhachHang() { return dsKhachHang; }
    public ComboBox<String> getDsPhong() { return dsPhong; }
    public TextField getSoDT() { return soDT; }
    public TextField getCCCD() { return CCCD; }
    public Button getBtnCheck() { return btnCheck; }
    public TextField getMaPhieu() { return maPhieu; }
    public TextField getMaPhong() { return maPhong; }
    public TextField getTang() { return tang; }
    public TextField getHoTen() { return hoTen; }
    public DatePicker getNgayDen() { return ngayDen; }
    public DatePicker getNgayDi() { return ngayDi; }
    public DatePicker getNgayDat() { return ngayDat; }
    public Button getBtnNhanPhong() { return btnNhanPhong; }
}