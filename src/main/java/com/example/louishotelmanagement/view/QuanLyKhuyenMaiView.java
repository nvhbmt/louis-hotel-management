package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.QuanLyKhuyenMaiController;
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

public class QuanLyKhuyenMaiView {
  private static QuanLyKhuyenMaiView instance;

  private Button btnLamMoi;
  private Button btnThemKhuyenMai;
  private TextField txtTimKiem;
  private ComboBox<String> cbKieuGiamGia;
  private ComboBox<String> cbTrangThai;
  private TableView tableViewKhuyenMai;
  private TableColumn colMaKM;
  private TableColumn colCode;
  private TableColumn colGiamGia;
  private TableColumn colKieuGiamGia;
  private TableColumn colNgayBatDau;
  private TableColumn colNgayKetThuc;
  private TableColumn colTongTienToiThieu;
  private TableColumn colTrangThai;
  private TableColumn colMoTa;
  private TableColumn colThaoTac;

  // StatsCard components for statistics
  private StatsCard totalCouponsCard;
  private StatsCard activeCouponsCard;
  private StatsCard expiredCouponsCard;
  private StatsCard upcomingCouponsCard;
  private StatsCard disabledCouponsCard;

  private Parent root;
  public QuanLyKhuyenMaiController quanLyKhuyenMaiController;

  public static QuanLyKhuyenMaiView getInstance() {
    if (instance == null) {
      instance = new QuanLyKhuyenMaiView();
    }
    return instance;
  }

  public static String getIdentifier() {
    return "quan-ly-khuyen-mai-view";
  }

  public QuanLyKhuyenMaiView() {
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
    headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/coupon.png").toExternalForm()));
    headerIconContainer.getChildren().addAll(headerIcon);

    VBox headerTitleContainer = new VBox();
    headerTitleContainer.getStyleClass().addAll("header-title-section");
    Label headerTitleLabel = new Label("Quản Lý Khuyến Mãi");
    headerTitleLabel.getStyleClass().addAll("header-title");
    Label headerSubtitleLabel = new Label("Quản lý các khuyến mãi trong khách sạn");
    headerSubtitleLabel.getStyleClass().addAll("header-subtitle");
    headerTitleContainer.getChildren().addAll(headerTitleLabel, headerSubtitleLabel);
    HBox.setHgrow(headerTitleContainer, Priority.ALWAYS);

    VBox headerButtonContainer = new VBox();
    headerButtonContainer.getStyleClass().addAll("header-button-section");
    btnThemKhuyenMai = new Button("➕ Thêm khuyến mãi");
    btnThemKhuyenMai.getStyleClass().addAll("btn-primary");
    btnThemKhuyenMai.setOnAction(e -> this.handleThemKhuyenMai());
    btnThemKhuyenMai.setCursor(Cursor.HAND);
    headerButtonContainer.getChildren().addAll(btnThemKhuyenMai);

    headerSection.getChildren().addAll(headerIconContainer, headerTitleContainer, headerButtonContainer);

    // ============================================
    // STATISTICS GRID
    // ============================================
    GridPane statsGrid = new GridPane();
    statsGrid.getStyleClass().addAll("stats-grid");

    // Configure grid constraints
    ColumnConstraints colConstraint = new ColumnConstraints();
    colConstraint.setHgrow(Priority.SOMETIMES);
    colConstraint.setPercentWidth(20.0);
    statsGrid.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint, colConstraint, colConstraint);

    RowConstraints rowConstraint = new RowConstraints();
    rowConstraint.setVgrow(Priority.SOMETIMES);
    statsGrid.getRowConstraints().addAll(rowConstraint);

    // Total Coupons Stat Card
    totalCouponsCard = new StatsCard(
        "Tổng số mã", "69", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/coupons.png", "stats-card-icon-container-success"
    );

    // Active Coupons Stat Card
    activeCouponsCard = new StatsCard(
        "Đang sử dụng", "15", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/green-circle.png", "stats-card-icon-container-success"
    );
    GridPane.setColumnIndex(activeCouponsCard, 1);

    // Expired Coupons Stat Card
    expiredCouponsCard = new StatsCard(
        "Đã hết hạn", "14", "stats-card-number-warning",
        "/com/example/louishotelmanagement/image/cross.png", "stats-card-icon-container-warning"
    );
    GridPane.setColumnIndex(expiredCouponsCard, 2);

    // Upcoming Coupons Stat Card
    upcomingCouponsCard = new StatsCard(
        "Chưa bắt đầu", "10", "stats-card-number-info",
        "/com/example/louishotelmanagement/image/wait.png", "stats-card-icon-container-info"
    );
    GridPane.setColumnIndex(upcomingCouponsCard, 3);

    // Disabled Coupons Stat Card
    disabledCouponsCard = new StatsCard(
        "Vô hiệu hóa", "5", "stats-card-number-danger",
        "/com/example/louishotelmanagement/image/remove.png", "stats-card-icon-container-danger"
    );
    GridPane.setColumnIndex(disabledCouponsCard, 4);

    statsGrid.getChildren().addAll(totalCouponsCard, activeCouponsCard, expiredCouponsCard, upcomingCouponsCard, disabledCouponsCard);
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
    txtTimKiem.setPromptText("Nhập mã giảm giá...");
    txtTimKiem.setOnKeyReleased(e -> this.handleTimKiem());
    HBox.setHgrow(searchFilterVBox, Priority.ALWAYS);

    // Type Filter
    VBox typeFilterVBox = createFilterField("Kiểu giảm giá", cbKieuGiamGia = new ComboBox(), "filter-combobox");
    cbKieuGiamGia.setPromptText("Tất cả kiểu giảm giá");
    cbKieuGiamGia.setOnAction(e -> this.handleLocKieuGiamGia());

