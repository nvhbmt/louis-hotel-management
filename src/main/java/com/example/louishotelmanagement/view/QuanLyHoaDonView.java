package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.QuanLyHoaDonController;
import com.example.louishotelmanagement.ui.components.StatsCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class QuanLyHoaDonView {
    private static QuanLyHoaDonView instance;

    private Button btnLamMoi;
    private TextField txtTimKiem;
    private ComboBox<String> cmbNgayLap;
    private TableView tableViewKhachHang;
    private TableColumn colMaHD;
    private TableColumn colNgayLap;
    private TableColumn colPhuongThuc;
    private TableColumn colTrangThai;
    private TableColumn colTongTien;
    private TableColumn colMaKhuyenMai;
    private TableColumn colTenKH;
    private TableColumn colThaoTac;

    // StatsCard components for statistics
    private StatsCard unpaidInvoicesCard;
    private StatsCard paidInvoicesCard;
    private StatsCard dailyRevenueCard;
    private StatsCard promotionalInvoicesCard;

    private Parent root;
    public QuanLyHoaDonController quanLyThanhToanController;

    public static QuanLyHoaDonView getInstance() {
        if (instance == null) {
            instance = new QuanLyHoaDonView();
        }
        return instance;
    }

    public static String getIdentifier() {
        return "quan-ly-hoa-don-view";
    }

    public QuanLyHoaDonView() {
        // ============================================
        // MAIN CONTAINER SETUP
        // ============================================
        BorderPane mainContainer = new BorderPane();
        mainContainer.setPrefHeight(1018.0);
        mainContainer.setPrefWidth(1340.0);
        mainContainer.getStyleClass().addAll("main-background");
        mainContainer.getStylesheets().addAll(
                getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm()
        );

        VBox mainContentVBox = new VBox(32.0);
        mainContentVBox.setPrefHeight(800.0);
        mainContentVBox.setPrefWidth(1276.0);
        mainContentVBox.setAlignment(Pos.TOP_CENTER);

        // ============================================
        // HEADER SECTION
        // ============================================
        HBox headerSection = new HBox();
        headerSection.getStyleClass().addAll("header-section");

        VBox headerIconContainer = new VBox();
        headerIconContainer.getStyleClass().addAll("header-icon-container");
        ImageView headerIcon = new ImageView();
        headerIcon.getStyleClass().addAll("header-icon");
        headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/pay.png").toExternalForm()));
        headerIconContainer.getChildren().addAll(headerIcon);

        VBox headerTitleContainer = new VBox();
        headerTitleContainer.getStyleClass().addAll("header-title-section");
        Label headerTitleLabel = new Label("Quản Lý Hóa Đơn");
        headerTitleLabel.getStyleClass().addAll("header-title");
        Label headerSubtitleLabel = new Label("Quản lý các hóa đơn trong khách sạn");
        headerSubtitleLabel.getStyleClass().addAll("header-subtitle");
        headerTitleContainer.getChildren().addAll(headerTitleLabel, headerSubtitleLabel);
        HBox.setHgrow(headerTitleContainer, Priority.ALWAYS);

        headerSection.getChildren().addAll(headerIconContainer, headerTitleContainer);

        // ============================================
        // STATISTICS GRID
        // ============================================
        GridPane statsGrid = new GridPane();
        statsGrid.getStyleClass().addAll("stats-grid");

        // Configure grid constraints
        ColumnConstraints colConstraint = new ColumnConstraints();
        colConstraint.setHgrow(Priority.SOMETIMES);
        colConstraint.setPercentWidth(25.0);
        statsGrid.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint, colConstraint);

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.SOMETIMES);
        statsGrid.getRowConstraints().addAll(rowConstraint);

        // Unpaid Invoices Stat Card
        unpaidInvoicesCard = new StatsCard(
                "Chưa thanh toán", "69", "stats-card-number-danger",
                "/com/example/louishotelmanagement/image/no-money.png", "stats-card-icon-container-danger"
        );

        // Paid Invoices Stat Card
        paidInvoicesCard = new StatsCard(
                "Đã thanh toán", "15", "stats-card-number-success",
                "/com/example/louishotelmanagement/image/card.png", "stats-card-icon-container-success"
        );
        GridPane.setColumnIndex(paidInvoicesCard, 1);

        // Daily Revenue Stat Card
        dailyRevenueCard = new StatsCard(
                "Doanh thu trong ngày", "14", "stats-card-number-warning",
                "/com/example/louishotelmanagement/image/money-bag.png", "stats-card-icon-container-warning"
        );
        GridPane.setColumnIndex(dailyRevenueCard, 2);

        // Promotional Invoices Stat Card
        promotionalInvoicesCard = new StatsCard(
                "Số hóa đơn có khuyến mãi", "10", "stats-card-number-info",
                "/com/example/louishotelmanagement/image/coupons.png", "stats-card-icon-container-info"
        );
        GridPane.setColumnIndex(promotionalInvoicesCard, 3);

        statsGrid.getChildren().addAll(unpaidInvoicesCard, paidInvoicesCard, dailyRevenueCard, promotionalInvoicesCard);
        VBox.setVgrow(statsGrid, Priority.NEVER);

        // ============================================
        // FILTER SECTION
        // ============================================
        VBox filterSectionVBox = new VBox();
        filterSectionVBox.getStyleClass().addAll("spacing-filter");

        HBox filterControlsHBox = new HBox();
        filterControlsHBox.getStyleClass().addAll("filter-section");

        // Search Filter
        VBox searchFilterVBox = createFilterField("Tìm kiếm", txtTimKiem = new TextField(), "filter-input");
        txtTimKiem.setPromptText("Nhập thông tin khách hàng, phòng...");
        txtTimKiem.setOnKeyReleased(e -> this.handleTimKiem());

        // Date Filter
        VBox dateFilterVBox = createFilterField("Ngày Lập", cmbNgayLap = new ComboBox(), "filter-combobox");
        cmbNgayLap.setPromptText("Ngày Lập");
        cmbNgayLap.setOnAction(e -> this.handleTimKiem());

        // Refresh Button
        VBox refreshButtonVBox = new VBox();
        refreshButtonVBox.getStyleClass().addAll("filter-field-container");
        Label invisibleLabel = new Label(".");
        invisibleLabel.getStyleClass().addAll("opacity-zero");
        btnLamMoi = new Button("🔄 Làm mới");
        btnLamMoi.setMnemonicParsing(false);
        btnLamMoi.getStyleClass().addAll("btn-secondary");
        btnLamMoi.setOnAction(e -> this.handleLamMoi());
        btnLamMoi.setCursor(Cursor.HAND);
        refreshButtonVBox.getChildren().addAll(invisibleLabel, btnLamMoi);

        filterControlsHBox.getChildren().addAll(searchFilterVBox, dateFilterVBox, refreshButtonVBox);
        filterSectionVBox.getChildren().addAll(filterControlsHBox);

        // ============================================
        // TABLE SECTION
        // ============================================
        VBox tableContainerVBox = new VBox();
        tableContainerVBox.getStyleClass().addAll("table-container");
        tableContainerVBox.setPrefHeight(561.0);
        tableContainerVBox.setPrefWidth(890.0);

        tableViewKhachHang = new TableView();
        tableViewKhachHang.getStyleClass().addAll("table-view");
        tableViewKhachHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Table Columns
        colMaHD = new TableColumn();
        colMaHD.setText("Mã Hóa Đơn");

        colNgayLap = new TableColumn();
        colNgayLap.setText("Ngày Lập");

        colPhuongThuc = new TableColumn();
        colPhuongThuc.setText("Phương Thức");

        colTrangThai = new TableColumn();
        colTrangThai.setText("Trạng Thái");

        colTongTien = new TableColumn();
        colTongTien.setText("Tổng Tiền");

        colMaKhuyenMai = new TableColumn();
        colMaKhuyenMai.setText("Mã Khuyến Mãi");

        colTenKH = new TableColumn();
        colTenKH.setText("Tên Khách Hàng");

        colThaoTac = new TableColumn();
        colThaoTac.setText("Thao tác");

        tableViewKhachHang.getColumns().addAll(colMaHD, colNgayLap, colPhuongThuc, colTrangThai, colTongTien, colMaKhuyenMai, colTenKH, colThaoTac);
        tableViewKhachHang.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        VBox.setVgrow(tableViewKhachHang, Priority.ALWAYS);
        tableContainerVBox.getChildren().addAll(tableViewKhachHang);
        VBox.setVgrow(tableContainerVBox, Priority.ALWAYS);

        // Assemble main content
        mainContentVBox.getChildren().addAll(headerSection, statsGrid, filterSectionVBox, tableContainerVBox);
        mainContainer.setCenter(mainContentVBox);
        BorderPane.setAlignment(mainContentVBox, Pos.CENTER);
        mainContainer.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));
        this.root = mainContainer;
        this.quanLyThanhToanController = new QuanLyHoaDonController(this);
    }

    /**
     * Helper method to create a filter field
     */
    private VBox createFilterField(String labelText, javafx.scene.control.Control control, String controlStyleClass) {
        VBox filterFieldVBox = new VBox();
        filterFieldVBox.getStyleClass().addAll("filter-field-container");

        Label fieldLabel = new Label(labelText);
        fieldLabel.getStyleClass().addAll("filter-label");

        control.getStyleClass().addAll(controlStyleClass);
        control.setCursor(Cursor.HAND);
        if (control instanceof TextField) {
            ((TextField) control).setMaxWidth(Double.MAX_VALUE);
            ((TextField) control).setPrefHeight(41.0);
            ((TextField) control).setPrefWidth(200.0);
        } else if (control instanceof ComboBox) {
            ((ComboBox) control).setMaxWidth(Double.MAX_VALUE);
            ((ComboBox) control).setPrefHeight(41.0);
            ((ComboBox) control).setPrefWidth(150.0);
        }

        filterFieldVBox.getChildren().addAll(fieldLabel, control);
        return filterFieldVBox;
    }

    public Parent getRoot() {
        return root;
    }

    public Button getBtnLamMoi() {
        return btnLamMoi;
    }

    public TextField getTxtTimKiem() {
        return txtTimKiem;
    }

    public ComboBox<String> getCmbNgayLap() {
        return cmbNgayLap;
    }

    public TableView getTableViewKhachHang() {
        return tableViewKhachHang;
    }

    public TableColumn getColMaHD() {
        return colMaHD;
    }

    public TableColumn getColNgayLap() {
        return colNgayLap;
    }

    public TableColumn getColPhuongThuc() {
        return colPhuongThuc;
    }

    public TableColumn getColTrangThai() {
        return colTrangThai;
    }

    public TableColumn getColTongTien() {
        return colTongTien;
    }

    public TableColumn getColMaKhuyenMai() {
        return colMaKhuyenMai;
    }

    public TableColumn getColTenKH() {
        return colTenKH;
    }

    public TableColumn getColThaoTac() {
        return colThaoTac;
    }

    public StatsCard getUnpaidInvoicesCard() {
        return unpaidInvoicesCard;
    }

    public StatsCard getPaidInvoicesCard() {
        return paidInvoicesCard;
    }

    public StatsCard getDailyRevenueCard() {
        return dailyRevenueCard;
    }

    public StatsCard getPromotionalInvoicesCard() {
        return promotionalInvoicesCard;
    }

    private void handleLamMoi() {
        if (quanLyThanhToanController != null) {
            quanLyThanhToanController.handleLamMoi();
        }
    }

    private void handleTimKiem() {
        if (quanLyThanhToanController != null) {
            quanLyThanhToanController.handleTimKiem();
        }
    }
}
