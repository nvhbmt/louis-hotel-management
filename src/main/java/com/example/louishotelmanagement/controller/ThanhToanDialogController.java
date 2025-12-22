package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;
import com.example.louishotelmanagement.util.ThongBaoUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ThanhToanDialogController {

    @FXML private TextField txtMaKhachHang, txtHoTen, txtSoDienThoai, txtEmail;
    @FXML private TextField txtHangkhach, txtSoPhong, txtNgayNhan, txtNgayTra, txtMaKhuyenMai;
    @FXML private RadioButton rbtnTienMat, rbtnTheNganHang;
    @FXML private Button btnThanhToan, btnHuy, btnLamMoi;

    // Label thống kê
    @FXML private Label lblTongTienPhong, lblTongTienDichVu, lblVat, lblTienCoc, lblTamTinh, lblTongThanhToan;
    @FXML private Label lblTongTienPhat, lblPhatNhanTre, lblPhatTraSom, lblPhatTraTre;
    @FXML private Label lblGiamGiaMaGG, lblGiamGiaHangKH;

    // TableView chi tiết
    @FXML private TableView<HoaDonChiTietItem> tblChiTietHoaDon;
    @FXML private TableColumn<HoaDonChiTietItem, Integer> colStt;
    @FXML private TableColumn<HoaDonChiTietItem, String> colTen;
    @FXML private TableColumn<HoaDonChiTietItem, Integer> colSL;
    @FXML private TableColumn<HoaDonChiTietItem, BigDecimal> colDonGia, colThanhTien;

    private final PhieuDatPhongDAO pdpDao = new PhieuDatPhongDAO();
    private final CTHoaDonPhongDAO cthddpDao = new CTHoaDonPhongDAO();
    private CTHoaDonDichVuDAO cthddv;
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    private HoaDon hoaDon;

    // ================= KHAI BÁO CÁC BIẾN LƯU TRỮ (MÀ BẠN ĐÃ HỎI) =================
    private BigDecimal finalTienPhat = BigDecimal.ZERO;
    private BigDecimal finalTongGiamGia = BigDecimal.ZERO;
    private BigDecimal finalVat = BigDecimal.ZERO;

    // Biến phụ trợ hiển thị chi tiết lên UI
    private BigDecimal phatNhanTreChiTiet = BigDecimal.ZERO;
    private BigDecimal phatTraSomChiTiet = BigDecimal.ZERO;
    private BigDecimal phatTraTreChiTiet = BigDecimal.ZERO;
    private BigDecimal giamGiaMaGGChiTiet = BigDecimal.ZERO;
    private BigDecimal giamGiaHangKHChiTiet = BigDecimal.ZERO;

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        rbtnTienMat.setToggleGroup(group);
        rbtnTheNganHang.setToggleGroup(group);
        btnThanhToan.setDisable(true);
        group.selectedToggleProperty().addListener((obs, o, n) -> btnThanhToan.setDisable(n == null));

        cthddv = new CTHoaDonDichVuDAO();
        btnThanhToan.setOnAction(e -> thanhToan());
        btnHuy.setOnAction(e -> dongForm());
        btnLamMoi.setOnAction(e -> lamMoiForm());

        // Ánh xạ cột TableView (Lưu ý: STT nếu getter trong Model là getSTT)
        colStt.setCellValueFactory(new PropertyValueFactory<>("STT"));
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenChiTiet"));
        colSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
        hienThiThongTinHoaDon();
        tinhTongThanhToan();
        hienThiChiTietHoaDon();
    }

    private void tinhTongThanhToan() {
        try {
            BigDecimal tienPhong = BigDecimal.ZERO;
            BigDecimal tienDichVu = BigDecimal.ZERO;
            BigDecimal tienCoc = BigDecimal.ZERO;

            // Reset biến chi tiết trước khi tính
            phatNhanTreChiTiet = BigDecimal.ZERO;
            phatTraSomChiTiet = BigDecimal.ZERO;
            phatTraTreChiTiet = BigDecimal.ZERO;
            giamGiaMaGGChiTiet = BigDecimal.ZERO;
            giamGiaHangKHChiTiet = BigDecimal.ZERO;

            final BigDecimal PHAT_P_TRE = new BigDecimal("0.10");
            final BigDecimal PHAT_P_SOM = new BigDecimal("0.30");
            final BigDecimal PHAT_N_TRE = new BigDecimal("0.50");

            List<CTHoaDonPhong> ds = cthddpDao.getCTHoaDonPhongTheoMaHD(hoaDon.getMaHD());
            KhachHang kh = khachHangDAO.layKhachHangTheoMa(hoaDon.getMaKH());

            for (CTHoaDonPhong ct : ds) {
                PhieuDatPhong pdp = pdpDao.layPhieuDatPhongTheoMa(ct.getMaPhieu());
                if (pdp != null && pdp.getTienCoc() != null) tienCoc = pdp.getTienCoc();

                BigDecimal tienGoc = ct.getThanhTien();
                tienPhong = tienPhong.add(tienGoc);

                // Tính phạt
                if (ct.getNgayDen().isAfter(pdp.getNgayDen())) {
                    long days = ChronoUnit.DAYS.between(pdp.getNgayDen(), ct.getNgayDen());
                    phatNhanTreChiTiet = phatNhanTreChiTiet.add(ct.getGiaPhong().multiply(PHAT_N_TRE).multiply(BigDecimal.valueOf(days)));
                }
                if (ct.getNgayDi().isAfter(LocalDate.now())) {
                    long days = ChronoUnit.DAYS.between(LocalDate.now(), ct.getNgayDi());
                    phatTraSomChiTiet = phatTraSomChiTiet.add(tienGoc.multiply(PHAT_P_SOM).multiply(BigDecimal.valueOf(days)));
                } else if (ct.getNgayDi().isBefore(LocalDate.now())) {
                    long days = ChronoUnit.DAYS.between(ct.getNgayDi(), LocalDate.now());
                    phatTraTreChiTiet = phatTraTreChiTiet.add(tienGoc.multiply(PHAT_P_TRE).multiply(BigDecimal.valueOf(days)));
                }
            }

            finalTienPhat = phatNhanTreChiTiet.add(phatTraSomChiTiet).add(phatTraTreChiTiet).setScale(0, RoundingMode.HALF_UP);
            tienDichVu = cthddv.tinhTongTienDichVu(hoaDon.getMaHD());

            // 1. Giảm giá mã Voucher
            if (hoaDon.getMaKM() != null) {
                KhuyenMai km = khuyenMaiDAO.layKhuyenMaiTheoMa(hoaDon.getMaKM());
                if (km != null) {
                    if (km.getKieuGiamGia() == KieuGiamGia.PERCENT)
                        giamGiaMaGGChiTiet = tienPhong.multiply(BigDecimal.valueOf(km.getGiamGia() / 100.0));
                    else
                        giamGiaMaGGChiTiet = BigDecimal.valueOf(km.getGiamGia());
                }
            }

            // 2. Logic giảm giá theo HangKhach của bạn
            if (kh != null && kh.getHangKhach() != null) {
                BigDecimal phanTram = BigDecimal.ZERO;
                switch (kh.getHangKhach()) {
                    case KHACH_QUEN: phanTram = new BigDecimal("0.05"); break; // 5%
                    case KHACH_VIP: phanTram = new BigDecimal("0.10"); break; // 10%
                    case KHACH_DOANH_NGHIEP: phanTram = new BigDecimal("0.15"); break; // 15%
                    default: phanTram = BigDecimal.ZERO; break; // Khách thường 0%
                }
                giamGiaHangKHChiTiet = tienPhong.multiply(phanTram);
            }

            finalTongGiamGia = giamGiaMaGGChiTiet.add(giamGiaHangKHChiTiet).setScale(0, RoundingMode.HALF_UP);
            BigDecimal tamTinh = tienPhong.add(finalTienPhat).add(tienDichVu).subtract(finalTongGiamGia);
            finalVat = tamTinh.multiply(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP);
            BigDecimal tongThanhToan = tamTinh.add(finalVat).subtract(tienCoc);

            // Gán UI
            lblTongTienPhong.setText(String.format("%,.0f ₫", tienPhong));
            lblTongTienDichVu.setText(String.format("%,.0f ₫", tienDichVu));
            lblTienCoc.setText(String.format("%,.0f ₫", tienCoc));
            lblTamTinh.setText(String.format("%,.0f ₫", tamTinh));
            lblVat.setText(String.format("%,.0f ₫", finalVat));
            lblTongThanhToan.setText(String.format("%,.0f ₫", tongThanhToan));
            lblTongTienPhat.setText(String.format("%,.0f ₫", finalTienPhat));
            lblPhatNhanTre.setText(String.format("%,.0f ₫", phatNhanTreChiTiet));
            lblPhatTraSom.setText(String.format("%,.0f ₫", phatTraSomChiTiet));
            lblPhatTraTre.setText(String.format("%,.0f ₫", phatTraTreChiTiet));
            lblGiamGiaMaGG.setText(String.format("%,.0f ₫", giamGiaMaGGChiTiet));
            lblGiamGiaHangKH.setText(String.format("%,.0f ₫", giamGiaHangKHChiTiet));

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void hienThiThongTinHoaDon() {
        try {
            KhachHang kh = khachHangDAO.layKhachHangTheoMa(hoaDon.getMaKH());
            if (kh != null) {
                txtMaKhachHang.setText(kh.getMaKH());
                txtHoTen.setText(kh.getHoTen());
                txtSoDienThoai.setText(kh.getSoDT());
                txtEmail.setText(kh.getEmail());
                txtHangkhach.setText(kh.getHangKhach().toString());
            }
            txtSoPhong.setText(String.valueOf(cthddpDao.getCTHoaDonPhongTheoMaHD(hoaDon.getMaHD()).size()));
            txtNgayNhan.setText(hoaDon.getNgayLap().toString());
            txtNgayTra.setText(LocalDate.now().toString());
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void hienThiChiTietHoaDon() {
        try {
            List<HoaDonChiTietItem> list = hoaDonDAO.layChiTietHoaDon(hoaDon.getMaHD());
            tblChiTietHoaDon.getItems().setAll(list);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void xuLyThanhToan(PhuongThucThanhToan phuongThuc) {
        try {
            // 1. Gán thông tin trạng thái và hình thức
            hoaDon.setPhuongThuc(phuongThuc);
            hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
            hoaDon.setNgayCheckOut(LocalDate.now());

            // 2. Lấy tổng tiền cuối cùng (loại bỏ ký tự tiền tệ)
            String cleanedTongTien = lblTongThanhToan.getText().replaceAll("[^0-9]", "");
            hoaDon.setTongTien(new BigDecimal(cleanedTongTien));

            // 3. QUAN TRỌNG: Gán các giá trị phạt và giảm giá đã tính được vào Object
            // Các biến này đã được bạn tính toán trong hàm tinhTongThanhToan()
            hoaDon.setPhatNhanPhongTre(phatNhanTreChiTiet);
            hoaDon.setPhatTraPhongSom(phatTraSomChiTiet);
            hoaDon.setPhatTraPhongTre(phatTraTreChiTiet);

            hoaDon.setGiamGiaTheoMa(giamGiaMaGGChiTiet);
            hoaDon.setGiamGiaTheoHangKH(giamGiaHangKHChiTiet);

            hoaDon.setTongVAT(finalVat);
            // Lưu ý: Đảm bảo hoaDon.setTienPhat(finalTienPhat) nếu DB có cột tổng phạt

            // 4. Gọi DAO để thực thi Stored Procedure
            boolean success = hoaDonDAO.capNhatHoaDon(hoaDon);

            if (success) {
                ThongBaoUtil.hienThiThongBao("Thành công", "Hóa đơn " + hoaDon.getMaHD() + " đã được thanh toán và lưu trữ chi tiết.");
                dongForm();
            } else {
                ThongBaoUtil.hienThiLoi("Thất bại", "Không thể cập nhật hóa đơn vào database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ThongBaoUtil.hienThiLoi("Lỗi", "Lỗi xử lý dữ liệu: " + e.getMessage());
        }
    }

    private void thanhToan() {
        if (rbtnTienMat.isSelected()) xuLyThanhToan(PhuongThucThanhToan.TIEN_MAT);
        else if (rbtnTheNganHang.isSelected()) moManHinhQRCode();
    }

    private void moManHinhQRCode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/ma-qr-view.fxml"));
            Parent p = loader.load();
            QRController qr = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(p));
            stage.showAndWait();
            if (qr.isTransactionConfirmed()) xuLyThanhToan(PhuongThucThanhToan.CHUYEN_KHOAN);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML private void handleChonGiamGia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/louishotelmanagement/fxml/khuyen-mai-screen.fxml"));
            Parent p = loader.load();
            ChonKhuyenMaiController ctrl = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(p));
            stage.showAndWait();
            KhuyenMai selected = ctrl.getKhuyenMaiDuocChon();
            if (selected != null) {
                txtMaKhuyenMai.setText(selected.getMaKM());
                hoaDon.setMaKM(selected.getMaKM());
                tinhTongThanhToan();
                hienThiChiTietHoaDon();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void dongForm() { ((Stage) btnHuy.getScene().getWindow()).close(); }
    private void lamMoiForm() {
        txtMaKhuyenMai.clear();
        rbtnTienMat.setSelected(false);
        rbtnTheNganHang.setSelected(false);
        hoaDon.setMaKM(null);
        tinhTongThanhToan();
        hienThiChiTietHoaDon();
    }
}