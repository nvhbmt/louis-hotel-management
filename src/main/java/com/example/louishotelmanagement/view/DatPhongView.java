package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class DatPhongView {
    private ComboBox<String> dsKhachHang;
    private TextField maNhanVien;
    private DatePicker ngayDen;
    private Label lbLoaiDatPhong;
    private DatePicker ngayDi;
    private Label lbSoDem;
    private Label lbLoadingPhong;
    private Label lbSoPhongTrong;
    private ComboBox<Integer> cbTang;
    private ComboBox<LoaiPhong> cbLocLoaiPhong;
    private TableView<Phong> tablePhong;
    private TableColumn<Phong, String> colMaPhong;
    private TableColumn<Phong, String> colTenLoaiPhong;
    private TableColumn<Phong, Integer> colTang;
    private TableColumn<Phong, Double> colDonGia;
    private TableColumn<Phong, TrangThaiPhong> colTrangThai;
    private TableColumn<Phong, String> colMoTa;
    private TableColumn<Phong, Void> colThaoTac;
    private Label SoPhongDaChon;
    private Label lbTongSoDem;
    private Label TongTien;
    private Button btnDatPhong;
    private Button btnThemKhachHang;
    private Button btnLamMoi;
    private Parent root;

    public DatPhongView() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1014.4, 864.0);
        borderPane.setPadding(new Insets(32.0));
        borderPane.getStyleClass().add("main-background");
        borderPane.getStylesheets().add(getClass().getResource("/com/example/louishotelmanagement/css/dat-phong.css").toExternalForm());

        // --- TOP SECTION (HEADER) ---
        HBox topBox = new HBox(16.0);
        ImageView headerIcon = new ImageView(new Image(getClass().getResource("/com/example/louishotelmanagement/image/key-chain.png").toExternalForm()));
        headerIcon.setFitHeight(72.0);
        headerIcon.setFitWidth(72.0);
        headerIcon.setPreserveRatio(true);

        VBox titleBox = new VBox();
        Label lblHeaderTitle = new Label("Đặt phòng");
        lblHeaderTitle.getStyleClass().add("header-title");
        Label lblHeaderSub = new Label("Thực hiện yêu cầu đặt phòng trước cho khách hàng");
        lblHeaderSub.getStyleClass().add("header-subtitle");
        lblHeaderSub.setFont(new Font(14.0));
        titleBox.getChildren().addAll(lblHeaderTitle, lblHeaderSub);
        topBox.getChildren().addAll(headerIcon, titleBox);
        borderPane.setTop(topBox);

        // --- CENTER SECTION ---
        VBox centerVBox = new VBox();
        centerVBox.setAlignment(Pos.TOP_CENTER);
        centerVBox.setPrefSize(100.0, 200.0);

        // Section Title
        VBox sectionTitleBox = new VBox();
        sectionTitleBox.setPrefSize(1014.0, 50.0);
        sectionTitleBox.getStyleClass().add("section-border");
        VBox.setMargin(sectionTitleBox, new Insets(0, 0, 10.0, 0));
        Label lblSection = new Label("Thông tin đặt phòng");
        lblSection.getStyleClass().add("section-title-custom");
        lblSection.setPrefSize(244.0, 30.0);
        VBox.setMargin(lblSection, new Insets(10.0, 0, 0, 30.0));
        sectionTitleBox.getChildren().add(lblSection);

        // Form Inputs
        VBox formVBox = new VBox(15.0);
        formVBox.setAlignment(Pos.CENTER);
        formVBox.setPrefSize(1014.0, 210.0);
        VBox.setMargin(formVBox, new Insets(0, 0, 20.0, 0));

        // Rows
        dsKhachHang = new ComboBox<>();
        dsKhachHang.setPrefSize(480.0, 30.0);
        dsKhachHang.setPromptText("Chọn hoặc tìm khách hàng");
        btnThemKhachHang = new Button("Thêm khách hàng");
        btnThemKhachHang.getStyleClass().add("add-customer-button");
        HBox row1 = createInputRow("Khách hàng", "/com/example/louishotelmanagement/image/detective.png", dsKhachHang);
        row1.getChildren().add(btnThemKhachHang);
        HBox.setMargin(btnThemKhachHang, new Insets(0, 0, 0, 20.0));

        maNhanVien = new TextField();
        maNhanVien.setEditable(false);
        maNhanVien.setPrefSize(609.0, 30.0);
        maNhanVien.setPromptText("Tên nhân viên đang đăng nhập");
        maNhanVien.getStyleClass().add("employee-field");
        HBox row2 = createInputRow("Nhân viên", "/com/example/louishotelmanagement/image/steward.png", maNhanVien);

        ngayDen = new DatePicker();
        ngayDen.setPrefSize(250.0, 30.0);
        ngayDen.setPromptText("Chọn ngày nhận phòng");
        lbLoaiDatPhong = new Label("");
        lbLoaiDatPhong.getStyleClass().add("info-text");
        lbLoaiDatPhong.setPrefWidth(200.0);
        lbLoaiDatPhong.setVisible(false);
        HBox row3 = createInputRow("Ngày đến", "/com/example/louishotelmanagement/image/calendar.png", ngayDen);
        row3.getChildren().add(lbLoaiDatPhong);
        HBox.setMargin(lbLoaiDatPhong, new Insets(0, 0, 0, 20.0));

        ngayDi = new DatePicker();
        ngayDi.setPrefSize(250.0, 30.0);
        ngayDi.setPromptText("Chọn ngày trả phòng");
        lbSoDem = new Label("");
        lbSoDem.getStyleClass().add("secondary-text");
        lbSoDem.setPrefWidth(200.0);
        HBox row4 = createInputRow("Ngày đi dự kiến", "/com/example/louishotelmanagement/image/calendar.png", ngayDi);
        row4.getChildren().add(lbSoDem);
        HBox.setMargin(lbSoDem, new Insets(0, 0, 0, 20.0));

        // Loading and Status
        HBox rowStatus = new HBox();
        rowStatus.setAlignment(Pos.CENTER);
        rowStatus.setPrefSize(900.0, 40.0);
        lbLoadingPhong = new Label("");
        lbLoadingPhong.getStyleClass().add("info-text");
        lbLoadingPhong.setPrefWidth(300.0);
        lbLoadingPhong.setVisible(false);
        lbSoPhongTrong = new Label("");
        lbSoPhongTrong.getStyleClass().add("success-text");
        lbSoPhongTrong.setPrefWidth(300.0);
        rowStatus.getChildren().addAll(lbLoadingPhong, lbSoPhongTrong);

        // Filters
        HBox rowFilter = new HBox();
        rowFilter.setAlignment(Pos.CENTER);
        cbTang = new ComboBox<>();
        cbTang.setPromptText("Chọn tầng");
        cbTang.setPrefWidth(150.0);
        HBox.setMargin(cbTang, new Insets(0, 20.0, 0, 0));
        cbLocLoaiPhong = new ComboBox<>();
        cbLocLoaiPhong.setPromptText("Chọn loại phòng");
        cbLocLoaiPhong.setPrefSize(173.0, 26.0);
        HBox.setMargin(cbLocLoaiPhong, new Insets(0, 20.0, 0, 0));
        btnLamMoi = new Button("Làm mới");
        btnLamMoi.getStyleClass().add("refresh-button");
        btnLamMoi.setPrefSize(150.0, 35.0);
        rowFilter.getChildren().addAll(cbTang, cbLocLoaiPhong, btnLamMoi);

        formVBox.getChildren().addAll(row1, row2, row3, row4, rowStatus, rowFilter);

        // Table
        tablePhong = new TableView<>();
        tablePhong.setPrefSize(1014.0, 431.0);
        VBox.setVgrow(tablePhong, Priority.ALWAYS);
        tablePhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colMaPhong = new TableColumn<>("Mã phòng"); colMaPhong.setPrefWidth(75.0);
        colTenLoaiPhong = new TableColumn<>("Loại phòng"); colTenLoaiPhong.setPrefWidth(120.0);
        colTang = new TableColumn<>("Tầng"); colTang.setPrefWidth(50.0);
        colDonGia = new TableColumn<>("Đơn giá/đêm"); colDonGia.setPrefWidth(100.0);
        colTrangThai = new TableColumn<>("Trạng thái"); colTrangThai.setPrefWidth(100.0);
        colMoTa = new TableColumn<>("Mô tả"); colMoTa.setPrefWidth(280.0);
        colThaoTac = new TableColumn<>("Thao tác"); colThaoTac.setPrefWidth(90.0);
        tablePhong.getColumns().addAll(colMaPhong, colTenLoaiPhong, colTang, colDonGia, colTrangThai, colMoTa, colThaoTac);

        centerVBox.getChildren().addAll(sectionTitleBox, formVBox, tablePhong);
        borderPane.setCenter(centerVBox);

        // --- BOTTOM SECTION (SUMMARY) ---
        VBox bottomVBox = new VBox();
        bottomVBox.setPrefSize(1014.0, 150.0);

        VBox summaryBox = new VBox();
        summaryBox.setPrefSize(1014.0, 90.0);
        summaryBox.getStyleClass().add("summary-border");
        summaryBox.setPadding(new Insets(0, 0, 10.0, 0));

        HBox summaryRow1 = new HBox(20.0);
        summaryRow1.setAlignment(Pos.CENTER_LEFT);
        summaryRow1.setPrefSize(1014.0, 40.0);
        VBox.setMargin(summaryRow1, new Insets(10.0, 0, 0, 0));
        Label lblSelected = new Label("Số phòng đã chọn:");
        HBox.setMargin(lblSelected, new Insets(0, 0, 0, 20.0));
        SoPhongDaChon = new Label("0");
        SoPhongDaChon.getStyleClass().add("room-count");
        SoPhongDaChon.setPrefWidth(50.0);
        HBox.setMargin(SoPhongDaChon, new Insets(0, 40.0, 0, 0));
        Label lblNights = new Label("Số đêm:");
        lbTongSoDem = new Label("0");
        lbTongSoDem.getStyleClass().add("night-count");
        lbTongSoDem.setPrefWidth(50.0);
        HBox.setMargin(lbTongSoDem, new Insets(0, 40.0, 0, 0));
        summaryRow1.getChildren().addAll(lblSelected, SoPhongDaChon, lblNights, lbTongSoDem);

        HBox summaryRow2 = new HBox(20.0);
        summaryRow2.setAlignment(Pos.CENTER_LEFT);
        summaryRow2.setPrefSize(1014.0, 40.0);
        Label lblTotal = new Label("TỔNG TIỀN DỰ KIẾN (chưa bao gồm dịch vụ): ");
        HBox.setMargin(lblTotal, new Insets(0, 0, 0, 20.0));
        TongTien = new Label("0 VNĐ");
        TongTien.getStyleClass().add("total-amount");
        TongTien.setPrefWidth(200.0);
        summaryRow2.getChildren().addAll(lblTotal, TongTien);
        summaryBox.getChildren().addAll(summaryRow1, summaryRow2);

        HBox confirmBox = new HBox();
        confirmBox.setAlignment(Pos.CENTER);
        confirmBox.setPrefSize(1014.0, 50.0);
        VBox.setMargin(confirmBox, new Insets(0, 0, 10.0, 0));
        btnDatPhong = new Button("Xác nhận đặt");
        btnDatPhong.getStyleClass().add("confirm-button");
        btnDatPhong.setPrefSize(150.0, 35.0);
        confirmBox.getChildren().add(btnDatPhong);

        bottomVBox.getChildren().addAll(summaryBox, confirmBox);
        borderPane.setBottom(bottomVBox);

        this.root = borderPane;
    }

    private HBox createInputRow(String labelText, String iconPath, Control input) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefSize(900.0, 35.0);
        Label lbl = new Label(labelText);
        lbl.setPrefWidth(120.0);
        lbl.setFont(new Font(15.0));
        HBox.setMargin(lbl, new Insets(0, 0, 0, 70.0));

        ImageView icon = new ImageView(new Image(getClass().getResource(iconPath).toExternalForm()));
        icon.setFitHeight(25.0);
        icon.setFitWidth(25.0);
        icon.setPreserveRatio(true);
        HBox.setMargin(icon, new Insets(0, 10.0, 0, 10.0));

        hbox.getChildren().addAll(lbl, icon, input);
        return hbox;
    }

    // Getters
    public Parent getRoot() { return root; }
    public ComboBox<String> getDsKhachHang() { return dsKhachHang; }
    public TextField getMaNhanVien() { return maNhanVien; }
    public DatePicker getNgayDen() { return ngayDen; }
    public Label getLbLoaiDatPhong() { return lbLoaiDatPhong; }
    public DatePicker getNgayDi() { return ngayDi; }
    public Label getLbSoDem() { return lbSoDem; }
    public Label getLbLoadingPhong() { return lbLoadingPhong; }
    public Label getLbSoPhongTrong() { return lbSoPhongTrong; }
    public ComboBox<Integer> getCbTang() { return cbTang; }
    public ComboBox<LoaiPhong> getCbLocLoaiPhong() { return cbLocLoaiPhong; }
    public TableView<Phong> getTablePhong() { return tablePhong; }
    public TableColumn<Phong, String> getColMaPhong() { return colMaPhong; }
    public TableColumn<Phong, String> getColTenLoaiPhong() { return colTenLoaiPhong; }
    public TableColumn<Phong, Integer> getColTang() { return colTang; }
    public TableColumn<Phong, Double> getColDonGia() { return colDonGia; }
    public TableColumn<Phong, TrangThaiPhong> getColTrangThai() { return colTrangThai; }
    public TableColumn<Phong, String> getColMoTa() { return colMoTa; }
    public TableColumn<Phong, Void> getColThaoTac() { return colThaoTac; }
    public Label getSoPhongDaChon() { return SoPhongDaChon; }
    public Label getLbTongSoDem() { return lbTongSoDem; }
    public Label getTongTien() { return TongTien; }
    public Button getBtnDatPhong() { return btnDatPhong; }
    public Button getBtnThemKhachHang() { return btnThemKhachHang; }
    public Button getBtnLamMoi() { return btnLamMoi; }
}