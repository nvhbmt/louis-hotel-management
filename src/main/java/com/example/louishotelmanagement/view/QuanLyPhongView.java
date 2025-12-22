package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.QuanLyPhongController;
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

public class QuanLyPhongView {
  private Button btnThemPhong;
  private Label lblsoPhongTrong;
  private Label lblSoPhongTrong;
  private Label lblSoPhongSuDung;
  private Label lblSoPhongBaoTri;
  private ComboBox cbTrangThai;
  private ComboBox cbTang;
  private ComboBox cbLocLoaiPhong;
  private TextField txtTimKiem;
  private Button btnLamMoi;
  private TableView tableViewPhong;
  private TableColumn colMaPhong;
  private TableColumn colTenLoaiPhong;
  private TableColumn colTang;
  private TableColumn colTrangThai;
  private TableColumn colMoTa;
  private TableColumn colThaoTac;
  private Parent root;
  private QuanLyPhongController quanLyPhongController;

  public QuanLyPhongView() {
    // ============================================
    // MAIN CONTAINER SETUP
    // ============================================
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPrefHeight(737.0);
    mainContainer.setPrefWidth(1153.0);
    mainContainer.getStyleClass().addAll("main-background");
    mainContainer.getStylesheets().addAll(
        getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm(),
        getClass().getResource("/com/example/louishotelmanagement/css/quan-ly-phong-view.css").toExternalForm()
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
    headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/interior-design.png").toExternalForm()));
    headerIconContainer.getChildren().addAll(headerIcon);

    VBox headerTitleContainer = new VBox();
    headerTitleContainer.getStyleClass().addAll("header-title-section");
    Label headerTitleLabel = new Label("Quản Lý Phòng");
    headerTitleLabel.getStyleClass().addAll("header-title");
    Label headerSubtitleLabel = new Label("Quản lý thông tin và trạng thái các phòng trong khách sạn");
    headerSubtitleLabel.getStyleClass().addAll("header-subtitle");
    headerTitleContainer.getChildren().addAll(headerTitleLabel, headerSubtitleLabel);
    HBox.setHgrow(headerTitleContainer, Priority.ALWAYS);

    VBox headerButtonContainer = new VBox();
    headerButtonContainer.getStyleClass().addAll("header-button-section");
    btnThemPhong = new Button("➕ Thêm phòng");
    btnThemPhong.getStyleClass().addAll("add-room-button");
    btnThemPhong.setOnAction(e -> this.handleThemPhong());
    headerButtonContainer.getChildren().addAll(btnThemPhong);
    headerSection.getChildren().addAll(headerIconContainer, headerTitleContainer, headerButtonContainer);

    // ============================================
    // STATISTICS GRID
    // ============================================
    GridPane statsGrid = new GridPane();
    statsGrid.setAlignment(Pos.TOP_CENTER);
    statsGrid.setHgap(20);
    statsGrid.setPrefHeight(100.0);

    // Configure grid constraints
    ColumnConstraints colConstraint = new ColumnConstraints();
    colConstraint.setHgrow(Priority.SOMETIMES);
    statsGrid.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint, colConstraint);

    RowConstraints rowConstraint = new RowConstraints();
    rowConstraint.setVgrow(Priority.SOMETIMES);
    statsGrid.getRowConstraints().addAll(rowConstraint);

    // Initialize labels (they will be managed by StatsCard components)
    lblsoPhongTrong = new Label("24");
    lblSoPhongTrong = new Label("8");
    lblSoPhongSuDung = new Label("14");
    lblSoPhongBaoTri = new Label("2");

    // Total Rooms Stat Card
    StatsCard totalRoomsCard = new StatsCard(
        "Tổng số phòng", "24", "stats-card-number-success",
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
    VBox.setVgrow(statsGrid, Priority.NEVER);

    // ============================================
    // FILTER SECTION
    // ============================================
    VBox filterSectionVBox = new VBox();
    filterSectionVBox.getStyleClass().addAll("spacing-filter");

    HBox filterControlsHBox = new HBox();
    filterControlsHBox.getStyleClass().addAll("filter-section");

    // Status Filter
    VBox statusFilterVBox = createFilterField("Trạng thái", cbTrangThai = new ComboBox(), "filter-combobox");

    // Floor Filter
    VBox floorFilterVBox = createFilterField("Tầng", cbTang = new ComboBox(), "filter-combobox");
    cbTang.setOnAction(e -> this.handleLocTang());

    // Room Type Filter
    VBox roomTypeFilterVBox = createFilterField("Loại phòng", cbLocLoaiPhong = new ComboBox(), "filter-combobox");
    cbLocLoaiPhong.setOnAction(e -> this.handleLocLoaiPhong());

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

    filterControlsHBox.getChildren().addAll(statusFilterVBox, floorFilterVBox, roomTypeFilterVBox, searchFilterVBox, refreshButtonVBox);
    filterSectionVBox.getChildren().addAll(filterControlsHBox);

    // ============================================
    // TABLE SECTION
    // ============================================
    VBox tableContainerVBox = new VBox();
    tableContainerVBox.getStyleClass().addAll("table-container");

    tableViewPhong = new TableView();
    tableViewPhong.getStyleClass().addAll("table-view");
    tableViewPhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // Table Columns
    colMaPhong = new TableColumn();
    colMaPhong.setText("Mã Phòng");

    colTenLoaiPhong = new TableColumn();
    colTenLoaiPhong.setText("Loại phòng");

    colTang = new TableColumn();
    colTang.setText("Tầng");

    colTrangThai = new TableColumn();
    colTrangThai.setText("Trạng thái");

    colMoTa = new TableColumn();
    colMoTa.setText("Mô tả");

    colThaoTac = new TableColumn();
    colThaoTac.setText("Thao tác");

    tableViewPhong.getColumns().addAll(colMaPhong, colTenLoaiPhong, colTang, colTrangThai, colMoTa, colThaoTac);
    VBox.setVgrow(tableViewPhong, Priority.ALWAYS);
    tableContainerVBox.getChildren().addAll(tableViewPhong);
    VBox.setVgrow(tableContainerVBox, Priority.ALWAYS);

    // Assemble main content
    mainContentVBox.getChildren().addAll(headerSection, statsGrid, filterSectionVBox, tableContainerVBox);
    mainContainer.setCenter(mainContentVBox);
    BorderPane.setAlignment(mainContentVBox, Pos.CENTER);
    mainContainer.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));
    this.root = mainContainer;
    this.quanLyPhongController = new QuanLyPhongController(this);
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
  public Button getBtnThemPhong() {
    return btnThemPhong;
  }
  public Label getLblsoPhongTrong() {
    return lblsoPhongTrong;
  }
  public Label getLblSoPhongTrong() {
    return lblSoPhongTrong;
  }
  public Label getLblSoPhongSuDung() {
    return lblSoPhongSuDung;
  }
  public Label getLblSoPhongBaoTri() {
    return lblSoPhongBaoTri;
  }
  public ComboBox getCbTrangThai() {
    return cbTrangThai;
  }
  public ComboBox getCbTang() {
    return cbTang;
  }
  public ComboBox getCbLocLoaiPhong() {
    return cbLocLoaiPhong;
  }
  public TextField getTxtTimKiem() {
    return txtTimKiem;
  }
  public Button getBtnLamMoi() {
    return btnLamMoi;
  }
  public TableView getTableViewPhong() {
    return tableViewPhong;
  }
  public TableColumn getColMaPhong() {
    return colMaPhong;
  }
  public TableColumn getColTenLoaiPhong() {
    return colTenLoaiPhong;
  }
  public TableColumn getColTang() {
    return colTang;
  }
  public TableColumn getColTrangThai() {
    return colTrangThai;
  }
  public TableColumn getColMoTa() {
    return colMoTa;
  }

  public TableColumn getColThaoTac() {
      return colThaoTac;
  }
  
  private void handleThemPhong() {
    if (quanLyPhongController != null) {
      quanLyPhongController.handleThemPhong();
    }
  }
  private void handleLocTang() {
    if (quanLyPhongController != null) {
      quanLyPhongController.handleLocTang();
    }
  }
  private void handleLocLoaiPhong() {
    if (quanLyPhongController != null) {
      quanLyPhongController.handleLocLoaiPhong();
    }
  }

  private void handleLamMoi() {
        if (quanLyPhongController != null) {
        quanLyPhongController.handleLamMoi();
        }
  }
  private void handleTimKiem() {
    if (quanLyPhongController != null) {
      quanLyPhongController.handleTimKiem();
    }
  }
}
