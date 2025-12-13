package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class HoaDonChiTietItem {
    private int STT;
    private String tenChiTiet;
    private int soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;

    private final NumberFormat currency =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    // Constructor
    public HoaDonChiTietItem(int STT, String tenChiTiet, int soLuong, BigDecimal donGia, BigDecimal thanhTien) {
        this.STT = STT;
        this.tenChiTiet = tenChiTiet;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

    // ====== GETTERS ======

    public int getSTT() {
        return STT;
    }

    public String getTenChiTiet() {
        return tenChiTiet;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    // ====== Xác định loại dựa vào tên ======
    public boolean isPhong() {
        if (tenChiTiet == null) return false;
        return tenChiTiet.trim().toLowerCase().startsWith("phòng");
    }

    // ====== Hậu tố số lượng ======
    public String getSoLuongHienThi() {
        if (isPhong()) {
            return soLuong + " đêm";    // hoặc "ngày"
        } else {
            return soLuong + " lần";    // dịch vụ
        }
    }

    // ====== Format tiền ======
    public String getDonGiaFormatted() {
        return donGia == null ? "0" : currency.format(donGia);
    }

    public String getThanhTienFormatted() {
        return thanhTien == null ? "0" : currency.format(thanhTien);
    }
}
