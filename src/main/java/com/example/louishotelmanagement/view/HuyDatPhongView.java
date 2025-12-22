package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.TrangThaiPhieuDatPhong;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.time.LocalDate;

public class HuyDatPhongView {
    // Khai báo các thành phần giao diện
    private ComboBox<String> dsKhachHang;
    private TextField searchTextField;
    private Button btnTim;
    private Button btnLamMoi;
    private Button btnXemChiTiet;
    private Button btnHuyPhieuDat;

    private TableView<PhieuDatPhong> tablePhieu; // Nên thay Object bằng Class Model (ví dụ: PhieuDat)
    private TableColumn<PhieuDatPhong, String> colMaPhieu;
    private TableColumn<PhieuDatPhong, LocalDate> colNgayDat;
    private TableColumn<PhieuDatPhong, LocalDate> colNgayDen;
    private TableColumn<PhieuDatPhong, LocalDate> colNgayDi;
    private TableColumn<PhieuDatPhong, TrangThaiPhieuDatPhong> colTrangThai;
    private TableColumn<PhieuDatPhong, String> colGhiChu;
    private TableColumn<PhieuDatPhong, String> colTenKhachHang;
    private TableColumn<PhieuDatPhong, String> colMaNhanVien;

    private TextField txtMaPhieu;
    private TextField txtSoPhong;
    private Parent root;

    // Tên Constructor phải trùng với tên Class: HuyDatPhongView
    public HuyDatPhongView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.setPrefSize(1014.4, 864.0);
        borderpane1.setPadding(new Insets(32.0));

        // Load CSS
        try {
            borderpane1.getStylesheets().add(getClass().getResource("/com/example/louishotelmanagement/css/huy-dat-phong.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Lỗi: Không tìm thấy file CSS.");
        }

        // --- 1. HEADER (TOP) ---
        HBox header = new HBox(15);
        header.getStyleClass().add("header-section");

        VBox iconBox = new VBox();
        iconBox.getStyleClass().add("header-icon-container");
        ImageView iconView = new ImageView();
        try {
            iconView.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/booking-cancel-icon.png").toExternalForm()));
            iconView.setFitWidth(50);
            iconView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Lỗi: Không tìm thấy icon.");
        }
        iconBox.getChildren().add(iconView);

        VBox titleBox = new VBox(5);
        Label lblTitle = new Label("Hủy đặt phòng");
        lblTitle.getStyleClass().add("header-title");
        Label lblSubTitle = new Label("Thực hiện chức năng hủy đặt phòng cho khách hàng");
        lblSubTitle.getStyleClass().add("header-subtitle");
        titleBox.getChildren().addAll(lblTitle, lblSubTitle);

        header.getChildren().addAll(iconBox, titleBox);
        borderpane1.setTop(header);

        // --- 2. CONTENT (CENTER) ---
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(20, 0, 0, 0));

        // Hàng chọn khách hàng
        HBox row1 = new HBox(15);
        row1.setAlignment(Pos.CENTER);
        Label lblKhach = new Label("Khách hàng");
        lblKhach.setPrefWidth(120);
        dsKhachHang = new ComboBox<>();
        dsKhachHang.setPrefSize(607, 42);
        row1.getChildren().addAll(lblKhach, dsKhachHang);

        // Hàng tìm kiếm
        HBox row2 = new HBox(10);
        row2.setAlignment(Pos.CENTER);
        row2.getStyleClass().add("search-container");
        Label lblSearch = new Label("Nhập mã phiếu");
        lblSearch.setPrefWidth(120);
        searchTextField = new TextField();
        searchTextField.setPrefWidth(376);

        btnTim = new Button("Tìm");
        btnTim.getStyleClass().add("button-search");
        btnTim.setOnAction(this::handleTim);

        btnLamMoi = new Button("Làm mới");
        btnLamMoi.getStyleClass().add("button-refresh");
        btnLamMoi.setOnAction(this::handleLamMoi);

        row2.getChildren().addAll(lblSearch, searchTextField, btnTim, btnLamMoi);

        // Bảng dữ liệu
        tablePhieu = new TableView<>();
        tablePhieu.setPrefHeight(300);
        VBox.setVgrow(tablePhieu, Priority.ALWAYS);
        initTableColumns();

        // Chi tiết thông tin
        VBox detailBox = new VBox(15);
        detailBox.getStyleClass().add("detail-info-box");
        detailBox.setMaxWidth(800);

