package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.QuanLyPhieuDatPhongController;
import com.example.louishotelmanagement.ui.components.StatsCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class QuanLyPhieuDatPhongView {
  private static QuanLyPhieuDatPhongView instance;

  // StatsCard components for statistics
  private StatsCard totalBookingsCard;
  private StatsCard reservedBookingsCard;
  private StatsCard activeBookingsCard;
  private StatsCard completedBookingsCard;

  // Filter components
  private TextField txtTimKiem;
  private ComboBox<String> cbTrangThai;
  private Button btnLamMoi;

  // Table components
  private TableView tableViewPhieuDatPhong;
  private TableColumn colMaPDP;
  private TableColumn colKH;
  private TableColumn colNgayDat;
  private TableColumn colNgayDen;
  private TableColumn colNgayDi;
  private TableColumn colTrangThai;
  private TableColumn colTienCoc;
  private TableColumn colThaoTac;

  private Parent root;
  public QuanLyPhieuDatPhongController quanLyPhieuDatPhongController;

  public static QuanLyPhieuDatPhongView getInstance() {
    if (instance == null) {
      instance = new QuanLyPhieuDatPhongView();
    }
    return instance;
  }

  public static String getIdentifier() {
    return "quan-ly-phieu-dat-phong-view";
  }

  public QuanLyPhieuDatPhongView() {
    // ============================================
    // MAIN CONTAINER SETUP
    // ============================================
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPrefHeight(1054.0);
    mainContainer.setPrefWidth(1014.0);
    mainContainer.getStyleClass().addAll("main-background", "main-content-padding");
    mainContainer.getStylesheets().addAll(
        getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm()
    );

    VBox mainContentVBox = new VBox(32.0);
    mainContentVBox.getStyleClass().addAll("spacing-normal");
    BorderPane.setAlignment(mainContentVBox, Pos.CENTER);

    // ============================================
    // HEADER SECTION
    // ============================================
    HBox headerSection = new HBox();
    headerSection.getStyleClass().addAll("header-section");

    VBox headerIconContainer = new VBox();
    headerIconContainer.getStyleClass().addAll("header-icon-container");
    ImageView headerIcon = new ImageView();
    headerIcon.getStyleClass().addAll("header-icon");
    headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/booking-online.png").toExternalForm()));
    headerIconContainer.getChildren().addAll(headerIcon);

    VBox headerTitleContainer = new VBox();
    headerTitleContainer.getStyleClass().addAll("header-title-section");
    Label headerTitleLabel = new Label("Quản Lý Phiếu Đặt Phòng");
    headerTitleLabel.getStyleClass().addAll("header-title");
    Label headerSubtitleLabel = new Label("Quản lý các phiếu đặt phòng trong khách sạn");
    headerSubtitleLabel.getStyleClass().addAll("header-subtitle");
    headerTitleContainer.getChildren().addAll(headerTitleLabel, headerSubtitleLabel);
    HBox.setHgrow(headerTitleContainer, Priority.ALWAYS);

    headerSection.getChildren().addAll(headerIconContainer, headerTitleContainer);

    // ============================================
    // STATISTICS GRID
    // ============================================
    GridPane statsGrid = new GridPane();
    statsGrid.setHgap(20.0);
    statsGrid.setAlignment(Pos.TOP_CENTER);
    statsGrid.setPrefHeight(100.0);
    // GridPane doesn't have setHBoxGrow method

    // Configure grid constraints
    ColumnConstraints colConstraint = new ColumnConstraints();
    colConstraint.setHgrow(Priority.SOMETIMES);
    colConstraint.setMinWidth(10.0);
    colConstraint.setPrefWidth(100.0);
    statsGrid.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint, colConstraint);

    RowConstraints rowConstraint = new RowConstraints();
    rowConstraint.setVgrow(Priority.SOMETIMES);
    statsGrid.getRowConstraints().addAll(rowConstraint);

    // Total Bookings Stat Card
    totalBookingsCard = new StatsCard(
        "Tổng số phiếu", "0", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/reserved.png", "stats-card-icon-container-success"
    );

    // Reserved Bookings Stat Card
    reservedBookingsCard = new StatsCard(
        "Đã đặt", "0", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/document_hotel.png", "stats-card-icon-container-success"
    );
    GridPane.setColumnIndex(reservedBookingsCard, 1);

    // Active Bookings Stat Card
    activeBookingsCard = new StatsCard(
        "Đang sử dụng", "0", "stats-card-number-warning",
        "/com/example/louishotelmanagement/image/bed.png", "stats-card-icon-container-warning"
    );
    GridPane.setColumnIndex(activeBookingsCard, 2);

    // Completed Bookings Stat Card
    completedBookingsCard = new StatsCard(
        "Đã hoàn thành", "0", "stats-card-number-info",
        "/com/example/louishotelmanagement/image/check.png", "stats-card-icon-container-info"
    );
    GridPane.setColumnIndex(completedBookingsCard, 3);

    statsGrid.getChildren().addAll(totalBookingsCard, reservedBookingsCard, activeBookingsCard, completedBookingsCard);

    // ============================================
    // FILTER SECTION
    // ============================================
    VBox filterSectionVBox = new VBox();
    filterSectionVBox.getStyleClass().addAll("spacing-filter");

    HBox filterControlsHBox = new HBox();
    filterControlsHBox.getStyleClass().addAll("filter-section");

    // Search Filter
    VBox searchFilterVBox = createFilterField("Tìm kiếm", txtTimKiem = new TextField(), "filter-input");
    txtTimKiem.setPromptText("Nhập tên khách hàng...");
    txtTimKiem.setOnKeyReleased(e -> this.handleTimKiem());
    HBox.setHgrow(searchFilterVBox, Priority.ALWAYS);

    // Status Filter
    VBox statusFilterVBox = createFilterField("Trạng thái", cbTrangThai = new ComboBox(), "filter-combobox");
    cbTrangThai.setPromptText("Tất cả trạng thái");
    cbTrangThai.setValue("Tất cả trạng thái");
    cbTrangThai.setOnAction(e -> this.handleLocTrangThai());

    // Refresh Button
    VBox refreshButtonVBox = new VBox();
    refreshButtonVBox.getStyleClass().addAll("filter-field-container");
    Label invisibleLabel = new Label(".");
    invisibleLabel.getStyleClass().addAll("opacity-zero");
    btnLamMoi = new Button("🔄 Làm Mới");
    btnLamMoi.getStyleClass().addAll("btn-secondary");
    btnLamMoi.setOnAction(e -> this.handleLamMoi());
    btnLamMoi.setCursor(Cursor.HAND);
    refreshButtonVBox.getChildren().addAll(invisibleLabel, btnLamMoi);

    filterControlsHBox.getChildren().addAll(searchFilterVBox, statusFilterVBox, refreshButtonVBox);
    filterSectionVBox.getChildren().addAll(filterControlsHBox);

    // ============================================
    // TABLE SECTION
    // ============================================
    VBox tableContainerVBox = new VBox();
    tableContainerVBox.getStyleClass().addAll("table-container");
    // VBox doesn't have setVBoxGrow method

    tableViewPhieuDatPhong = new TableView();
    tableViewPhieuDatPhong.getStyleClass().addAll("table-view");
    tableViewPhieuDatPhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    VBox.setVgrow(tableViewPhieuDatPhong, Priority.ALWAYS);

    // Table Columns
    colMaPDP = new TableColumn();
    colMaPDP.setText("Mã phiếu");

    colKH = new TableColumn();
    colKH.setText("Tên khách hàng");

    colNgayDat = new TableColumn();
    colNgayDat.setText("Ngày đặt");

    colNgayDen = new TableColumn();
    colNgayDen.setText("Ngày đến");

    colNgayDi = new TableColumn();
    colNgayDi.setText("Ngày đi");

    colTrangThai = new TableColumn();
    colTrangThai.setText("Trạng thái");
    colTrangThai.getStyleClass().addAll("table-cell-center");

    colTienCoc = new TableColumn();
    colTienCoc.setText("Tiền cọc");

    colThaoTac = new TableColumn();
    colThaoTac.setText("Thao Tác");

    tableViewPhieuDatPhong.getColumns().addAll(colMaPDP, colKH, colNgayDat, colNgayDen, colNgayDi, colTrangThai, colTienCoc, colThaoTac);
    tableViewPhieuDatPhong.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));

    tableContainerVBox.getChildren().addAll(tableViewPhieuDatPhong);
    VBox.setVgrow(tableContainerVBox, Priority.ALWAYS);

    // Assemble main content
    mainContentVBox.getChildren().addAll(headerSection, statsGrid, filterSectionVBox, tableContainerVBox);
    mainContainer.setCenter(mainContentVBox);
    BorderPane.setAlignment(mainContentVBox, Pos.CENTER);
    mainContainer.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));

    this.root = mainContainer;
    this.quanLyPhieuDatPhongController = new QuanLyPhieuDatPhongController(this);
  }

  /**
   * Helper method to create a filter field
   */
  private VBox createFilterField(String labelText, Control control, String controlStyleClass) {
    VBox filterFieldVBox = new VBox();
    filterFieldVBox.getStyleClass().addAll("filter-field-container");

    if (!labelText.isEmpty()) {
      Label fieldLabel = new Label(labelText);
      fieldLabel.getStyleClass().addAll("filter-label");
      filterFieldVBox.getChildren().add(fieldLabel);
    }

    control.getStyleClass().addAll(controlStyleClass);
    control.setCursor(Cursor.HAND);
    if (control instanceof TextField) {
      ((TextField) control).setMaxWidth(Double.MAX_VALUE);
      ((TextField) control).setPrefHeight(41.0);
    } else if (control instanceof ComboBox) {
      ((ComboBox) control).setMaxWidth(Double.MAX_VALUE);
      ((ComboBox) control).setPrefHeight(41.0);
      ((ComboBox) control).setPrefWidth(150.0);
    } else if (control instanceof Button) {
      ((Button) control).setMaxWidth(Double.MAX_VALUE);
      ((Button) control).setPrefHeight(41.0);
      ((Button) control).setPrefWidth(120.0);
    }

    filterFieldVBox.getChildren().add(control);
    return filterFieldVBox;
  }

  public Parent getRoot() {
    return root;
  }

  // Getters for controller
  public StatsCard getTotalBookingsCard() {
    return totalBookingsCard;
  }

  public StatsCard getReservedBookingsCard() {
    return reservedBookingsCard;
  }

  public StatsCard getActiveBookingsCard() {
    return activeBookingsCard;
  }

  public StatsCard getCompletedBookingsCard() {
    return completedBookingsCard;
  }

  public TextField getTxtTimKiem() {
    return txtTimKiem;
  }

  public ComboBox<String> getCbTrangThai() {
    return cbTrangThai;
  }

  public Button getBtnLamMoi() {
    return btnLamMoi;
  }

  public TableView getTableViewPhieuDatPhong() {
    return tableViewPhieuDatPhong;
  }

  public TableColumn getColMaPDP() {
    return colMaPDP;
  }

  public TableColumn getColKH() {
    return colKH;
  }

  public TableColumn getColNgayDat() {
    return colNgayDat;
  }

  public TableColumn getColNgayDen() {
    return colNgayDen;
  }

  public TableColumn getColNgayDi() {
    return colNgayDi;
  }

  public TableColumn getColTrangThai() {
    return colTrangThai;
  }

  public TableColumn getColTienCoc() {
    return colTienCoc;
  }

  public TableColumn getColThaoTac() {
    return colThaoTac;
  }

  // Event handlers
  private void handleTimKiem() {
    if (quanLyPhieuDatPhongController != null) {
      quanLyPhieuDatPhongController.handleTimKiem();
    }
  }

  private void handleLocTrangThai() {
    if (quanLyPhieuDatPhongController != null) {
      quanLyPhieuDatPhongController.handleLocTrangThai();
    }
  }

  private void handleLamMoi() {
    if (quanLyPhieuDatPhongController != null) {
      quanLyPhieuDatPhongController.handleLamMoi();
    }
  }
}
