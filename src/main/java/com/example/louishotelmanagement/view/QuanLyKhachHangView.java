package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.QuanLyKhachHangController;
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

public class QuanLyKhachHangView {
  private static QuanLyKhachHangView instance;

  private Button btnLamMoi;
  private Button btnThemKhachHang;
  private TextField txtTimKiem;
  private ComboBox<String> cmbHang;
  private ComboBox<String> cmbTrangThai;
  private TableView tableViewKhachHang;
  private TableColumn colMaKH;
  private TableColumn colHoTen;
  private TableColumn colSoDT;
  private TableColumn colEmail;
  private TableColumn colGhiChu;
  private TableColumn colHangKhach;
  private TableColumn colTrangThai;
  private TableColumn colThaoTac;

  // StatsCard components for statistics
  private StatsCard totalCustomersCard;
  private StatsCard stayingCustomersCard;
  private StatsCard checkedOutCustomersCard;
  private StatsCard vipCustomersCard;

  private Parent root;
  public QuanLyKhachHangController quanLyKhachHangController;

  public static QuanLyKhachHangView getInstance() {
    if (instance == null) {
      instance = new QuanLyKhachHangView();
    }
    return instance;
  }

  public static String getIdentifier() {
    return "quan-ly-khach-hang-view";
  }

  public QuanLyKhachHangView() {
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
    headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/detective.png").toExternalForm()));
    headerIconContainer.getChildren().addAll(headerIcon);

    VBox headerTitleContainer = new VBox();
    headerTitleContainer.getStyleClass().addAll("header-title-section");
    Label headerTitleLabel = new Label("Quản Lý Khách Hàng");
    headerTitleLabel.getStyleClass().addAll("header-title");
    Label headerSubtitleLabel = new Label("Quản lý thông tin khách hàng đã và đang sử dụng dịch vụ khách sạn");
    headerSubtitleLabel.getStyleClass().addAll("header-subtitle");
    headerTitleContainer.getChildren().addAll(headerTitleLabel, headerSubtitleLabel);
    HBox.setHgrow(headerTitleContainer, Priority.ALWAYS);

    VBox headerButtonContainer = new VBox();
    headerButtonContainer.getStyleClass().addAll("header-button-section");
    btnThemKhachHang = new Button("➕ Thêm Khách Hàng");
    btnThemKhachHang.getStyleClass().addAll("btn-primary");
    btnThemKhachHang.setOnAction(e -> this.handleThemKhachHang());
    btnThemKhachHang.setCursor(Cursor.HAND);
    headerButtonContainer.getChildren().addAll(btnThemKhachHang);

    headerSection.getChildren().addAll(headerIconContainer, headerTitleContainer, headerButtonContainer);

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

    // Total Customers Stat Card
    totalCustomersCard = new StatsCard(
        "Số khách hàng", "69", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/users.png", "stats-card-icon-container-success"
    );

    // Staying Customers Stat Card
    stayingCustomersCard = new StatsCard(
        "Đang lưu trú", "15", "stats-card-number-warning",
        "/com/example/louishotelmanagement/image/double-bed.png", "stats-card-icon-container-warning"
    );
    GridPane.setColumnIndex(stayingCustomersCard, 1);

    // Checked Out Customers Stat Card
    checkedOutCustomersCard = new StatsCard(
        "Đã check-out", "14", "stats-card-number-info",
        "/com/example/louishotelmanagement/image/door-open.png", "stats-card-icon-container-info"
    );
    GridPane.setColumnIndex(checkedOutCustomersCard, 2);

    // VIP Customers Stat Card
    vipCustomersCard = new StatsCard(
        "Khách VIP", "10", "stats-card-number-danger",
        "/com/example/louishotelmanagement/image/crown.png", "stats-card-icon-container-danger"
    );
    GridPane.setColumnIndex(vipCustomersCard, 3);

    statsGrid.getChildren().addAll(totalCustomersCard, stayingCustomersCard, checkedOutCustomersCard, vipCustomersCard);
    VBox.setVgrow(statsGrid, Priority.NEVER);

    // ============================================
    // FILTER SECTION
    // ============================================
    VBox filterSectionVBox = new VBox();
    filterSectionVBox.getStyleClass().addAll("spacing-filter");

    HBox filterControlsHBox = new HBox();
    filterControlsHBox.getStyleClass().addAll("filter-section");

    // Refresh Button
    VBox refreshButtonVBox = createFilterField("", btnLamMoi = new Button("🔄 Làm Mới"), "btn-secondary");
    btnLamMoi.setOnAction(e -> this.handleLamMoi());
    btnLamMoi.setCursor(Cursor.HAND);

    // Search Filter
    VBox searchFilterVBox = createFilterField("Tìm kiếm", txtTimKiem = new TextField(), "filter-input");
    txtTimKiem.setPromptText("Tìm tên, số điện thoại, email...");
    txtTimKiem.setOnKeyReleased(e -> this.handleTimKiem());
    HBox.setHgrow(searchFilterVBox, Priority.ALWAYS);

    // Rank Filter
    VBox rankFilterVBox = createFilterField("Hạng khách", cmbHang = new ComboBox(), "filter-combobox");
    cmbHang.setPromptText("Hạng khách");
    cmbHang.setOnAction(e -> this.handleTimKiem());

    // Status Filter
    VBox statusFilterVBox = createFilterField("Trạng thái", cmbTrangThai = new ComboBox(), "filter-combobox");
    cmbTrangThai.setPromptText("Tất cả trạng thái");
    cmbTrangThai.setOnAction(e -> this.handleTimKiem());

    filterControlsHBox.getChildren().addAll(searchFilterVBox, rankFilterVBox, statusFilterVBox,refreshButtonVBox);
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
    colMaKH = new TableColumn();
    colMaKH.setText("Mã Khách Hàng");

    colHoTen = new TableColumn();
    colHoTen.setText("Họ và Tên");

    colSoDT = new TableColumn();
    colSoDT.setText("Số điện thoại");

    colEmail = new TableColumn();
    colEmail.setText("Email");

    colGhiChu = new TableColumn();
    colGhiChu.setText("Ghi chú");

    colHangKhach = new TableColumn();
    colHangKhach.setText("Hạng khách");

    colTrangThai = new TableColumn();
    colTrangThai.setText("Trạng thái");

    colThaoTac = new TableColumn();
    colThaoTac.setText("Thao tác");

    tableViewKhachHang.getColumns().addAll(colMaKH, colHoTen, colSoDT, colEmail, colGhiChu, colHangKhach, colTrangThai, colThaoTac);
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
    this.quanLyKhachHangController = new QuanLyKhachHangController(this);
  }

