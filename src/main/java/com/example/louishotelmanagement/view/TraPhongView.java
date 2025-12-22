package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.Phong;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class TraPhongView {
    private ComboBox<String> dsKhachHang;
    private ComboBox<String> dsPhieu;
    private ComboBox<String> dsPhong;
    private DatePicker ngayTraPhong;
    private Button btnCheck;
    private TextField hoTen;
    private TextField maPhieuThue;
    private DatePicker ngayDen;
    private TextField soLuongPhong;
    private Button btnXemChiTiet;
    private Button btnTraPhong;
    private Parent root;

    public TraPhongView() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefHeight(864.0);
        borderPane.setPrefWidth(1014.4);
        borderPane.getStyleClass().add("main-background");
        borderPane.setPadding(new Insets(32.0));

        // Load CSS
        try {
            borderPane.getStylesheets().add(getClass().getResource("/com/example/louishotelmanagement/css/tra-phong-view.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Không tìm thấy file CSS: tra-phong-view.css");
        }

        // --- TOP SECTION ---
        HBox topHBox = new HBox(16.0);
        BorderPane.setMargin(topHBox, new Insets(0, 0, 32.0, 0));

        ImageView headerIcon = new ImageView();
        try {
            headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/computer.png").toExternalForm()));
            headerIcon.setFitHeight(72.0);
            headerIcon.setFitWidth(72.0);
            headerIcon.setPreserveRatio(true);
        } catch (Exception e) {}

        VBox headerTitleBox = new VBox();
        headerTitleBox.setPrefHeight(62.0);
        headerTitleBox.setPrefWidth(875.0);
        HBox.setHgrow(headerTitleBox, Priority.ALWAYS);

        Label headerTitle = new Label("Trả phòng");
        headerTitle.getStyleClass().add("header-title");
        Label headerSubtitle = new Label("Thực hiện chức năng trả phòng cho khách hàng");
        headerSubtitle.getStyleClass().add("header-subtitle");
        headerSubtitle.setFont(new Font(14.0));

        headerTitleBox.getChildren().addAll(headerTitle, headerSubtitle);
        topHBox.getChildren().addAll(headerIcon, headerTitleBox);
        borderPane.setTop(topHBox);

        // --- CENTER SECTION ---
        VBox centerVBox = new VBox(10.0);
        centerVBox.setPrefHeight(650.0);
        centerVBox.setPrefWidth(100.0);

        // 1. Info Panel (Trên)
        VBox infoPanel = new VBox(10.0);
        infoPanel.setAlignment(Pos.CENTER);
        infoPanel.setPrefHeight(230.0);
        infoPanel.setPrefWidth(100.0);
        infoPanel.getStyleClass().add("info-panel");

        Label infoTitle = new Label("Thông tin trả phòng");
        infoTitle.getStyleClass().add("info-title");
        VBox.setMargin(infoTitle, new Insets(0, 0, 10.0, 0));

        // Rows in Info Panel
        HBox rowKhach = createInputRow("Khách hàng", dsKhachHang = new ComboBox<>(), "/com/example/louishotelmanagement/image/detective.png");

        dsPhieu = new ComboBox<>();
        dsPhieu.setPromptText("Danh sách phiếu theo khách hàng");
        HBox rowPhieu = createInputRow("Phiếu", dsPhieu, "/com/example/louishotelmanagement/image/document_hotel.png");

        dsPhong = new ComboBox<>();
        dsPhong.setPromptText("Tất cả phòng của phiếu");
        HBox rowPhong = createInputRow("Phòng thuê", dsPhong, "/com/example/louishotelmanagement/image/double-bed.png");

        ngayTraPhong = new DatePicker();
        ngayTraPhong.setEditable(true);
        ngayTraPhong.setPrefSize(670.0, 26.0);
        HBox rowNgay = createInputRow("Ngày trả phòng", ngayTraPhong, "/com/example/louishotelmanagement/image/calendar.png");

        HBox checkBtnBox = new HBox();
        checkBtnBox.setAlignment(Pos.CENTER);
        checkBtnBox.setPrefHeight(49.0);
        btnCheck = new Button("Kiểm tra");
        btnCheck.setPrefSize(120.0, 42.0);
        btnCheck.getStyleClass().add("check-button");
        checkBtnBox.getChildren().add(btnCheck);

        infoPanel.getChildren().addAll(infoTitle, rowKhach, rowPhieu, rowPhong, rowNgay, checkBtnBox);

        // 2. Detail Panel (Dưới)
        HBox detailPanel = new HBox(20.0);
        detailPanel.setAlignment(Pos.CENTER);
        detailPanel.setPrefHeight(420.0);
        detailPanel.setPrefWidth(880.0);
        detailPanel.getStyleClass().add("detail-panel-bg");
        VBox.setMargin(detailPanel, new Insets(0, 50.0, 0, 50.0));

        VBox detailContainer = new VBox(10.0);
        detailContainer.setAlignment(Pos.TOP_CENTER);
        detailContainer.setPrefHeight(379.0);
        detailContainer.setPrefWidth(649.0);
        detailContainer.getStyleClass().add("detail-container");

        Label detailTitle = new Label("Thông tin Phiếu thuê");
        detailTitle.getStyleClass().add("detail-title");
        VBox.setMargin(detailTitle, new Insets(0, 0, 5.0, 0));

        hoTen = new TextField(); hoTen.setEditable(false);
        HBox rowHoTen = createDetailRow("Họ và tên KH:", hoTen);

        maPhieuThue = new TextField(); maPhieuThue.setEditable(false);
        HBox rowMaPhieu = createDetailRow("Mã phiếu thuê:", maPhieuThue);

        ngayDen = new DatePicker(); ngayDen.setEditable(true); ngayDen.setPrefSize(446.0, 30.0);
        HBox rowNgayDen = createDetailRow("Ngày Check-in:", ngayDen);

        soLuongPhong = new TextField(); soLuongPhong.setEditable(false);
        HBox rowSoLuong = createDetailRow("Số lượng phòng:", soLuongPhong);

        btnXemChiTiet = new Button("Xem chi tiết từng phòng");
        btnXemChiTiet.setPrefSize(250.0, 30.0);
        btnXemChiTiet.getStyleClass().add("view-detail-button");

        detailContainer.getChildren().addAll(detailTitle, rowHoTen, rowMaPhieu, rowNgayDen, rowSoLuong, btnXemChiTiet);
        detailPanel.getChildren().add(detailContainer);

        centerVBox.getChildren().addAll(infoPanel, detailPanel);
        borderPane.setCenter(centerVBox);

        // --- BOTTOM SECTION ---
        HBox footer = new HBox(50.0);
        footer.setAlignment(Pos.CENTER);
        footer.setPrefHeight(80.0);
        footer.setPrefWidth(880.0);
        BorderPane.setMargin(footer, new Insets(10.0, 0, 30.0, 0));

        btnTraPhong = new Button("Trả phòng/Thanh toán");
        btnTraPhong.setPrefSize(261.0, 50.0);
        btnTraPhong.getStyleClass().add("return-room-button");
        footer.getChildren().add(btnTraPhong);
        borderPane.setBottom(footer);

        this.root = borderPane;
    }

    // Hàm tạo Row cho phần trên (có Icon)
    private HBox createInputRow(String labelText, Control input, String imgPath) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefHeight(37.0);

        Label label = new Label(labelText);
        label.setPrefWidth(137.6);
        label.setFont(new Font(18.0));
        HBox.setMargin(label, new Insets(0, 30.0, 0, 0));

        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResource(imgPath).toExternalForm()));
            icon.setFitHeight(24.0);
            icon.setFitWidth(30.0);
            icon.setPreserveRatio(true);
        } catch (Exception e) {}
        HBox.setMargin(icon, new Insets(0, 10.0, 0, 0));

        input.setPrefHeight(30.0);
        input.setPrefWidth(670.0);

        hbox.getChildren().addAll(label, icon, input);
        return hbox;
    }

    // Hàm tạo Row cho phần dưới (Chi tiết)
    private HBox createDetailRow(String labelText, Control input) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefHeight(35.0);

        Label label = new Label(labelText);
        label.setPrefWidth(120.0);
        label.getStyleClass().add("detail-label");

        input.setPrefHeight(30.0);
        input.setPrefWidth(446.0);

        hbox.getChildren().addAll(label, input);
        return hbox;
    }

    public Parent getRoot() { return root; }
    // Getters giữ nguyên để Controller sử dụng
    public ComboBox<String> getDsKhachHang() { return dsKhachHang; }
    public ComboBox<String> getDsPhieu() { return dsPhieu; }
    public ComboBox<String> getDsPhong() { return dsPhong; }
    public DatePicker getNgayTraPhong() { return ngayTraPhong; }
    public Button getBtnCheck() { return btnCheck; }
    public TextField getHoTen() { return hoTen; }
    public TextField getMaPhieuThue() { return maPhieuThue; }
    public DatePicker getNgayDen() { return ngayDen; }
    public TextField getSoLuongPhong() { return soLuongPhong; }
    public Button getBtnXemChiTiet() { return btnXemChiTiet; }
    public Button getBtnTraPhong() { return btnTraPhong; }
}