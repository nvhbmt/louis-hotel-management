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
import javafx.event.ActionEvent;
public class ChonKhuyenMaiDiaLogView {
    private BorderPane rootPane;
    private TextField txtTimKiem;
    private ComboBox cbKieuGiamGia;
    private ComboBox cbTrangThai;
    private Button btnLamMoi;
    private TableView tableViewKhuyenMai;
    private TableColumn colMaKM;
    private TableColumn colCode;
    private TableColumn colGiamGia;
    private TableColumn colKieuGiamGia;
    private TableColumn colNgayBatDau;
    private TableColumn colNgayKetThuc;
    private TableColumn colTongTienToiThieu;
    private TableColumn colTrangThai;
    private TableColumn colMoTa;
    private Button btnChon;
    private Button btnHuy;
    private Parent root;

    public ChonKhuyenMaiDiaLogView() {
        rootPane = new BorderPane();
        rootPane.setPrefHeight(600.0);
        rootPane.setPrefWidth(1200.0);
        rootPane.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm(), getClass().getResource("/com/example/louishotelmanagement/css/khuyen-mai-screen.css").toExternalForm());

        VBox vbox1 = new VBox(32.0);
        vbox1.setAlignment(Pos.TOP_CENTER);
        vbox1.setPrefHeight(800.0);
        vbox1.setPrefWidth(950.0);

        GridPane gridpane1 = new GridPane();
        ColumnConstraints colConst1 = new ColumnConstraints();
        colConst1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints colConst2 = new ColumnConstraints();
        colConst2.setHgrow(Priority.SOMETIMES);
        gridpane1.getColumnConstraints().addAll(colConst1, colConst2);
        RowConstraints rowConst1 = new RowConstraints();
        rowConst1.setVgrow(Priority.SOMETIMES);
        gridpane1.getRowConstraints().addAll(rowConst1);

        HBox hbox1 = new HBox(16.0);
        VBox vbox2 = new VBox();
        Label label1 = new Label("Chọn mã giảm giá");
        label1.getStyleClass().addAll("main-header");
        vbox2.getChildren().addAll(label1);
        HBox.setHgrow(vbox2, Priority.ALWAYS);
        hbox1.getChildren().addAll(vbox2);

        HBox hbox2 = new HBox(5.0);
        hbox2.setAlignment(Pos.CENTER_RIGHT);
        GridPane.setColumnIndex(hbox2, 1);
        gridpane1.getChildren().addAll(hbox1, hbox2);

        HBox hbox3 = new HBox(15.0);
        hbox3.setAlignment(Pos.CENTER_LEFT);
        txtTimKiem = new TextField();
        txtTimKiem.setMaxWidth(Double.MAX_VALUE);
        txtTimKiem.setPrefHeight(41.0);
        txtTimKiem.setPrefWidth(200.0);
        txtTimKiem.setPromptText("Nhập mã giảm giá...");
        txtTimKiem.getStyleClass().addAll("form-field", "search-field");
        txtTimKiem.setOnAction(e -> this.handleTimKiem(e));
        HBox.setHgrow(txtTimKiem, Priority.ALWAYS);

        cbKieuGiamGia = new ComboBox();
        cbKieuGiamGia.setMaxWidth(Double.MAX_VALUE);
        cbKieuGiamGia.setPrefHeight(41.0);
        cbKieuGiamGia.setPrefWidth(150.0);
        cbKieuGiamGia.getStyleClass().addAll("hover-effect", "filter-combo");
        cbKieuGiamGia.setOnAction(e -> this.handleLocKieuGiamGia((ActionEvent) e));
        HBox.setHgrow(cbKieuGiamGia, Priority.ALWAYS);

        cbTrangThai = new ComboBox();
        cbTrangThai.setMaxWidth(Double.MAX_VALUE);
        cbTrangThai.setPrefHeight(41.0);
        cbTrangThai.setPrefWidth(150.0);
        cbTrangThai.getStyleClass().addAll("hover-effect", "filter-combo");
        cbTrangThai.setOnAction(e -> this.handleLocTrangThai((ActionEvent) e));
        HBox.setHgrow(cbTrangThai, Priority.ALWAYS);

        btnLamMoi = new Button("🔄 Làm Mới");
        btnLamMoi.setMaxWidth(Double.MAX_VALUE);
        btnLamMoi.setMnemonicParsing(false);
        btnLamMoi.setPrefHeight(37.0);
        btnLamMoi.setPrefWidth(150.0);
        btnLamMoi.getStyleClass().addAll("btn-lam-moi");
        btnLamMoi.setOnAction(e -> this.handleLamMoi(e));
        HBox.setHgrow(btnLamMoi, Priority.ALWAYS);
        hbox3.getChildren().addAll(txtTimKiem, cbKieuGiamGia, cbTrangThai, btnLamMoi);

