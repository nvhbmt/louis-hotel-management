package com.example.louishotelmanagement.view;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.event.ActionEvent;
public class NhanVienDialogView {
    private Label lblTitle;
    private TextField tfMaNV;
    private TextField tfHoTen;
    private DatePicker dpNgaySinh;
    private TextField tfSoDT;
    private TextField tfDiaChi;
    private ComboBox cbChucVu;
    private TextField tfTenDangNhap;
    private PasswordField pfMatKhau;
    private ComboBox cbQuyen;
    private CheckBox ckTrangThai;
    private Button btnLuu;
    private Button btnHuy;
    private Parent root;

    public NhanVienDialogView() {
        VBox vbox1 = new VBox(15.0);
        vbox1.setPrefWidth(500.0);
        vbox1.getStyleClass().addAll("dialog-container");
        vbox1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/nhan-vien-dialog.css").toExternalForm());
        lblTitle = new Label("Quản Lý Nhân Viên & Tài Khoản");
        lblTitle.getStyleClass().addAll("dialog-title");

        TabPane tabpane1 = new TabPane();
        tabpane1.setPrefHeight(350.0);
        tabpane1.getStyleClass().addAll("tab-pane");
        Tab tab1 = new Tab();
        tab1.setText("Thông tin cá nhân");

        VBox vbox2 = new VBox(10.0);
        vbox2.getStyleClass().addAll("tab-content");

        GridPane gridpane1 = new GridPane();
        gridpane1.setHgap(10.0);
        gridpane1.setVgap(12.0);
        ColumnConstraints colConst1 = new ColumnConstraints();
        ColumnConstraints colConst2 = new ColumnConstraints();
        colConst2.setHgrow(Priority.ALWAYS);
        gridpane1.getColumnConstraints().addAll(colConst1, colConst2);
        Label label1 = new Label("Mã nhân viên:");
        tfMaNV = new TextField();
        GridPane.setColumnIndex(tfMaNV, 1);
        Label label2 = new Label("Họ tên:");
        GridPane.setRowIndex(label2, 1);
        tfHoTen = new TextField();
        GridPane.setColumnIndex(tfHoTen, 1);
        GridPane.setRowIndex(tfHoTen, 1);
        Label label3 = new Label("Ngày sinh:");
        GridPane.setRowIndex(label3, 2);
        dpNgaySinh = new DatePicker();
        dpNgaySinh.setMaxWidth(Double.MAX_VALUE);
        GridPane.setColumnIndex(dpNgaySinh, 1);
        GridPane.setRowIndex(dpNgaySinh, 2);
        Label label4 = new Label("Số điện thoại:");
        GridPane.setRowIndex(label4, 3);
        tfSoDT = new TextField();
        GridPane.setColumnIndex(tfSoDT, 1);
        GridPane.setRowIndex(tfSoDT, 3);
        Label label5 = new Label("Địa chỉ:");
        GridPane.setRowIndex(label5, 4);
        tfDiaChi = new TextField();
        GridPane.setColumnIndex(tfDiaChi, 1);
        GridPane.setRowIndex(tfDiaChi, 4);
        Label label6 = new Label("Chức vụ:");
        GridPane.setRowIndex(label6, 5);
        cbChucVu = new ComboBox();
        cbChucVu.setMaxWidth(Double.MAX_VALUE);
        GridPane.setColumnIndex(cbChucVu, 1);
        GridPane.setRowIndex(cbChucVu, 5);
        gridpane1.getChildren().addAll(label1, tfMaNV, label2, tfHoTen, label3, dpNgaySinh, label4, tfSoDT, label5, tfDiaChi, label6, cbChucVu);
        vbox2.getChildren().addAll(gridpane1);
        tab1.setContent(vbox2);
        Tab tab2 = new Tab();
        tab2.setText("Tài khoản hệ thống");

        VBox vbox3 = new VBox(10.0);
        vbox3.getStyleClass().addAll("tab-content");

        GridPane gridpane2 = new GridPane();
        gridpane2.setHgap(10.0);
        gridpane2.setVgap(12.0);
        ColumnConstraints colConst3 = new ColumnConstraints();
        ColumnConstraints colConst4 = new ColumnConstraints();
        colConst4.setHgrow(Priority.ALWAYS);
        gridpane2.getColumnConstraints().addAll(colConst3, colConst4);
        Label label7 = new Label("Tên đăng nhập:");
        tfTenDangNhap = new TextField();
        GridPane.setColumnIndex(tfTenDangNhap, 1);
        Label label8 = new Label("Mật khẩu:");
        GridPane.setRowIndex(label8, 1);
        pfMatKhau = new PasswordField();
        GridPane.setColumnIndex(pfMatKhau, 1);
        GridPane.setRowIndex(pfMatKhau, 1);
        Label label9 = new Label("Quyền hạn:");
        GridPane.setRowIndex(label9, 2);
        cbQuyen = new ComboBox();
        cbQuyen.setMaxWidth(Double.MAX_VALUE);
        GridPane.setColumnIndex(cbQuyen, 1);
        GridPane.setRowIndex(cbQuyen, 2);
        Label label10 = new Label("Trạng thái:");
        GridPane.setRowIndex(label10, 3);
        ckTrangThai = new CheckBox();
        GridPane.setColumnIndex(ckTrangThai, 1);
        GridPane.setRowIndex(ckTrangThai, 3);
        gridpane2.getChildren().addAll(label7, tfTenDangNhap, label8, pfMatKhau, label9, cbQuyen, label10, ckTrangThai);
        Label label11 = new Label("* Nếu sửa: Để trống mật khẩu nếu muốn giữ nguyên mật khẩu cũ.");
        label11.getStyleClass().addAll("note-label");
        vbox3.getChildren().addAll(gridpane2, label11);
        tab2.setContent(vbox3);
        tabpane1.getTabs().addAll(tab1, tab2);

        HBox hbox1 = new HBox(10.0);
        hbox1.setAlignment(Pos.CENTER_RIGHT);
        btnLuu = new Button("Lưu dữ liệu");
        btnLuu.setPrefWidth(100.0);
        btnLuu.getStyleClass().addAll("save-button");
        btnLuu.setOnAction(e -> this.handleLuu(e));
        btnHuy = new Button("Hủy bỏ");
        btnHuy.setPrefWidth(80.0);
        btnHuy.setOnAction(e -> this.handleHuy(e));
        hbox1.getChildren().addAll(btnLuu, btnHuy);
        vbox1.getChildren().addAll(lblTitle, tabpane1, hbox1);
        this.root = vbox1;
    }

    public Parent getRoot() {
        return root;
    }
    public Label getLblTitle() {
        return lblTitle;
    }
    public TextField getTfMaNV() {
        return tfMaNV;
    }
    public TextField getTfHoTen() {
        return tfHoTen;
    }
    public DatePicker getDpNgaySinh() {
        return dpNgaySinh;
    }
    public TextField getTfSoDT() {
        return tfSoDT;
    }
    public TextField getTfDiaChi() {
        return tfDiaChi;
    }
    public ComboBox getCbChucVu() {
        return cbChucVu;
    }
    public TextField getTfTenDangNhap() {
        return tfTenDangNhap;
    }
    public PasswordField getPfMatKhau() {
        return pfMatKhau;
    }
    public ComboBox getCbQuyen() {
        return cbQuyen;
    }
    public CheckBox getCkTrangThai() {
        return ckTrangThai;
    }
    public Button getBtnLuu() {
        return btnLuu;
    }
    public Button getBtnHuy() {
        return btnHuy;
    }
    private void handleLuu(ActionEvent e) {
        // TODO: implement handler
    }
    private void handleHuy(ActionEvent e) {
        // TODO: implement handler
    }
}
