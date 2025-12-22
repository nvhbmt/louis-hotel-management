package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.TrangThaiPhieuDatPhong;
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
    private ComboBox<String> dsKhachHang;
    private TextField searchTextField;
    private Button btnTim, btnLamMoi, btnXemChiTiet, btnHuyPhieuDat;
    private TableView<PhieuDatPhong> tablePhieu;
    private TableColumn<PhieuDatPhong, String> colMaPhieu, colTenKhachHang, colMaNhanVien, colGhiChu;
    private TableColumn<PhieuDatPhong, LocalDate> colNgayDat, colNgayDen, colNgayDi;
    private TableColumn<PhieuDatPhong, TrangThaiPhieuDatPhong> colTrangThai;
    private TextField txtMaPhieu, txtSoPhong;
    private Parent root;

    public HuyDatPhongView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.setPrefSize(1014.4, 864.0);
        borderpane1.setPadding(new Insets(32.0));
        borderpane1.getStylesheets().add(getClass().getResource("/com/example/louishotelmanagement/css/huy-dat-phong.css").toExternalForm());

        // --- TOP: HEADER ---
        HBox header = new HBox();
        header.getStyleClass().add("header-section");
        VBox iconBox = new VBox();
        iconBox.getStyleClass().add("header-icon-container");
        ImageView iconView = new ImageView(new Image(getClass().getResource("/com/example/louishotelmanagement/image/booking-cancel-icon.png").toExternalForm()));
        iconView.getStyleClass().add("header-icon");
        iconBox.getChildren().add(iconView);
        VBox titleBox = new VBox();
        titleBox.getStyleClass().add("header-title-section");
        Label lblTitle = new Label("Hủy đặt phòng");
        lblTitle.getStyleClass().add("header-title");
        Label lblSubTitle = new Label("Thực hiện chức năng hủy đặt phòng cho khách hàng");
        lblSubTitle.getStyleClass().add("header-subtitle");
        titleBox.getChildren().addAll(lblTitle, lblSubTitle);
        header.getChildren().addAll(iconBox, titleBox);
        borderpane1.setTop(header);

        // --- CENTER: CONTENT ---
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.TOP_CENTER);

        // Row chọn khách hàng
        HBox rowKhach = new HBox();
        rowKhach.setAlignment(Pos.CENTER);
        rowKhach.setPrefHeight(100.0);
        Label lblKhach = new Label("Khách hàng");
        lblKhach.setPrefWidth(120.0);
        HBox.setMargin(lblKhach, new Insets(0, 15.0, 0, 0));
        dsKhachHang = new ComboBox<>();
        dsKhachHang.setPrefSize(607.0, 42.0);
        rowKhach.getChildren().addAll(lblKhach, dsKhachHang);

        // Row tìm kiếm
        HBox rowSearch = new HBox();
        rowSearch.getStyleClass().add("search-container");
        Label lblSearch = new Label("Nhập mã phiếu");
        lblSearch.setPrefWidth(120.0);
        searchTextField = new TextField();
        searchTextField.setPrefWidth(376.0);
        btnTim = new Button("Tìm");
        btnTim.getStyleClass().add("button-search");
        HBox.setMargin(btnTim, new Insets(0, 0, 0, 20.0));
        btnLamMoi = new Button("Làm mới");
        btnLamMoi.getStyleClass().add("button-refresh");
        HBox.setMargin(btnLamMoi, new Insets(0, 0, 0, 10.0));
        rowSearch.getChildren().addAll(lblSearch, searchTextField, btnTim, btnLamMoi);

        // TableView
        tablePhieu = new TableView<>();
        tablePhieu.setPrefHeight(250.0);
        VBox.setVgrow(tablePhieu, Priority.ALWAYS);
        VBox.setMargin(tablePhieu, new Insets(0, 20.0, 0, 20.0));
        initColumns();

        // Detail Box
        HBox detailContainer = new HBox();
        detailContainer.getStyleClass().add("detail-box-container");
        VBox detailInfoBox = new VBox();
        detailInfoBox.getStyleClass().add("detail-info-box");
        Label lblDetail = new Label("Chi tiết thông tin phòng");
        lblDetail.setAlignment(Pos.CENTER);
        lblDetail.getStyleClass().add("detail-heading");

        HBox rowDetail1 = new HBox();
        rowDetail1.getStyleClass().add("detail-row");
        rowDetail1.setPadding(new Insets(0, 0, 20.0, 0));
        Label lblMa = new Label("Mã phiếu");
        lblMa.getStyleClass().add("detail-label");
        txtMaPhieu = new TextField();
        txtMaPhieu.setEditable(false);
        txtMaPhieu.getStyleClass().add("detail-text-field");
        rowDetail1.getChildren().addAll(lblMa, txtMaPhieu);

        HBox rowDetail2 = new HBox();
        rowDetail2.getStyleClass().add("detail-row");
        Label lblSo = new Label("Số phòng");
        lblSo.getStyleClass().add("detail-label");
        txtSoPhong = new TextField();
        txtSoPhong.setEditable(false);
        txtSoPhong.getStyleClass().add("detail-text-field");
        rowDetail2.getChildren().addAll(lblSo, txtSoPhong);

        HBox rowDetailBtn = new HBox();
        rowDetailBtn.setAlignment(Pos.CENTER);
        rowDetailBtn.setPrefHeight(100.0);
        btnXemChiTiet = new Button("Xem chi tiết");
        btnXemChiTiet.setPrefSize(145.0, 38.0);
        rowDetailBtn.getChildren().add(btnXemChiTiet);

        detailInfoBox.getChildren().addAll(lblDetail, rowDetail1, rowDetail2, rowDetailBtn);
        detailContainer.getChildren().add(detailInfoBox);

        centerBox.getChildren().addAll(rowKhach, rowSearch, tablePhieu, detailContainer);
        borderpane1.setCenter(centerBox);

        // --- BOTTOM: BUTTON HUY ---
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPrefHeight(100.0);
        btnHuyPhieuDat = new Button("Hủy phiếu đặt phòng");
        btnHuyPhieuDat.getStyleClass().add("button-huy");
        btnHuyPhieuDat.setCursor(Cursor.HAND);
        footer.getChildren().add(btnHuyPhieuDat);
        borderpane1.setBottom(footer);

        this.root = borderpane1;
    }

    private void initColumns() {
        colMaPhieu = new TableColumn<>("Mã phiếu"); colMaPhieu.setPrefWidth(75.0);
        colNgayDat = new TableColumn<>("Ngày đặt"); colNgayDat.setPrefWidth(75.0);
        colNgayDen = new TableColumn<>("Ngày đến"); colNgayDen.setPrefWidth(75.0);
        colNgayDi = new TableColumn<>("Ngày đi"); colNgayDi.setPrefWidth(75.0);
        colTrangThai = new TableColumn<>("Trạng thái"); colTrangThai.setPrefWidth(146.4);
        colGhiChu = new TableColumn<>("Ghi chú"); colGhiChu.setPrefWidth(208.0);
        colTenKhachHang = new TableColumn<>("Tên khách hàng"); colTenKhachHang.setPrefWidth(164.8);
        colMaNhanVien = new TableColumn<>("Mã nhân viên"); colMaNhanVien.setPrefWidth(195.2);
        tablePhieu.getColumns().addAll(colMaPhieu, colNgayDat, colNgayDen, colNgayDi, colTrangThai, colGhiChu, colTenKhachHang, colMaNhanVien);
    }

    // --- GETTERS ---
    public Parent getRoot() { return root; }
    public ComboBox<String> getDsKhachHang() { return dsKhachHang; }
    public TextField getSearchTextField() { return searchTextField; }
    public Button getBtnTim() { return btnTim; }
    public Button getBtnLamMoi() { return btnLamMoi; }
    public TableView<PhieuDatPhong> getTablePhieu() { return tablePhieu; }
    public TableColumn<PhieuDatPhong, String> getColMaPhieu() { return colMaPhieu; }
    public TableColumn<PhieuDatPhong, LocalDate> getColNgayDat() { return colNgayDat; }
    public TableColumn<PhieuDatPhong, LocalDate> getColNgayDen() { return colNgayDen; }
    public TableColumn<PhieuDatPhong, LocalDate> getColNgayDi() { return colNgayDi; }
    public TableColumn<PhieuDatPhong, TrangThaiPhieuDatPhong> getColTrangThai() { return colTrangThai; }
    public TableColumn<PhieuDatPhong, String> getColGhiChu() { return colGhiChu; }
    public TableColumn<PhieuDatPhong, String> getColTenKhachHang() { return colTenKhachHang; }
    public TableColumn<PhieuDatPhong, String> getColMaNhanVien() { return colMaNhanVien; }
    public TextField getTxtMaPhieu() { return txtMaPhieu; }
    public TextField getTxtSoPhong() { return txtSoPhong; }
    public Button getBtnXemChiTiet() { return btnXemChiTiet; }
    public Button getBtnHuyPhieuDat() { return btnHuyPhieuDat; }
}