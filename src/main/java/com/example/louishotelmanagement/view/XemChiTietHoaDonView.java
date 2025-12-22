package com.example.louishotelmanagement.view;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
public class XemChiTietHoaDonView {
    private Label lblMaHD;
    private Label lblNgayLap;
    private Label lblNgayCheckout;
    private Label lblTenKH;
    private Label lblPhuongThuc;
    private Label lblTrangThai;
    private Label lblGiamGiaMa;
    private Label lblGiamGiaHang;
    private Label lblVAT;
    private Label lblPhatNhanTre;
    private Label lblPhatTraSom;
    private Label lblPhatTraTre;
    private Label lblTongTien;
    private ComboBox cbFilter;
    private TableView tableChiTiet;
    private TableColumn colTenDV;
    private TableColumn colSoLuong;
    private TableColumn colDonGia;
    private TableColumn colThanhTien;
    private Button btnClose;
    private Parent root;

    public XemChiTietHoaDonView() {
        AnchorPane anchorpane1 = new AnchorPane();
        anchorpane1.setPrefWidth(900);
        anchorpane1.setPrefHeight(650);
        anchorpane1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/xem-chi-tiet-hoa-don.css").toExternalForm());

        VBox vbox1 = new VBox(15);
        vbox1.setPrefWidth(900);
        vbox1.getStyleClass().addAll("main-container");

        GridPane gridpane1 = new GridPane();
        gridpane1.setHgap(20);
        gridpane1.setVgap(10);
        ColumnConstraints colConst1 = new ColumnConstraints();
        colConst1.setPercentWidth(20);
        ColumnConstraints colConst2 = new ColumnConstraints();
        colConst2.setPercentWidth(30);
        ColumnConstraints colConst3 = new ColumnConstraints();
        colConst3.setPercentWidth(20);
        ColumnConstraints colConst4 = new ColumnConstraints();
        colConst4.setPercentWidth(30);
        gridpane1.getColumnConstraints().addAll(colConst1, colConst2, colConst3, colConst4);
        Label label1 = new Label("Mã hóa đơn:");
        label1.getStyleClass().addAll("bold-label");
        GridPane.setColumnIndex(label1, 0);
        GridPane.setRowIndex(label1, 0);
        lblMaHD = new Label();
        GridPane.setColumnIndex(lblMaHD, 1);
        GridPane.setRowIndex(lblMaHD, 0);
        Label label2 = new Label("Ngày lập:");
        GridPane.setColumnIndex(label2, 0);
        GridPane.setRowIndex(label2, 1);
        lblNgayLap = new Label();
        GridPane.setColumnIndex(lblNgayLap, 1);
        GridPane.setRowIndex(lblNgayLap, 1);
        Label label3 = new Label("Ngày checkout:");
        GridPane.setColumnIndex(label3, 0);
        GridPane.setRowIndex(label3, 2);
        lblNgayCheckout = new Label();
        GridPane.setColumnIndex(lblNgayCheckout, 1);
        GridPane.setRowIndex(lblNgayCheckout, 2);
        Label label4 = new Label("Khách hàng:");
        label4.getStyleClass().addAll("bold-label");
        GridPane.setColumnIndex(label4, 2);
        GridPane.setRowIndex(label4, 0);
        lblTenKH = new Label();
        GridPane.setColumnIndex(lblTenKH, 3);
        GridPane.setRowIndex(lblTenKH, 0);
        Label label5 = new Label("Phương thức:");
        GridPane.setColumnIndex(label5, 2);
        GridPane.setRowIndex(label5, 1);
        lblPhuongThuc = new Label();
        GridPane.setColumnIndex(lblPhuongThuc, 3);
        GridPane.setRowIndex(lblPhuongThuc, 1);
        Label label6 = new Label("Trạng thái:");
        GridPane.setColumnIndex(label6, 2);
        GridPane.setRowIndex(label6, 2);
        lblTrangThai = new Label();
        lblTrangThai.getStyleClass().addAll("blue-text");
        GridPane.setColumnIndex(lblTrangThai, 3);
        GridPane.setRowIndex(lblTrangThai, 2);
        gridpane1.getChildren().addAll(label1, lblMaHD, label2, lblNgayLap, label3, lblNgayCheckout, label4, lblTenKH, label5, lblPhuongThuc, label6, lblTrangThai);
        Separator separator1 = new Separator();

        GridPane gridpane2 = new GridPane();
        gridpane2.setHgap(20);
        gridpane2.setVgap(8);
        ColumnConstraints colConst5 = new ColumnConstraints();
        colConst5.setPercentWidth(20);
        ColumnConstraints colConst6 = new ColumnConstraints();
        colConst6.setPercentWidth(30);
        ColumnConstraints colConst7 = new ColumnConstraints();
        colConst7.setPercentWidth(20);
        ColumnConstraints colConst8 = new ColumnConstraints();
        colConst8.setPercentWidth(30);
        gridpane2.getColumnConstraints().addAll(colConst5, colConst6, colConst7, colConst8);
        Label label7 = new Label("Giảm giá Voucher:");
        label7.getStyleClass().addAll("green-text");
        GridPane.setColumnIndex(label7, 0);
        GridPane.setRowIndex(label7, 0);
        lblGiamGiaMa = new Label("0 ₫");
        GridPane.setColumnIndex(lblGiamGiaMa, 1);
        GridPane.setRowIndex(lblGiamGiaMa, 0);
        Label label8 = new Label("Giảm giá hạng KH:");
        label8.getStyleClass().addAll("green-text");
        GridPane.setColumnIndex(label8, 0);
        GridPane.setRowIndex(label8, 1);
        lblGiamGiaHang = new Label("0 ₫");
        GridPane.setColumnIndex(lblGiamGiaHang, 1);
        GridPane.setRowIndex(lblGiamGiaHang, 1);
        Label label9 = new Label("Thuế VAT (10%):");
        GridPane.setColumnIndex(label9, 0);
        GridPane.setRowIndex(label9, 2);
        lblVAT = new Label("0 ₫");
        GridPane.setColumnIndex(lblVAT, 1);
        GridPane.setRowIndex(lblVAT, 2);
        Label label10 = new Label("Phạt nhận trễ:");
        label10.getStyleClass().addAll("red-text");
        GridPane.setColumnIndex(label10, 2);
        GridPane.setRowIndex(label10, 0);
        lblPhatNhanTre = new Label("0 ₫");
        GridPane.setColumnIndex(lblPhatNhanTre, 3);
        GridPane.setRowIndex(lblPhatNhanTre, 0);
        Label label11 = new Label("Phạt trả sớm:");
        label11.getStyleClass().addAll("red-text");
        GridPane.setColumnIndex(label11, 2);
        GridPane.setRowIndex(label11, 1);
        lblPhatTraSom = new Label("0 ₫");
        GridPane.setColumnIndex(lblPhatTraSom, 3);
        GridPane.setRowIndex(lblPhatTraSom, 1);
        Label label12 = new Label("Phạt trả trễ:");
        label12.getStyleClass().addAll("red-text");
        GridPane.setColumnIndex(label12, 2);
        GridPane.setRowIndex(label12, 2);
        lblPhatTraTre = new Label("0 ₫");
        GridPane.setColumnIndex(lblPhatTraTre, 3);
        GridPane.setRowIndex(lblPhatTraTre, 2);
        Label label13 = new Label("TỔNG THANH TOÁN:");
        label13.getStyleClass().addAll("total-label");
        GridPane.setColumnIndex(label13, 2);
        GridPane.setRowIndex(label13, 3);
        lblTongTien = new Label();
        lblTongTien.getStyleClass().addAll("total-value");
        GridPane.setColumnIndex(lblTongTien, 3);
        GridPane.setRowIndex(lblTongTien, 3);
        gridpane2.getChildren().addAll(label7, lblGiamGiaMa, label8, lblGiamGiaHang, label9, lblVAT, label10, lblPhatNhanTre, label11, lblPhatTraSom, label12, lblPhatTraTre, label13, lblTongTien);

        HBox hbox1 = new HBox(10);
        hbox1.setAlignment(Pos.CENTER_LEFT);
        Label label14 = new Label("Lọc danh sách:");
        label14.getStyleClass().addAll("filter-label");
        cbFilter = new ComboBox();
        cbFilter.setPrefWidth(200);
        hbox1.getChildren().addAll(label14, cbFilter);
        tableChiTiet = new TableView();
        colTenDV = new TableColumn();
        colTenDV.setText("Tên dịch vụ / Phòng");
        colTenDV.setPrefWidth(350);
        colSoLuong = new TableColumn();
        colSoLuong.setText("Số lượng");
        colSoLuong.setPrefWidth(120);
        colDonGia = new TableColumn();
        colDonGia.setText("Đơn giá");
        colDonGia.setPrefWidth(180);
        colThanhTien = new TableColumn();
        colThanhTien.setText("Thành tiền");
        colThanhTien.setPrefWidth(200);
        tableChiTiet.getColumns().addAll(colTenDV, colSoLuong, colDonGia, colThanhTien);
        VBox.setVgrow(tableChiTiet, Priority.ALWAYS);

        HBox hbox2 = new HBox();
        hbox2.setAlignment(Pos.CENTER_RIGHT);
        btnClose = new Button("Đóng cửa sổ");
        btnClose.setPrefWidth(120);
        btnClose.getStyleClass().addAll("close-button");
        hbox2.getChildren().addAll(btnClose);
        vbox1.getChildren().addAll(gridpane1, separator1, gridpane2, hbox1, tableChiTiet, hbox2);
        anchorpane1.getChildren().addAll(vbox1);
        this.root = anchorpane1;
    }

