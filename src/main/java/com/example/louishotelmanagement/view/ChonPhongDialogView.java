package com.example.louishotelmanagement.view;

import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;

public class ChonPhongDialogView {
    private TableView tblPhong;
    private TableColumn colMaPhong, colLoaiPhong, colGiaPhong, colTrangThai, colChon;
    private Button btnDong; // Đổi tên nút
    private Parent root;

    public ChonPhongDialogView() {
        BorderPane borderpane1 = new BorderPane();
        borderpane1.setPrefSize(760.0, 494.0);
        borderpane1.getStylesheets().addAll(getClass().getResource("/com/example/louishotelmanagement/css/chon-phong-dialog.css").toExternalForm());

        // Bảng danh sách phòng
        tblPhong = new TableView();
        colMaPhong = new TableColumn("Phòng");
        colLoaiPhong = new TableColumn("Loại phòng");
        colGiaPhong = new TableColumn("Giá / đêm");
        colTrangThai = new TableColumn("Trạng thái");
        colChon = new TableColumn("Thao tác");

        tblPhong.getColumns().addAll(colMaPhong, colLoaiPhong, colGiaPhong, colTrangThai, colChon);
        borderpane1.setCenter(tblPhong);

        // Tiêu đề
        Label label1 = new Label("CHỌN PHÒNG");
        label1.getStyleClass().addAll("dialog-title");
        borderpane1.setTop(label1);
        BorderPane.setMargin(label1, new Insets(10));

        // Nút chức năng phía dưới
        HBox hbox1 = new HBox(10);
        hbox1.setAlignment(Pos.CENTER_RIGHT);
        hbox1.setPadding(new Insets(10));

        btnDong = new Button("Đóng");
        hbox1.getChildren().addAll(btnDong);
        borderpane1.setBottom(hbox1);

        this.root = borderpane1;
    }

    public Parent getRoot() { return root; }
    public TableView getTblPhong() { return tblPhong; }
    public TableColumn getColMaPhong() { return colMaPhong; }
    public TableColumn getColLoaiPhong() { return colLoaiPhong; }
    public TableColumn getColGiaPhong() { return colGiaPhong; }
    public TableColumn getColTrangThai() { return colTrangThai; }
    public TableColumn getColChon() { return colChon; }
    public Button getBtnDong() { return btnDong; }
}