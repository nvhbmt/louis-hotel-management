package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.Phong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DatPhongTaiQuayController implements Initializable {

    public TextField maNhanVien;
    public DatePicker ngayDi;
    public ComboBox dsKhachHang;
    public ComboBox dsPhong;
    public TableView tablePhong;
    public TableColumn maPhong;
    public TableColumn tang;
    public TableColumn trangThai;
    public TableColumn moTa;
    public TableColumn loaiPhong;
    private PhongDAO Pdao;
    private KhachHangDAO Kdao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        tang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        moTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        loaiPhong.setCellValueFactory(new PropertyValueFactory<>("loaiPhong"));
            try {
                layDsPhong();
                setComboBoxPhong();
                laydsKhachHang();
                loadTable();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            dsKhachHang.getSelectionModel().selectFirst();
    }
    public void setComboBoxPhong(){
        dsPhong.getSelectionModel().selectFirst();
    }
    public void laydsKhachHang() throws  SQLException{
        ArrayList<KhachHang> khs = Kdao.layDSKhachHang();
        for(KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getMaKH());
        }
    }
    public void layDsPhong() throws SQLException {
        ArrayList<Phong> phongs = Pdao.layDSPhongTrong();
        for(Phong phong : phongs) {
            dsPhong.getItems().add(phong.getMaPhong());
        }
    }
    public void loadTable() throws SQLException {
        tablePhong.getItems().clear();
        ArrayList<Phong> phongs = Pdao.layDSPhongTrong();
        ObservableList<Phong> dsPhong = FXCollections.observableArrayList(phongs);
        tablePhong.setItems(dsPhong);

    }
}
