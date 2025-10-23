package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.CTHoaDonPhongDAO;
import com.example.louishotelmanagement.dao.KhachHangDAO;
import com.example.louishotelmanagement.dao.PhieuDatPhongDAO;
import com.example.louishotelmanagement.dao.PhongDAO;
import com.example.louishotelmanagement.model.CTHoaDonPhong;
import com.example.louishotelmanagement.model.KhachHang;
import com.example.louishotelmanagement.model.PhieuDatPhong;
import com.example.louishotelmanagement.model.TrangThaiPhieuDatPhong;
import com.example.louishotelmanagement.util.ThongBaoUtil;

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
    public CTHoaDonPhongDAO ctHoaDondao;
    public PhongDAO phongDAO;
    public KhachHangDAO khachHangDAO;
    public ComboBox dsPhong;
    public Boolean check = false;
    public DatePicker ngayDat;
    private ArrayList<String> dsMaKH;
    ArrayList<CTHoaDonPhong> dsCTHoaDonPhong;
    private PhieuDatPhong pTam;
    private ArrayList<PhieuDatPhong> dspdp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        ctHoaDondao = new CTHoaDonPhongDAO();
        khachHangDAO = new KhachHangDAO();
        phongDAO = new PhongDAO();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void capNhatNgayDatTheoPhong(String maPhong) {
        try {
            ArrayList<CTHoaDonPhong> dsCTPDP = ctHoaDondao.getDSCTHoaDonPhongTheoMaPhong(maPhong);
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
        for (KhachHang khachHang : khachhangs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }
        dsKhachHang.getSelectionModel().selectFirst();
    }

    public void loadData() throws SQLException {
        if (dsKhachHang.getItems().size() != 0) {
            soDT.setText(khachHangDAO.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex())).getSoDT());
            CCCD.setText(khachHangDAO.layKhachHangTheoMa(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex())).getCCCD());
        }
    }

    public void laydsPhongTheoKhachHang() throws SQLException {
        dsPhong.getItems().clear();
        dspdp = new ArrayList<>();
        ArrayList<PhieuDatPhong> dsPhieu = phieuDatPhongDAO.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(dsKhachHang.getSelectionModel().getSelectedIndex()));
        if (dsPhieu.size() > 0) {
            for (PhieuDatPhong p : dsPhieu) {
                    if (p.getTrangThai() != null && p.getTrangThai().equals(TrangThaiPhieuDatPhong.DA_DAT)) {
                        dspdp.add(p);
                        ArrayList<CTHoaDonPhong> dsCTP = ctHoaDondao.getCTHoaDonPhongTheoMaPhieu(p.getMaPhieu());
                        for (CTHoaDonPhong ctp : dsCTP) {
                            dsPhong.getItems().add(ctp.getMaPhong());
                        }
                    }

            }
        }else{
            dsPhong.getItems().clear();
        }
        if (dsPhong.getItems().size() != 0) {
            dsPhong.getSelectionModel().selectFirst();
            capNhatNgayDatTheoPhong(dsPhong.getSelectionModel().getSelectedItem().toString());
        } else {
            ngayDat.setValue(null);
        }

    }

    public void handleCheck(javafx.event.ActionEvent actionEvent) throws SQLException {
        Boolean found = false;
        dsCTHoaDonPhong = ctHoaDondao.getDSCTHoaDonPhongTheoMaPhong(dsPhong.getSelectionModel().getSelectedItem().toString());
        if (dsCTHoaDonPhong.size() == 0) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không tìm được phòng");
            check = false;
            return;
        } else {
            if (ngayDat.getValue() == null) {
                ThongBaoUtil.hienThiLoi("Lỗi", "Ngày đặt trống");
                return;
            } else {
                for (CTHoaDonPhong ctpdp : dsCTHoaDonPhong) {
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
                if (!check) {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Không tìm thông tin");
                }

            }
        }
    }

    public void handleNhanPhong(ActionEvent actionEvent) throws SQLException {
        if (check) {
            phongDAO.capNhatTrangThaiPhong(maPhong.getText(), "Đang sử dụng");
            PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(maPhieu.getText());
            phieuDatPhongDAO.capNhatTrangThaiPhieuDatPhong(pdp.getMaPhieu(), "Đang sử dụng");
            ctHoaDondao.capNhatNgayDenThucTe(pTam.getMaPhieu(), maPhong.getText(), LocalDate.now());
            ThongBaoUtil.hienThiThongBao("Thành công", "Bạn đã nhận phòng thành công");
            dsPhong.getItems().remove(dsPhong.getSelectionModel().getSelectedIndex());
            dsPhong.getSelectionModel().selectFirst();
        } else {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không đặt được phòng");
        }
    }
}







