package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import com.example.louishotelmanagement.util.Refreshable;
import com.example.louishotelmanagement.util.ContentSwitcher;
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
import java.util.List;
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

    public KhachHangDAO khDao;
    public PhongDAO phDao;
    public CTHoaDonPhongDAO cthdpDao;
    public PhieuDatPhongDAO phieuDatPhongDAO;
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

            // Listener cho khách hàng -> load phiếu
            dsKhachHang.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    try {
                        laydsPhieuTheoKhachHang();
                    } catch (SQLException e) { e.printStackTrace(); }
                }
            });

            // Listener cho phiếu -> load các phòng trong phiếu đó
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

    /**
     * PHƯƠNG THỨC QUAN TRỌNG: Nhận mã phòng từ màn hình Quản lý phòng
     * Tự động chọn Khách hàng -> Chọn Phiếu -> Load thông tin
     */
    public void truyenDuLieuTuPhong(String maPhongTruyenVao) {
        try {
            // 1. Tìm CTHDP đang sử dụng của phòng này
            ArrayList<CTHoaDonPhong> listCT = cthdpDao.getDSCTHoaDonPhongTheoMaPhong(maPhongTruyenVao);
            CTHoaDonPhong activeCT = null;

            for (CTHoaDonPhong ct : listCT) {
                PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(ct.getMaPhieu());
                if (pdp != null && pdp.getTrangThai().equals(TrangThaiPhieuDatPhong.DANG_SU_DUNG)) {
                    activeCT = ct;
                    break;
                }
            }

            if (activeCT != null) {
                PhieuDatPhong pdp = phieuDatPhongDAO.layPhieuDatPhongTheoMa(activeCT.getMaPhieu());
                KhachHang kh = khDao.layKhachHangTheoMa(pdp.getMaKH());

                // 2. Set ComboBox Khách hàng
                dsKhachHang.getSelectionModel().select(kh.getHoTen());

                // 3. Set ComboBox Phiếu
                laydsPhieuTheoKhachHang(); // Load lại list phiếu của KH này
                dsPhieu.getSelectionModel().select(pdp.getMaPhieu());

                // 4. Set ComboBox Phòng
                laydsPhongTheoPhieu(); // Load lại list phòng của phiếu này
                dsPhong.getSelectionModel().select(maPhongTruyenVao);

                // 5. Tự động nhấn nút Kiểm tra để hiện thông tin xuống dưới
                handleCheck(null);
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
        if (!dsKhachHang.getItems().isEmpty()) dsKhachHang.getSelectionModel().selectFirst();
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
                hoTen.setText(kh.getHoTen());
                maPhieuThue.setText(pdp.getMaPhieu());

                ArrayList<CTHoaDonPhong> listCT = cthdpDao.getCTHoaDonPhongTheoMaPhieu(pdp.getMaPhieu());
                if (!listCT.isEmpty()) {
                    ngayDen.setValue(listCT.get(0).getNgayDen());
                    soLuongPhong.setText(String.valueOf(listCT.size()));
                }
                btnXemChiTiet.setDisable(false);
            }
        } else {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng chọn phiếu để kiểm tra");
            btnXemChiTiet.setDisable(true);
        }
    }

    @FXML
    public void handleTraPhong(ActionEvent actionEvent) throws Exception {
        String maPhieu = maPhieuThue.getText();
        if (maPhieu == null || maPhieu.isEmpty()) {
            ThongBaoUtil.hienThiLoi("Lỗi", "Vui lòng Kiểm tra phiếu trước.");
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
            }
        }
    }

    private void moDialogThanhToan(HoaDon hoaDon) {
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

            // Sau khi đóng dialog thanh toán, refresh lại để xem trạng thái mới
            handleCheck(null);
        } catch (Exception e) { e.printStackTrace(); }
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
        hoTen.clear();
        maPhieuThue.clear();
        ngayDen.setValue(null);
        soLuongPhong.clear();
        btnXemChiTiet.setDisable(true);
    }
}