package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.CTHoaDonPhongDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.service.AuthService;
import com.example.louishotelmanagement.util.ThongBaoUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DoiPhongController implements Initializable,Refreshable {
    public ComboBox dsKhachHang;
    public ComboBox dsPhongHienTai;
    public ComboBox dsPhong;
    public TableView tablePhong;
    public Button btnDoiPhong;
    @FXML
    private TableColumn<Phong, String> maPhong;
    @FXML
    private TableColumn<Phong, Integer> tang;
    @FXML
    private TableColumn<Phong, TrangThaiPhong> trangThai;
    @FXML
    private TableColumn<Phong, String> moTa;
    @FXML
    private TableColumn<Phong, String> loaiPhong;
    private PhongDAO Pdao;
    private KhachHangDAO Kdao;
    private PhieuDatPhongDAO pdpDao;
    private CTHoaDonPhongDAO cthdpDao;
    private ArrayList<String> dsMaKH;
    public ArrayList<PhieuDatPhong> dspdp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pdao = new PhongDAO();
        Kdao = new KhachHangDAO();
        pdpDao = new PhieuDatPhongDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        VBox.setVgrow(tablePhong, Priority.ALWAYS);
        try {
            laydsKhachHang();
            layDsPhong();
            dsKhachHang.getSelectionModel().selectFirst();
            dsPhong.getSelectionModel().selectFirst();
            laydsPhongTheoKhachHang();
            khoiTaoTableView();
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        laydsPhongTheoKhachHang();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            tablePhong.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Phong newPhong = (Phong) newValue;
                    String maphong = newPhong.getMaPhong();
                    dsPhong.getSelectionModel().select(maphong);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void khoiTaoTableView() throws SQLException {
        // Thiết lập các cột
        maPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        tang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        trangThai.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(TrangThaiPhong item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().clear();
                } else {
                    setText(item.toString());
                    setAlignment(Pos.TOP_CENTER);
                    getStyleClass().clear();
                    getStyleClass().add("status-trong");
                }
            }
        });
        moTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        loaiPhong.setCellValueFactory(cellData -> {
            LoaiPhong loaiPhong = cellData.getValue().getLoaiPhong();

            // 2. Trả về StringBinding chứa Tên Loại.
            // Nếu LoaiPhong không null, liên kết (bind) với thuộc tính TenLoai.
            return loaiPhong != null ?
                    javafx.beans.binding.Bindings.createStringBinding(loaiPhong::getTenLoai) :
                    javafx.beans.binding.Bindings.createStringBinding(() -> ""); // Xử lý trường hợp null
        });

        // Thiết lập TableView
        ArrayList<Phong> dsPhongTrong = Pdao.layDSPhongTrong();
        ObservableList<Phong> observableListPhong = FXCollections.observableArrayList(dsPhongTrong);
        tablePhong.setItems(observableListPhong);

        // Cho phép chọn nhiều dòng
        tablePhong.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void laydsKhachHang() throws SQLException {
        ArrayList<KhachHang> khs = Kdao.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for (KhachHang khachHang : khs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
    }

    public void laydsPhongTheoKhachHang() throws SQLException {
        dsPhongHienTai.getItems().clear();
        dspdp = new ArrayList<>();
        ArrayList<PhieuDatPhong> dsPhieu = pdpDao.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        if (dsPhieu.size() > 0) {
            for (PhieuDatPhong p : dsPhieu) {
                if (p.getTrangThai() != null && p.getTrangThai().equals(TrangThaiPhieuDatPhong.DANG_SU_DUNG)) {
                    dspdp.add(p);
                    ArrayList<CTHoaDonPhong> dsCTP = cthdpDao.getCTHoaDonPhongTheoMaPhieu(p.getMaPhieu());
                    for (CTHoaDonPhong ctp : dsCTP) {
                        dsPhongHienTai.getItems().add(ctp.getMaPhong());
                    }
                }
            }
        }else{
            dsPhong.getItems().clear();
        }
    }

    public void layDsPhong() throws SQLException {
        ArrayList<Phong> phongs = Pdao.layDSPhongTrong();

        for (Phong phong : phongs) {
            dsPhong.getItems().add(phong.getMaPhong());
        }
    }

    public void handleDoiPhong(ActionEvent actionEvent) throws SQLException {
        if (dsPhongHienTai.getSelectionModel().getSelectedItem() != null) {
            ArrayList<CTHoaDonPhong> dscthdp = cthdpDao.getDSCTHoaDonPhongTheoMaPhong(dsPhongHienTai.getSelectionModel().getSelectedItem().toString());
            CTHoaDonPhong ctHoaDonPhongMoiNhat = dscthdp.getLast();
            if(ctHoaDonPhongMoiNhat!=null){
                Phong pMoi = Pdao.layPhongTheoMa(dsPhongHienTai.getSelectionModel().getSelectedItem().toString());
                cthdpDao.capNhatMaPhongVaGia(ctHoaDonPhongMoiNhat.getMaPhieu(),dsPhongHienTai.getSelectionModel().getSelectedItem().toString(),dsPhong.getSelectionModel().getSelectedItem().toString(),BigDecimal.valueOf(pMoi.getLoaiPhong().getDonGia()));
                Pdao.capNhatTrangThaiPhong(dsPhongHienTai.getSelectionModel().getSelectedItem().toString(), TrangThaiPhong.TRONG.toString());
                Pdao.capNhatTrangThaiPhong(dsPhong.getSelectionModel().getSelectedItem().toString(), TrangThaiPhong.DANG_SU_DUNG.toString());
                refreshData();
            }
            ThongBaoUtil.hienThiThongBao("Thông báo","Đổi phòng thành công");
        } else {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không thực hiện được");
        }
    }


    @Override
    public void refreshData() throws SQLException {
        dsPhongHienTai.getItems().clear();
        laydsPhongTheoKhachHang();
        dsPhongHienTai.getSelectionModel().selectFirst();
        dsPhong.getItems().clear();
        layDsPhong();
        dsPhong.getSelectionModel().selectFirst();
        dsKhachHang.getSelectionModel().selectFirst();
        khoiTaoTableView();
    }
}
