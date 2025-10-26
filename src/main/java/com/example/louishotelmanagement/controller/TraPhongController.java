package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TraPhongController implements Initializable,Refreshable{

    public Button btnCheck;
    public DatePicker ngayDi;
    public ComboBox dsPhong;
    public ComboBox dsKhachHang;
    public Button btnTraPhong;
    public KhachHangDAO khDao;
    public PhongDAO phDao;
    public CTHoaDonPhongDAO cthdpDao;
    public PhieuDatPhongDAO phieuDatPhongDAO;
    public DatePicker ngayTraPhong;
    public TextField hoTen;
    public TextField maPhieuThue;
    public TextField ngayDen;
    public TextField soLuongPhong;
    public Label lblCheDo;
    public Button btnXemChiTiet;
    private ArrayList<String> dsMaKH = new ArrayList<>();
    private ArrayList<PhieuDatPhong> dspdp;
    private HoaDonDAO hdDao;
    private List<CTHoaDonPhong> listCTHoaDonPhong = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        khDao = new KhachHangDAO();
        phDao = new PhongDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        hdDao = new HoaDonDAO();
        try{
            laydsKhachHang();
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue!=null){
                    try {
                        laydsPhongTheoKhachHang();
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
        if(dsPhong.getSelectionModel().getSelectedItem()==null&&dsPhong.getItems().size()!=0){
            CTHoaDonPhong cthdp = cthdpDao.getDSCTHoaDonPhongTheoMaPhong(dsPhong.getItems().get(0).toString()).getLast();
            listCTHoaDonPhong = new ArrayList<>();
            listCTHoaDonPhong = cthdpDao.getCTHoaDonPhongTheoMaHD(cthdp.getMaHD());
            if(cthdp!=null){
                hoTen.setText(dsKhachHang.getSelectionModel().getSelectedItem().toString());
                maPhieuThue.setText(cthdp.getMaPhieu());
                ngayDen.setText(cthdp.getNgayDen().toString());
                soLuongPhong.setText(String.valueOf(listCTHoaDonPhong.size()));
                lblCheDo.setText("Chế độ: Đang chọn tất cả phòng");
            }else{
                ThongBaoUtil.hienThiLoi("Lỗi kiểm tra","Không tìm thấy bất kì chi tiết hóa đơn phòng nào");
            }
        }else{
            CTHoaDonPhong cthdp = cthdpDao.getDSCTHoaDonPhongTheoMaPhong(dsPhong.getSelectionModel().getSelectedItem().toString()).getLast();
            listCTHoaDonPhong = new ArrayList<>();
            listCTHoaDonPhong = cthdpDao.getCTHoaDonPhongTheoMaHD(cthdp.getMaHD());
            if(cthdp!=null){
                hoTen.setText(dsKhachHang.getSelectionModel().getSelectedItem().toString());
                maPhieuThue.setText(cthdp.getMaPhieu());
                ngayDen.setText(cthdp.getNgayDen().toString());
                soLuongPhong.setText(String.valueOf(listCTHoaDonPhong.size()));
                lblCheDo.setText("Chế độ: Đang chọn tất cả phòng");
            }else{
                ThongBaoUtil.hienThiLoi("Lỗi kiểm tra","Không tìm thấy bất kì chi tiết hóa đơn phòng nào");
            }
        }
    }

    public void handleTraPhong(ActionEvent actionEvent) throws SQLException {

    }


    public void handleXemChiTiet(ActionEvent actionEvent) {
    }

    @Override
    public void refreshData() throws SQLException, Exception {
        laydsKhachHang();
        laydsPhongTheoKhachHang();
    }
}
