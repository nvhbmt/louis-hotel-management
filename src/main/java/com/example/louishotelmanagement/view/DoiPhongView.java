package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

public class DoiPhongView {
    private ComboBox<String> dsKhachHang;
    private ComboBox<String> dsPhongHienTai;
    private ComboBox<String> dsPhongMuonDoi;
    private ComboBox<Integer> cbTang;
    private ComboBox<LoaiPhong> cbLocLoaiPhong;
    private TableView<Phong> tablePhong;
    private TableColumn<Phong, String> maPhong;
    private TableColumn<Phong, Integer> tang;
    private TableColumn<Phong, TrangThaiPhong> trangThai;
    private TableColumn<Phong, String> moTa;
    private TableColumn<Phong, String> loaiPhong;
    private Button btnDoiPhong;
    private Button btnLamMoi;
    private Parent root;

    public DoiPhongView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.setPrefHeight(864.0);
        borderpane1.setPrefWidth(1014.4);

        // Nạp CSS
        borderpane1.getStylesheets().add(getClass().getResource("/com/example/louishotelmanagement/css/doi-phong.css").toExternalForm());

        // --- TOP SECTION (HEADER) ---
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.getStyleClass().add("header-section");

        ImageView headerIcon = new ImageView(new Image(getClass().getResource("/com/example/louishotelmanagement/image/exchange.png").toExternalForm()));
        headerIcon.getStyleClass().add("header-icon"); // Khớp với .header-icon trong CSS

        VBox titleBox = new VBox();
        Label lblHeaderTitle = new Label("Đổi phòng");
        lblHeaderTitle.getStyleClass().add("header-title"); // Khớp với .header-title trong CSS
        Label lblHeaderSub = new Label("Thực hiện chức năng đổi phòng cho khách hàng");
        lblHeaderSub.getStyleClass().add("header-subtitle");

        titleBox.getChildren().addAll(lblHeaderTitle, lblHeaderSub);
        headerBox.getChildren().addAll(headerIcon, titleBox);
        borderpane1.setTop(headerBox);

        // --- CENTER SECTION ---
        VBox vboxCenter = new VBox();
        vboxCenter.setAlignment(Pos.TOP_CENTER);
        vboxCenter.setPrefWidth(922.0);

        Label label1 = new Label("Thông tin đổi phòng");
        label1.getStyleClass().add("section-heading"); // Khớp với .section-heading trong CSS

        // Các hàng nhập liệu
        dsKhachHang = new ComboBox<>();
        HBox hbox1 = createInputRow("Khách hàng", "/com/example/louishotelmanagement/image/detective.png", dsKhachHang);

        dsPhongHienTai = new ComboBox<>();
        HBox hbox2 = createInputRow("Phòng hiện tại", "/com/example/louishotelmanagement/image/double-bed.png", dsPhongHienTai);

        dsPhongMuonDoi = new ComboBox<>();
        HBox hbox3 = createInputRow("Phòng muốn đổi", "/com/example/louishotelmanagement/image/double-bed.png", dsPhongMuonDoi);
        VBox.setMargin(hbox3, new Insets(0, 0, 30.0, 0));

        // Thanh bộ lọc
        HBox hboxFilter = new HBox();
        hboxFilter.setAlignment(Pos.CENTER);
        hboxFilter.setPrefHeight(100.0);
        hboxFilter.setSpacing(20.0);

        cbTang = new ComboBox<>();
        cbTang.setPromptText("Chọn tầng");
        cbTang.setPrefWidth(200.0);

        cbLocLoaiPhong = new ComboBox<>();
        cbLocLoaiPhong.setPromptText("Chọn loại phòng");
        cbLocLoaiPhong.setPrefWidth(250.0);

        btnLamMoi = new Button("Làm mới");
        btnLamMoi.getStyleClass().add("refresh-button"); // Khớp với .refresh-button trong CSS
        btnLamMoi.setPrefSize(100, 27);

