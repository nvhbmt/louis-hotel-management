package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.model.*;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HuyDatPhongController implements Initializable {

    public TextField searchTextField;
    @FXML
    public TableView tablePhieu;
    @FXML
    public TableColumn<PhieuDatPhong, String> colMaPhieu;
    @FXML
    public TableColumn<PhieuDatPhong, LocalDate> colNgayDat;
    @FXML
    public TableColumn<PhieuDatPhong, LocalDate> colNgayDen;
    @FXML
    public TableColumn<PhieuDatPhong, LocalDate> colNgayDi;
    @FXML
    public TableColumn<PhieuDatPhong, TrangThaiPhieuDatPhong> colTrangThai;
    @FXML
    public TableColumn<PhieuDatPhong, String> colGhiChu;
    @FXML
    public TableColumn<PhieuDatPhong, String> colTenKhachHang;
    @FXML
    public TableColumn<PhieuDatPhong, String> colMaNhanVien;
    public TextField txtMaPhong;
    public TextField txtLoaiPhong;
    public TextField txtTang;
    public TextField txtTrangThai;
    public TextField txtMoTa;
    public TextField txtDonGia;
    public KhachHangDAO kDao;
    public PhieuDatPhongDAO pDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kDao = new KhachHangDAO();
        pDao = new PhieuDatPhongDAO();
        try{
            KhoiTaoTableView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void KhoiTaoTableView() throws SQLException {
        colMaPhieu.setCellValueFactory(new PropertyValueFactory<>("maPhieu"));
        colNgayDat.setCellValueFactory(new PropertyValueFactory<>("ngayDat"));
        colNgayDen.setCellValueFactory(new PropertyValueFactory<>("ngayDen"));
        colNgayDi.setCellValueFactory(new PropertyValueFactory<>("ngayDi"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colTrangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhieuDatPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().clear();
                } else {
                    setText(item.toString());
                    getStyleClass().clear();
                    switch (item) {
                        case TrangThaiPhieuDatPhong.HOAN_THANH -> getStyleClass().add("status-trong");
                        case TrangThaiPhieuDatPhong.DA_DAT -> getStyleClass().add("status-da-dat");
                        case TrangThaiPhieuDatPhong.DANG_SU_DUNG -> getStyleClass().add("status-dang-su-dung");
                        default -> {
                            // Không thêm style class nào
                        }
                    }
                }
            }
        });
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colTenKhachHang.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        ArrayList<PhieuDatPhong> dsPhieu = pDao.layDSPhieuDatPhong();
        ObservableList<PhieuDatPhong> observableListPhieu = FXCollections.observableArrayList(dsPhieu);
        tablePhieu.setItems(observableListPhieu);
        // Cho phép chọn nhiều dòng
        tablePhieu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    public void handleTim(ActionEvent actionEvent) {
    }

    public void handleLamMoi(ActionEvent actionEvent) {
    }

    public void handleHuyPhieuDat(ActionEvent actionEvent) {
    }
}
