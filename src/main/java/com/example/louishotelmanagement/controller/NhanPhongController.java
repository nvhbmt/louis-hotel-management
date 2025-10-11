package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.CTPhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.CTPhieuDatPhong;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.Phong;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NhanPhongController implements Initializable {
    public ComboBox dsKhachHang;
    public TextField soDT;
    public TextField CCCD;
    public Button btnCheck;
    public TextField maPhong;
    public TextField tang;
    public TextField hoTen;
    public DatePicker ngayDen;
    public DatePicker ngayDi;
    public Button btnNhanPhong;
    public PhieuDatPhongDAO phieuDatPhongDAO;
    public CTPhieuDatPhongDAO ctPhieuDatPhongDAO;
    public PhongDAO phongDAO;
    public KhachHangDAO khachHangDAO;
    public ComboBox dsPhong;
    public Boolean check = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        ctPhieuDatPhongDAO = new CTPhieuDatPhongDAO();
        khachHangDAO = new KhachHangDAO();
        phongDAO = new PhongDAO();
        try{
            laydsKh();
            loadData();
            laydsPhong();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void laydsKh() throws SQLException {
        ArrayList<KhachHang> khachhangs = khachHangDAO.layDSKhachHang();
        for(KhachHang khachHang : khachhangs){
            dsKhachHang.getItems().add(khachHang.getMaKH());
        }
        dsKhachHang.getSelectionModel().selectFirst();
    }
    public void loadData() throws SQLException{
        if(dsKhachHang.getItems().size()!=0){
            soDT.setText(khachHangDAO.layKhachHangTheoMa(dsKhachHang.getSelectionModel().getSelectedItem().toString()).getSoDT());
            CCCD.setText(khachHangDAO.layKhachHangTheoMa(dsKhachHang.getSelectionModel().getSelectedItem().toString()).getCCCD());
        }
    }
    public void laydsPhong() throws SQLException {
        ArrayList<Phong> phongs = phongDAO.layDSPhong();
        for(Phong phong : phongs){
            dsPhong.getItems().add(phong.getMaPhong());
        }
        dsPhong.getSelectionModel().selectFirst();
    }
    public void handleCheck(javafx.event.ActionEvent actionEvent) throws SQLException {
        PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(String.valueOf(dsKhachHang.getPlaceholder()));
        CTPhieuDatPhong ctpdp = ctPhieuDatPhongDAO.layCTPhieuDatPhongTheoMa(pdp.getMaPhieu(), String.valueOf(dsPhong.getPlaceholder()));
        KhachHang kh = khachHangDAO.layKhachHangTheoMa(pdp.getMaKH());
        if(ctpdp!=null){
            maPhong.setText(String.valueOf(ctpdp.getMaPhong()));
            tang.setText(String.valueOf(ctpdp.getPhong().getTang()));
            hoTen.setText(kh.getHoTen());
            ngayDen.setValue(ctpdp.getNgayDen());
            ngayDi.setValue(ctpdp.getNgayDi());
            check = true;
        }
    }
    public void showAlertError(String header,String message){
        Alert alert = new  Alert(Alert.AlertType.ERROR);
        alert.setTitle("Đã xảy ra lỗi");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleNhanPhong(ActionEvent actionEvent) throws SQLException {
        if(check){
            phongDAO.capNhatTrangThaiPhong(maPhong.getText(),"Đang sử dụng");
            PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(String.valueOf(dsKhachHang.getPlaceholder()));
            phieuDatPhongDAO.capNhatTrangThaiPhieuDatPhong(pdp.getMaPhieu(),"Hoàn thành");
        }
    }
}







