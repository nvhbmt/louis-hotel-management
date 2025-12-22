package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ContentSwitchable;
import com.example.louishotelmanagement.util.ContentSwitcher;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.util.Refreshable;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class NhanPhongController implements Initializable, Refreshable,ContentSwitchable {

    public ComboBox<String> dsKhachHang;
    public ComboBox<String> dsPhong;
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
    public DatePicker ngayDat;

    public PhieuDatPhongDAO phieuDatPhongDAO;
    public CTHoaDonPhongDAO ctHoaDondao;
    public PhongDAO phongDAO;
    public KhachHangDAO khachHangDAO;

    public Boolean check = false;
    private ArrayList<String> dsMaKH;
    ArrayList<CTHoaDonPhong> dsCTHoaDonPhong;
    private PhieuDatPhong pTam;
    private ArrayList<PhieuDatPhong> dspdp;

    private ContentSwitcher switcher;

    public void setContentSwitcher(ContentSwitcher switcher) {
        this.switcher = switcher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phieuDatPhongDAO = new PhieuDatPhongDAO();
        ctHoaDondao = new CTHoaDonPhongDAO();
        khachHangDAO = new KhachHangDAO();
        phongDAO = new PhongDAO();

        try {
            khoiTaoDinhDangNgay();
            laydsKh();

            // Listener khi chọn khách hàng -> load lại dữ liệu và ds phòng
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        loadData();
                        laydsPhongTheoKhachHang();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Listener khi chọn phòng -> cập nhật ngày đặt
            dsPhong.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    capNhatNgayDatTheoPhong(newValue);
                }
            });

            if (!dsKhachHang.getItems().isEmpty()) {
                dsKhachHang.getSelectionModel().selectFirst();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void khoiTaoDinhDangNgay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, formatter);
                    } catch (java.time.format.DateTimeParseException e) {
                        return null;
                    }
                }
                return null;
            }
        };

        ngayDi.setConverter(converter);
        ngayDat.setConverter(converter);
        ngayDen.setConverter(converter);
    }

    private void capNhatNgayDatTheoPhong(String maPhong) {
        try {
            ArrayList<CTHoaDonPhong> dsCTPDP = ctHoaDondao.getDSCTHoaDonPhongTheoMaPhong(maPhong);
            if (!dsCTPDP.isEmpty()) {
                // Lấy phiếu mới nhất của phòng đó
                String maPhieu = dsCTPDP.get(dsCTPDP.size() - 1).getMaPhieu();
                PhieuDatPhong phieu = phieuDatPhongDAO.layPhieuDatPhongTheoMa(maPhieu);

                if (phieu != null) {
                    ngayDat.setValue(phieu.getNgayDat());
                }
            } else {
                ngayDat.setValue(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void laydsKh() throws SQLException {
        dsKhachHang.getItems().clear();
        ArrayList<KhachHang> khachhangs = khachHangDAO.layDSKhachHang();
        dsMaKH = new ArrayList<>();
        for (KhachHang khachHang : khachhangs) {
            dsKhachHang.getItems().add(khachHang.getHoTen());
            dsMaKH.add(khachHang.getMaKH());
        }

        com.example.louishotelmanagement.util.SearchBoxUtil.makeSearchable(dsKhachHang);
    }

    public void loadData() throws SQLException {
        int index = dsKhachHang.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < dsMaKH.size()) {
            KhachHang kh = khachHangDAO.layKhachHangTheoMa(dsMaKH.get(index));
            if (kh != null) {
                soDT.setText(kh.getSoDT());
                CCCD.setText(kh.getCCCD());
            }
        }
    }

    public void laydsPhongTheoKhachHang() throws SQLException {
        dsPhong.getItems().clear();
        dspdp = new ArrayList<>();

        int index = dsKhachHang.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= dsMaKH.size()) return;

        ArrayList<PhieuDatPhong> dsPhieu = phieuDatPhongDAO.layDSPhieuDatPhongTheoKhachHang(dsMaKH.get(index));

        if (dsPhieu != null && !dsPhieu.isEmpty()) {
            for (PhieuDatPhong p : dsPhieu) {
                // ĐÃ CHECK: So sánh Enum chuẩn
                if (p.getTrangThai() != null &&
                        p.getTrangThai() == TrangThaiPhieuDatPhong.DA_DAT) {

                    dspdp.add(p);
                    ArrayList<CTHoaDonPhong> dsCTP = ctHoaDondao.getCTHoaDonPhongTheoMaPhieu(p.getMaPhieu());
                    for (CTHoaDonPhong ctp : dsCTP) {
                        dsPhong.getItems().add(ctp.getMaPhong());
                    }
                }
            }
        }

        if (!dsPhong.getItems().isEmpty()) {
            dsPhong.getSelectionModel().selectFirst();
            capNhatNgayDatTheoPhong(dsPhong.getSelectionModel().getSelectedItem());
        } else {
            ngayDat.setValue(null);
            maPhieu.clear();
            maPhong.clear();
            tang.clear();
            hoTen.clear();
            ngayDen.setValue(null);
            ngayDi.setValue(null);
        }
    }

    public void handleCheck(ActionEvent actionEvent) throws SQLException {
        check = false;

        if (dsPhong.getSelectionModel().getSelectedItem() == null) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phòng để kiểm tra.");
            return;
        }

        String maPhongChon = dsPhong.getSelectionModel().getSelectedItem();
        dsCTHoaDonPhong = ctHoaDondao.getDSCTHoaDonPhongTheoMaPhong(maPhongChon);

        if (ngayDat.getValue() == null) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Ngày đặt trống. Vui lòng kiểm tra lại dữ liệu.");
            return;
        }

        int indexKH = dsKhachHang.getSelectionModel().getSelectedIndex();
        if (indexKH < 0) return;
        String maKHChon = dsMaKH.get(indexKH);

        for (CTHoaDonPhong ctpdp : dsCTHoaDonPhong) {
            PhieuDatPhong phieu = phieuDatPhongDAO.layPhieuDatPhongTheoMa(ctpdp.getMaPhieu());

            // ĐÃ CHECK: So sánh Enum chuẩn
            if (phieu != null &&
                    Objects.equals(phieu.getMaKH(), maKHChon) &&
                    phieu.getNgayDat().isEqual(ngayDat.getValue()) &&
                    phieu.getTrangThai() == TrangThaiPhieuDatPhong.DA_DAT) {

                maPhieu.setText(ctpdp.getMaPhieu());
                maPhong.setText(String.valueOf(ctpdp.getMaPhong()));

                Phong p = phongDAO.layPhongTheoMa(ctpdp.getMaPhong());
                if (p != null) tang.setText(String.valueOf(p.getTang()));

                KhachHang kh = khachHangDAO.layKhachHangTheoMa(maKHChon);
                if (kh != null) hoTen.setText(kh.getHoTen());

                ngayDen.setValue(phieu.getNgayDen());
                ngayDi.setValue(phieu.getNgayDi());
                pTam = phieu;
                check = true;
                break;
            }
        }

        if (!check) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không tìm thấy thông tin phiếu đặt hợp lệ cho phòng này.");
        }
    }

    public void NhanPhong() throws Exception {
        String maPhieuChon = maPhieu.getText();
        if (maPhieuChon.isEmpty() || pTam == null) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng 'Kiểm tra' để lấy thông tin phiếu đặt.");
            return;
        }

        // 1. Lấy tất cả các chi tiết phòng thuộc mã phiếu này
        ArrayList<CTHoaDonPhong> dsChiTietPhieu = ctHoaDondao.getCTHoaDonPhongTheoMaPhieu(maPhieuChon);

        if (dsChiTietPhieu.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Không tìm thấy danh sách phòng trong phiếu này.");
            return;
        }

        // 2. Vòng lặp cập nhật cho TỪNG PHÒNG trong phiếu
        for (CTHoaDonPhong ctp : dsChiTietPhieu) {
            // A. Đổi trạng thái phòng thành 'Đang sử dụng'
            phongDAO.capNhatTrangThaiPhong(ctp.getMaPhong(), TrangThaiPhong.DANG_SU_DUNG.toString());

            // B. Cập nhật ngày đến thực tế cho từng phòng trong hóa đơn
            ctHoaDondao.capNhatNgayDenThucTe(ctp.getMaHD(), ctp.getMaPhong(), LocalDate.now());
        }

        // 3. Cập nhật trạng thái phiếu đặt -> Đang sử dụng (Một lần duy nhất)
        phieuDatPhongDAO.capNhatTrangThaiPhieuDatPhong(maPhieuChon, TrangThaiPhieuDatPhong.DANG_SU_DUNG.toString());

        // 4. Cập nhật trạng thái khách hàng -> Đang lưu trú
        khachHangDAO.capNhatTrangThaiKhachHang(pTam.getMaKH(), TrangThaiKhachHang.DANG_LUU_TRU);

        // 5. Thông báo tổng hợp
        StringBuilder sb = new StringBuilder("Đã nhận phòng thành công cho các phòng: ");
        for (CTHoaDonPhong ctp : dsChiTietPhieu) {
            sb.append(ctp.getMaPhong()).append(" ");
        }
        ThongBaoUtil.hienThiThongBao("Thành công", sb.toString());

        refreshData();
    }

    public void handleNhanPhong(ActionEvent actionEvent) throws Exception {
        if (check) {
            if (ngayDen.getValue() != null && ngayDen.getValue().isAfter(LocalDate.now())) {
                boolean xacNhan = ThongBaoUtil.hienThiXacNhan("Xác nhận", "Hôm nay chưa đến ngày nhận phòng dự kiến (" + ngayDen.getValue() + "). Bạn có chắc muốn nhận phòng sớm?");
                if (xacNhan) {
                    NhanPhong();
                }
            } else {
                NhanPhong();
            }
        } else {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng bấm nút 'Kiểm tra' (Check) để xác thực thông tin trước khi nhận phòng.");
        }
    }

    @Override
    public void refreshData() throws SQLException, Exception {
        laydsKh();

        maPhieu.clear();
        maPhong.clear();
        tang.clear();
        hoTen.clear();
        ngayDen.setValue(null);
        ngayDi.setValue(null);
        dsPhong.getItems().clear();
        ngayDat.setValue(null);
        check = false;

        if (!dsKhachHang.getItems().isEmpty()) {
            dsKhachHang.getSelectionModel().selectFirst();
        }
    }

    public void nhanDuLieuTuPhong(String maPhongCheckIn) {
        try {
            ArrayList<CTHoaDonPhong> listCT = ctHoaDondao.getDSCTHoaDonPhongTheoMaPhong(maPhongCheckIn);
            String maKHTimThay = null;

            for (CTHoaDonPhong ct : listCT) {
                PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(ct.getMaPhieu());

                // ĐÃ CHECK: So sánh Enum chuẩn
                if (pdp != null && pdp.getTrangThai() == TrangThaiPhieuDatPhong.DA_DAT) {
                    maKHTimThay = pdp.getMaKH();
                    break;
                }
            }

            if (maKHTimThay != null) {
                for (int i = 0; i < dsMaKH.size(); i++) {
                    if (dsMaKH.get(i).equals(maKHTimThay)) {
                        dsKhachHang.getSelectionModel().select(i);
                        dsPhong.setValue(maPhongCheckIn);
                        handleCheck(null);
                        break;
                    }
                }
            } else {
                ThongBaoUtil.hienThiThongBao("Thông báo", "Phòng " + maPhongCheckIn + " chưa được đặt trước hoặc đã nhận phòng.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}