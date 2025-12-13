package com.example.louishotelmanagement.util;

import com.example.louishotelmanagement.dao.*;
import com.example.louishotelmanagement.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class HoaDonTxtGenerator {

    private static final DecimalFormat currencyFormatter = new DecimalFormat("#,### đ");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String LINE = "=========================================================================\n";
    private static final String SUB_LINE = "-------------------------------------------------------------------------\n";

    private HoaDonDAO2 hoaDonDAO;
    private MaGiamGiaDAO maGiamGiaDAO;
    private KhachHangDAO khachHangDAO;
    private CTHoaDonPhongDAO ctHoaDonPhongDAO;
    private PhieuDatPhongDAO phieuDatPhongDAO;

    public HoaDonTxtGenerator() {
        this.hoaDonDAO = new HoaDonDAO2();
        this.maGiamGiaDAO = new MaGiamGiaDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.phieuDatPhongDAO = new PhieuDatPhongDAO();
        this.ctHoaDonPhongDAO = new CTHoaDonPhongDAO();
    }

    public String taoNoiDungHoaDon(HoaDon hoaDon) throws SQLException {
        StringBuilder sb = new StringBuilder();
        final BigDecimal TI_LE_PHAT = new BigDecimal("0.10"); // 10%
        final int SCALE = 2; // Làm tròn 2 chữ số thập phân (ví dụ: 0.00)
        final int SCALE_DISPLAY = 0; // Làm tròn 0 chữ số cho hiển thị
        sb.append(LINE);
        sb.append(String.format("%-40s %33s\n", "LOUIS HOTEL & RESORT", "HÓA ĐƠN THANH TOÁN"));
        sb.append(String.format("%-40s %33s\n", "Địa chỉ: 283/17 Phạm Ngũ Lão, Q.1, TP.HCM", "Số HĐ: " + hoaDon.getMaHD()));
        if (hoaDon.getNgayLap() != null) {
            sb.append(String.format("%-40s %33s\n", "MST: 031xxxxxxx", "Ngày lập: " + hoaDon.getNgayLap().format(dateFormatter)));
        }
        sb.append(LINE);
        sb.append("THÔNG TIN KHÁCH HÀNG\n");

        KhachHang kh = khachHangDAO.layKhachHangTheoMa(hoaDon.getMaKH() );
        String hoTen = (kh != null && kh.getHoTen() != null) ? kh.getHoTen() : "Khách vãng lai";
        String soDT = (kh != null && kh.getSoDT() != null) ? kh.getSoDT() : "N/A";
        String diaChi = (kh != null && kh.getDiaChi() != null) ? kh.getDiaChi() : "N/A";

        sb.append(String.format("  Tên khách hàng: %s\n", hoTen));
        sb.append(String.format("  Số điện thoại:  %s\n", soDT));
        sb.append(String.format("  Địa chỉ:        %s\n", diaChi));

        sb.append(SUB_LINE);

        sb.append("CHI TIẾT THANH TOÁN\n");
        sb.append(String.format("| %-4s | %-30s | %-3s | %-12s | %-13s |\n", "STT", "Tên phòng / Dịch vụ", "SL", "Đơn giá", "Thành tiền"));
        sb.append(SUB_LINE);

        List<HoaDonChiTietItem> chiTietList = hoaDonDAO.layChiTietHoaDon(hoaDon.getMaHD());
        BigDecimal congTienHang = BigDecimal.ZERO;

        if (chiTietList.isEmpty()) {
            sb.append(String.format("| %-71s |\n", "       (Không có chi tiết phòng/dịch vụ cho hóa đơn này)"));
        } else {
            for (HoaDonChiTietItem item : chiTietList) {
                sb.append(String.format("| %-4d | %-30s | %3d | %12s | %13s |\n",
                        item.getSTT(),
                        item.getTenChiTiet().length() > 30 ? item.getTenChiTiet().substring(0, 27) + "..." : item.getTenChiTiet(),
                        item.getSoLuong(),
                        item.getDonGiaFormatted(),
                        item.getThanhTienFormatted()
                ));
                if (item.getThanhTien() != null) {
                    congTienHang = congTienHang.add(item.getThanhTien());
                }
            }
        }
        sb.append(SUB_LINE);
        PhieuDatPhong phieuDatPhong = phieuDatPhongDAO.layPhieuDatPhongTheoMa(ctHoaDonPhongDAO.getCTHoaDonPhongTheoMaHD(hoaDon.getMaHD()).getFirst().getMaPhieu());
        BigDecimal tienCoc = phieuDatPhong.getTienCoc()!=null?phieuDatPhong.getTienCoc():BigDecimal.ZERO;
        BigDecimal tongTienCuoiCung = hoaDon.getTongTien() != null ? hoaDon.getTongTien() : BigDecimal.ZERO;
        BigDecimal giamGia = hoaDon.getTongGiamGia()!=null?hoaDon.getTongGiamGia():BigDecimal.ZERO;
        BigDecimal baseAmount = congTienHang.subtract(giamGia);
        BigDecimal tienPhat = hoaDon.getTienPhat()!=null?hoaDon.getTienPhat():BigDecimal.ZERO;
        BigDecimal thueVAT = baseAmount.multiply(new BigDecimal("0.1")).setScale(SCALE, RoundingMode.HALF_UP);
        if (thueVAT.compareTo(BigDecimal.ZERO) < 0) {
            thueVAT = BigDecimal.ZERO;
        }

        sb.append(String.format("  Phương thức: %-25s %21s %13s\n",
                hoaDon.getPhuongThuc() != null ? hoaDon.getPhuongThuc().toString() : "N/A",
                "Cộng tiền hàng:",
                currencyFormatter.format(congTienHang)));

        sb.append(String.format("  Trạng thái:   %-25s %17s %13s\n",
                hoaDon.getTrangThai() != null ? hoaDon.getTrangThai().toString() : "N/A",
                "Giảm giá:",
                "- " + currencyFormatter.format(giamGia)));

        sb.append(String.format("%59s %13s\n",
                "Tiền cọc:",
                currencyFormatter.format(tienCoc)));

        sb.append(String.format("%59s %13s\n",
                "Tiền phạt:",
                currencyFormatter.format(tienPhat)));

        sb.append(String.format("%59s %13s\n",
                "Thuế VAT:",
                currencyFormatter.format(thueVAT)));

        sb.append(String.format("%59s %13s\n",
                "TỔNG CỘNG:",
                currencyFormatter.format(tongTienCuoiCung)));

        sb.append(LINE);
        sb.append("Cảm ơn quý khách đã sử dụng dịch vụ!\n");
        sb.append(LINE);

        return sb.toString();
    }

    static void main() throws SQLException {
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        HoaDon hd = hoaDonDAO.timHoaDonTheoMa("HD007");
        System.out.println(hd);

    }
}

