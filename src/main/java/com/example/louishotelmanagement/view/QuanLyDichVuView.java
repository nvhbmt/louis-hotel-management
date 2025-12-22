package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.QuanLyDichVuController;
import com.example.louishotelmanagement.ui.components.StatsCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class QuanLyDichVuView {
  private static QuanLyDichVuView instance;

  private Button btnThemDichVu;
  private ComboBox cbTrangThaiKinhDoanh;
  private TextField txtTimKiem;
  private Button btnLamMoi;
  private TableView tableViewDichVu;
  private TableColumn colMaDV;
  private TableColumn colTenDV;
  private TableColumn colSoLuong;
  private TableColumn colDonGia;
  private TableColumn colMoTa;
  private TableColumn colTrangThai;
  private TableColumn colThaoTac;

  // StatsCard components for statistics
  private StatsCard totalServicesCard;
  private StatsCard activeServicesCard;
  private StatsCard dailyRevenueCard;
  private StatsCard monthlyRevenueCard;

  private Parent root;
  public QuanLyDichVuController quanLyDichVuController;

  public static QuanLyDichVuView getInstance() {
    if (instance == null) {
      instance = new QuanLyDichVuView();
    }
    return instance;
  }

  public static String getIdentifier() {
    return "quan-ly-dich-vu-view";
  }

  public QuanLyDichVuView() {
    // ============================================
    // MAIN CONTAINER SETUP
    // ============================================
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPrefHeight(737.0);
    mainContainer.setPrefWidth(1153.0);
    mainContainer.getStyleClass().addAll("main-background");
    mainContainer.getStylesheets().addAll(
        getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm()
    );

    VBox mainContentVBox = new VBox(32.0);
    mainContentVBox.setPrefHeight(904.0);
    mainContentVBox.setPrefWidth(1462.0);

    // ============================================
    // HEADER SECTION
    // ============================================
    HBox headerSection = new HBox();
    headerSection.getStyleClass().addAll("header-section");

    VBox headerIconContainer = new VBox();
    headerIconContainer.getStyleClass().addAll("header-icon-container");
    ImageView headerIcon = new ImageView();
    headerIcon.getStyleClass().addAll("header-icon");
    headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/client.png").toExternalForm()));
    headerIconContainer.getChildren().addAll(headerIcon);

    VBox headerTitleContainer = new VBox();
    headerTitleContainer.getStyleClass().addAll("header-title-section");
    Label headerTitleLabel = new Label("Quản Lý Dịch Vụ");
    headerTitleLabel.getStyleClass().addAll("header-title");
    Label headerSubtitleLabel = new Label("Quản lý thông tin và trạng thái của dịch vụ trong khách sạn");
    headerSubtitleLabel.getStyleClass().addAll("header-subtitle");
    headerTitleContainer.getChildren().addAll(headerTitleLabel, headerSubtitleLabel);
    HBox.setHgrow(headerTitleContainer, Priority.ALWAYS);

    VBox headerButtonContainer = new VBox();
    headerButtonContainer.getStyleClass().addAll("header-button-section");
    btnThemDichVu = new Button("➕ Thêm Dịch Vụ");
    btnThemDichVu.getStyleClass().addAll("btn-primary");
    btnThemDichVu.setOnAction(e -> this.handleThemDichVu());
    headerButtonContainer.getChildren().addAll(btnThemDichVu);
    headerSection.getChildren().addAll(headerIconContainer, headerTitleContainer, headerButtonContainer);

    // ============================================
    // STATISTICS GRID
    // ============================================
    GridPane statsGrid = new GridPane();
    statsGrid.getStyleClass().addAll("stats-grid");

    // Configure grid constraints
    ColumnConstraints colConstraint = new ColumnConstraints();
    colConstraint.setHgrow(Priority.SOMETIMES);
    statsGrid.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint, colConstraint);

    RowConstraints rowConstraint = new RowConstraints();
    rowConstraint.setVgrow(Priority.SOMETIMES);
    statsGrid.getRowConstraints().addAll(rowConstraint);

    // Total Services Stat Card
    totalServicesCard = new StatsCard(
        "Số dịch vụ", "69", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/receipt.png", "stats-card-icon-container-success"
    );

    // Active Services Stat Card
    activeServicesCard = new StatsCard(
        "Đang kinh doanh", "15", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/green-circle.png", "stats-card-icon-container-success"
    );
    GridPane.setColumnIndex(activeServicesCard, 1);

    // Daily Revenue Stat Card
    dailyRevenueCard = new StatsCard(
        "Doanh thu ngày", "14", "stats-card-number-warning",
        "/com/example/louishotelmanagement/image/money-bag.png", "stats-card-icon-container-warning"
    );
    GridPane.setColumnIndex(dailyRevenueCard, 2);

    // Monthly Revenue Stat Card
    monthlyRevenueCard = new StatsCard(
        "Doanh thu tháng", "10", "stats-card-number-info",
        "/com/example/louishotelmanagement/image/bar-chart.png", "stats-card-icon-container-info"
    );
    GridPane.setColumnIndex(monthlyRevenueCard, 3);

    statsGrid.getChildren().addAll(totalServicesCard, activeServicesCard, dailyRevenueCard, monthlyRevenueCard);
    VBox.setVgrow(statsGrid, Priority.NEVER);

    // ============================================
    // FILTER SECTION
    // ============================================
    VBox filterSectionVBox = new VBox();
    filterSectionVBox.getStyleClass().addAll("spacing-filter");

    HBox filterControlsHBox = new HBox();
    filterControlsHBox.getStyleClass().addAll("filter-section");

    // Search Filter
    VBox searchFilterVBox = new VBox();
    searchFilterVBox.getStyleClass().addAll("filter-field-container");
    Label searchLabel = new Label("Tìm kiếm");
    searchLabel.getStyleClass().addAll("filter-label");
    txtTimKiem = new TextField();
    txtTimKiem.getStyleClass().addAll("filter-input");
    txtTimKiem.setPromptText("Tìm kiếm mã, tên dịch vụ...");
    txtTimKiem.setOnKeyReleased(e -> this.handleTimKiem());
    txtTimKiem.setCursor(Cursor.HAND);
    searchFilterVBox.getChildren().addAll(searchLabel, txtTimKiem);
    HBox.setHgrow(searchFilterVBox, Priority.ALWAYS);

    // Status Filter
    VBox statusFilterVBox = createFilterField("Trạng thái", cbTrangThaiKinhDoanh = new ComboBox(), "filter-combobox");

    // Refresh Button
    VBox refreshButtonVBox = new VBox();
    refreshButtonVBox.getStyleClass().addAll("filter-field-container");
    Label invisibleLabel = new Label(".");
    invisibleLabel.getStyleClass().addAll("opacity-zero");
    btnLamMoi = new Button("🔄 Làm Mới");
    btnLamMoi.setMnemonicParsing(false);
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

    tableViewDichVu = new TableView();
    tableViewDichVu.getStyleClass().addAll("table-view");
    tableViewDichVu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // Table Columns
    colMaDV = new TableColumn();
    colMaDV.setText("Mã Dịch Vụ");

    colTenDV = new TableColumn();
    colTenDV.setText("Tên Dịch Vụ");

    colSoLuong = new TableColumn();
    colSoLuong.setText("Số Lượng");

    colDonGia = new TableColumn();
    colDonGia.setText("Đơn Giá");

    colMoTa = new TableColumn();
    colMoTa.setText("Mô Tả");

    colTrangThai = new TableColumn();
    colTrangThai.setText("Trạng Thái");
    colTrangThai.getStyleClass().addAll("table-cell-center");

    colThaoTac = new TableColumn();
    colThaoTac.setText("Thao tác");

    tableViewDichVu.getColumns().addAll(colMaDV, colTenDV, colSoLuong, colDonGia, colMoTa, colTrangThai, colThaoTac);
    VBox.setVgrow(tableViewDichVu, Priority.ALWAYS);
    tableContainerVBox.getChildren().addAll(tableViewDichVu);
    VBox.setVgrow(tableContainerVBox, Priority.ALWAYS);

    // Assemble main content
    mainContentVBox.getChildren().addAll(headerSection, statsGrid, filterSectionVBox, tableContainerVBox);
    mainContainer.setCenter(mainContentVBox);
    BorderPane.setAlignment(mainContentVBox, Pos.CENTER);
    mainContainer.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));
    this.root = mainContainer;
    this.quanLyDichVuController = new QuanLyDichVuController(this);
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

  public Parent getRoot() {
    return root;
  }

  public Button getBtnThemDichVu() {
    return btnThemDichVu;
  }

  public StatsCard getTotalServicesCard() {
    return totalServicesCard;
  }

  public StatsCard getActiveServicesCard() {
    return activeServicesCard;
  }

  public StatsCard getDailyRevenueCard() {
    return dailyRevenueCard;
  }

  public StatsCard getMonthlyRevenueCard() {
    return monthlyRevenueCard;
  }

  public ComboBox getCbTrangThaiKinhDoanh() {
    return cbTrangThaiKinhDoanh;
  }

  public TextField getTxtTimKiem() {
    return txtTimKiem;
  }

  public Button getBtnLamMoi() {
    return btnLamMoi;
  }

  public TableView getTableViewDichVu() {
    return tableViewDichVu;
  }

  public TableColumn getColMaDV() {
    return colMaDV;
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

  public TableColumn getColMoTa() {
    return colMoTa;
  }

  public TableColumn getColTrangThai() {
    return colTrangThai;
  }

  public TableColumn getColThaoTac() {
    return colThaoTac;
  }

  private void handleThemDichVu() {
    if (quanLyDichVuController != null) {
      quanLyDichVuController.handleThemDichVu();
    }
  }

  private void handleLamMoi() {
        if (quanLyDichVuController != null) {
        quanLyDichVuController.handleLamMoi();
        }
  }

  private void handleTimKiem() {
    if (quanLyDichVuController != null) {
      quanLyDichVuController.handleTimKiem();
    }
  }
}