        VBox vbox3 = new VBox();
        vbox3.getStyleClass().addAll("table-container");
        tableViewKhuyenMai = new TableView();
        tableViewKhuyenMai.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Khởi tạo các cột (Giữ nguyên thông số width)
        colMaKM = new TableColumn("Mã Khuyến Mãi");
        colMaKM.setPrefWidth(98.0);
        colMaKM.setMinWidth(80.0);
        colCode = new TableColumn("Code");
        colCode.setPrefWidth(73.60001373291016);
        colGiamGia = new TableColumn("Giá Trị");
        colGiamGia.setPrefWidth(118.39996337890625);
        colKieuGiamGia = new TableColumn("Kiểu");
        colKieuGiamGia.setPrefWidth(98.39996337890625);
        colNgayBatDau = new TableColumn("Ngày Bắt Đầu");
        colNgayBatDau.setPrefWidth(128.79998779296875);
        colNgayKetThuc = new TableColumn("Ngày Kết Thúc");
        colNgayKetThuc.setPrefWidth(107.0);
        colTongTienToiThieu = new TableColumn("Tổng Tiền Tối Thiểu");
        colTongTienToiThieu.setPrefWidth(108.0);
        colTrangThai = new TableColumn("Trạng Thái");
        colTrangThai.setPrefWidth(114.40008544921875);
        colMoTa = new TableColumn("Mô Tả");
        colMoTa.setPrefWidth(243.20001220703125);

        tableViewKhuyenMai.getColumns().addAll(colMaKM, colCode, colGiamGia, colKieuGiamGia, colNgayBatDau, colNgayKetThuc, colTongTienToiThieu, colTrangThai, colMoTa);
        tableViewKhuyenMai.setPadding(new Insets(10.0));
        VBox.setVgrow(tableViewKhuyenMai, Priority.ALWAYS);
        vbox3.getChildren().addAll(tableViewKhuyenMai);
        VBox.setVgrow(vbox3, Priority.ALWAYS);
        vbox1.getChildren().addAll(gridpane1, hbox3, vbox3);
        rootPane.setCenter(vbox1);

        // PHẦN SỬA LỖI MARGIN Ở HBOX DƯỚI CÙNG
        HBox hbox4 = new HBox(10.0);
        hbox4.setAlignment(Pos.CENTER_RIGHT);
        hbox4.setPrefHeight(45.0);

        btnChon = new Button("Chọn"); // Đổi tên biến
        btnChon.setPrefHeight(46.0);
        btnChon.setPrefWidth(80.0);
        HBox.setMargin(btnChon, new Insets(0, 20.0, 0, 0));

        btnHuy = new Button("Hủy"); // Đổi tên biến
        btnHuy.setPrefHeight(46.0);
        btnHuy.setPrefWidth(80.0);

        hbox4.getChildren().addAll(btnChon, btnHuy);

        rootPane.setBottom(hbox4);
        // Giữ nguyên Padding cho rootPane
        rootPane.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));
        this.root = rootPane;
    }

    public Parent getRoot() {
        return root;
    }
    public BorderPane getRootPane() {
        return rootPane;
    }
    public TextField getTxtTimKiem() {
        return txtTimKiem;
    }
    public ComboBox getCbKieuGiamGia() {
        return cbKieuGiamGia;
    }
    public ComboBox getCbTrangThai() {
        return cbTrangThai;
    }
    public Button getBtnLamMoi() {
        return btnLamMoi;
    }
    public TableView getTableViewKhuyenMai() {
        return tableViewKhuyenMai;
    }
    public TableColumn getColMaKM() {
        return colMaKM;
    }
    public TableColumn getColCode() {
        return colCode;
    }
    public TableColumn getColGiamGia() {
        return colGiamGia;
    }
    public TableColumn getColKieuGiamGia() {
        return colKieuGiamGia;
    }
    public TableColumn getColNgayBatDau() {
        return colNgayBatDau;
    }
    public TableColumn getColNgayKetThuc() {
        return colNgayKetThuc;
    }
    public TableColumn getColTongTienToiThieu() {
        return colTongTienToiThieu;
    }
    public TableColumn getColTrangThai() {
        return colTrangThai;
    }
    public TableColumn getColMoTa() {
        return colMoTa;
    }
    public Button getBtnChon() { return btnChon; }
    public Button getBtnHuy() { return btnHuy; }
    private void handleTimKiem(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleLocKieuGiamGia(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleLocTrangThai(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleLamMoi(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleChon(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleHuy(ActionEvent e) {
        // TODO: implement handler
    }
}