        hboxFilter.getChildren().addAll(cbTang, cbLocLoaiPhong, btnLamMoi);

        Label labelTableTitle = new Label("Danh sách phòng trống");
        labelTableTitle.getStyleClass().add("section-heading");

        // Bảng danh sách
        tablePhong = new TableView<>();
        tablePhong.getStyleClass().add("table-view"); // Khớp với .table-view trong CSS
        tablePhong.setPrefSize(1014, 405);
        VBox.setMargin(tablePhong, new Insets(0, 50, 0, 50));

        initTableColumns();

        vboxCenter.getChildren().addAll(label1, hbox1, hbox2, hbox3, hboxFilter, labelTableTitle, tablePhong);
        borderpane1.setCenter(vboxCenter);
// --- BOTTOM SECTION ---
        btnDoiPhong = new Button("Đổi phòng");
        btnDoiPhong.setId("btnDoiPhong"); // THÊM DÒNG NÀY ĐỂ CSS NHẬN DIỆN
        btnDoiPhong.getStyleClass().add("button-base"); // Giữ class base nếu cần style chung

        HBox footer = new HBox(btnDoiPhong);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20));
        borderpane1.setBottom(footer);

        borderpane1.setPadding(new Insets(32.0));
        this.root = borderpane1;
    }

    // Hàm hỗ trợ tạo hàng nhập liệu khớp với CSS .input-row và .input-label
    private HBox createInputRow(String text, String iconPath, ComboBox<String> combo) {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("input-row"); // Khớp CSS

        Label label = new Label(text);
        label.getStyleClass().add("input-label"); // Khớp CSS (prefWidth = 140px)
        label.setPrefWidth(140.0);

        ImageView icon = new ImageView(new Image(getClass().getResource(iconPath).toExternalForm()));
        icon.setFitHeight(30.0);
        icon.setFitWidth(30.0);
        icon.setPreserveRatio(true);
        HBox.setMargin(icon, new Insets(0, 0, 20.0, 0));

        combo.setPrefSize(600, 30);
        HBox.setMargin(combo, new Insets(0, 0, 20.0, 0));

        hbox.getChildren().addAll(label, icon, combo);
        return hbox;
    }

    private void initTableColumns() {
        maPhong = new TableColumn<>("Mã phòng"); maPhong.setPrefWidth(100);
        tang = new TableColumn<>("Tầng"); tang.setPrefWidth(91);
        trangThai = new TableColumn<>("Trạng thái"); trangThai.setPrefWidth(187);
        moTa = new TableColumn<>("Mô tả"); moTa.setPrefWidth(300);
        loaiPhong = new TableColumn<>("Loại phòng"); loaiPhong.setPrefWidth(240);
        tablePhong.getColumns().addAll(maPhong, tang, trangThai, moTa, loaiPhong);
    }

    // --- GETTERS ---
    public Parent getRoot() { return root; }
    public ComboBox<String> getDsKhachHang() { return dsKhachHang; }
    public ComboBox<String> getDsPhongHienTai() { return dsPhongHienTai; }
    public ComboBox<String> getDsPhong() { return dsPhongMuonDoi; }
    public ComboBox<Integer> getCbTang() { return cbTang; }
    public ComboBox<LoaiPhong> getCbLocLoaiPhong() { return cbLocLoaiPhong; }
    public Button getBtnDoiPhong() { return btnDoiPhong; }
    public Button getBtnLamMoi() { return btnLamMoi; }
    public TableView<Phong> getTablePhong() { return tablePhong; }
    public TableColumn<Phong, String> getMaPhong() { return maPhong; }
    public TableColumn<Phong, Integer> getTang() { return tang; }
    public TableColumn<Phong, TrangThaiPhong> getTrangThai() { return trangThai; }
    public TableColumn<Phong, String> getMoTa() { return moTa; }
    public TableColumn<Phong, String> getLoaiPhong() { return loaiPhong; }
}