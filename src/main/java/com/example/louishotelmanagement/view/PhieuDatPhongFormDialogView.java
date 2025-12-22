package com.example.louishotelmanagement.view;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;

public class PhieuDatPhongFormDialogView {
    private TextField txtMaPhieu;
    private DatePicker dpNgayDen;
    private DatePicker dpNgayDi;
    private TextField txtTienCocCu;
    private TableView tblPhong;
    private TableColumn colMaPhong, colNgayDen, colNgayDi, colGiaPhong, colAction;
    private HBox boxThanhToan;
    private Label lblSoTienThuThem;
    private ComboBox cbThanhToan;

    // Đổi tên các nút chức năng
    private Button btnThanhToan;
    private Button btnHuy;
    private Button btnLuu;

    private Parent root;

    public PhieuDatPhongFormDialogView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/phieu-dat-phong-form-dialog.css").toExternalForm());

        VBox vbox1 = new VBox(8);
        vbox1.setPadding(new Insets(10, 10, 10, 10));

        Label label1 = new Label("CHỈNH SỬA PHIẾU ĐẶT PHÒNG");
        label1.getStyleClass().addAll("dialog-title");

        txtMaPhieu = new TextField();
        txtMaPhieu.setPromptText("Mã phiếu");

        dpNgayDen = new DatePicker();
        dpNgayDi = new DatePicker();

        txtTienCocCu = new TextField();
        txtTienCocCu.setPromptText("Tiền cọc");

        tblPhong = new TableView();
        colMaPhong = new TableColumn("Phòng");
        colNgayDen = new TableColumn("Ngày đến");
        colNgayDi = new TableColumn("Ngày đi");
        colGiaPhong = new TableColumn("Giá");
        colAction = new TableColumn("Thao tác");
        tblPhong.getColumns().addAll(colMaPhong, colNgayDen, colNgayDi, colGiaPhong, colAction);

        // Phần thanh toán bổ sung
        boxThanhToan = new HBox(10);
        lblSoTienThuThem = new Label();
        cbThanhToan = new ComboBox();

        btnThanhToan = new Button("Thanh toán");
        btnThanhToan.getStyleClass().add("btn-pay");

        boxThanhToan.getChildren().addAll(new Label("Thu thêm:"), lblSoTienThuThem, cbThanhToan, btnThanhToan);

        vbox1.getChildren().addAll(label1, new Label("Mã phiếu:"), txtMaPhieu,
                new Label("Ngày đến:"), dpNgayDen,
                new Label("Ngày đi:"), dpNgayDi,
                new Label("Tiền cọc cũ:"), txtTienCocCu,
                new Label("Danh sách phòng:"), tblPhong, boxThanhToan);
        borderpane1.setCenter(vbox1);

        // HBox chứa nút Lưu và Hủy ở phía dưới
        HBox hboxBottom = new HBox(10);
        hboxBottom.setAlignment(Pos.CENTER_RIGHT);
        hboxBottom.setPadding(new Insets(10));

        btnHuy = new Button("Hủy");
        btnLuu = new Button("Lưu");
        btnLuu.getStyleClass().add("btn-save");

        hboxBottom.getChildren().addAll(btnHuy, btnLuu);
        borderpane1.setBottom(hboxBottom);

        this.root = borderpane1;
    }

    // --- Getters cho các nút ---
    public Button getBtnThanhToan() { return btnThanhToan; }
    public Button getBtnHuy() { return btnHuy; }
    public Button getBtnLuu() { return btnLuu; }

    // --- Các Getters khác ---
    public Parent getRoot() { return root; }
    public TextField getTxtMaPhieu() { return txtMaPhieu; }
    public DatePicker getDpNgayDen() { return dpNgayDen; }
    public DatePicker getDpNgayDi() { return dpNgayDi; }
    public TextField getTxtTienCocCu() { return txtTienCocCu; }
    public TableView getTblPhong() { return tblPhong; }
    public TableColumn getColMaPhong() { return colMaPhong; }
    public TableColumn getColNgayDen() { return colNgayDen; }
    public TableColumn getColNgayDi() { return colNgayDi; }
    public TableColumn getColGiaPhong() { return colGiaPhong; }
    public TableColumn getColAction() { return colAction; }
    public HBox getBoxThanhToan() { return boxThanhToan; }
    public Label getLblSoTienThuThem() { return lblSoTienThuThem; }
    public ComboBox getCbThanhToan() { return cbThanhToan; }
}