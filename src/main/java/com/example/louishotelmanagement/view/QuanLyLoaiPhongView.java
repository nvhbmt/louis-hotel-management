package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.QuanLyLoaiPhongController;
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

public class QuanLyLoaiPhongView {
  private static QuanLyLoaiPhongView instance;

  private Button btnThemLoaiPhong;
  private ComboBox cbLocGia;
  private TextField txtTimKiem;
  private Button btnLamMoi;
  private TableView tableViewLoaiPhong;
  private TableColumn colMaLoaiPhong;
  private TableColumn colTenLoai;
  private TableColumn colDonGia;
  private TableColumn colMoTa;
  private TableColumn colThaoTac;

  // StatsCard components for statistics
  private StatsCard totalRoomTypesCard;
  private StatsCard lowestPriceCard;
  private StatsCard highestPriceCard;
  private StatsCard averagePriceCard;

  private Parent root;
  public QuanLyLoaiPhongController quanLyLoaiPhongController;

  public static QuanLyLoaiPhongView getInstance() {
    if (instance == null) {
      instance = new QuanLyLoaiPhongView();
    }
    return instance;
  }

  public static String getIdentifier() {
    return "quan-ly-loai-phong-view";
  }

  public QuanLyLoaiPhongView() {
    // ============================================
    // MAIN CONTAINER SETUP
    // ============================================
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPrefHeight(1017.0);
    mainContainer.setPrefWidth(1194.0);
    mainContainer.getStyleClass().addAll("main-background");
    mainContainer.getStylesheets().addAll(
        getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm()
    );

    VBox mainContentVBox = new VBox(32.0);
    mainContentVBox.setPrefHeight(800.0);
    mainContentVBox.setPrefWidth(950.0);

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
    Label headerTitleLabel = new Label("Quản Lý Loại Phòng");
    headerTitleLabel.getStyleClass().addAll("header-title");
    Label headerSubtitleLabel = new Label("Quản lý thông tin và giá cả các loại phòng trong khách sạn");
    headerSubtitleLabel.getStyleClass().addAll("header-subtitle");
    headerTitleContainer.getChildren().addAll(headerTitleLabel, headerSubtitleLabel);
    HBox.setHgrow(headerTitleContainer, Priority.ALWAYS);

    VBox headerButtonContainer = new VBox();
    headerButtonContainer.getStyleClass().addAll("header-button-section");
    btnThemLoaiPhong = new Button("➕ Thêm Loại Phòng");
    btnThemLoaiPhong.getStyleClass().addAll("btn-primary");
    btnThemLoaiPhong.setOnAction(e -> this.handleThemLoaiPhong());
    headerButtonContainer.getChildren().addAll(btnThemLoaiPhong);
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

    // Total Room Types Stat Card
    totalRoomTypesCard = new StatsCard(
        "Tổng số loại phòng", "0", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/living-room.png", "stats-card-icon-container-success"
    );

    // Lowest Price Stat Card
    lowestPriceCard = new StatsCard(
        "Giá thấp nhất", "0", "stats-card-number-danger",
        "/com/example/louishotelmanagement/image/dining-table.png", "stats-card-icon-container-danger"
    );
    GridPane.setColumnIndex(lowestPriceCard, 1);

    // Highest Price Stat Card
    highestPriceCard = new StatsCard(
        "Giá cao nhất", "0", "stats-card-number-warning",
        "/com/example/louishotelmanagement/image/bed.png", "stats-card-icon-container-warning"
    );
    GridPane.setColumnIndex(highestPriceCard, 2);

    // Average Price Stat Card
    averagePriceCard = new StatsCard(
        "Giá trung bình", "0", "stats-card-number-info",
        "/com/example/louishotelmanagement/image/double-bed_avg.png", "stats-card-icon-container-info"
    );
    GridPane.setColumnIndex(averagePriceCard, 3);

    statsGrid.getChildren().addAll(totalRoomTypesCard, lowestPriceCard, highestPriceCard, averagePriceCard);
    VBox.setVgrow(statsGrid, Priority.NEVER);

    // ============================================
    // FILTER SECTION
    // ============================================
    VBox filterSectionVBox = new VBox();
    filterSectionVBox.getStyleClass().addAll("spacing-filter");

    HBox filterControlsHBox = new HBox();
    filterControlsHBox.getStyleClass().addAll("filter-section");

    // Price Range Filter
    VBox priceRangeFilterVBox = createFilterField("Loại phòng", cbLocGia = new ComboBox(), "filter-combobox");

    // Search Filter
    VBox searchFilterVBox = new VBox();
    searchFilterVBox.getStyleClass().addAll("filter-field-container");
    Label searchLabel = new Label("Tìm kiếm");
    searchLabel.getStyleClass().addAll("filter-label");
    txtTimKiem = new TextField();
    txtTimKiem.getStyleClass().addAll("filter-input");
    txtTimKiem.setPromptText("Nhập mã phòng hoặc tầng...");
    txtTimKiem.setOnKeyReleased(e -> this.handleTimKiem());
    txtTimKiem.setCursor(Cursor.HAND);
    searchFilterVBox.getChildren().addAll(searchLabel, txtTimKiem);

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

    filterControlsHBox.getChildren().addAll(priceRangeFilterVBox, searchFilterVBox, refreshButtonVBox);
    filterSectionVBox.getChildren().addAll(filterControlsHBox);

    // ============================================
    // TABLE SECTION
    // ============================================
    VBox tableContainerVBox = new VBox();
    tableContainerVBox.getStyleClass().addAll("table-container");

    tableViewLoaiPhong = new TableView();
    tableViewLoaiPhong.getStyleClass().addAll("table-view");
    tableViewLoaiPhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // Table Columns
    colMaLoaiPhong = new TableColumn();
    colMaLoaiPhong.setText("Mã Loại Phòng");

    colTenLoai = new TableColumn();
    colTenLoai.setText("Tên Loại Phòng");

    colDonGia = new TableColumn();
    colDonGia.setText("Đơn Giá");

    colMoTa = new TableColumn();
    colMoTa.setText("Mô Tả");

    colThaoTac = new TableColumn();
    colThaoTac.setText("Thao tác");

    tableViewLoaiPhong.getColumns().addAll(colMaLoaiPhong, colTenLoai, colDonGia, colMoTa, colThaoTac);
    VBox.setVgrow(tableViewLoaiPhong, Priority.ALWAYS);
    tableContainerVBox.getChildren().addAll(tableViewLoaiPhong);
    VBox.setVgrow(tableContainerVBox, Priority.ALWAYS);

    // Assemble main content
    mainContentVBox.getChildren().addAll(headerSection, statsGrid, filterSectionVBox, tableContainerVBox);
    mainContainer.setCenter(mainContentVBox);
    BorderPane.setAlignment(mainContentVBox, Pos.CENTER);
    mainContainer.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));
    this.root = mainContainer;
    this.quanLyLoaiPhongController = new QuanLyLoaiPhongController(this);
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

  public Button getBtnThemLoaiPhong() {
    return btnThemLoaiPhong;
  }

  public StatsCard getTotalRoomTypesCard() {
    return totalRoomTypesCard;
  }

  public StatsCard getLowestPriceCard() {
    return lowestPriceCard;
  }

  public StatsCard getHighestPriceCard() {
    return highestPriceCard;
  }

  public StatsCard getAveragePriceCard() {
    return averagePriceCard;
  }

  public ComboBox getCbLocGia() {
    return cbLocGia;
  }

  public TextField getTxtTimKiem() {
    return txtTimKiem;
  }

  public Button getBtnLamMoi() {
    return btnLamMoi;
  }

  public TableView getTableViewLoaiPhong() {
    return tableViewLoaiPhong;
  }

  public TableColumn getColMaLoaiPhong() {
    return colMaLoaiPhong;
  }

  public TableColumn getColTenLoai() {
    return colTenLoai;
  }

  public TableColumn getColDonGia() {
    return colDonGia;
  }

  public TableColumn getColMoTa() {
    return colMoTa;
  }

  public TableColumn getColThaoTac() {
    return colThaoTac;
  }

  private void handleThemLoaiPhong() {
    if (quanLyLoaiPhongController != null) {
      quanLyLoaiPhongController.handleThemLoaiPhong();
    }
  }

  private void handleLamMoi() {
        if (quanLyLoaiPhongController != null) {
        quanLyLoaiPhongController.handleLamMoi();
        }
  }

  private void handleTimKiem() {
    if (quanLyLoaiPhongController != null) {
      quanLyLoaiPhongController.handleTimKiem();
    }
  }
}
