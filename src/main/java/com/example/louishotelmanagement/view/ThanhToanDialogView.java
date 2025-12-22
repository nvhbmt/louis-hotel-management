package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.model.HoaDonChiTietItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.math.BigDecimal;

public class ThanhToanDialogView {
    private VBox thanhToanContent;
    private Button btnLamMoi;
    private TextField txtMaKhachHang;
    private TextField txtHoTen;
    private TextField txtSoDienThoai;
    private TextField txtEmail;
    private TextField txtHangkhach;
    private TextField txtSoPhong;
    private TextField txtNgayNhan;
    private TextField txtNgayTra;
    private TextField txtMaKhuyenMai;
    private TableView tblChiTietHoaDon;
    private TableColumn colStt;
    private TableColumn colTen;
    private TableColumn colSL;
    private TableColumn colDonGia;
    private TableColumn colThanhTien;
    private RadioButton rbtnTienMat;
    private RadioButton rbtnTheNganHang;
    private Label lblTongTienPhong;
    private Label lblTongTienDichVu;
    private Label lblTienCoc;
    private Label lblTamTinh;
    private Label lblVat;
    private Label lblTongThanhToan;
    private Label lblGiamGiaMaGG;
    private Label lblGiamGiaHangKH;
    private Label lblPhatNhanTre;
    private Label lblPhatTraSom;
    private Label lblPhatTraTre;
    private Label lblTongTienPhat;
    private Button btnHuy;
    private Button btnThanhToan;
    private Parent root;

