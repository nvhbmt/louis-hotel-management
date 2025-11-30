package com.example.louishotelmanagement.util;

import com.example.louishotelmanagement.dao.HoaDonDAO2;
import com.example.louishotelmanagement.dao.MaGiamGiaDAO;
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

    public HoaDonTxtGenerator() {
        this.hoaDonDAO = new HoaDonDAO2();
        this.maGiamGiaDAO = new MaGiamGiaDAO();
    }

    public String taoNoiDungHoaDon(HoaDon hoaDon) throws SQLException {
        StringBuilder sb = new StringBuilder();

        sb.append(LINE);
        sb.append(String.format("%-40s %33s\n", "LOUIS HOTEL & RESORT", "HÓA ĐƠN THANH TOÁN"));
        sb.append(String.format("%-40s %33s\n", "Địa chỉ: 283/17 Phạm Ngũ Lão, Q.1, TP.HCM", "Số HĐ: " + hoaDon.getMaHD()));
        if (hoaDon.getNgayLap() != null) {
            sb.append(String.format("%-40s %33s\n", "MST: 031xxxxxxx", "Ngày lập: " + hoaDon.getNgayLap().format(dateFormatter)));
        }
        sb.append(LINE);
        sb.append("THÔNG TIN KHÁCH HÀNG\n");

        KhachHang kh = hoaDon.getKhachHang();
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

        BigDecimal tongTienCuoiCung = hoaDon.getTongTien() != null ? hoaDon.getTongTien() : BigDecimal.ZERO;
        BigDecimal giamGia = BigDecimal.ZERO;
        String maGGCuaHoaDon = hoaDon.getMaGG();

        if (maGGCuaHoaDon != null && !maGGCuaHoaDon.isEmpty()) {
            MaGiamGia mgm = maGiamGiaDAO.layMaGiamGiaThepMa(maGGCuaHoaDon);

            if (mgm != null) {
                if (mgm.getKieuGiamGia() == KieuGiamGia.AMOUNT) {
                    giamGia = BigDecimal.valueOf(mgm.getGiamGia());
                } else if (mgm.getKieuGiamGia() == KieuGiamGia.PERCENT) {
                    BigDecimal percentValue = BigDecimal.valueOf(mgm.getGiamGia()).divide(new BigDecimal("100"));
                    giamGia = congTienHang.multiply(percentValue).setScale(2, RoundingMode.HALF_UP);
                }
            } else {
                System.err.println("Không tìm thấy chi tiết cho maGG: " + maGGCuaHoaDon);
            }
        }

        BigDecimal thueVAT = tongTienCuoiCung.subtract(congTienHang.subtract(giamGia));
        if (thueVAT.compareTo(BigDecimal.ZERO) < 0) {
            thueVAT = BigDecimal.ZERO;
        }

        sb.append(String.format("  Phương thức: %-25s %21s %13s\n",
                hoaDon.getPhuongThuc() != null ? hoaDon.getPhuongThuc().toString() : "N/A",
                "Cộng tiền hàng:",
                currencyFormatter.format(congTienHang)));

        sb.append(String.format("  Trạng thái:   %-25s %21s %13s\n",
                hoaDon.getTrangThai() != null ? hoaDon.getTrangThai().toString() : "N/A",
                "Giảm giá:",
                "- " + currencyFormatter.format(giamGia)));

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
}