  /**
   * Helper method to create a filter field
   */
  private VBox createFilterField(String labelText, javafx.scene.control.Control control, String controlStyleClass) {
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

  public Button getBtnLamMoi() {
    return btnLamMoi;
  }

  public Button getBtnThemKhachHang() {
    return btnThemKhachHang;
  }

  public TextField getTxtTimKiem() {
    return txtTimKiem;
  }

  public ComboBox<String> getCmbHang() {
    return cmbHang;
  }

  public ComboBox<String> getCmbTrangThai() {
    return cmbTrangThai;
  }

  public TableView getTableViewKhachHang() {
    return tableViewKhachHang;
  }

  public TableColumn getColMaKH() {
    return colMaKH;
  }

  public TableColumn getColHoTen() {
    return colHoTen;
  }

  public TableColumn getColSoDT() {
    return colSoDT;
  }

  public TableColumn getColEmail() {
    return colEmail;
  }

  public TableColumn getColGhiChu() {
    return colGhiChu;
  }

  public TableColumn getColHangKhach() {
    return colHangKhach;
  }

  public TableColumn getColTrangThai() {
    return colTrangThai;
  }

  public TableColumn getColThaoTac() {
    return colThaoTac;
  }

  public StatsCard getTotalCustomersCard() {
    return totalCustomersCard;
  }

  public StatsCard getStayingCustomersCard() {
    return stayingCustomersCard;
  }

  public StatsCard getCheckedOutCustomersCard() {
    return checkedOutCustomersCard;
  }

  public StatsCard getVipCustomersCard() {
    return vipCustomersCard;
  }

  private void handleLamMoi() {
        if (quanLyKhachHangController != null) {
        quanLyKhachHangController.handleLamMoi();
        }
  }

  private void handleTimKiem() {
    if (quanLyKhachHangController != null) {
      quanLyKhachHangController.handleTimKiem();
    }
  }

  private void handleThemKhachHang() {
    if (quanLyKhachHangController != null) {
      quanLyKhachHangController.handleThemKhachHang();
    }
  }
}
