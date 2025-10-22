package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.CTPhieuDatPhongDAO;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class NhanPhongController implements Initializable {
    public ComboBox dsKhachHang;
    public TextField soDT;
    public TextField CCCD;
    public Button btnCheck;
    public TextField maPhieu;
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
    public DatePicker ngayDat;
    private ArrayList<String> dsMaKH;
    ArrayList<CTPhieuDatPhong> dsCTPhieuDatPhong;
    private PhieuDatPhong pTam;
    private ArrayList<PhieuDatPhong> dspdp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        ctPhieuDatPhongDAO = new CTPhieuDatPhongDAO();
        khachHangDAO = new KhachHangDAO();
        phongDAO = new PhongDAO();
        try{
            laydsKh();
            loadData();
            laydsPhongTheoKhachHang();
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    loadData();
                    laydsPhongTheoKhachHang();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            dsPhong.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    capNhatNgayDatTheoPhong(newValue.toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void capNhatNgayDatTheoPhong(String maPhong) {
        try {
            ArrayList<CTPhieuDatPhong> dsCTPDP = ctPhieuDatPhongDAO.layDSCTPhieuDatPhongTheoPhong(maPhong);
            if (!dsCTPDP.isEmpty()) {
                PhieuDatPhong phieu = phieuDatPhongDAO.layPhieuDatPhongTheoMa(dsCTPDP.getLast().getMaPhieu());
                ngayDat.setValue(phieu.getNgayDat());
            } else {
                ngayDat.setValue(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void laydsKh() throws SQLException {
        ArrayList<KhachHang> khachhangs = khachHangDAO.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for(KhachHang khachHang : khachhangs){
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
        dsKhachHang.getSelectionModel().selectFirst();
    }
    public void loadData() throws SQLException{
        if(dsKhachHang.getItems().size()!=0){
            soDT.setText(khachHangDAO.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex())).getSoDT());
            CCCD.setText(khachHangDAO.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex())).getCCCD());
        }
    }
    public void laydsPhongTheoKhachHang() throws SQLException {
        dsPhong.getItems().clear();
        dspdp = new ArrayList<>();
        ArrayList<PhieuDatPhong> dsPhieu = phieuDatPhongDAO.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        if(dsPhieu.size()>0) {
            for(PhieuDatPhong p:dsPhieu){
                if(p.getTrangThai() == TrangThaiPhieuDatPhong.DA_DAT){
                    dspdp.add(p);
                    ArrayList<CTPhieuDatPhong> dsCTP = ctPhieuDatPhongDAO.layDSCTPhieuDatPhongTheoPhieu(p.getMaPhieu());
                    for(CTPhieuDatPhong ctp : dsCTP){
                        dsPhong.getItems().add(ctp.getMaPhong());
                    }
                }
            }
        }
        if(dsPhong.getItems().size()!=0){
            dsPhong.getSelectionModel().selectFirst();
            capNhatNgayDatTheoPhong(dsPhong.getSelectionModel().getSelectedItem().toString());
        }else{
            ngayDat.setValue(null);
        }

    }
    public void handleCheck(javafx.event.ActionEvent actionEvent) throws SQLException {
        Boolean found = false;
        dsCTPhieuDatPhong = ctPhieuDatPhongDAO.layDSCTPhieuDatPhongTheoPhong(dsPhong.getSelectionModel().getSelectedItem().toString());
        if(dsCTPhieuDatPhong.size()==0){
            showAlertError("Không tìm được phòng","Không tìm thấy bất kì phòng nào có thể nhận");
            check = false;
            return;
        }
        else{
            if(ngayDat.getValue()==null){
                showAlertError("Ngày đặt trống","Không được thiếu thông tin ngày đặt");
                return ;
            }else{
                for(CTPhieuDatPhong ctpdp : dsCTPhieuDatPhong) {
                    if ((Objects.equals(phieuDatPhongDAO.layPhieuDatPhongTheoMa(ctpdp.getMaPhieu()).getMaKH(), dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()))) && (phieuDatPhongDAO.layPhieuDatPhongTheoMa(ctpdp.getMaPhieu()).getNgayDat().isEqual(ngayDat.getValue()))) {
                        maPhieu.setText(ctpdp.getMaPhieu());
                        maPhong.setText(String.valueOf(ctpdp.getMaPhong()));
                        tang.setText(String.valueOf(phongDAO.layPhongTheoMa(ctpdp.getMaPhong()).getTang()));
                        KhachHang kh = khachHangDAO.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
                        hoTen.setText(kh.getHoTen());
                        ngayDen.setValue(ctpdp.getNgayDen());
                        ngayDi.setValue(ctpdp.getNgayDi());
                        pTam = phieuDatPhongDAO.layPhieuDatPhongTheoMa(ctpdp.getMaPhieu());
                        check = true;
                        break;
                    }
                }
                if(!check){
                    showAlertError("Không tìm thông tin","Không có bất kì thông tin nào về khách hàng và phòng");
                }

            }
        }
    }

    public void showAlertError(String header,String message){
        Alert alert = new  Alert(Alert.AlertType.ERROR);
        alert.setTitle("Đã xảy ra lỗi");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void showAlert(String header,String message){
        Alert alert = new  Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void handleNhanPhong(ActionEvent actionEvent) throws SQLException {
        if(check){
            phongDAO.capNhatTrangThaiPhong(maPhong.getText(),"Đang sử dụng");
            PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(maPhieu.getText());
            phieuDatPhongDAO.capNhatTrangThaiPhieuDatPhong(pdp.getMaPhieu(),"Đang sử dụng");
            ctPhieuDatPhongDAO.capNhatNgayNhan(pTam.getMaPhieu(),maPhong.getText(), LocalDate.now());
            showAlert("Thành công","Bạn đã nhận phòng thành công");
            dsPhong.getItems().remove(dsPhong.getSelectionModel().getSelectedIndex());
            dsPhong.getSelectionModel().selectFirst();
        }else{
            showAlertError("Không đặt được phòng","Không tìm thấy bất kì phòng nào có thể nhận");
        }
    }
}







