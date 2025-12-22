package com.example.louishotelmanagement.view;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

public class ChiTietPhongTrongPhieuView {
    private Label lblMaPhieu;
    private TableView tblChiTietPhong;
    private TableColumn colTang, colMaPhong, colTenLoaiPhong, colGia;
    private Label lblChiTietMaPhong, lblChiTietTang, lblChiTietTrangThai, lblChiTietLoaiPhong, lblChiTietGia;
    private TextArea txtChiTietMoTa;
    private Button btnClose; // Đặt tên biến rõ ràng
    private Parent root;

    public ChiTietPhongTrongPhieuView() {
        VBox container = new VBox(15.0);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPrefSize(900, 650);
        container.setPadding(new Insets(20));

        // Load CSS (đảm bảo file tồn tại)
        try {
            container.getStylesheets().add(getClass().getResource("/com/example/louishotelmanagement/css/chi-tiet-phong-trong-phieu.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Không tìm thấy file CSS");
        }

        lblMaPhieu = new Label("Chi Tiết Phòng Đặt");
        lblMaPhieu.getStyleClass().add("title-label");

        SplitPane splitPane = new SplitPane();
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        // --- BÊN TRÁI: DANH SÁCH PHÒNG ---
        VBox leftPane = new VBox(10);
        leftPane.setAlignment(Pos.TOP_CENTER);
        Label lblListHeader = new Label("Danh Sách Phòng Trong Phiếu");
        lblListHeader.getStyleClass().add("section-header");

        tblChiTietPhong = new TableView();
        tblChiTietPhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colTang = new TableColumn("Tầng");
        colMaPhong = new TableColumn("Mã Phòng");
        colTenLoaiPhong = new TableColumn("Loại Phòng");
        colGia = new TableColumn("Giá / Đêm");
        tblChiTietPhong.getColumns().addAll(colTang, colMaPhong, colTenLoaiPhong, colGia);
        VBox.setVgrow(tblChiTietPhong, Priority.ALWAYS);
        leftPane.getChildren().addAll(lblListHeader, tblChiTietPhong);

        // --- BÊN PHẢI: CHI TIẾT ---
        VBox rightPane = new VBox(15.0);
        rightPane.getStyleClass().add("detail-section");
        Label lblDetailHeader = new Label("Chi Tiết Phòng");
        lblDetailHeader.getStyleClass().add("detail-header");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        // Khởi tạo các Label hiển thị thông tin
        lblChiTietMaPhong = new Label("-");
        lblChiTietTang = new Label("-");
        lblChiTietTrangThai = new Label("-");
        lblChiTietLoaiPhong = new Label("-");
        lblChiTietGia = new Label("-");

        grid.add(new Label("Mã Phòng:"), 0, 0); grid.add(lblChiTietMaPhong, 1, 0);
        grid.add(new Label("Tầng:"), 0, 1);     grid.add(lblChiTietTang, 1, 1);
        grid.add(new Label("Trạng Thái:"), 0, 2); grid.add(lblChiTietTrangThai, 1, 2);
        grid.add(new Label("Loại Phòng:"), 0, 3); grid.add(lblChiTietLoaiPhong, 1, 3);
        grid.add(new Label("Giá Đơn Vị:"), 0, 4); grid.add(lblChiTietGia, 1, 4);

        txtChiTietMoTa = new TextArea();
        txtChiTietMoTa.setEditable(false); // Chỉ xem, không sửa
        txtChiTietMoTa.setWrapText(true);
        VBox.setVgrow(txtChiTietMoTa, Priority.ALWAYS);

        rightPane.getChildren().addAll(lblDetailHeader, grid, new Label("Mô Tả:"), txtChiTietMoTa);

        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.55);

        btnClose = new Button("Đóng");
        btnClose.setPrefWidth(100);

        container.getChildren().addAll(lblMaPhieu, splitPane, btnClose);
        this.root = container;
    }

    // Getters
    public Parent getRoot() { return root; }
    public Label getLblMaPhieu() { return lblMaPhieu; }
    public TableView getTblChiTietPhong() { return tblChiTietPhong; }
    public TableColumn getColTang() { return colTang; }
    public TableColumn getColMaPhong() { return colMaPhong; }
    public TableColumn getColTenLoaiPhong() { return colTenLoaiPhong; }
    public TableColumn getColGia() { return colGia; }
    public Label getLblChiTietMaPhong() { return lblChiTietMaPhong; }
    public Label getLblChiTietTang() { return lblChiTietTang; }
    public Label getLblChiTietTrangThai() { return lblChiTietTrangThai; }
    public Label getLblChiTietLoaiPhong() { return lblChiTietLoaiPhong; }
    public Label getLblChiTietGia() { return lblChiTietGia; }
    public TextArea getTxtChiTietMoTa() { return txtChiTietMoTa; }
    public Button getBtnClose() { return btnClose; }
}