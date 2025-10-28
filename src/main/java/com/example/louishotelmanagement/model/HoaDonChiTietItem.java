package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Model này dùng để chứa dữ liệu trả về từ SP sp_LayChiTietHoaDonTheoMaHD
 * Dùng cho TableView trong màn hình Chi Tiết Hóa Đơn
 */
public class HoaDonChiTietItem {
    private int STT;
    private String tenChiTiet;
    private int soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;

    // Dùng để định dạng tiền tệ
    private static final DecimalFormat formatter = new DecimalFormat("#,### đ");

    // Constructor
    public HoaDonChiTietItem(int STT, String tenChiTiet, int soLuong, BigDecimal donGia, BigDecimal thanhTien) {
        this.STT = STT;
        this.tenChiTiet = tenChiTiet;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

    // Getters
    public int getSTT() { return STT; }
    public String getTenChiTiet() { return tenChiTiet; }
    public int getSoLuong() { return soLuong; }
    public BigDecimal getDonGia() { return donGia; }
    public BigDecimal getThanhTien() { return thanhTien; }

    // Setters (Nếu cần)
    // ...

    // Getters đặc biệt cho TableView (để hiển thị tiền đã định dạng)
    public String getDonGiaFormatted() {
        return (donGia != null) ? formatter.format(donGia) : "0 đ";
    }

    public String getThanhTienFormatted() {
        return (thanhTien != null) ? formatter.format(thanhTien) : "0 đ";
    }
}