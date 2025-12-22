package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.DatDichVuController;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class DatDichVuView {
  private static DatDichVuView instance;

  // Left panel components
  private TextField txtTimKiem;
  private FlowPane pnDanhSachDichVu;

  // Right panel components
  private ComboBox<String> dsKhachHang;
  private ComboBox<String> dsPhong;
  private TextField maNV;
  private TableView tblGioHang;
  private TableColumn colGH_Ten;
  private TableColumn colGH_SL;
  private TableColumn colGH_ThanhTien;
  private TableColumn colGH_Xoa;
  private TextArea txtGhiChu;
  private Label lblTongTienTam;
  private Button btnXacNhanLapPhieu;

  private Parent root;
  public DatDichVuController datDichVuController;

  public static DatDichVuView getInstance() {
    if (instance == null) {
      instance = new DatDichVuView();
    }
    return instance;
  }

  public static String getIdentifier() {
    return "dat-dich-vu-view";
  }

  public DatDichVuView() {
    // ============================================
    // MAIN CONTAINER SETUP
    // ============================================
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPrefHeight(768.0);
    mainContainer.setPrefWidth(1200.0);
    mainContainer.getStyleClass().addAll("main-background");
    mainContainer.getStylesheets().addAll(
        getClass().getResource("/com/example/louishotelmanagement/css/dat-dich-vu-view.css").toExternalForm()
    );

    // ============================================
    // SPLIT PANE SETUP
    // ============================================
    SplitPane splitPane = new SplitPane();
    splitPane.setDividerPositions(0.65);
    splitPane.getStyleClass().addAll("main-background");

    // ============================================
    // LEFT PANEL - SERVICE LIST
    // ============================================
    VBox leftPanel = new VBox(10.0);
    leftPanel.getStyleClass().addAll("left-panel");
    leftPanel.setPadding(new Insets(15.0, 15.0, 15.0, 15.0));

    // Header with title and search
    HBox headerHBox = new HBox(10.0);
    headerHBox.setAlignment(Pos.CENTER_LEFT);

    Label serviceTitleLabel = new Label("Danh Sách Dịch Vụ");
    serviceTitleLabel.getStyleClass().addAll("service-title");

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    txtTimKiem = new TextField();
    txtTimKiem.setPrefWidth(250.0);
    txtTimKiem.setPromptText("Tìm tên dịch vụ...");
    txtTimKiem.getStyleClass().addAll("search-field");
    txtTimKiem.setOnKeyReleased(e -> this.handleTimKiem());

    headerHBox.getChildren().addAll(serviceTitleLabel, spacer, txtTimKiem);

    // Separator
    Separator separator = new Separator();
    separator.setPrefWidth(200.0);

    // Service list scroll pane
    ScrollPane serviceScrollPane = new ScrollPane();
    serviceScrollPane.setFitToWidth(true);
    serviceScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    serviceScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    serviceScrollPane.getStyleClass().addAll("transparent-scroll");
    VBox.setVgrow(serviceScrollPane, Priority.ALWAYS);

    pnDanhSachDichVu = new FlowPane();
    pnDanhSachDichVu.setHgap(15.0);
    pnDanhSachDichVu.setVgap(15.0);
    pnDanhSachDichVu.getStyleClass().addAll("flow-container");

    serviceScrollPane.setContent(pnDanhSachDichVu);

    leftPanel.getChildren().addAll(headerHBox, separator, serviceScrollPane);

    // ============================================
    // RIGHT PANEL - ORDER DETAILS
    // ============================================
    VBox rightPanel = new VBox(15.0);
    rightPanel.getStyleClass().addAll("right-panel");
    rightPanel.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));

    // Order title
    HBox titleHBox = new HBox(10.0);
    titleHBox.setAlignment(Pos.CENTER_LEFT);

    ImageView bellIcon = new ImageView();
    bellIcon.setFitHeight(30.0);
    bellIcon.setFitWidth(30.0);
    bellIcon.setPickOnBounds(true);
    bellIcon.setPreserveRatio(true);
    bellIcon.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/desk-bell.png").toExternalForm()));

    Label orderTitleLabel = new Label("Thông Tin Đặt");
    orderTitleLabel.getStyleClass().addAll("order-title");

    titleHBox.getChildren().addAll(bellIcon, orderTitleLabel);

    // Customer/Employee info grid
    GridPane infoGrid = new GridPane();
    infoGrid.setHgap(10.0);
    infoGrid.setVgap(10.0);

    // Configure grid columns
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setHgrow(Priority.SOMETIMES);
    col1.setMaxWidth(120.0);
    col1.setMinWidth(10.0);
    col1.setPrefWidth(90.0);

    ColumnConstraints col2 = new ColumnConstraints();
    col2.setHgrow(Priority.SOMETIMES);
    col2.setMinWidth(10.0);

    infoGrid.getColumnConstraints().addAll(col1, col2);

    // Configure grid rows
    RowConstraints row1 = new RowConstraints();
    row1.setMinHeight(10.0);
    row1.setVgrow(Priority.SOMETIMES);

    RowConstraints row2 = new RowConstraints();
    row2.setMinHeight(10.0);
    row2.setVgrow(Priority.SOMETIMES);

    RowConstraints row3 = new RowConstraints();
    row3.setMinHeight(10.0);
    row3.setVgrow(Priority.SOMETIMES);

    infoGrid.getRowConstraints().addAll(row1, row2, row3);

    // Customer label and combo
    Label customerLabel = new Label("Khách hàng:");
    customerLabel.setTextFill(javafx.scene.paint.Color.web("#555555"));

    dsKhachHang = new ComboBox<>();
    dsKhachHang.setMaxWidth(Double.MAX_VALUE);
    dsKhachHang.setPromptText("Chọn khách hàng");
    GridPane.setColumnIndex(dsKhachHang, 1);

    // Room label and combo
    Label roomLabel = new Label("Phòng:");
    roomLabel.setTextFill(javafx.scene.paint.Color.web("#555555"));
    GridPane.setRowIndex(roomLabel, 1);

    dsPhong = new ComboBox<>();
    dsPhong.setMaxWidth(Double.MAX_VALUE);
    dsPhong.setPromptText("Chọn phòng");
    GridPane.setColumnIndex(dsPhong, 1);
    GridPane.setRowIndex(dsPhong, 1);

    // Employee label and field
    Label employeeLabel = new Label("Nhân viên:");
    employeeLabel.setTextFill(javafx.scene.paint.Color.web("#555555"));
    GridPane.setRowIndex(employeeLabel, 2);

    maNV = new TextField();
    maNV.setEditable(false);
    maNV.getStyleClass().addAll("employee-field");
    maNV.setText("NV001");
    GridPane.setColumnIndex(maNV, 1);
    GridPane.setRowIndex(maNV, 2);

    infoGrid.getChildren().addAll(
        customerLabel, dsKhachHang,
        roomLabel, dsPhong,
        employeeLabel, maNV
    );

    // Separator
    Separator infoSeparator = new Separator();
    infoSeparator.setPrefWidth(200.0);

    // Cart title
    Label cartTitleLabel = new Label("Dịch vụ đã chọn");
    cartTitleLabel.getStyleClass().addAll("bold-label");

    // Cart table
    tblGioHang = new TableView();
    tblGioHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    VBox.setVgrow(tblGioHang, Priority.ALWAYS);

    // Table columns
    colGH_Ten = new TableColumn();
    colGH_Ten.setText("Tên DV");
    colGH_Ten.setPrefWidth(130.0);

    colGH_SL = new TableColumn();
    colGH_SL.setText("SL");
    colGH_SL.setPrefWidth(50.0);
    colGH_SL.getStyleClass().addAll("center-align");

    colGH_ThanhTien = new TableColumn();
    colGH_ThanhTien.setText("Thành tiền");
    colGH_ThanhTien.setPrefWidth(100.0);
    colGH_ThanhTien.getStyleClass().addAll("center-right-align");

    colGH_Xoa = new TableColumn();
    colGH_Xoa.setText("#");
    colGH_Xoa.setPrefWidth(50.0);
    colGH_Xoa.getStyleClass().addAll("center-align");

    tblGioHang.getColumns().addAll(colGH_Ten, colGH_SL, colGH_ThanhTien, colGH_Xoa);

    // Summary section
    VBox summaryVBox = new VBox(10.0);
    summaryVBox.getStyleClass().addAll("summary-container");

    txtGhiChu = new TextArea();
    txtGhiChu.setPrefHeight(50.0);
    txtGhiChu.setPromptText("Ghi chú (nếu có)...");
    txtGhiChu.setWrapText(true);

    HBox totalHBox = new HBox();
    totalHBox.setAlignment(Pos.CENTER_RIGHT);

    Label totalLabel = new Label("Tổng cộng: ");
    Font totalLabelFont = new Font(16.0);
    totalLabel.setFont(totalLabelFont);

    lblTongTienTam = new Label("0 VND");
    lblTongTienTam.getStyleClass().addAll("total-amount");
    Font totalAmountFont = Font.font("System Bold", 20.0);
    lblTongTienTam.setFont(totalAmountFont);

    totalHBox.getChildren().addAll(totalLabel, lblTongTienTam);

    btnXacNhanLapPhieu = new Button("XÁC NHẬN ĐẶT DỊCH VỤ");
    btnXacNhanLapPhieu.setMaxWidth(Double.MAX_VALUE);
    btnXacNhanLapPhieu.setMnemonicParsing(false);
    btnXacNhanLapPhieu.setPrefHeight(40.0);
    btnXacNhanLapPhieu.getStyleClass().addAll("confirm-button");
    btnXacNhanLapPhieu.setOnAction(e -> this.handleXacNhanLapPhieu());
    btnXacNhanLapPhieu.setCursor(Cursor.HAND);

    summaryVBox.getChildren().addAll(txtGhiChu, totalHBox, btnXacNhanLapPhieu);

    rightPanel.getChildren().addAll(titleHBox, infoGrid, infoSeparator, cartTitleLabel, tblGioHang, summaryVBox);

    // Add panels to split pane
    splitPane.getItems().addAll(leftPanel, rightPanel);

    mainContainer.setCenter(splitPane);
    BorderPane.setAlignment(splitPane, Pos.CENTER);

    this.root = mainContainer;
    this.datDichVuController = new DatDichVuController(this);
  }

  // Helper method to create filter field
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
    filterFieldVBox.getChildren().add(control);
    return filterFieldVBox;
  }

  public Parent getRoot() {
    return root;
  }

  // Getters for controller
  public TextField getTxtTimKiem() {
    return txtTimKiem;
  }

  public FlowPane getPnDanhSachDichVu() {
    return pnDanhSachDichVu;
  }

  public ComboBox<String> getDsKhachHang() {
    return dsKhachHang;
  }

  public ComboBox<String> getDsPhong() {
    return dsPhong;
  }

  public TextField getMaNV() {
    return maNV;
  }

  public TableView getTblGioHang() {
    return tblGioHang;
  }

  public TableColumn getColGH_Ten() {
    return colGH_Ten;
  }

  public TableColumn getColGH_SL() {
    return colGH_SL;
  }

  public TableColumn getColGH_ThanhTien() {
    return colGH_ThanhTien;
  }

  public TableColumn getColGH_Xoa() {
    return colGH_Xoa;
  }

  public TextArea getTxtGhiChu() {
    return txtGhiChu;
  }

  public Label getLblTongTienTam() {
    return lblTongTienTam;
  }

  public Button getBtnXacNhanLapPhieu() {
    return btnXacNhanLapPhieu;
  }

  // Event handlers
  private void handleTimKiem() {
    if (datDichVuController != null) {
      datDichVuController.handleTimKiem();
    }
  }

  private void handleXacNhanLapPhieu() {
    if (datDichVuController != null) {
      try {
        datDichVuController.handleXacNhanLapPhieu(null);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
