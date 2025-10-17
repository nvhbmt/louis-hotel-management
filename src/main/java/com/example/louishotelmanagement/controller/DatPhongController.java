package com.example.louishotelmanagement.controller;

import com.dlsc.formsfx.model.structure.Element;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.example.louishotelmanagement.dao.CTPhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class DatPhongController implements Initializable{

    public ComboBox dsPhong;
    public ComboBox dsKhachHang;
    public DatePicker ngayDen;
    public DatePicker ngayDi;
    public Button btnDatPhong;
    public PhongDAO Pdao;
    public TextField maNhanVien;
    public KhachHangDAO Kdao;
    public PhieuDatPhongDAO pdpDao;
    public ArrayList<String> dsMaKH;
    @FXML
    private TableView<Phong> tablePhong;
    @FXML
    private TableColumn<Phong, String> maPhong;
    @FXML
    private TableColumn<Phong, Integer> tang;
    @FXML
    private TableColumn<Phong, String> trangThai;
    @FXML
    private TableColumn<Phong, String> moTa;
    @FXML
    private TableColumn<Phong, String> loaiPhong;
    private String maPhieu;
    private CTPhieuDatPhongDAO ctpDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        pdpDao = new PhieuDatPhongDAO();
        ctpDao = new CTPhieuDatPhongDAO();
        maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        tang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        moTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        loaiPhong.setCellValueFactory(new PropertyValueFactory<>("loaiPhong"));

        try{
            layDsPhong();
            setComboBoxPhong();
            laydsKhachHang();
            loadTable();
        }catch (SQLException e){
            e.printStackTrace();
        }
        dsKhachHang.getSelectionModel().selectFirst();
    }
    public void setComboBoxPhong(){
        dsPhong.getSelectionModel().selectFirst();
    }
    public void laydsKhachHang() throws  SQLException{
        ArrayList<KhachHang> khs = Kdao.layDSKhachHang();
        for(KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
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
    public void showAlertError(String header,String message){
        Alert alert = new  Alert(Alert.AlertType.ERROR);
        alert.setTitle("Đã xảy ra lỗi");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public boolean checkMaPhieu(String maPhieu) throws SQLException {
        ArrayList<PhieuDatPhong> dsPhieuDatPhong = pdpDao.layDSPhieuDatPhong();
        ArrayList<String> dsMaPhieu = new ArrayList<>();
        for(PhieuDatPhong pdph : dsPhieuDatPhong) {
            dsMaPhieu.add(pdph.getMaPhieu());
        }
        if(dsMaPhieu.contains(maPhieu)) {
            return true;
        }
        return false;
    }

    public void showAlert(String header,String message){
        Alert alert = new  Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void DatPhong(KhachHang newKh,String maP,LoaiPhong LoaiP) throws SQLException {
        PhieuDatPhong pdp = new PhieuDatPhong(maPhieu, LocalDate.now(),LocalDate.now(),ngayDi.getValue(),"Đã đặt","Khách đặt online",newKh.getMaKH(),"NV01");
        pdpDao.themPhieuDatPhong(pdp);
        CTPhieuDatPhong ctpdp = new CTPhieuDatPhong(maPhieu,maP,LocalDate.now(),ngayDi.getValue(), BigDecimal.valueOf(LoaiP.getDonGia()));
        ctpDao.themCTPhieuDatPhong(ctpdp);
        showAlert("Thành Công","Bạn đã đặt phòng thành công");
    }
    public void handleDatPhong(ActionEvent actionEvent) throws SQLException {
        if(ngayDen.getValue() == null || ngayDi.getValue() == null) {
            showAlertError("KHÔNG ĐƯỢC ĐỂ TRỐNG DỮ LIỆU","Dữ liệu của ngày đến hoặc đi đã trống");
        }else{
            if(ngayDen.getValue().isBefore(ngayDi.getValue())) {
                showAlertError("XUẤT HIỆN LỖI NGÀY","Không được để ngày đến sau ngày đi");
            }else{
                Phong newPhong =  Pdao.layPhongTheoMa(dsPhong.getSelectionModel().getSelectedItem().toString());
                String maP = newPhong.getMaPhong();
                KhachHang newKh = Kdao.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
                LoaiPhong LoaiP = newPhong.getLoaiPhong();
                Random ran =  new Random();
                do {
                    maPhieu = "PD"+String.valueOf(ran.nextInt(90)+ran.nextInt(9));
                }while(checkMaPhieu(maPhieu));
                ArrayList<CTPhieuDatPhong> dsCTPhieuDatPhong = ctpDao.layDSCTPhieuDatPhongTheoPhong(maP);
                if(dsCTPhieuDatPhong.size()!=0){
                    CTPhieuDatPhong ctp = dsCTPhieuDatPhong.getLast();
                    if(ctpDao.kiemTraPhongDaDuocDat(ctp.getMaPhong(),ngayDen.getValue(),ngayDi.getValue())) {
                        showAlertError("Lỗi đặt phòng", "Phòng đã được đặt trong khoảng thời gian này");
                        return;
                    }else{
                        DatPhong(newKh,maP,LoaiP);
                    }
                }else{
                    DatPhong(newKh,maP,LoaiP);
                }

            }
        }
    }
}
