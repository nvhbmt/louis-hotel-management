package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.ThongKeController;
import com.example.louishotelmanagement.ui.components.StatsCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ThongKeView {
  private static ThongKeView instance;

  // Header components
  private Button btnLamMoi;
  private Label lblCapNhatCuoi;

  // StatsCard components for statistics
  private StatsCard totalRoomsCard;
  private StatsCard occupiedRoomsCard;
  private StatsCard totalCustomersCard;
  private StatsCard roomTypesCard;

  // Chart components
  private ToggleButton btnTheoNgay;
  private ToggleButton btnTheoTuan;
  private ToggleButton btnTheoThang;
  private HBox hboxChonNgay;
  private DatePicker dpTuNgay;
  private DatePicker dpDenNgay;
  private HBox hboxChonNam;
  private ComboBox<Integer> cbNam;
  private Button btnCapNhatChart;
  private BarChart<String, Number> thongkeBarChart;
  private Label lblTongDoanhThu;

  // Room status components
  private Label lblPhongTrong;
  private Label lblPhongDaDat;
  private Label lblPhongDangSuDungChiTiet;
  private Label lblPhongBaoTri;
  private Rectangle barPhongTrong;
  private Label lblTiLeTrong;
  private Rectangle barPhongDaDat;
  private Label lblTiLeDaDat;
  private Rectangle barPhongDangSuDung;
  private Label lblTiLeDangSuDung;
  private Rectangle barPhongBaoTri;
  private Label lblTiLeBaoTri;

  // Room types components
  private VBox vboxLoaiPhong;
  private Label lblGiaTrungBinh;
  private Label lblGiaCaoNhat;
  private Label lblGiaThapNhat;

  private Parent root;
  public ThongKeController thongKeController;

  public static ThongKeView getInstance() {
    if (instance == null) {
      instance = new ThongKeView();
    }
    return instance;
  }

  public static String getIdentifier() {
    return "thong-ke-view";
  }

  public ThongKeView() {
    // ============================================
    // MAIN CONTAINER SETUP
    // ============================================
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPrefHeight(1092.0);
    mainContainer.setPrefWidth(1179.0);
    mainContainer.getStyleClass().addAll("main-background");
    mainContainer.getStylesheets().addAll(
        getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm(),
        getClass().getResource("/com/example/louishotelmanagement/css/thong-ke.css").toExternalForm()
    );

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(false);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    VBox thongKeContent = new VBox(20.0);
    thongKeContent.getStyleClass().addAll("main-content");
    thongKeContent.setFillWidth(true);

    // ============================================
    // HEADER SECTION
    // ============================================
    GridPane headerGrid = new GridPane();

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setHgrow(Priority.SOMETIMES);
    col1.setMinWidth(10.0);

    ColumnConstraints col2 = new ColumnConstraints();
    col2.setHgrow(Priority.SOMETIMES);
    col2.setMinWidth(10.0);
    col2.setPrefWidth(100.0);

    headerGrid.getColumnConstraints().addAll(col1, col2);

    RowConstraints row1 = new RowConstraints();
    row1.setMinHeight(10.0);
    row1.setVgrow(Priority.SOMETIMES);
    headerGrid.getRowConstraints().addAll(row1);

    // Header title section
    VBox headerTitleVBox = new VBox(5.0);
    headerTitleVBox.setPrefWidth(363.0);

    Label mainHeaderLabel = new Label("Thống kê");
    mainHeaderLabel.getStyleClass().addAll("main-header");

    Label mainSubtitleLabel = new Label("Xem tổng quan về hiệu suất kinh doanh khách sạn");
    mainSubtitleLabel.getStyleClass().addAll("main-subtitle");

    headerTitleVBox.getChildren().addAll(mainHeaderLabel, mainSubtitleLabel);

    // Header button section
    VBox headerButtonVBox = new VBox();
    headerButtonVBox.setAlignment(Pos.TOP_RIGHT);
    headerButtonVBox.setPrefWidth(502.0);

    btnLamMoi = new Button("🔄 Làm Mới");
    btnLamMoi.getStyleClass().addAll("btn-info");
    btnLamMoi.setPrefHeight(37.0);
    btnLamMoi.setPrefWidth(112.0);
    btnLamMoi.setOnAction(e -> this.handleLamMoi());
    btnLamMoi.setCursor(Cursor.HAND);

    lblCapNhatCuoi = new Label("Cập nhật lần cuối: --");
    lblCapNhatCuoi.getStyleClass().addAll("font-12");
    lblCapNhatCuoi.setPrefHeight(17.0);
    lblCapNhatCuoi.setPrefWidth(209.0);

    headerButtonVBox.getChildren().addAll(btnLamMoi, lblCapNhatCuoi);

    headerGrid.add(headerTitleVBox, 0, 0);
    headerGrid.add(headerButtonVBox, 1, 0);

    // ============================================
    // STATISTICS CARDS
    // ============================================
    GridPane statsGrid = new GridPane();
    statsGrid.setHgap(16.0);

    ColumnConstraints statsCol1 = new ColumnConstraints();
    statsCol1.setHgrow(Priority.SOMETIMES);
    statsCol1.setMinWidth(10.0);

    ColumnConstraints statsCol2 = new ColumnConstraints();
    statsCol2.setHgrow(Priority.SOMETIMES);
    statsCol2.setMinWidth(10.0);
    statsCol2.setPrefWidth(100.0);

    ColumnConstraints statsCol3 = new ColumnConstraints();
    statsCol3.setHgrow(Priority.SOMETIMES);
    statsCol3.setMinWidth(10.0);
    statsCol3.setPrefWidth(100.0);

    ColumnConstraints statsCol4 = new ColumnConstraints();
    statsCol4.setHgrow(Priority.SOMETIMES);
    statsCol4.setMinWidth(10.0);
    statsCol4.setPrefWidth(100.0);

    statsGrid.getColumnConstraints().addAll(statsCol1, statsCol2, statsCol3, statsCol4);

    RowConstraints statsRow = new RowConstraints();
    statsRow.setMinHeight(10.0);
    statsRow.setVgrow(Priority.SOMETIMES);
    statsGrid.getRowConstraints().addAll(statsRow);

    // Total Rooms Card
    totalRoomsCard = new StatsCard(
        "Tổng số phòng", "0", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/bed.png", "stats-card-icon-container-success"
    );
    totalRoomsCard.setPrefHeight(142.0);
    totalRoomsCard.setPrefWidth(140.0);

    // Occupied Rooms Card
    occupiedRoomsCard = new StatsCard(
        "Phòng đang sử dụng", "0", "stats-card-number-success",
        "/com/example/louishotelmanagement/image/bed.png", "stats-card-icon-container-success"
    );
    occupiedRoomsCard.setPrefHeight(120.0);
    occupiedRoomsCard.setPrefWidth(200.0);

    // Total Customers Card
    totalCustomersCard = new StatsCard(
        "Tổng khách hàng", "0", "stats-card-number-info",
        "/com/example/louishotelmanagement/image/user.png", "stats-card-icon-container-info"
    );
    totalCustomersCard.setPrefHeight(120.0);
    totalCustomersCard.setPrefWidth(200.0);

    // Room Types Card
    roomTypesCard = new StatsCard(
        "Loại phòng", "0", "stats-card-number-warning",
        "/com/example/louishotelmanagement/image/card.png", "stats-card-icon-container-warning"
    );
    roomTypesCard.setPrefHeight(120.0);
    roomTypesCard.setPrefWidth(200.0);

    statsGrid.add(totalRoomsCard, 0, 0);
    statsGrid.add(occupiedRoomsCard, 1, 0);
    statsGrid.add(totalCustomersCard, 2, 0);
    statsGrid.add(roomTypesCard, 3, 0);

    // ============================================
    // CHART SECTION
    // ============================================
    GridPane chartGrid = new GridPane();
    chartGrid.setHgap(16.0);

    ColumnConstraints chartCol1 = new ColumnConstraints();
    chartCol1.setHgrow(Priority.ALWAYS);
    chartCol1.setMinWidth(10.0);

    ColumnConstraints chartCol2 = new ColumnConstraints();
    chartCol2.setHgrow(Priority.NEVER);
    chartCol2.setMinWidth(280.0);
    chartCol2.setPrefWidth(320.0);

    chartGrid.getColumnConstraints().addAll(chartCol1, chartCol2);

    RowConstraints chartRow = new RowConstraints();
    chartRow.setMinHeight(10.0);
    chartRow.setVgrow(Priority.SOMETIMES);
    chartGrid.getRowConstraints().addAll(chartRow);

    // Revenue Chart Container
    VBox revenueChartVBox = new VBox(15.0);
    revenueChartVBox.getStyleClass().addAll("chart-container");
    revenueChartVBox.setPrefHeight(500.0);
    revenueChartVBox.setPrefWidth(536.0);

    // Chart Header
    HBox chartHeaderHBox = new HBox(20.0);
    chartHeaderHBox.setAlignment(Pos.CENTER_LEFT);

    Label chartTitleLabel = new Label("Biểu đồ doanh thu");
    chartTitleLabel.getStyleClass().addAll("chart-title");

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    // Time Period Controls
    HBox timeControlsHBox = new HBox(5.0);

    btnTheoNgay = new ToggleButton("Ngày");
    btnTheoNgay.getStyleClass().addAll("toggle-button");
    btnTheoNgay.setOnAction(e -> this.handleChonTheoNgay());

    btnTheoTuan = new ToggleButton("Tuần");
    btnTheoTuan.getStyleClass().addAll("toggle-button");
    btnTheoTuan.setOnAction(e -> this.handleChonTheoTuan());

    btnTheoThang = new ToggleButton("Tháng");
    btnTheoThang.getStyleClass().addAll("toggle-button");
    btnTheoThang.setSelected(true);
    btnTheoThang.setOnAction(e -> this.handleChonTheoThang());

    timeControlsHBox.getChildren().addAll(btnTheoNgay, btnTheoTuan, btnTheoThang);
    chartHeaderHBox.getChildren().addAll(chartTitleLabel, spacer, timeControlsHBox);

    // Selection Controls
    HBox selectionControlsHBox = new HBox(10.0);
    selectionControlsHBox.getStyleClass().addAll("selection-controls-container");
    selectionControlsHBox.setAlignment(Pos.CENTER_LEFT);

    // Date Range Selection
    hboxChonNgay = new HBox(10.0);
    hboxChonNgay.setAlignment(Pos.CENTER_LEFT);
    hboxChonNgay.setVisible(false);

    Label tuNgayLabel = new Label("Từ ngày:");
    tuNgayLabel.getStyleClass().addAll("form-label");

    dpTuNgay = new DatePicker();
    dpTuNgay.setOnAction(e -> this.handleChonNgay());

    Label denNgayLabel = new Label("Đến ngày:");
    denNgayLabel.getStyleClass().addAll("form-label");

    dpDenNgay = new DatePicker();
    dpDenNgay.setOnAction(e -> this.handleChonNgay());

    hboxChonNgay.getChildren().addAll(tuNgayLabel, dpTuNgay, denNgayLabel, dpDenNgay);

    // Year Selection
    hboxChonNam = new HBox(10.0);
    hboxChonNam.setAlignment(Pos.CENTER_LEFT);

    Label namLabel = new Label("Năm:");
    namLabel.getStyleClass().addAll("form-label");

    cbNam = new ComboBox<>();
    cbNam.setPrefWidth(100.0);
    cbNam.getStyleClass().addAll("combo-box");
    cbNam.setOnAction(e -> this.handleChonNam());

    hboxChonNam.getChildren().addAll(namLabel, cbNam);

    // Update Button
    btnCapNhatChart = new Button("Cập nhật");
    btnCapNhatChart.getStyleClass().addAll("btn-success");
    btnCapNhatChart.setOnAction(e -> this.handleCapNhatChart());

    selectionControlsHBox.getChildren().addAll(hboxChonNgay, hboxChonNam, btnCapNhatChart);

    // Chart Container
    VBox chartDataVBox = new VBox();
    chartDataVBox.getStyleClass().addAll("chart-data-container");
    chartDataVBox.setAlignment(Pos.CENTER);
    chartDataVBox.setPrefHeight(300.0);

    thongkeBarChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
    chartDataVBox.getChildren().addAll(thongkeBarChart);

    // Total Revenue
    HBox totalRevenueHBox = new HBox();
    totalRevenueHBox.setAlignment(Pos.CENTER);
    totalRevenueHBox.setSpacing(20.0);

    lblTongDoanhThu = new Label("Tổng doanh thu: 0 VNĐ");
    lblTongDoanhThu.getStyleClass().addAll("total-revenue-label");
    totalRevenueHBox.getChildren().addAll(lblTongDoanhThu);

    // Chart Legend
    HBox legendHBox = new HBox();
    legendHBox.setAlignment(Pos.CENTER);
    legendHBox.setSpacing(20.0);

    HBox revenueLegendHBox = new HBox(10.0);
    Rectangle revenueLegendRect = new Rectangle(15.0, 15.0, Color.web("#27ae60"));
    Label revenueLegendLabel = new Label("Doanh thu");
    revenueLegendLabel.getStyleClass().addAll("chart-legend");
    revenueLegendHBox.getChildren().addAll(revenueLegendRect, revenueLegendLabel);

    HBox bookingLegendHBox = new HBox(10.0);
    Rectangle bookingLegendRect = new Rectangle(15.0, 15.0, Color.web("#3498db"));
    Label bookingLegendLabel = new Label("Số lượng đặt phòng");
    bookingLegendLabel.getStyleClass().addAll("chart-legend");
    bookingLegendHBox.getChildren().addAll(bookingLegendRect, bookingLegendLabel);

    legendHBox.getChildren().addAll(revenueLegendHBox, bookingLegendHBox);

    revenueChartVBox.getChildren().addAll(chartHeaderHBox, selectionControlsHBox, chartDataVBox, totalRevenueHBox, legendHBox);

    // ============================================
    // ROOM STATUS SECTION
    // ============================================
    VBox roomStatusVBox = new VBox(15.0);
    roomStatusVBox.getStyleClass().addAll("chart-container");
    roomStatusVBox.setPrefWidth(300.0);

    Label roomStatusTitleLabel = new Label("Trạng thái phòng");
    roomStatusTitleLabel.getStyleClass().addAll("chart-title");
    roomStatusVBox.getChildren().addAll(roomStatusTitleLabel);

    // Room Status Statistics
    VBox statusStatsVBox = new VBox(15.0);

    // Empty Rooms
    HBox emptyRoomsHBox = new HBox(15.0);
    emptyRoomsHBox.setAlignment(Pos.CENTER_LEFT);
    Rectangle emptyRect = new Rectangle(20.0, 20.0, Color.web("#27ae60"));
    lblPhongTrong = new Label("Phòng trống: 0");
    lblPhongTrong.getStyleClass().addAll("status-item");
    emptyRoomsHBox.getChildren().addAll(emptyRect, lblPhongTrong);

    // Booked Rooms
    HBox bookedRoomsHBox = new HBox(15.0);
    bookedRoomsHBox.setAlignment(Pos.CENTER_LEFT);
    Rectangle bookedRect = new Rectangle(20.0, 20.0, Color.web("#3498db"));
    lblPhongDaDat = new Label("Phòng đã đặt: 0");
    lblPhongDaDat.getStyleClass().addAll("status-item");
    bookedRoomsHBox.getChildren().addAll(bookedRect, lblPhongDaDat);

    // Occupied Rooms
    HBox occupiedRoomsHBox = new HBox(15.0);
    occupiedRoomsHBox.setAlignment(Pos.CENTER_LEFT);
    Rectangle occupiedRect = new Rectangle(20.0, 20.0, Color.web("#f39c12"));
    lblPhongDangSuDungChiTiet = new Label("Phòng đang sử dụng: 0");
    lblPhongDangSuDungChiTiet.getStyleClass().addAll("status-item");
    occupiedRoomsHBox.getChildren().addAll(occupiedRect, lblPhongDangSuDungChiTiet);

    // Maintenance Rooms
    HBox maintenanceRoomsHBox = new HBox(15.0);
    maintenanceRoomsHBox.setAlignment(Pos.CENTER_LEFT);
    Rectangle maintenanceRect = new Rectangle(20.0, 20.0, Color.web("#e74c3c"));
    lblPhongBaoTri = new Label("Phòng bảo trì: 0");
    lblPhongBaoTri.getStyleClass().addAll("status-item");
    maintenanceRoomsHBox.getChildren().addAll(maintenanceRect, lblPhongBaoTri);

    statusStatsVBox.getChildren().addAll(emptyRoomsHBox, bookedRoomsHBox, occupiedRoomsHBox, maintenanceRoomsHBox);
    roomStatusVBox.getChildren().addAll(statusStatsVBox);

    // Room Usage Bars
    VBox usageBarsVBox = new VBox(10.0);
    usageBarsVBox.getStyleClass().addAll("room-status-container");

    Label usageTitleLabel = new Label("Tỷ lệ sử dụng phòng");
    usageTitleLabel.getStyleClass().addAll("room-status-title");
    usageBarsVBox.getChildren().addAll(usageTitleLabel);

    // Empty Rooms Bar
    VBox emptyBarVBox = new VBox(5.0);
    Label emptyLabel = new Label("Trống");
    emptyLabel.getStyleClass().addAll("room-status-label");

    HBox emptyBarHBox = new HBox();
    barPhongTrong = new Rectangle(0.0, 20.0, Color.web("#27ae60"));
    lblTiLeTrong = new Label("0%");
    lblTiLeTrong.getStyleClass().addAll("status-percentage");
    emptyBarHBox.getChildren().addAll(barPhongTrong, lblTiLeTrong);

    emptyBarVBox.getChildren().addAll(emptyLabel, emptyBarHBox);

    // Booked Rooms Bar
    VBox bookedBarVBox = new VBox(5.0);
    Label bookedLabel = new Label("Đã đặt");
    bookedLabel.getStyleClass().addAll("room-status-label");

    HBox bookedBarHBox = new HBox();
    barPhongDaDat = new Rectangle(0.0, 20.0, Color.web("#3498db"));
    lblTiLeDaDat = new Label("0%");
    lblTiLeDaDat.getStyleClass().addAll("status-percentage");
    bookedBarHBox.getChildren().addAll(barPhongDaDat, lblTiLeDaDat);

    bookedBarVBox.getChildren().addAll(bookedLabel, bookedBarHBox);

    // Occupied Rooms Bar
    VBox occupiedBarVBox = new VBox(5.0);
    Label occupiedLabel = new Label("Đang sử dụng");
    occupiedLabel.getStyleClass().addAll("room-status-label");

    HBox occupiedBarHBox = new HBox();
    barPhongDangSuDung = new Rectangle(0.0, 20.0, Color.web("#f39c12"));
    lblTiLeDangSuDung = new Label("0%");
    lblTiLeDangSuDung.getStyleClass().addAll("status-percentage");
    occupiedBarHBox.getChildren().addAll(barPhongDangSuDung, lblTiLeDangSuDung);

    occupiedBarVBox.getChildren().addAll(occupiedLabel, occupiedBarHBox);

    // Maintenance Rooms Bar
    VBox maintenanceBarVBox = new VBox(5.0);
    Label maintenanceLabel = new Label("Bảo trì");
    maintenanceLabel.getStyleClass().addAll("room-status-label");

    HBox maintenanceBarHBox = new HBox();
    barPhongBaoTri = new Rectangle(0.0, 20.0, Color.web("#e74c3c"));
    lblTiLeBaoTri = new Label("0%");
    lblTiLeBaoTri.getStyleClass().addAll("status-percentage");
    maintenanceBarHBox.getChildren().addAll(barPhongBaoTri, lblTiLeBaoTri);

    maintenanceBarVBox.getChildren().addAll(maintenanceLabel, maintenanceBarHBox);

    usageBarsVBox.getChildren().addAll(emptyBarVBox, bookedBarVBox, occupiedBarVBox, maintenanceBarVBox);
    roomStatusVBox.getChildren().addAll(usageBarsVBox);

    chartGrid.add(revenueChartVBox, 0, 0);
    chartGrid.add(roomStatusVBox, 1, 0);

    // ============================================
    // ROOM TYPES SECTION
    // ============================================
    VBox roomTypesVBox = new VBox(15.0);
    roomTypesVBox.getStyleClass().addAll("chart-container");

    Label roomTypesTitleLabel = new Label("Loại phòng");
    roomTypesTitleLabel.getStyleClass().addAll("chart-title");
    roomTypesVBox.getChildren().addAll(roomTypesTitleLabel);

    // Room Types List Container
    vboxLoaiPhong = new VBox(10.0);
    vboxLoaiPhong.getStyleClass().addAll("room-types-container");

    Label roomTypesListTitle = new Label("Danh sách loại phòng");
    roomTypesListTitle.getStyleClass().addAll("room-types-title");
    vboxLoaiPhong.getChildren().addAll(roomTypesListTitle);

    roomTypesVBox.getChildren().addAll(vboxLoaiPhong);

    // Summary Section
    VBox summaryVBox = new VBox(8.0);
    summaryVBox.getStyleClass().addAll("summary-container");

    Label summaryTitleLabel = new Label("Tóm tắt");
    summaryTitleLabel.getStyleClass().addAll("summary-title");

    lblGiaTrungBinh = new Label("Giá trung bình: 0 VNĐ");
    lblGiaTrungBinh.getStyleClass().addAll("summary-item");

    lblGiaCaoNhat = new Label("Giá cao nhất: 0 VNĐ");
    lblGiaCaoNhat.getStyleClass().addAll("summary-item");

    lblGiaThapNhat = new Label("Giá thấp nhất: 0 VNĐ");
    lblGiaThapNhat.getStyleClass().addAll("summary-item");

    summaryVBox.getChildren().addAll(summaryTitleLabel, lblGiaTrungBinh, lblGiaCaoNhat, lblGiaThapNhat);
    roomTypesVBox.getChildren().addAll(summaryVBox);

    // Assemble all sections
    thongKeContent.getChildren().addAll(headerGrid, statsGrid, chartGrid, roomTypesVBox);
    scrollPane.setContent(thongKeContent);
    mainContainer.setCenter(scrollPane);

    this.root = mainContainer;
    this.thongKeController = new ThongKeController(this);
  }

  // Getters for controller
  public Button getBtnLamMoi() {
    return btnLamMoi;
  }

  public Label getLblCapNhatCuoi() {
    return lblCapNhatCuoi;
  }

  public StatsCard getTotalRoomsCard() {
    return totalRoomsCard;
  }

  public StatsCard getOccupiedRoomsCard() {
    return occupiedRoomsCard;
  }

  public StatsCard getTotalCustomersCard() {
    return totalCustomersCard;
  }

  public StatsCard getRoomTypesCard() {
    return roomTypesCard;
  }

  public ToggleButton getBtnTheoNgay() {
    return btnTheoNgay;
  }

  public ToggleButton getBtnTheoTuan() {
    return btnTheoTuan;
  }

  public ToggleButton getBtnTheoThang() {
    return btnTheoThang;
  }

  public HBox getHboxChonNgay() {
    return hboxChonNgay;
  }

  public DatePicker getDpTuNgay() {
    return dpTuNgay;
  }

  public DatePicker getDpDenNgay() {
    return dpDenNgay;
  }

  public HBox getHboxChonNam() {
    return hboxChonNam;
  }

  public ComboBox<Integer> getCbNam() {
    return cbNam;
  }

  public Button getBtnCapNhatChart() {
    return btnCapNhatChart;
  }

  public BarChart<String, Number> getThongkeBarChart() {
    return thongkeBarChart;
  }

  public Label getLblTongDoanhThu() {
    return lblTongDoanhThu;
  }

  public Label getLblPhongTrong() {
    return lblPhongTrong;
  }

  public Label getLblPhongDaDat() {
    return lblPhongDaDat;
  }

  public Label getLblPhongDangSuDungChiTiet() {
    return lblPhongDangSuDungChiTiet;
  }

  public Label getLblPhongBaoTri() {
    return lblPhongBaoTri;
  }

  public Rectangle getBarPhongTrong() {
    return barPhongTrong;
  }

  public Label getLblTiLeTrong() {
    return lblTiLeTrong;
  }

  public Rectangle getBarPhongDaDat() {
    return barPhongDaDat;
  }

  public Label getLblTiLeDaDat() {
    return lblTiLeDaDat;
  }

  public Rectangle getBarPhongDangSuDung() {
    return barPhongDangSuDung;
  }

  public Label getLblTiLeDangSuDung() {
    return lblTiLeDangSuDung;
  }

  public Rectangle getBarPhongBaoTri() {
    return barPhongBaoTri;
  }

  public Label getLblTiLeBaoTri() {
    return lblTiLeBaoTri;
  }

  public VBox getVboxLoaiPhong() {
    return vboxLoaiPhong;
  }

  public Label getLblGiaTrungBinh() {
    return lblGiaTrungBinh;
  }

  public Label getLblGiaCaoNhat() {
    return lblGiaCaoNhat;
  }

  public Label getLblGiaThapNhat() {
    return lblGiaThapNhat;
  }

  public Parent getRoot() {
    return root;
  }

  // Event handlers
  private void handleLamMoi() {
    if (thongKeController != null) {
      thongKeController.handleLamMoi();
    }
  }

  private void handleChonTheoNgay() {
    if (thongKeController != null) {
      thongKeController.handleChonTheoNgay();
    }
  }

  private void handleChonTheoTuan() {
    if (thongKeController != null) {
      thongKeController.handleChonTheoTuan();
    }
  }

  private void handleChonTheoThang() {
    if (thongKeController != null) {
      thongKeController.handleChonTheoThang();
    }
  }

  private void handleChonNgay() {
    if (thongKeController != null) {
      thongKeController.handleChonNgay();
    }
  }

  private void handleChonNam() {
    if (thongKeController != null) {
      thongKeController.handleChonNam();
    }
  }

  private void handleCapNhatChart() {
    if (thongKeController != null) {
      thongKeController.handleCapNhatChart();
    }
  }
}
