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
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    loadData();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void laydsKh() throws SQLException {
        ArrayList<KhachHang> khachhangs = khachHangDAO.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for(KhachHang khachHang : khachhangs){
            dsKhachHang.getItems().add(khachHang.getMaKH());
            dsMaKH.add(khachHang.getMaKH());
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
                    }else{
                        showAlertError("Không tìm thông tin","Không có bất kì thông tin nào về khách hàng và phòng");
                    }
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
            phieuDatPhongDAO.capNhatTrangThaiPhieuDatPhong(pdp.getMaPhieu(),"Hoàn thành");
            ctPhieuDatPhongDAO.capNhatNgayNhan(pTam.getMaPhieu(),maPhong.getText(), LocalDate.now());
            showAlert("Thành công","Bạn đã nhận phòng thành công");
        }else{
            showAlertError("Không đặt được phòng","Không tìm thấy bất kì phòng nào có thể nhận");
        }
    }
}







