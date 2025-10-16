package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
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
    public Button btnDatPhong;
    private PhongDAO Pdao;
    private KhachHangDAO Kdao;
    private PhieuDatPhongDAO pdpDao;
    private CTPhieuDatPhongDAO ctpDao;
    private ArrayList<String> dsMaKH;
    private NhanVienDAO nvdao;
    private String maPhieu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        pdpDao = new PhieuDatPhongDAO();
        nvdao = new NhanVienDAO();
        ctpDao = new CTPhieuDatPhongDAO();
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
        dsMaKH = new ArrayList<>();
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
    public void showAlert(String header,String message){
        Alert alert = new  Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
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
    public void handleDatPhong(ActionEvent actionEvent) throws SQLException {
        if(ngayDi.getValue().isAfter(LocalDate.now())||ngayDi.getValue().isEqual(LocalDate.now())){
            Phong newPhong =  Pdao.layPhongTheoMa(dsPhong.getSelectionModel().getSelectedItem().toString());
            String maP = newPhong.getMaPhong();
            KhachHang newKh = Kdao.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
            LoaiPhong LoaiP = newPhong.getLoaiPhong();
            Random ran =  new Random();
            do {
                maPhieu = "PD"+String.valueOf(ran.nextInt(90)+ran.nextInt(9));
            }while(checkMaPhieu(maPhieu));
            PhieuDatPhong pdp = new PhieuDatPhong(maPhieu, LocalDate.now(),LocalDate.now(),ngayDi.getValue(),"Đã đặt","",newKh.getMaKH(),"NV01");
            pdpDao.themPhieuDatPhong(pdp);
            CTPhieuDatPhong ctpdp = new CTPhieuDatPhong(maPhieu,maP,LocalDate.now(),ngayDi.getValue(), BigDecimal.valueOf(LoaiP.getDonGia()));
            ctpDao.themCTPhieuDatPhong(ctpdp);
            showAlert("Thành Công","Bạn đã đặt phòng thành công");
        }else{
            showAlertError("lỖI NGÀY","không được chọn ngày trước ngày hôm nay");
        }
    }
}
