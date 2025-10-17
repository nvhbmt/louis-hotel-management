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

public class TraPhongController implements Initializable{

    public Button btnCheck;
    public DatePicker ngayDi;
    public ComboBox dsPhong;
    public ComboBox dsKhachHang;
    public TextField maPhong;
    public TextField tang;
    public TextField hoTen;
    public DatePicker ngayDat;
    public DatePicker ngayDen;
    public Button btnTraPhong;
    public KhachHangDAO khDao;
    public PhongDAO phDao;
    public CTPhieuDatPhongDAO ctpDao;
    public PhieuDatPhongDAO phieuDatPhongDAO;
    private ArrayList<String> dsMaKH = new ArrayList<>();
    private ArrayList<CTPhieuDatPhong> dsCTPhieuDatPhong;
    private boolean check = false;
    private PhieuDatPhong pTam;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        khDao = new KhachHangDAO();
        phDao = new PhongDAO();
        ctpDao = new CTPhieuDatPhongDAO();
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        try{
            layDsPhong();
            laydsKhachHang();
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
    public void layDsPhong() throws SQLException {
        ArrayList<Phong> phongs = phDao.layDSPhongTrong();
        for(Phong phong : phongs) {
            dsPhong.getItems().add(phong.getMaPhong());
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
    public void handleCheck(javafx.event.ActionEvent actionEvent) throws SQLException {
        dsCTPhieuDatPhong = ctpDao.layDSCTPhieuDatPhongTheoPhong(dsPhong.getSelectionModel().getSelectedItem().toString());
        if(dsCTPhieuDatPhong.size()==0){
            showAlertError("Không tìm được phòng","Không tìm thấy bất kì phòng nào có thể trả");
            check = false;
            return;
        }
        else{
            if(ngayDat.getValue()==null){
                showAlertError("Ngày đi trống","Không được thiếu thông tin ngày đi");
                return ;
            }else{
                for(CTPhieuDatPhong ctpdp : dsCTPhieuDatPhong) {
                    if ((Objects.equals(phieuDatPhongDAO.layPhieuDatPhongTheoMa(ctpdp.getMaPhieu()).getMaKH(), dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()))) && (phieuDatPhongDAO.layPhieuDatPhongTheoMa(ctpdp.getMaPhieu()).getNgayDi().isEqual(ngayDi.getValue()))) {
                        maPhong.setText(String.valueOf(ctpdp.getMaPhong()));
                        tang.setText(String.valueOf(phDao.layPhongTheoMa(ctpdp.getMaPhong()).getTang()));
                        KhachHang kh = khDao.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
                        hoTen.setText(kh.getHoTen());
                        pTam = phieuDatPhongDAO.layPhieuDatPhongTheoMa(ctpdp.getMaPhieu());
                        ngayDat.setValue(pTam.getNgayDat());
                        ngayDen.setValue(ctpdp.getNgayDi());
                        check = true;
                    }else{
                        showAlertError("Không tìm thông tin","Không có bất kì thông tin nào về khách hàng và phòng");
                        return;
                    }
                }

            }
        }
    }

    public void handleTraPhong(ActionEvent actionEvent) throws SQLException {
        if(check){
            phDao.capNhatTrangThaiPhong(maPhong.getText(),"Trống");
            ctpDao.capNhatNgayTra(pTam.getMaPhieu(),maPhong.getText(), LocalDate.now());
            showAlert("Thành công","Đã trả phòng thành công");
        }else{
            showAlertError("Không thể trả phòng","Không tìm được bất kì thông tin nào về phòng");
        }
    }


}
