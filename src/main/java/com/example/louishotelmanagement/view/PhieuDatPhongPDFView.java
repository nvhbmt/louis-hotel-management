package com.example.louishotelmanagement.view;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.shape.*;
import javafx.event.ActionEvent;
public class PhieuDatPhongPDFView {
    private VBox rootContainer;
    private Label lblMaPhieu;
    private Label lblNgayLap;
    private Label lblKhachHang;
    private Label lblNgayDen;
    private Label lblNgayDi;
    private VBox containerChiTietPhong;
    private Label lblTongTien;
    private Button btnIn;
    private Button btnDong;
    private Parent root;

    public PhieuDatPhongPDFView() {
        rootContainer = new VBox(15.0);
        rootContainer.setAlignment(Pos.TOP_CENTER);
        rootContainer.setPrefHeight(700.0);
        rootContainer.setPrefWidth(600.0);
        rootContainer.getStyleClass().addAll("card");
        rootContainer.setPadding(new Insets(30.0, 30.0, 30.0, 30.0));

        // --- Header ---
        VBox vbox1 = new VBox(5.0);
        vbox1.setAlignment(Pos.CENTER);
        vbox1.setPadding(new Insets(0, 0, 10.0, 0));
        Label label1 = new Label("LOIUS HOTEL");
        label1.getStyleClass().addAll("text-primary");
        Label label2 = new Label("123 Đường ABC, Hà Nội | SĐT: (024) 1234 5678");
        label2.getStyleClass().addAll("text-secondary");

        // Tối ưu đường kẻ: Bind chiều rộng đường kẻ theo container
        Line line1 = new Line();
        line1.setEndX(540.0); // 600px - (30px padding * 2)
        line1.getStyleClass().add("separator-line");

        vbox1.getChildren().addAll(label1, label2, line1);

        Label label3 = new Label("PHIẾU XÁC NHẬN ĐẶT PHÒNG");
        label3.setAlignment(Pos.CENTER);
        label3.setMaxWidth(Double.MAX_VALUE);
        label3.getStyleClass().addAll("title");

        lblMaPhieu = new Label("Mã Phiếu: PDXXXXXX");
        lblMaPhieu.setAlignment(Pos.CENTER);
        lblMaPhieu.setMaxWidth(Double.MAX_VALUE);
        lblMaPhieu.getStyleClass().addAll("text-info");

        // --- Section Thông tin chung ---
        VBox vbox3 = new VBox(5.0);
        vbox3.getStyleClass().addAll("section");
        Label label4 = new Label("THÔNG TIN CHUNG");
        label4.getStyleClass().addAll("section-header");

        HBox hbox1 = new HBox(20.0);
        Label label5 = new Label("Ngày Lập Phiếu:");
        label5.setPrefWidth(120.0);
        lblNgayLap = new Label("22/10/2025");
        hbox1.getChildren().addAll(label5, lblNgayLap);

        HBox hbox2 = new HBox(20.0);
        Label label6 = new Label("Khách Hàng:");
        label6.setPrefWidth(120.0);
        lblKhachHang = new Label("Nguyễn Văn A (KH001)");
        hbox2.getChildren().addAll(label6, lblKhachHang);
        vbox3.getChildren().addAll(label4, hbox1, hbox2);

        // --- Section Thông tin lưu trú ---
        VBox vbox4 = new VBox(5.0);
        vbox4.getStyleClass().addAll("section");
        Label label7 = new Label("THÔNG TIN LƯU TRÚ");
        label7.getStyleClass().addAll("section-header");

        HBox hbox3 = new HBox(20.0);
        Label label8 = new Label("Ngày Đến (Check-in):");
        label8.setPrefWidth(150.0);
        lblNgayDen = new Label("23/10/2025");
        hbox3.getChildren().addAll(label8, lblNgayDen);

        HBox hbox4 = new HBox(20.0);
        Label label9 = new Label("Ngày Đi (Check-out):");
        label9.setPrefWidth(150.0);
        lblNgayDi = new Label("25/10/2025");
        hbox4.getChildren().addAll(label9, lblNgayDi);
        vbox4.getChildren().addAll(label7, hbox3, hbox4);

        // --- Chi tiết phòng (Table-like) ---
        VBox vbox5 = new VBox(5.0);
        vbox5.getStyleClass().addAll("section");
        Label label10 = new Label("CHI TIẾT PHÒNG ĐÃ ĐẶT");
        label10.getStyleClass().addAll("section-header");

        HBox hbox5 = new HBox();
        hbox5.setAlignment(Pos.CENTER_LEFT);
        hbox5.getStyleClass().addAll("table-header");
        Label label11 = new Label("Mã Phòng");
        label11.setPrefWidth(100.0);
        Label label12 = new Label("Loại Phòng");
        label12.setPrefWidth(200.0);
        Label label13 = new Label("Đơn Giá/Đêm");
        label13.setPrefWidth(200.0);
        hbox5.getChildren().addAll(label11, label12, label13);

        containerChiTietPhong = new VBox(3.0);
        vbox5.getChildren().addAll(label10, hbox5, containerChiTietPhong);

        // --- Tổng tiền ---
        VBox vbox6 = new VBox(5.0);
        vbox6.getStyleClass().addAll("total-section");
        Line line3 = new Line(0, 0, 540.0, 0); // Đường kẻ ngang dài 540px

        HBox hbox6 = new HBox(10.0);
        hbox6.setAlignment(Pos.CENTER_RIGHT);
        Label label14 = new Label("TỔNG TIỀN PHÒNG (TẠM TÍNH):");
        lblTongTien = new Label("0 VNĐ");
        hbox6.getChildren().addAll(label14, lblTongTien);

        Label label15 = new Label("*Giá chưa bao gồm VAT và dịch vụ phát sinh.");
        label15.setMaxWidth(Double.MAX_VALUE);
        label15.setAlignment(Pos.CENTER_RIGHT);
        vbox6.getChildren().addAll(line3, hbox6, label15);

        // --- Footer & Buttons ---
        VBox vbox7 = new VBox(15.0);
        vbox7.setAlignment(Pos.CENTER);
        Line line4 = new Line(0, 0, 540.0, 0);
        Label label16 = new Label("Cảm ơn quý khách đã tin tưởng và lựa chọn LOIUS HOTEL.");

        HBox hbox7 = new HBox(20.0);
        hbox7.setAlignment(Pos.CENTER);
        btnIn = new Button("Xuất File Ảnh (.PNG)");
        btnIn.getStyleClass().add("btn-primary");
        btnIn.setOnAction(this::handleIn);

        this.btnDong = new Button("Đóng");
        btnDong.getStyleClass().add("btn-secondary");
        btnDong.setOnAction(this::handleDong);

        hbox7.getChildren().addAll(btnIn, btnDong);
        vbox7.getChildren().addAll(line4, label16, hbox7);

        VBox.setVgrow(vbox7, Priority.ALWAYS);

        // Add all to root
        rootContainer.getChildren().addAll(vbox1, label3, lblMaPhieu, vbox3, vbox4, vbox5, vbox6, vbox7);
        this.root = rootContainer;
    }

    public Parent getRoot() {
        return root;
    }
    public VBox getRootContainer() {
        return rootContainer;
    }
    public Label getLblMaPhieu() {
        return lblMaPhieu;
    }
    public Label getLblNgayLap() {
        return lblNgayLap;
    }
    public Label getLblKhachHang() {
        return lblKhachHang;
    }
    public Label getLblNgayDen() {
        return lblNgayDen;
    }
    public Label getLblNgayDi() {
        return lblNgayDi;
    }
    public VBox getContainerChiTietPhong() {
        return containerChiTietPhong;
    }
    public Label getLblTongTien() {
        return lblTongTien;
    }
    public Button getBtnIn() {
        return btnIn;
    }
    public Button getBtnDong() {
        return btnDong;
    }
    private void handleIn(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleDong(ActionEvent e) {
        // TODO: implement handler
    }
}
