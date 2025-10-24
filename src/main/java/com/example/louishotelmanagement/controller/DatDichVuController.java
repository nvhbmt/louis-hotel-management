package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatDichVuController implements Initializable {


    
    public TextField txtNgayLap;
    public TextField txtMaPhieuTam;
    public TableView tblChiTietTam;
    public TableColumn colDVMa;
    public TableColumn colDVTen;
    public TableColumn colDVSL;
    public TableColumn colDVGia;
    public ComboBox cboDichVu;
    public TextField txtSoLuong;
    public Button btnThemDV;
    public Button btnXoaDV;
    public TextArea txtGhiChu;
    public VBox dsDichVuDaDat;
    public Label lblTongTienTam;
    public Button btnXacNhanLapPhieu;
    public ComboBox dsPhong;
    public ComboBox dsKhachHang;
    public TableColumn colDVMoTa;
    public TableColumn colDVConKinhDoanh;
    @FXML
    private Label tieuDeLabel;
    public KhachHangDAO kDao;
    public PhongDAO pDao;
    public CTHoaDonDichVuDAO cthddvDao;
    public PhieuDichVuDAO pdvDao;
    public DichVuDAO dvDao;
    public PhieuDatPhongDAO phieuDatPhongDAO;
    public CTHoaDonPhongDAO ctHoaDondao;
    public ArrayList<String> dsMaKH;
    public ArrayList<String> dsMaDV;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        kDao = new KhachHangDAO();
        pDao = new PhongDAO();
        cthddvDao = new CTHoaDonDichVuDAO();
        pdvDao = new PhieuDichVuDAO();
        dvDao = new DichVuDAO();
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        ctHoaDondao = new CTHoaDonPhongDAO();
        try{
            laydsKh();
            laydsPhongTheoKhachHang();
            khoiTaoTableView();
            layDsDichVu();
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    laydsPhongTheoKhachHang();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            tblChiTietTam.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null){
                    DichVu dv = (DichVu) newValue;
                    cboDichVu.getSelectionModel().select(dv.getTenDV());
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void laydsKh() throws SQLException {
        ArrayList<KhachHang> khachhangs = kDao.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for (KhachHang khachHang : khachhangs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
        dsKhachHang.getSelectionModel().selectFirst();
    }
    public void laydsPhongTheoKhachHang() throws SQLException {
        dsPhong.getItems().clear();
        ArrayList<PhieuDatPhong> dsPhieu = phieuDatPhongDAO.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        if (dsPhieu.size() > 0) {
            for (PhieuDatPhong p : dsPhieu) {
                if (p.getTrangThai() != null && p.getTrangThai().equals(TrangThaiPhieuDatPhong.DANG_SU_DUNG)) {
                    ArrayList<CTHoaDonPhong> dsCTP = ctHoaDondao.getCTHoaDonPhongTheoMaPhieu(p.getMaPhieu());
                    for (CTHoaDonPhong ctp : dsCTP) {
                        dsPhong.getItems().add(ctp.getMaPhong());
                    }
                }

            }
        }else{
            dsPhong.getItems().clear();
        }

    }
    public void khoiTaoTableView() throws Exception {
        colDVMa.setCellValueFactory(new PropertyValueFactory<>("maDV"));
        colDVTen.setCellValueFactory(new PropertyValueFactory<>("tenDV"));
        colDVSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colDVGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colDVMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colDVConKinhDoanh.setCellValueFactory(new PropertyValueFactory<>("conKinhDoanh"));
        colDVConKinhDoanh.setCellFactory(column->new TableCell<DichVu, Boolean>(){
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null){
                    setText(null);
                }else{
                    setText("Còn kinh doanh");
                    setStyle("-fx-text-fill: #198754; -fx-font-weight: bold;");
                }
            }
        });
        List<DichVu> listDv = dvDao.layTatCaDichVu(true);
        ObservableList<DichVu> list =  FXCollections.observableArrayList(listDv);
        tblChiTietTam.setItems(list);
    }
    public void layDsDichVu() throws Exception {
        List<DichVu> listDv = dvDao.layTatCaDichVu(true);
        dsMaDV = new ArrayList<>();
        for(DichVu dv : listDv){
            cboDichVu.getItems().add(dv.getTenDV());
            dsMaDV.add(dv.getMaDV());
        }
    }


    public void handleXacNhanLapPhieu(ActionEvent actionEvent) {
    }

    public void handleThemDichVu(ActionEvent actionEvent) {
    }

    public void handleXoaDichVu(ActionEvent actionEvent) {
    }
}
