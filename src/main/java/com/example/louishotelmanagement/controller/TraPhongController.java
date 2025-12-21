package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.util.Refreshable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TraPhongController implements Initializable, Refreshable {

    @FXML public Button btnCheck;
    @FXML public ComboBox<String> dsPhong;
    @FXML public ComboBox<String> dsKhachHang;
    @FXML public ComboBox<String> dsPhieu;
    @FXML public Button btnTraPhong;
    @FXML public DatePicker ngayTraPhong;
    @FXML public TextField hoTen;
    @FXML public TextField maPhieuThue;
    @FXML public DatePicker ngayDen;
    @FXML public TextField soLuongPhong;
    @FXML public Button btnXemChiTiet;

    private KhachHangDAO khDao;
    private PhongDAO phDao;
    private CTHoaDonPhongDAO cthdpDao;
    private PhieuDatPhongDAO phieuDatPhongDAO;
    private HoaDonDAO hdDao;

    private ArrayList<String> listMaKH = new ArrayList<>();
    private ContentSwitcher switcher;

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        khDao = new KhachHangDAO();
        phDao = new PhongDAO();
        cthdpDao = new CTHoaDonPhongDAO();
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        hdDao = new HoaDonDAO();

        btnXemChiTiet.setDisable(true);
        khoiTaoDinhDangNgay();
        setDisableFields();

        try {
            laydsKhachHang();

            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    try {
                        laydsPhieuTheoKhachHang();
                    } catch (SQLException e) { e.printStackTrace(); }
                }
            });

            dsPhieu.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    try {
                        laydsPhongTheoPhieu();
                    } catch (SQLException e) { e.printStackTrace(); }
                }
            });

            ngayTraPhong.setValue(LocalDate.now());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void truyenDuLieuTuPhong(String maPhongTruyenVao) {
        try {
            ArrayList<CTHoaDonPhong> listCT = cthdpDao.getDSCTHoaDonPhongTheoMaPhong(maPhongTruyenVao);

            String maPhieuTimThay = null;

            for (CTHoaDonPhong ct : listCT) {
                PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(ct.getMaPhieu());
                if (pdp != null && pdp.getTrangThai().equals(TrangThaiPhieuDatPhong.DANG_SU_DUNG)) {
                    maPhieuTimThay = pdp.getMaPhieu();
                    break;
                }
            }

            if (maPhieuTimThay != null) {
                PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(maPhieuTimThay);
                KhachHang kh = khDao.layKhachHangTheoMa(pdp.getMaKH());

                if (kh != null) {
                    dsKhachHang.getSelectionModel().select(kh.getHoTen());

                    laydsPhieuTheoKhachHang();
                    dsPhieu.getSelectionModel().select(maPhieuTimThay);

                    laydsPhongTheoPhieu();
                    dsPhong.getSelectionModel().select(maPhongTruyenVao);

                    handleCheck(null);
                }
            } else {
                ThongBaoUtil.hienThiLoi("Lỗi", "Phòng " + maPhongTruyenVao + " hiện không có phiếu thuê đang hoạt động.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDisableFields() {
        ngayDen.setDisable(true);
        ngayDen.setStyle("-fx-opacity: 1; -fx-text-fill: black; -fx-background-color: #eee;");
        ngayTraPhong.setDisable(true);
        ngayTraPhong.setStyle("-fx-opacity: 1; -fx-text-fill: black; -fx-background-color: #eee;");
    }

    private void khoiTaoDinhDangNgay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override public String toString(LocalDate date) { return (date != null) ? formatter.format(date) : ""; }
            @Override public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        };
        ngayTraPhong.setConverter(converter);
        ngayDen.setConverter(converter);
    }

    public void laydsKhachHang() throws SQLException {
        dsKhachHang.getItems().clear();
        listMaKH.clear();
        ArrayList<KhachHang> khs = khDao.layDSKhachHang();
        for (KhachHang kh : khs) {
            dsKhachHang.getItems().add(kh.getHoTen());
            listMaKH.add(kh.getMaKH());
        }

        com.example.louishotelmanagement.util.SearchBoxUtil.makeSearchable(dsKhachHang);
    }

    public void laydsPhieuTheoKhachHang() throws SQLException {
        dsPhieu.getItems().clear();
        int index = dsKhachHang.getSelectionModel().getSelectedIndex();
        if (index < 0) return;

        ArrayList<PhieuDatPhong> listpdp = phieuDatPhongDAO.layDSPhieuDatPhongTheoKhachHang(listMaKH.get(index));
        for (PhieuDatPhong pdp : listpdp) {
            if (pdp.getTrangThai().equals(TrangThaiPhieuDatPhong.DANG_SU_DUNG)) {
                dsPhieu.getItems().add(pdp.getMaPhieu());
            }
        }
        if (!dsPhieu.getItems().isEmpty()) dsPhieu.getSelectionModel().selectFirst();
    }

    public void laydsPhongTheoPhieu() throws SQLException {
        dsPhong.getItems().clear();
        String selectedPhieu = dsPhieu.getSelectionModel().getSelectedItem();
        if (selectedPhieu != null) {
            ArrayList<CTHoaDonPhong> listCT = cthdpDao.getCTHoaDonPhongTheoMaPhieu(selectedPhieu);
            for (CTHoaDonPhong ct : listCT) {
                dsPhong.getItems().add(ct.getMaPhong());
            }
            if (!dsPhong.getItems().isEmpty()) dsPhong.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void handleCheck(ActionEvent actionEvent) throws Exception {
        String selectedPhieuId = dsPhieu.getSelectionModel().getSelectedItem();
        if (selectedPhieuId != null) {
            PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(selectedPhieuId);
            if (pdp != null) {
                KhachHang kh = khDao.layKhachHangTheoMa(pdp.getMaKH());
                if (kh != null) hoTen.setText(kh.getHoTen());

                maPhieuThue.setText(pdp.getMaPhieu());

                ArrayList<CTHoaDonPhong> listCT = cthdpDao.getCTHoaDonPhongTheoMaPhieu(pdp.getMaPhieu());
                if (!listCT.isEmpty()) {
                    ngayDen.setValue(listCT.get(0).getNgayDen());
                    soLuongPhong.setText(String.valueOf(listCT.size()));
                }
                btnXemChiTiet.setDisable(false);
            }
        } else {
            if(actionEvent != null) ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phiếu để kiểm tra");
            btnXemChiTiet.setDisable(true);
        }
    }

    @FXML
    public void handleTraPhong(ActionEvent actionEvent) throws Exception {
        String maPhieu = maPhieuThue.getText();
        if (maPhieu == null || maPhieu.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng kiểm tra phiếu trước khi trả.");
            return;
        }

        ArrayList<CTHoaDonPhong> listCT = cthdpDao.getCTHoaDonPhongTheoMaPhieu(maPhieu);
        if (listCT.isEmpty()) return;

        CTHoaDonPhong activeCT = listCT.get(0);
        HoaDon hd = hdDao.timHoaDonTheoMa(activeCT.getMaHD());
        PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(maPhieu);

        if (hd != null) {
            if (hd.getTrangThai().equals(TrangThaiHoaDon.CHUA_THANH_TOAN)) {
                moDialogThanhToan(hd);
            } else {
                // 1. Cập nhật trạng thái phiếu và phòng
                phieuDatPhongDAO.capNhatTrangThaiPhieuDatPhong(maPhieu, TrangThaiPhieuDatPhong.HOAN_THANH.toString());
                for (CTHoaDonPhong ct : listCT) {
                    phDao.capNhatTrangThaiPhong(ct.getMaPhong(), TrangThaiPhong.TRONG.toString());
                    cthdpDao.capNhatNgayDiThucTe(ct.getMaHD(), ct.getMaPhong(), LocalDate.now());
                }

                // 2. THÊM MỚI: Cập nhật trạng thái khách hàng sang CHECK_OUT
                if (pdp != null) {
                    khDao.capNhatTrangThaiKhachHang(pdp.getMaKH(), TrangThaiKhachHang.CHECK_OUT);
                }

                ThongBaoUtil.hienThiThongBao("Thành công", "Đã trả phòng và cập nhật trạng thái khách hàng!");
                refreshData();
                boolean daThanhToanXong = moDialogThanhToan(hd);
                if(daThanhToanXong) {
                    thucHienTraPhong(maPhieu, listCT);
                } else {
                    ThongBaoUtil.hienThiLoi("Lỗi", "Thanh toán chưa hoàn tất, không thể trả phòng.");
                }
            }
        }
    }

    private void thucHienTraPhong(String maPhieu, ArrayList<CTHoaDonPhong> listCT) throws SQLException {
        phieuDatPhongDAO.capNhatTrangThaiPhieuDatPhong(maPhieu, TrangThaiPhieuDatPhong.HOAN_THANH.toString());

        for (CTHoaDonPhong ct : listCT) {
            phDao.capNhatTrangThaiPhong(ct.getMaPhong(), TrangThaiPhong.TRONG.toString());
            cthdpDao.capNhatNgayDiThucTe(ct.getMaHD(), ct.getMaPhong(), LocalDate.now());
        }

        ThongBaoUtil.hienThiThongBao("Thành công", "Đã trả phòng thành công!");
        try {
            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean moDialogThanhToan(HoaDon hoaDon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/thanh-toan-dialog.fxml"));
            Parent root = loader.load();
            ThanhToanDialogController controller = loader.getController();
            controller.setHoaDon(hoaDon);

            Stage stage = new Stage();
            stage.setTitle("Thanh Toán #" + hoaDon.getMaHD());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            HoaDon hdKiemTra = hdDao.timHoaDonTheoMa(hoaDon.getMaHD());
            return hdKiemTra != null && hdKiemTra.getTrangThai() == TrangThaiHoaDon.DA_THANH_TOAN;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void handleXemChiTiet(ActionEvent actionEvent) throws SQLException {
        String maPhieu = dsPhieu.getSelectionModel().getSelectedItem();
        if (maPhieu == null) return;

        try {
            ArrayList<CTHoaDonPhong> dsCTP = cthdpDao.getCTHoaDonPhongTheoMaPhieu(maPhieu);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/chi-tiet-phong-trong-phieu-view.fxml"));
            Parent root = loader.load();
            ChiTietPhongTrongPhieuController ctrl = loader.getController();
            ctrl.setChiTietData(maPhieu, dsCTP);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void refreshData() throws Exception {
        laydsKhachHang();
        dsKhachHang.getSelectionModel().clearSelection();
        dsPhieu.getItems().clear();
        dsPhong.getItems().clear();

        hoTen.clear();
        maPhieuThue.clear();
        ngayDen.setValue(null);
        soLuongPhong.clear();
        btnXemChiTiet.setDisable(true);
    }
}