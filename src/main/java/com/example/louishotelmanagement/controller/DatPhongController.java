package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.Phong;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DatPhongController implements Initializable{

    public ComboBox dsPhong;
    public ComboBox dsKhachHang;
    public DatePicker ngayDen;
    public DatePicker ngayDi;
    public Button btnDatPhong;
    public DatePicker ngayDat;
    public PhongDAO Pdao;
    public TextField maNhanVien;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        try{
            layDsPhong();
            setComboBoxPhong();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void setComboBoxPhong(){
        dsPhong.getSelectionModel().selectFirst();
    }
    public void layDsPhong() throws SQLException {
        ArrayList<Phong> phongs = Pdao.layDSPhong();
        for(Phong phong : phongs) {
            dsPhong.getItems().add(phong.getMaPhong());
        }
    }



}
