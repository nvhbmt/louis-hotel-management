package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.model.Phong;
import com.example.louishotelmanagement.model.TrangThaiPhieuDatPhong;
import com.example.louishotelmanagement.model.TrangThaiPhong;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DialogChonPhongController {

    @FXML
    private TableView<Phong> tblPhong;
    @FXML
    private TableColumn<Phong, String> colMaPhong;
    @FXML
    private TableColumn<Phong, String> colLoaiPhong;
    @FXML
    private TableColumn<Phong, Double> colGiaPhong;
    @FXML
    private TableColumn<Phong, TrangThaiPhong> colTrangThai;
    @FXML
    private TableColumn<Phong, Void> colChon;

    private Phong phongDuocChon;

    // ================= INIT =================
    @FXML
    private void initialize() {
        colMaPhong.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMaPhong()));

        colLoaiPhong.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getLoaiPhong().getTenLoai()
                ));

        colGiaPhong.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getLoaiPhong().getDonGia()
                ));

        colGiaPhong.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? "" :
                        NumberFormat.getCurrencyInstance(
                                new Locale("vi", "VN")
                        ).format(value));
            }
        });

        colTrangThai.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getTrangThai()));

        taoNutChon();
    }


    // ================= LOAD DATA =================
    public void setDanhSachPhong(List<Phong> dsPhong) {
        tblPhong.getItems().setAll(dsPhong);
    }

    // ================= BUTTON CHỌN =================
    private void taoNutChon() {
        colChon.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Chọn");

            {
                btn.setOnAction(e -> {
                    phongDuocChon = getTableView().getItems().get(getIndex());
                    dongDialog();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    public Phong getPhongDuocChon() {
        return phongDuocChon;
    }

    // ================= CLOSE =================
    @FXML
    private void handleDong() {
        dongDialog();
    }

    private void dongDialog() {
        Stage stage = (Stage) tblPhong.getScene().getWindow();
        stage.close();
    }
}
