package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.QuanLyNhanVienController;
import com.example.louishotelmanagement.ui.components.StatsCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class QuanLyNhanVienView {
  private static QuanLyNhanVienView instance;

  // Header components
  private Button btnThemNV;

  // StatsCard components for statistics
  private StatsCard totalEmployeesCard;
  private StatsCard receptionistsCard;
  private StatsCard otherEmployeesCard;
  private StatsCard managersCard;

  // Filter components
  private TextField timKiemField;
  private ComboBox<String> cbxChucVu;
  private Button btnLamMoi;

  // Table components
  private TableView nhanVienTable;
  private TableColumn colMaNV;
  private TableColumn colHoTen;
  private TableColumn colNgaySinh;
  private TableColumn colDiaChi;
  private TableColumn colSDT;
  private TableColumn colChucVu;
  private TableColumn colThaoTac;

  private Parent root;
  public QuanLyNhanVienController quanLyNhanVienController;

  public static QuanLyNhanVienView getInstance() {
    if (instance == null) {
      instance = new QuanLyNhanVienView();
    }
    return instance;
  }

  public static String getIdentifier() {
    return "quan-ly-nhan-vien-view";
  }

  public QuanLyNhanVienView() {
    // ============================================
    // MAIN CONTAINER SETUP
    // ============================================
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPrefHeight(768.0);
    mainContainer.setPrefWidth(1200.0);
    mainContainer.getStyleClass().addAll("main-background");
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
    headerIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/user.png").toExternalForm()));
    headerIconContainer.getChildren().addAll(headerIcon);

    VBox headerTitleContainer = new VBox();
    headerTitleContainer.getStyleClass().addAll("header-title-section");
    Label headerTitleLabel = new Label("Quản lý nhân viên");
    headerTitleLabel.getStyleClass().addAll("header-title");
    Label headerSubtitleLabel = new Label("Quản lý tài khoản đăng nhập của nhân viên");
    headerSubtitleLabel.getStyleClass().addAll("header-subtitle");
    headerTitleContainer.getChildren().addAll(headerTitleLabel, headerSubtitleLabel);
    HBox.setHgrow(headerTitleContainer, Priority.ALWAYS);

    VBox headerButtonContainer = new VBox();
    headerButtonContainer.getStyleClass().addAll("header-button-section");
    btnThemNV = new Button("➕ Thêm Tài Khoản");
    btnThemNV.getStyleClass().addAll("btn-primary");
    btnThemNV.setOnAction(e -> this.handleThemNV());
    btnThemNV.setCursor(Cursor.HAND);
    headerButtonContainer.getChildren().addAll(btnThemNV);

    headerSection.getChildren().addAll(headerIconContainer, headerTitleContainer, headerButtonContainer);

    // ============================================
    // STATISTICS GRID
    // ============================================
    GridPane statsGrid = new GridPane();
    statsGrid.setHgap(20.0);

    // Configure grid constraints
    ColumnConstraints colConstraint = new ColumnConstraints();
    colConstraint.setHgrow(Priority.ALWAYS);
    colConstraint.setPercentWidth(25.0);
    statsGrid.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint, colConstraint);

    RowConstraints rowConstraint = new RowConstraints();
    rowConstraint.setVgrow(Priority.SOMETIMES);
    statsGrid.getRowConstraints().addAll(rowConstraint);

    // Total Employees Stat Card
    totalEmployeesCard = new StatsCard(
        "Tổng số nhân viên", "0", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/user.png", "stats-card-icon-container-success"
    );

    // Receptionists Stat Card
    receptionistsCard = new StatsCard(
        "Lễ tân", "0", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/steward.png", "stats-card-icon-container-success"
    );
    GridPane.setColumnIndex(receptionistsCard, 1);

    // Other Employees Stat Card
    otherEmployeesCard = new StatsCard(
        "Nhân viên khác", "0", "stats-card-number-warning",
        "/com/example/louishotelmanagement/image/people.png", "stats-card-icon-container-warning"
    );
    GridPane.setColumnIndex(otherEmployeesCard, 2);

    // Managers Stat Card
    managersCard = new StatsCard(
        "Người Quản lý", "0", "stats-card-number-info",
        "/com/example/louishotelmanagement/image/profile.png", "stats-card-icon-container-info"
    );
    GridPane.setColumnIndex(managersCard, 3);

    statsGrid.getChildren().addAll(totalEmployeesCard, receptionistsCard, otherEmployeesCard, managersCard);

    // ============================================
    // FILTER SECTION
    // ============================================
    VBox filterSectionVBox = new VBox();
    filterSectionVBox.getStyleClass().addAll("spacing-filter");

    HBox filterControlsHBox = new HBox();
    filterControlsHBox.getStyleClass().addAll("filter-section");

    // Search Filter
    VBox searchFilterVBox = createFilterField("Tìm kiếm", timKiemField = new TextField(), "filter-input");
    timKiemField.setPromptText("Mã nhân viên/ số điện thoại / họ tên");
    timKiemField.setOnKeyReleased(e -> this.handleTimKiem());
    HBox.setHgrow(searchFilterVBox, Priority.ALWAYS);

    // Role Filter
    VBox roleFilterVBox = createFilterField("Chức vụ", cbxChucVu = new ComboBox(), "filter-combobox");
    cbxChucVu.setPromptText("Tất cả chức vụ");
    cbxChucVu.setOnAction(e -> this.handleLocChucVu());

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

    filterControlsHBox.getChildren().addAll(searchFilterVBox, roleFilterVBox, refreshButtonVBox);
    filterSectionVBox.getChildren().addAll(filterControlsHBox);

    // ============================================
    // TABLE SECTION
    // ============================================
    VBox tableContainerVBox = new VBox();
    tableContainerVBox.getStyleClass().addAll("table-container");
    tableContainerVBox.setPrefHeight(400.0);

    nhanVienTable = new TableView();
    nhanVienTable.getStyleClass().addAll("table-view");
    nhanVienTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    VBox.setVgrow(nhanVienTable, Priority.ALWAYS);

    // Table Columns
    colMaNV = new TableColumn();
    colMaNV.setText("Mã nhân viên");

    colHoTen = new TableColumn();
    colHoTen.setText("Họ tên");

    colNgaySinh = new TableColumn();
    colNgaySinh.setText("Ngày sinh");

    colDiaChi = new TableColumn();
    colDiaChi.setText("Địa chỉ");

    colSDT = new TableColumn();
    colSDT.setText("Số điện thoại");

    colChucVu = new TableColumn();
    colChucVu.setText("chức vụ");

    colThaoTac = new TableColumn();
    colThaoTac.setText("Thao tác");

    nhanVienTable.getColumns().addAll(colMaNV, colHoTen, colNgaySinh, colDiaChi, colSDT, colChucVu, colThaoTac);
    nhanVienTable.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));

    tableContainerVBox.getChildren().addAll(nhanVienTable);
    VBox.setVgrow(tableContainerVBox, Priority.ALWAYS);

    // Assemble main content
    mainContentVBox.getChildren().addAll(headerSection, statsGrid, filterSectionVBox, tableContainerVBox);
    mainContainer.setCenter(mainContentVBox);
    BorderPane.setAlignment(mainContentVBox, Pos.CENTER);
    mainContainer.setPadding(new Insets(32.0, 32.0, 32.0, 32.0));

    this.root = mainContainer;
    this.quanLyNhanVienController = new QuanLyNhanVienController(this);
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
  public Button getBtnThemNV() {
    return btnThemNV;
  }

  public StatsCard getTotalEmployeesCard() {
    return totalEmployeesCard;
  }

  public StatsCard getReceptionistsCard() {
    return receptionistsCard;
  }

  public StatsCard getOtherEmployeesCard() {
    return otherEmployeesCard;
  }

  public StatsCard getManagersCard() {
    return managersCard;
  }

  public TextField getTimKiemField() {
    return timKiemField;
  }

  public ComboBox<String> getCbxChucVu() {
    return cbxChucVu;
  }

  public Button getBtnLamMoi() {
    return btnLamMoi;
  }

  public TableView getNhanVienTable() {
    return nhanVienTable;
  }

  public TableColumn getColMaNV() {
    return colMaNV;
  }

  public TableColumn getColHoTen() {
    return colHoTen;
  }

  public TableColumn getColNgaySinh() {
    return colNgaySinh;
  }

  public TableColumn getColDiaChi() {
    return colDiaChi;
  }

  public TableColumn getColSDT() {
    return colSDT;
  }

  public TableColumn getColChucVu() {
    return colChucVu;
  }

  public TableColumn getColThaoTac() {
    return colThaoTac;
  }

  // Event handlers
  private void handleThemNV() {
    if (quanLyNhanVienController != null) {
      quanLyNhanVienController.handleThemNV();
    }
  }

  private void handleTimKiem() {
    if (quanLyNhanVienController != null) {
      quanLyNhanVienController.handleTimKiem();
    }
  }

  private void handleLocChucVu() {
    if (quanLyNhanVienController != null) {
      quanLyNhanVienController.handleLocChucVu();
    }
  }

  private void handleLamMoi() {
    if (quanLyNhanVienController != null) {
      quanLyNhanVienController.handleLamMoi();
    }
  }
}