        Label lblDetailHeader = new Label("Chi tiết thông tin phòng");
        lblDetailHeader.getStyleClass().add("detail-heading");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.add(new Label("Mã phiếu:"), 0, 0);
        txtMaPhieu = new TextField();
        grid.add(txtMaPhieu, 1, 0);
        grid.add(new Label("Số phòng:"), 0, 1);
        txtSoPhong = new TextField();
        grid.add(txtSoPhong, 1, 1);

        btnXemChiTiet = new Button("Xem chi tiết");
        btnXemChiTiet.setPrefSize(145, 38);
        btnXemChiTiet.setOnAction(this::handleXemChiTiet);

        detailBox.getChildren().addAll(lblDetailHeader, grid, btnXemChiTiet);

        centerBox.getChildren().addAll(row1, row2, tablePhieu, detailBox);
        borderpane1.setCenter(centerBox);

        // --- 3. FOOTER (BOTTOM) ---
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20));
        btnHuyPhieuDat = new Button("Hủy phiếu đặt phòng");
        btnHuyPhieuDat.getStyleClass().add("button-huy");
        btnHuyPhieuDat.setCursor(Cursor.HAND);
        btnHuyPhieuDat.setOnAction(this::handleHuyPhieuDat);

        footer.getChildren().add(btnHuyPhieuDat);
        borderpane1.setBottom(footer);

        this.root = borderpane1;
    }

    private void initTableColumns() {
        colMaPhieu = new TableColumn<>("Mã phiếu");
        colNgayDat = new TableColumn<>("Ngày đặt");
        colNgayDen = new TableColumn<>("Ngày đến");
        colNgayDi = new TableColumn<>("Ngày đi");
        colTrangThai = new TableColumn<>("Trạng thái");
        colGhiChu = new TableColumn<>("Ghi chú");
        colTenKhachHang = new TableColumn<>("Tên khách hàng");
        colMaNhanVien = new TableColumn<>("Mã nhân viên");

        tablePhieu.getColumns().addAll(colMaPhieu, colNgayDat, colNgayDen, colNgayDi, colTrangThai, colGhiChu, colTenKhachHang, colMaNhanVien);
    }

    // Các phương thức xử lý sự kiện
    private void handleTim(ActionEvent e) { System.out.println("Tìm kiếm..."); }
    private void handleLamMoi(ActionEvent e) {
        searchTextField.clear();
        txtMaPhieu.clear();
        txtSoPhong.clear();
    }
    private void handleXemChiTiet(ActionEvent e) { System.out.println("Xem chi tiết..."); }
    private void handleHuyPhieuDat(ActionEvent e) { System.out.println("Hủy phiếu..."); }

    // Getters
    // --- CÁC PHƯƠNG THỨC GETTER ---

    public Parent getRoot() {
        return root;
    }

    public ComboBox<String> getDsKhachHang() {
        return dsKhachHang;
    }

    public TextField getSearchTextField() {
        return searchTextField;
    }

    public Button getBtnTim() {
        return btnTim;
    }

    public Button getBtnLamMoi() {
        return btnLamMoi;
    }

    public TableView<PhieuDatPhong> getTablePhieu() {
        return tablePhieu;
    }

    public TableColumn<PhieuDatPhong, String> getColMaPhieu() {
        return colMaPhieu;
    }

    public TableColumn<PhieuDatPhong, LocalDate> getColNgayDat() {
        return colNgayDat;
    }

    public TableColumn<PhieuDatPhong, LocalDate> getColNgayDen() {
        return colNgayDen;
    }

    public TableColumn<PhieuDatPhong, LocalDate> getColNgayDi() {
        return colNgayDi;
    }

    public TableColumn<PhieuDatPhong, TrangThaiPhieuDatPhong> getColTrangThai() {
        return colTrangThai;
    }

    public TableColumn<PhieuDatPhong, String> getColGhiChu() {
        return colGhiChu;
    }

    public TableColumn<PhieuDatPhong, String> getColTenKhachHang() {
        return colTenKhachHang;
    }

    public TableColumn<PhieuDatPhong, String> getColMaNhanVien() {
        return colMaNhanVien;
    }

    public TextField getTxtMaPhieu() {
        return txtMaPhieu;
    }

    public TextField getTxtSoPhong() {
        return txtSoPhong;
    }

    public Button getBtnXemChiTiet() {
        return btnXemChiTiet;
    }

    public Button getBtnHuyPhieuDat() {
        return btnHuyPhieuDat;
    }
}