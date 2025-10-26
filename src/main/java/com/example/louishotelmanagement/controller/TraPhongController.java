package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.CTHoaDonPhongDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TraPhongController implements Initializable{

    public Button btnCheck;
    public DatePicker ngayDi;
    public ComboBox dsPhong;
    public ComboBox dsKhachHang;
    public Button btnTraPhong;
    public KhachHangDAO khDao;
    public PhongDAO phDao;
    public CTHoaDonPhongDAO cthdpDao;
    public PhieuDatPhongDAO phieuDatPhongDAO;
    public TextField ngayTraPhong;
    public TextField hoTen;
    public TextField maPhieuThue;
    public TextField ngayDen;
    public TextField soLuongPhong;
    public Label lblCheDo;
    public Button btnXemChiTiet;
    public TextField tongTienPhong;
    public TextField tienDichVu;
    public TextField giamGia;
    public TextField tongCong;
    private ArrayList<String> dsMaKH = new ArrayList<>();
    private ArrayList<PhieuDatPhong> dspdp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        khDao = new KhachHangDAO();
        phDao = new PhongDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        try{
            laydsKhachHang();
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue!=null){
                    try {
                        laydsPhongTheoKhachHang();
                        dsPhong.getSelectionModel().selectFirst();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dsPhong.getSelectionModel().selectFirst();
        dsKhachHang.getSelectionModel().selectFirst();
    }
    public void laydsKhachHang() throws  SQLException{
        ArrayList<KhachHang> khs = khDao.layDSKhachHang();
        for(KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
    }
    public void laydsPhongTheoKhachHang() throws SQLException {
        dsPhong.getItems().clear();
        dspdp = new ArrayList<>();
        ArrayList<PhieuDatPhong> dsPhieu = phieuDatPhongDAO.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        if(dsPhieu.size()>0) {
            for(PhieuDatPhong p:dsPhieu){
                if(p.getTrangThai()== TrangThaiPhieuDatPhong.DANG_SU_DUNG){
                    dspdp.add(p);
                    ArrayList<CTHoaDonPhong> dsCTP = cthdpDao.getCTHoaDonPhongTheoMaPhieu(p.getMaPhieu());
                    for(CTHoaDonPhong ctp : dsCTP){
                        dsPhong.getItems().add(ctp.getMaPhong());
                    }
                }
            }
        }
    }
    public void handleCheck(javafx.event.ActionEvent actionEvent) throws SQLException {

    }

    public void handleTraPhong(ActionEvent actionEvent) throws SQLException {

    }


    public void handleXemChiTiet(ActionEvent actionEvent) {
    }
}
