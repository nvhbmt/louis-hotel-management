package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.PhongController;
import com.example.louishotelmanagement.ui.components.StatsCard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class PhongView {
    // Statistics Labels
    private Label lblTongPhong;
    private Label lblPhongTrong;
    private Label lblPhongSuDung;
    private Label lblPhongBaoTri;

    // Filter Controls
    private ComboBox cbxTrangThai;
    private ComboBox cbxTang;
    private DatePicker dpTuNgay;
    private DatePicker dpDenNgay;
    private Button btnLamMoi;

    // Table Components
    private TableView tableViewPhong;
    private TableColumn chonPhong;
    private TableColumn maPhong;
    private TableColumn loaiPhong;
    private TableColumn donGia;
    private TableColumn trangThai;

    // Action Buttons
    private Button btnDatPhong;
    private Button btnNhanPhong;
    private Button btnDoiPhong;
    private Button btnHuy;
    private Button btnHuyDat;
    private Button btnDichVu;

    // Stats grid for accessing individual stats cards
    private GridPane statsGrid;

    private Parent root;
    private PhongController phongController;

    public PhongView() {
        // ============================================
        // MAIN CONTAINER SETUP
        // ============================================
        BorderPane mainContainer = new BorderPane();
        mainContainer.getStyleClass().addAll("main-background");
        mainContainer.getStylesheets().addAll(
                getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm(),
                getClass().getResource("/com/example/louishotelmanagement/css/phong-view.css").toExternalForm()
        );

        VBox mainContentVBox = new VBox(32.0);
        mainContentVBox.getStyleClass().addAll("main-container");

        // ============================================
        // HEADER SECTION
        // ============================================
        HBox headerSection = new HBox();
        headerSection.getStyleClass().addAll("header-section");

        VBox headerIconContainer = new VBox();
        headerIconContainer.getStyleClass().addAll("header-icon-container");
        ImageView headerIcon = new ImageView();
        headerIcon.getStyleClass().addAll("header-icon");
        headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/interior-design.png").toExternalForm()));
        headerIconContainer.getChildren().addAll(headerIcon);

        VBox headerTitleContainer = new VBox();
        headerTitleContainer.getStyleClass().addAll("header-title-section");
        Label headerTitleLabel = new Label("Quản Lý Phòng");
        headerTitleLabel.getStyleClass().addAll("header-title");
        Label headerSubtitleLabel = new Label("Màn hình chính các chức năng phòng");
        headerSubtitleLabel.getStyleClass().addAll("header-subtitle");
        headerTitleContainer.getChildren().addAll(headerTitleLabel, headerSubtitleLabel);
        headerSection.getChildren().addAll(headerIconContainer, headerTitleContainer);

        // ============================================
        // STATISTICS GRID
        // ============================================
        statsGrid = new GridPane();
        statsGrid.getStyleClass().addAll("stats-grid");

        // Configure grid constraints
        ColumnConstraints colConstraint = new ColumnConstraints();
        colConstraint.setHgrow(Priority.SOMETIMES);
        statsGrid.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint, colConstraint);

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.SOMETIMES);
        statsGrid.getRowConstraints().addAll(rowConstraint);

        // Initialize labels (they will be managed by StatsCard components)
        lblTongPhong = new Label("24");
        lblPhongTrong = new Label("8");
        lblPhongSuDung = new Label("14");
        lblPhongBaoTri = new Label("2");

        // Total Rooms Stat Card
        StatsCard totalRoomsCard = new StatsCard(
                "Tổng phòng", "24", "stats-card-number-success",
                "/com/example/louishotelmanagement/image/double-bed.png", "stats-card-icon-container-success"
        );

        // Available Rooms Stat Card
        StatsCard availableRoomsCard = new StatsCard(
                "Phòng trống", "8", "stats-card-number-danger",
                "/com/example/louishotelmanagement/image/door-open.png", "stats-card-icon-container-danger"
        );
        GridPane.setColumnIndex(availableRoomsCard, 1);

        // Occupied Rooms Stat Card
        StatsCard occupiedRoomsCard = new StatsCard(
                "Đang sử dụng", "14", "stats-card-number-warning",
                "/com/example/louishotelmanagement/image/key-chain.png", "stats-card-icon-container-warning"
        );
        GridPane.setColumnIndex(occupiedRoomsCard, 2);

        // Maintenance Rooms Stat Card
        StatsCard maintenanceRoomsCard = new StatsCard(
                "Bảo trì", "2", "stats-card-number-info",
                "/com/example/louishotelmanagement/image/maintenance.png", "stats-card-icon-container-info"
        );
        GridPane.setColumnIndex(maintenanceRoomsCard, 3);

        statsGrid.getChildren().addAll(totalRoomsCard, availableRoomsCard, occupiedRoomsCard, maintenanceRoomsCard);

        // ============================================
        // FILTER SECTION
        // ============================================
        VBox filterSectionVBox = new VBox();
        filterSectionVBox.getStyleClass().addAll("spacing-filter");

        HBox filterControlsHBox = new HBox();
        filterControlsHBox.getStyleClass().addAll("filter-section");

        // Status Filter
        VBox statusFilterVBox = createFilterField("Trạng thái", cbxTrangThai = new ComboBox(), "filter-combobox");

        // Floor Filter
        VBox floorFilterVBox = createFilterField("Tầng", cbxTang = new ComboBox(), "filter-combobox");

        // Start Date Filter
        VBox startDateFilterVBox = new VBox();
        startDateFilterVBox.getStyleClass().addAll("filter-field-container");
        Label startDateLabel = new Label("Từ ngày");
        startDateLabel.getStyleClass().addAll("filter-label");
        dpTuNgay = new DatePicker();
        dpTuNgay.setPrefHeight(45.0);
        dpTuNgay.setPrefWidth(200.0);
        dpTuNgay.setCursor(Cursor.HAND);
        startDateFilterVBox.getChildren().addAll(startDateLabel, dpTuNgay);

        // End Date Filter
        VBox endDateFilterVBox = new VBox();
        endDateFilterVBox.getStyleClass().addAll("filter-field-container");
        Label endDateLabel = new Label("Đến ngày");
        endDateLabel.getStyleClass().addAll("filter-label");
        dpDenNgay = new DatePicker();
        dpDenNgay.setPrefHeight(45.0);
        dpDenNgay.setPrefWidth(200.0);
        dpDenNgay.setCursor(Cursor.HAND);
        endDateFilterVBox.getChildren().addAll(endDateLabel, dpDenNgay);

        // Refresh Button
        VBox refreshButtonVBox = new VBox();
        refreshButtonVBox.getStyleClass().addAll("filter-field-container");
        Label invisibleLabel = new Label(".");
        invisibleLabel.getStyleClass().addAll("opacity-zero");
        btnLamMoi = new Button("🔄 Làm mới");
        btnLamMoi.setMnemonicParsing(false);
        btnLamMoi.getStyleClass().addAll("btn-secondary");
        btnLamMoi.setOnAction(e -> this.handleLamMoi(e));
        btnLamMoi.setCursor(Cursor.HAND);
        refreshButtonVBox.getChildren().addAll(invisibleLabel, btnLamMoi);

        filterControlsHBox.getChildren().addAll(statusFilterVBox, floorFilterVBox, startDateFilterVBox, endDateFilterVBox, refreshButtonVBox);
        filterSectionVBox.getChildren().addAll(filterControlsHBox);

        // ============================================
        // MAIN CONTENT AREA (TABLE + ACTIONS)
        // ============================================
        HBox mainContentAreaHBox = new HBox(20.0);
        mainContentAreaHBox.setPrefHeight(408.0);
        mainContentAreaHBox.setPrefWidth(819.0);

        // Table Container
        VBox tableContainerVBox = new VBox();
        tableContainerVBox.getStyleClass().addAll("table-container");

        tableViewPhong = new TableView();
        tableViewPhong.getStyleClass().addAll("table-view");
        tableViewPhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Table Columns
        chonPhong = new TableColumn();
        chonPhong.setPrefWidth(60.0);

        maPhong = new TableColumn();
        maPhong.setText("Mã Phòng");
        maPhong.setPrefWidth(140.0);

        loaiPhong = new TableColumn();
        loaiPhong.setText("Loại phòng");
        loaiPhong.setPrefWidth(200.0);
        loaiPhong.setMinWidth(0.0);

        donGia = new TableColumn();
        donGia.setText("Đơn giá");
        donGia.setPrefWidth(180.0);

        trangThai = new TableColumn();
        trangThai.setText("Trạng thái");
        trangThai.setPrefWidth(200.0);

        tableViewPhong.getColumns().addAll(chonPhong, maPhong, loaiPhong, donGia, trangThai);
        VBox.setVgrow(tableViewPhong, Priority.ALWAYS);
        tableContainerVBox.getChildren().addAll(tableViewPhong);
        HBox.setHgrow(tableContainerVBox, Priority.ALWAYS);

        // Action Panel
        VBox actionPanelVBox = new VBox(16);
        actionPanelVBox.setAlignment(Pos.TOP_CENTER);
        actionPanelVBox.setPrefHeight(408.0);
        actionPanelVBox.setPrefWidth(280.0);
        actionPanelVBox.getStyleClass().addAll("action-panel");

        Label actionTitleLabel = new Label("Thao tác");
        actionTitleLabel.setAlignment(Pos.CENTER);
        actionTitleLabel.getStyleClass().addAll("action-title");
        VBox.setMargin(actionTitleLabel, new Insets(0, 0, 8, 0));

        // Create action buttons - controller will be set up after UI creation
        btnDatPhong = createActionButton("📅 Đặt phòng", "btn-advance-booking", e -> handleDatPhong(e));
        btnNhanPhong = createActionButton("🔑 Nhận phòng", "btn-secondary-action", e -> handleNhanPhong(e));
        btnDoiPhong = createActionButton("🔄 Đổi phòng", "btn-secondary-action", e -> handleDoiPhong(e));
        btnHuy = createActionButton("🚪 Trả phòng", "btn-secondary-action", e -> handleHuy(e));
        btnHuyDat = createActionButton("❌ Hủy đặt phòng", "btn-cancel-booking", e -> handleHuyDat(e));
        btnDichVu = createActionButton("⚙️ Dịch vụ", "btn-secondary-action", e -> handleDichVu(e));

        actionPanelVBox.getChildren().addAll(actionTitleLabel, btnDatPhong, btnNhanPhong, btnDoiPhong, btnHuy, btnHuyDat, btnDichVu);
        HBox.setHgrow(actionPanelVBox, Priority.NEVER);

        mainContentAreaHBox.getChildren().addAll(tableContainerVBox, actionPanelVBox);
        VBox.setVgrow(mainContentAreaHBox, Priority.ALWAYS);

        // Assemble main content
        mainContentVBox.getChildren().addAll(headerSection, statsGrid, filterSectionVBox, mainContentAreaHBox);
        mainContainer.setCenter(mainContentVBox);
        BorderPane.setAlignment(mainContentVBox, Pos.CENTER);
        mainContainer.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));
        this.root = mainContainer;
    }


    /**
     * Helper method to create a filter field
     */
    private VBox createFilterField(String labelText, ComboBox comboBox, String comboBoxStyleClass) {
        VBox filterFieldVBox = new VBox();
        filterFieldVBox.getStyleClass().addAll("filter-field-container");

        Label fieldLabel = new Label(labelText);
        fieldLabel.getStyleClass().addAll("filter-label");

        comboBox.getStyleClass().addAll(comboBoxStyleClass);
        comboBox.setCursor(Cursor.HAND);

        filterFieldVBox.getChildren().addAll(fieldLabel, comboBox);
        return filterFieldVBox;
    }

    /**
     * Helper method to create an action button
     */
    private Button createActionButton(String text, String styleClass, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinHeight(50.0);
        button.setMnemonicParsing(false);
        button.setPrefHeight(50.0);
        button.getStyleClass().addAll(styleClass);
        button.setOnAction(eventHandler);
        button.setCursor(Cursor.HAND);
        return button;
    }

    public Parent getRoot() {
        return root;
    }

    public Label getLblTongPhong() {
        return lblTongPhong;
    }

    public Label getLblPhongTrong() {
        return lblPhongTrong;
    }

    public Label getLblPhongSuDung() {
        return lblPhongSuDung;
    }

    public Label getLblPhongBaoTri() {
        return lblPhongBaoTri;
    }

    public ComboBox getCbxTrangThai() {
        return cbxTrangThai;
    }

    public ComboBox getCbxTang() {
        return cbxTang;
    }

    public DatePicker getDpTuNgay() {
        return dpTuNgay;
    }

    public DatePicker getDpDenNgay() {
        return dpDenNgay;
    }

    public Button getBtnLamMoi() {
        return btnLamMoi;
    }

    public TableView getTableViewPhong() {
        return tableViewPhong;
    }

    public TableColumn getChonPhong() {
        return chonPhong;
    }

    public TableColumn getMaPhong() {
        return maPhong;
    }

    public TableColumn getLoaiPhong() {
        return loaiPhong;
    }

    public TableColumn getDonGia() {
        return donGia;
    }

    public TableColumn getTrangThai() {
        return trangThai;
    }

    public Button getBtnDatPhong() {
        return btnDatPhong;
    }

    public Button getBtnNhanPhong() {
        return btnNhanPhong;
    }

    public Button getBtnDoiPhong() {
        return btnDoiPhong;
    }

    public Button getBtnHuy() {
        return btnHuy;
    }

    public Button getBtnHuyDat() {
        return btnHuyDat;
    }

    public Button getBtnDichVu() {
        return btnDichVu;
    }

    /**
     * Set up the PhongController with UI components after UI is fully created
     */
    public void setupController(PhongController controller) {
        this.phongController = controller;
        // Connect UI components to controller
        controller.setTableViewPhong(tableViewPhong);
        controller.setChonPhong(chonPhong);
        controller.setMaPhong(maPhong);
        controller.setLoaiPhong(loaiPhong);
        controller.setDonGia(donGia);
        controller.setTrangThai(trangThai);
        // Labels are now managed by StatsCard components, but we still provide them for backward compatibility
        controller.setLblTongPhong(lblTongPhong);
        controller.setLblPhongTrong(lblPhongTrong);
        controller.setLblPhongSuDung(lblPhongSuDung);
        controller.setLblPhongBaoTri(lblPhongBaoTri);
        controller.setCbxTrangThai(cbxTrangThai);
        controller.setCbxTang(cbxTang);
        controller.setDpTuNgay(dpTuNgay);
        controller.setDpDenNgay(dpDenNgay);
        controller.setBtnLamMoi(btnLamMoi);
    }

    private void handleLamMoi(ActionEvent e) {
        if (phongController != null) {
            phongController.handleLamMoi(e);
        }
    }

    private void handleDatPhong(ActionEvent e) {
        if (phongController != null) {
            phongController.moDatPhong(e);
        }
    }

    private void handleNhanPhong(ActionEvent e) {
        if (phongController != null) {
            phongController.moNhanPhong(e);
        }
    }

    private void handleDoiPhong(ActionEvent e) {
        if (phongController != null) {
            phongController.moDoiPhong(e);
        }
    }

    private void handleHuy(ActionEvent e) {
        if (phongController != null) {
            phongController.moHuy(e);
        }
    }

    private void handleHuyDat(ActionEvent e) {
        if (phongController != null) {
            phongController.moHuyDat(e);
        }
    }

    private void handleDichVu(ActionEvent e) {
        if (phongController != null) {
            phongController.moDatPhong(e);
        }
    }
}
