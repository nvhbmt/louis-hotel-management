package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.CTPhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DoiPhongController implements Initializable {
    public ComboBox dsKhachHang;
        public ComboBox dsPhongHienTai;
    public ComboBox dsPhong;
    public TextField maNV;
    public TableView tablePhong;
    public Button btnDoiPhong;
    @FXML
    private TableColumn maPhong;
    @FXML
    private TableColumn tang;
    @FXML
    private TableColumn trangThai;
    @FXML
    private TableColumn moTa;
    @FXML
    private TableColumn loaiPhong;
    private PhongDAO Pdao;
    private KhachHangDAO Kdao;
    private PhieuDatPhongDAO pdpDao;
    private CTPhieuDatPhongDAO ctpDao;
    private ArrayList<String> dsMaKH;
    public ArrayList<PhieuDatPhong> dspdp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        pdpDao = new PhieuDatPhongDAO();
        ctpDao =  new CTPhieuDatPhongDAO();
        maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        tang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        moTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        loaiPhong.setCellValueFactory(new PropertyValueFactory<>("loaiPhong"));
        tablePhong.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tablePhong, Priority.ALWAYS);
        try{
            laydsKhachHang();
            layDsPhong();
            dsKhachHang.getSelectionModel().selectFirst();
            dsPhong.getSelectionModel().selectFirst();
            laydsPhongTheoKhachHang();
            loadTable();

            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue!=null){
                    try {
                        laydsPhongTheoKhachHang();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            tablePhong.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null) {
                    Phong newPhong = (Phong) newValue;
                    String maphong = newPhong.getMaPhong();
                    dsPhong.getSelectionModel().select(maphong);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public void laydsKhachHang() throws SQLException {
        ArrayList<KhachHang> khs = Kdao.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for(KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
    }
    public void laydsPhongTheoKhachHang() throws SQLException {
        dsPhongHienTai.getItems().clear();
        dspdp = new ArrayList<>();
        ArrayList<PhieuDatPhong> dsPhieu = pdpDao.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        if(dsPhieu.size()>0) {
            for(PhieuDatPhong p:dsPhieu){
                if(p.getTrangThai() == TrangThaiPhieuDatPhong.DANG_SU_DUNG){
                    dspdp.add(p);
                    ArrayList<CTPhieuDatPhong> dsCTP = ctpDao.layDSCTPhieuDatPhongTheoPhieu(p.getMaPhieu());
                    for(CTPhieuDatPhong ctp : dsCTP){
                        dsPhongHienTai.getItems().add(ctp.getMaPhong());
                    }
                }
            }
            dsPhongHienTai.getSelectionModel().selectFirst();
        }
    }
    public void layDsPhong() throws SQLException {
        ArrayList<Phong> phongs = Pdao.layDSPhongTrong();

        for(Phong phong : phongs) {
            dsPhong.getItems().add(phong.getMaPhong());
        }
    }
    public void handleDoiPhong(ActionEvent actionEvent) throws SQLException {
        if(dsPhongHienTai.getSelectionModel().getSelectedItem()!=null){
            CTPhieuDatPhong ctp = ctpDao.layCTPhieuDatPhongTheoMa(dspdp.get(0).getMaPhieu(),dsPhongHienTai.getSelectionModel().getSelectedItem().toString());
            if(ctp!=null){
                if(ctp.getNgayDi().isAfter(LocalDate.now())){
                    ctp.setMaPhong(dsPhong.getSelectionModel().getSelectedItem().toString());
                    ctp.setGiaPhong(BigDecimal.valueOf(Pdao.layPhongTheoMa(dsPhong.getSelectionModel().getSelectedItem().toString()).getLoaiPhong().getDonGia()));
                    ctpDao.capNhatMaPhong(ctp.getMaPhieu(),dsPhongHienTai.getSelectionModel().getSelectedItem().toString(),ctp.getMaPhong());
                    ctpDao.capNhatGiaPhong(ctp.getMaPhieu(),ctp.getMaPhong(),ctp.getGiaPhong());
                    Pdao.capNhatTrangThaiPhong(dsPhongHienTai.getSelectionModel().getSelectedItem().toString(),"Trống");
                    Pdao.capNhatTrangThaiPhong(ctp.getMaPhong(),"Đang sử dụng");

                    showAlert("Thành công","Đã đổi phòng cho khách hàng");
                }else{
                    showAlertError("Không đổi phòng được","Bạn không thể đổi phòng vì đã đến ngày hạn");
                }
            }else{
                showAlertError("Không thực hiện được","Không tìm thấy bất kì chi tiết phòng nào");
            }
        }else{
            showAlertError("Không thực hiện được","Vui lòng chọn phòng của bạn");
        }
    }


}
