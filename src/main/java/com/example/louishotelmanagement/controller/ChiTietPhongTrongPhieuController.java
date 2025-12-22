package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.CTHoaDonPhong;
import com.example.louishotelmanagement.model.LoaiPhong;
import com.example.louishotelmanagement.model.Phong;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChiTietPhongTrongPhieuController implements Initializable {

    @FXML private Label lblMaPhieu;
    @FXML private TableView<Phong> tblChiTietPhong;
    @FXML private TableColumn<Phong, String> colMaPhong;
    @FXML private TableColumn<Phong, String> colTenLoaiPhong;
    @FXML private TableColumn<Phong, Double> colGia;
    @FXML private TableColumn<Phong, Integer> colTang;


    @FXML private Label lblChiTietMaPhong;
    @FXML private Label lblChiTietTang;
    @FXML private Label lblChiTietTrangThai;
    @FXML private Label lblChiTietLoaiPhong;
    @FXML private Label lblChiTietGia;
    @FXML private TextArea txtChiTietMoTa;


    private PhongDAO phDao = new PhongDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));

        colTenLoaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    new SimpleStringProperty(loaiPhong.getTenLoai()) :
                    new SimpleStringProperty("");
        });
        colGia.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();
            return loaiPhong != null ?
                    Bindings.createObjectBinding(loaiPhong::getDonGia) :
                    Bindings.createObjectBinding(() -> 0.0);
        });
        colTang.setCellValueFactory(new PropertyValueFactory<>("tang"));

        tblChiTietPhong.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Phong>() {
            @Override
            public void changed(ObservableValue<? extends Phong> observable, Phong oldValue, Phong newValue) {
                // Gọi hàm hiển thị chi tiết khi có dòng được chọn
                loadChiTietPhong(newValue);
            }
        });
    }

    /**
     * Phương thức cập nhật thông tin chi tiết của phòng được chọn
     */
    private void loadChiTietPhong(Phong phong) {
        if (phong != null) {
            lblChiTietMaPhong.setText(phong.getMaPhong());
            lblChiTietTang.setText(String.valueOf(phong.getTang()));
            lblChiTietTrangThai.setText(phong.getTrangThai() != null ? phong.getTrangThai().toString() : "N/A");

            LoaiPhong lp = phong.getLoaiPhong();
            if (lp != null) {
                lblChiTietLoaiPhong.setText(lp.getTenLoai());
                lblChiTietGia.setText(String.format("%,.0f VND", lp.getDonGia()));
            } else {
                lblChiTietLoaiPhong.setText("Không rõ");
                lblChiTietGia.setText("0 VND");
            }
            txtChiTietMoTa.setText(phong.getMoTa());
        } else {
            // Xóa thông tin khi không có dòng nào được chọn
            lblChiTietMaPhong.setText("");
            lblChiTietTang.setText("");
            lblChiTietTrangThai.setText("");
            lblChiTietLoaiPhong.setText("");
            lblChiTietGia.setText("");
            txtChiTietMoTa.setText("");
        }
    }


    /**
     * Phương thức nhận dữ liệu từ Controller cha và hiển thị.
     */
    public void setChiTietData(String maPhieu, ArrayList<CTHoaDonPhong> dsCTP) throws SQLException {
        lblMaPhieu.setText("Chi Tiết Phòng Phiếu: " + maPhieu);

        ArrayList<Phong> dsPhong = new ArrayList<>();
        for (CTHoaDonPhong ctp : dsCTP) {
            Phong phong = phDao.layPhongTheoMa(ctp.getMaPhong());
            if (phong != null) {
                dsPhong.add(phong);
            }
        }

        ObservableList<Phong> observableListPhong = FXCollections.observableArrayList(dsPhong);
        tblChiTietPhong.setItems(observableListPhong);

        // Tự động chọn dòng đầu tiên để load chi tiết lần đầu
        if (!dsPhong.isEmpty()) {
            tblChiTietPhong.getSelectionModel().selectFirst();
        } else {
            loadChiTietPhong(null);
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) lblMaPhieu.getScene().getWindow();
        stage.close();
    }
}