    // Status Filter
    VBox statusFilterVBox = createFilterField("Trạng thái", cbTrangThai = new ComboBox(), "filter-combobox");
    cbTrangThai.setPromptText("Tất cả trạng thái");
    cbTrangThai.setOnAction(e -> this.handleLocTrangThai());

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

    filterControlsHBox.getChildren().addAll(searchFilterVBox, typeFilterVBox, statusFilterVBox, refreshButtonVBox);
    filterSectionVBox.getChildren().addAll(filterControlsHBox);

    // ============================================
    // TABLE SECTION
    // ============================================
    VBox tableContainerVBox = new VBox();
    tableContainerVBox.getStyleClass().addAll("table-container");
    tableContainerVBox.setPrefHeight(561.0);
    tableContainerVBox.setPrefWidth(890.0);

    tableViewKhuyenMai = new TableView();
    tableViewKhuyenMai.getStyleClass().addAll("table-view");
    tableViewKhuyenMai.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // Table Columns
    colMaKM = new TableColumn();
    colMaKM.setText("Mã Khuyến Mãi");

    colCode = new TableColumn();
    colCode.setText("Code");

    colGiamGia = new TableColumn();
    colGiamGia.setText("Giá Trị");

    colKieuGiamGia = new TableColumn();
    colKieuGiamGia.setText("Kiểu");

    colNgayBatDau = new TableColumn();
    colNgayBatDau.setText("Ngày Bắt Đầu");

    colNgayKetThuc = new TableColumn();
    colNgayKetThuc.setText("Ngày Kết Thúc");

    colTongTienToiThieu = new TableColumn();
    colTongTienToiThieu.setText("Tổng Tiền Tối Thiểu");

    colTrangThai = new TableColumn();
    colTrangThai.setText("Trạng Thái");

    colMoTa = new TableColumn();
    colMoTa.setText("Mô Tả");

    colThaoTac = new TableColumn();
    colThaoTac.setText("Thao Tác");

    tableViewKhuyenMai.getColumns().addAll(colMaKM, colCode, colGiamGia, colKieuGiamGia, colNgayBatDau, colNgayKetThuc, colTongTienToiThieu, colTrangThai, colMoTa, colThaoTac);
    tableViewKhuyenMai.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
    VBox.setVgrow(tableViewKhuyenMai, Priority.ALWAYS);
    tableContainerVBox.getChildren().addAll(tableViewKhuyenMai);
    VBox.setVgrow(tableContainerVBox, Priority.ALWAYS);

    // Assemble main content
    mainContentVBox.getChildren().addAll(headerSection, statsGrid, filterSectionVBox, tableContainerVBox);
    mainContainer.setCenter(mainContentVBox);
    BorderPane.setAlignment(mainContentVBox, Pos.CENTER);
    mainContainer.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));
    this.root = mainContainer;
    this.quanLyKhuyenMaiController = new QuanLyKhuyenMaiController(this);
  }

  /**
   * Helper method to create a filter field
   */
  private VBox createFilterField(String labelText, javafx.scene.control.Control control, String controlStyleClass) {
    VBox filterFieldVBox = new VBox();
    filterFieldVBox.getStyleClass().addAll("filter-field-container");

    Label fieldLabel = new Label(labelText);
    fieldLabel.getStyleClass().addAll("filter-label");
    filterFieldVBox.getChildren().add(fieldLabel);

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

  public Button getBtnThemKhuyenMai() {
    return btnThemKhuyenMai;
  }

  public TextField getTxtTimKiem() {
    return txtTimKiem;
  }

  public ComboBox<String> getCbKieuGiamGia() {
    return cbKieuGiamGia;
  }

  public ComboBox<String> getCbTrangThai() {
    return cbTrangThai;
  }

  public TableView getTableViewKhuyenMai() {
    return tableViewKhuyenMai;
  }

  public TableColumn getColMaKM() {
    return colMaKM;
  }

  public TableColumn getColCode() {
    return colCode;
  }

  public TableColumn getColGiamGia() {
    return colGiamGia;
  }

  public TableColumn getColKieuGiamGia() {
    return colKieuGiamGia;
  }

  public TableColumn getColNgayBatDau() {
    return colNgayBatDau;
  }

  public TableColumn getColNgayKetThuc() {
    return colNgayKetThuc;
  }

  public TableColumn getColTongTienToiThieu() {
    return colTongTienToiThieu;
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

  public StatsCard getTotalCouponsCard() {
    return totalCouponsCard;
  }

  public StatsCard getActiveCouponsCard() {
    return activeCouponsCard;
  }

  public StatsCard getExpiredCouponsCard() {
    return expiredCouponsCard;
  }

  public StatsCard getUpcomingCouponsCard() {
    return upcomingCouponsCard;
  }

  public StatsCard getDisabledCouponsCard() {
    return disabledCouponsCard;
  }

  private void handleLamMoi() {
        if (quanLyKhuyenMaiController != null) {
        quanLyKhuyenMaiController.handleLamMoi();
        }
  }

  private void handleTimKiem() {
    if (quanLyKhuyenMaiController != null) {
      quanLyKhuyenMaiController.handleTimKiem();
    }
  }

  private void handleLocKieuGiamGia() {
    if (quanLyKhuyenMaiController != null) {
      quanLyKhuyenMaiController.handleLocKieuGiamGia();
    }
  }

  private void handleLocTrangThai() {
    if (quanLyKhuyenMaiController != null) {
      quanLyKhuyenMaiController.handleLocTrangThai();
    }
  }

  private void handleThemKhuyenMai() {
    if (quanLyKhuyenMaiController != null) {
      quanLyKhuyenMaiController.handleThemKhuyenMai();
    }
  }
}