    public Parent getRoot() {
        return root;
    }
    public Label getLblMaHD() {
        return lblMaHD;
    }
    public Label getLblNgayLap() {
        return lblNgayLap;
    }
    public Label getLblNgayCheckout() {
        return lblNgayCheckout;
    }
    public Label getLblTenKH() {
        return lblTenKH;
    }
    public Label getLblPhuongThuc() {
        return lblPhuongThuc;
    }
    public Label getLblTrangThai() {
        return lblTrangThai;
    }
    public Label getLblGiamGiaMa() {
        return lblGiamGiaMa;
    }
    public Label getLblGiamGiaHang() {
        return lblGiamGiaHang;
    }
    public Label getLblVAT() {
        return lblVAT;
    }
    public Label getLblPhatNhanTre() {
        return lblPhatNhanTre;
    }
    public Label getLblPhatTraSom() {
        return lblPhatTraSom;
    }
    public Label getLblPhatTraTre() {
        return lblPhatTraTre;
    }
    public Label getLblTongTien() {
        return lblTongTien;
    }
    public ComboBox getCbFilter() {
        return cbFilter;
    }
    public TableView getTableChiTiet() {
        return tableChiTiet;
    }
    public TableColumn getColTenDV() {
        return colTenDV;
    }
    public TableColumn getColSoLuong() {
        return colSoLuong;
    }
    public TableColumn getColDonGia() {
        return colDonGia;
    }
    public TableColumn getColThanhTien() {
        return colThanhTien;
    }
    public Button getBtnClose() {
        return btnClose;
    }
}