    public ThanhToanDialogView() {
        ScrollPane scrollpane1 = new ScrollPane();
        // Giúp ScrollPane tự động co giãn theo kích thước nội dung
        scrollpane1.setFitToWidth(true);
        scrollpane1.setFitToHeight(true);

        try {
            scrollpane1.getStylesheets().addAll(
                    getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm(),
                    getClass().getResource("/com/example/louishotelmanagement/css/thanh-toan.css").toExternalForm(),
                    getClass().getResource("/com/example/louishotelmanagement/css/thanh-toan-dialog.css").toExternalForm()
            );
        } catch (Exception e) {
            System.err.println("Cảnh báo: Không tìm thấy file CSS!");
        }

        BorderPane borderpane1 = new BorderPane();

        thanhToanContent = new VBox(20.0);
        thanhToanContent.setPrefHeight(823.0);
        thanhToanContent.setPrefWidth(1200.0);
        thanhToanContent.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));

        // --- HBOX 1: Header ---
        HBox hbox1 = new HBox();
        hbox1.setPrefHeight(72.0);
        hbox1.setPrefWidth(894.0);

        VBox vbox1 = new VBox(5.0);
        vbox1.setPrefHeight(100.0);
        vbox1.setPrefWidth(363.0);
        Label label1 = new Label("Thanh Toán Khách Sạn");
        label1.getStyleClass().addAll("main-header");
        Label label2 = new Label("Quản lý và thanh toán dịch vụ cho khách hàng");
        label2.getStyleClass().addAll("main-subtitle");
        vbox1.getChildren().addAll(label1, label2);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        VBox vbox2 = new VBox();
        vbox2.setAlignment(Pos.TOP_RIGHT);
        vbox2.setPrefHeight(100.0);
        vbox2.setPrefWidth(538.0);
        btnLamMoi = new Button("Làm mới");
        btnLamMoi.setPrefHeight(37.0);
        btnLamMoi.setPrefWidth(112.0);
        btnLamMoi.getStyleClass().addAll("btn-info");
        vbox2.getChildren().addAll(btnLamMoi);
        hbox1.getChildren().addAll(vbox1, region1, vbox2);

        // --- VBOX 3: Thông tin khách hàng ---
        VBox vbox3 = new VBox(15.0);
        vbox3.setMaxHeight(200);
        vbox3.getStyleClass().addAll("chart-container");
        Label label3 = new Label("Thông tin khách hàng và phòng");
        label3.getStyleClass().addAll("section-title");

        HBox hbox2 = new HBox(20.0);
        hbox2.setAlignment(Pos.CENTER_LEFT);
        hbox2.setPrefHeight(66.0);

        txtMaKhachHang = new TextField(); txtMaKhachHang.setPrefHeight(33.0);
        txtHoTen = new TextField(); txtHoTen.setPrefHeight(33.0);
        txtSoDienThoai = new TextField(); txtSoDienThoai.setPrefHeight(33.0);
        txtEmail = new TextField(); txtEmail.setPrefHeight(33.0);

        hbox2.getChildren().addAll(
                createVBoxField("Mã Khách Hàng", txtMaKhachHang),
                createVBoxField("Họ tên", txtHoTen),
                createVBoxField("Số điện thoại", txtSoDienThoai),
                createVBoxField("Email", txtEmail)
        );

        HBox hbox3 = new HBox(20.0);
        hbox3.setAlignment(Pos.CENTER_LEFT);
        hbox3.setPrefHeight(100.0);

        txtHangkhach = new TextField(); txtHangkhach.setPrefHeight(33.0);
        txtSoPhong = new TextField(); txtSoPhong.setPrefHeight(33.0);
        txtNgayNhan = new TextField(); txtNgayNhan.setPrefHeight(33.0);
        txtNgayTra = new TextField(); txtNgayTra.setPrefHeight(33.0);

        hbox3.getChildren().addAll(
                createVBoxField("Hạng khách", txtHangkhach),
                createVBoxField("Số phòng", txtSoPhong),
                createVBoxField("Ngày nhận", txtNgayNhan),
                createVBoxField("Ngày trả", txtNgayTra)
        );
        vbox3.getChildren().addAll(label3, hbox2, hbox3);
        VBox.setVgrow(vbox3, Priority.ALWAYS);

        // --- HBOX 4: Khuyến mãi, Bảng, PTTT ---
        HBox hbox4 = new HBox(20.0);
        hbox4.setPrefHeight(250.0);

        VBox vbox12 = new VBox(15.0);
        vbox12.setPrefWidth(300.0);
        vbox12.getStyleClass().addAll("chart-container");
        txtMaKhuyenMai = new TextField();
        txtMaKhuyenMai.setPrefSize(147.0, 34.0);
        Button btnChonMa = new Button("Chọn mã");
        btnChonMa.getStyleClass().add("button-chon-ma");
        vbox12.getChildren().addAll(new Label("Mã giảm giá"), txtMaKhuyenMai, btnChonMa);

        VBox vbox13 = new VBox(10.0);
        vbox13.setPrefWidth(600.0);
        vbox13.getStyleClass().add("chart-container");
        tblChiTietHoaDon = new TableView();
        tblChiTietHoaDon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colStt = new TableColumn("STT"); colStt.setPrefWidth(35);
        colTen = new TableColumn("Tên (Phòng/DV)"); colTen.setPrefWidth(150);
        colSL = new TableColumn("SL"); colSL.setPrefWidth(40);
        colDonGia = new TableColumn("Đơn giá"); colDonGia.setPrefWidth(100);
        colThanhTien = new TableColumn("Thành tiền"); colThanhTien.setPrefWidth(120);
        tblChiTietHoaDon.getColumns().addAll(colStt, colTen, colSL, colDonGia, colThanhTien);
        vbox13.getChildren().addAll(new Label("Phòng/Dịch vụ đã sử dụng"), tblChiTietHoaDon);
        HBox.setHgrow(vbox13, Priority.ALWAYS);

        VBox vbox14 = new VBox(10.0);
        vbox14.setPrefWidth(350.0);
        vbox14.getStyleClass().add("chart-container");
        rbtnTienMat = new RadioButton("Tiền mặt");
        rbtnTheNganHang = new RadioButton("Chuyển khoản");
        vbox14.getChildren().addAll(new Label("Phương thức thanh toán"), rbtnTienMat, rbtnTheNganHang);

        hbox4.getChildren().addAll(vbox12, vbox13, vbox14);

        // --- VBOX 15: Chi tiết thanh toán ---
        VBox vbox15 = new VBox(10.0);
        HBox hbox7 = new HBox(10);
        lblTongTienPhong = new Label("0 ₫"); lblTongTienPhong.getStyleClass().addAll("summary-value", "tong-tien-phong");
        lblTongTienDichVu = new Label("0 ₫"); lblTongTienDichVu.getStyleClass().addAll("summary-value", "tong-tien-dich-vu");
        lblTienCoc = new Label("0 ₫"); lblTienCoc.getStyleClass().addAll("summary-value", "tien-coc");
        lblTamTinh = new Label("0 ₫"); lblTamTinh.getStyleClass().addAll("summary-value", "tam-tinh");
        lblVat = new Label("0 ₫"); lblVat.getStyleClass().addAll("summary-value", "vat");
        lblTongThanhToan = new Label("0 ₫"); lblTongThanhToan.getStyleClass().addAll("summary-value", "tong-cuoi");

        hbox7.getChildren().addAll(
                createSummaryBox("Tổng tiền phòng", lblTongTienPhong),
                createSummaryBox("Tổng tiền DV", lblTongTienDichVu),
                createSummaryBox("Tiền cọc", lblTienCoc),
                createSummaryBox("Tạm tính", lblTamTinh),
                createSummaryBox("VAT (10%)", lblVat),
                createSummaryBox("TỔNG CUỐI", lblTongThanhToan)
        );

        HBox hbox8 = new HBox(10);
        lblGiamGiaMaGG = new Label("0 ₫"); lblGiamGiaMaGG.getStyleClass().addAll("summary-value", "giam-gia");
        lblGiamGiaHangKH = new Label("0 ₫"); lblGiamGiaHangKH.getStyleClass().addAll("summary-value", "giam-gia");
        lblPhatNhanTre = new Label("0 ₫"); lblPhatNhanTre.getStyleClass().addAll("summary-value", "phat");
        lblPhatTraSom = new Label("0 ₫"); lblPhatTraSom.getStyleClass().addAll("summary-value", "phat");
        lblPhatTraTre = new Label("0 ₫"); lblPhatTraTre.getStyleClass().addAll("summary-value", "phat");
        lblTongTienPhat = new Label("0 ₫"); lblTongTienPhat.getStyleClass().addAll("summary-value", "tong-tien-phat");

        hbox8.getChildren().addAll(
                createSummaryBox("Giảm Mã GG", lblGiamGiaMaGG),
                createSummaryBox("Giảm Hạng KH", lblGiamGiaHangKH),
                createSummaryBox("Phạt Nhận Trễ", lblPhatNhanTre),
                createSummaryBox("Phạt Trả Sớm", lblPhatTraSom),
                createSummaryBox("Phạt Trả Trễ", lblPhatTraTre),
                createSummaryBox("TỔNG PHẠT", lblTongTienPhat)
        );
        vbox15.getChildren().addAll(new Label("Chi tiết thanh toán"), hbox7, hbox8);

        // --- HBOX 9: Buttons ---
        HBox hbox9 = new HBox(20.0);
        hbox9.setAlignment(Pos.TOP_RIGHT);
        btnHuy = new Button("Hủy"); btnHuy.setPrefSize(107, 38); btnHuy.getStyleClass().add("btn-cancel");
        btnThanhToan = new Button("Thanh Toán"); btnThanhToan.getStyleClass().add("btn-success");
        hbox9.getChildren().addAll(btnHuy, btnThanhToan);

        // --- KẾT NỐI ROOT ---
        thanhToanContent.getChildren().addAll(hbox1, vbox3, hbox4, vbox15, hbox9);

        // Quan trọng: Đặt Content vào Center để hiển thị đầy đủ
        borderpane1.setCenter(thanhToanContent);
        scrollpane1.setContent(borderpane1);

        this.root = scrollpane1;
    }

    // --- Helper Methods để giữ code gọn sạch ---
    private VBox createVBoxField(String labelText, TextField field) {
        VBox vb = new VBox(5.0);
        Label lbl = new Label(labelText); lbl.getStyleClass().add("field-label");
        vb.getChildren().addAll(lbl, field);
        HBox.setHgrow(vb, Priority.ALWAYS);
        return vb;
    }

    private VBox createSummaryBox(String labelText, Label valueLbl) {
        VBox vb = new VBox(5.0);
        vb.getStyleClass().add("chart-container");
        Label lbl = new Label(labelText); lbl.getStyleClass().add("summary-label");
        vb.getChildren().addAll(lbl, valueLbl);
        HBox.setHgrow(vb, Priority.ALWAYS);
        return vb;
    }
    public Parent getRoot() {
        return root;
    }
    public VBox getThanhToanContent() { return thanhToanContent; }
    public Button getBtnLamMoi() { return btnLamMoi; }
    public TextField getTxtMaKhachHang() { return txtMaKhachHang; }
    public TextField getTxtHoTen() { return txtHoTen; }
    public TextField getTxtSoDienThoai() { return txtSoDienThoai; }
    public TextField getTxtEmail() { return txtEmail; }
    public TextField getTxtHangkhach() { return txtHangkhach; }
    public TextField getTxtSoPhong() { return txtSoPhong; }
    public TextField getTxtNgayNhan() { return txtNgayNhan; }
    public TextField getTxtNgayTra() { return txtNgayTra; }
    public TextField getTxtMaKhuyenMai() { return txtMaKhuyenMai; }
    public TableView<HoaDonChiTietItem> getTblChiTietHoaDon() { return tblChiTietHoaDon; }
    public TableColumn<HoaDonChiTietItem, Integer> getColStt() { return colStt; }
    public TableColumn<HoaDonChiTietItem, String> getColTen() { return colTen; }
    public TableColumn<HoaDonChiTietItem, Integer> getColSL() { return colSL; }
    public TableColumn<HoaDonChiTietItem, BigDecimal> getColDonGia() { return colDonGia; }
    public TableColumn<HoaDonChiTietItem, BigDecimal> getColThanhTien() { return colThanhTien; }
    public RadioButton getRbtnTienMat() { return rbtnTienMat; }
    public RadioButton getRbtnTheNganHang() { return rbtnTheNganHang; }
    public Label getLblTongTienPhong() { return lblTongTienPhong; }
    public Label getLblTongTienDichVu() { return lblTongTienDichVu; }
    public Label getLblTienCoc() { return lblTienCoc; }
    public Label getLblTamTinh() { return lblTamTinh; }
    public Label getLblVat() { return lblVat; }
    public Label getLblTongThanhToan() { return lblTongThanhToan; }
    public Label getLblGiamGiaMaGG() { return lblGiamGiaMaGG; }
    public Label getLblGiamGiaHangKH() { return lblGiamGiaHangKH; }
    public Label getLblPhatNhanTre() { return lblPhatNhanTre; }
    public Label getLblPhatTraSom() { return lblPhatTraSom; }
    public Label getLblPhatTraTre() { return lblPhatTraTre; }
    public Label getLblTongTienPhat() { return lblTongTienPhat; }
    public Button getBtnHuy() { return btnHuy; }
    public Button getBtnThanhToan() { return btnThanhToan; }
}