package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CTHoaDonPhong {

    private String maHD;
    private String maPhieu;
    private String maPhong;

    private LocalDate ngayDen;
    private LocalDate ngayDi;

    private BigDecimal giaPhong;
    private BigDecimal thanhTien;

    // ===== XÓA MỀM =====
    private boolean daHuy = false;
    private LocalDate ngayHuy;

    public CTHoaDonPhong(String maHD, String maPhieu, String maPhong,
                         LocalDate ngayDen, LocalDate ngayDi,
                         BigDecimal giaPhong) {
        this.maHD = maHD;
        this.maPhieu = maPhieu;
        this.maPhong = maPhong;
        this.ngayDen = ngayDen;
        this.ngayDi = ngayDi;
        this.giaPhong = giaPhong;

        // mặc định
        this.daHuy = false;
        this.ngayHuy = null;
        this.thanhTien = tinhThanhTien();
    }

    // ===== GET / SET =====
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public LocalDate getNgayDen() {
        return ngayDen;
    }

    public void setNgayDen(LocalDate ngayDen) {
        this.ngayDen = ngayDen;
    }

    public LocalDate getNgayDi() {
        return ngayDi;
    }

    public void setNgayDi(LocalDate ngayDi) {
        this.ngayDi = ngayDi;
    }

    public BigDecimal getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(BigDecimal giaPhong) {
        this.giaPhong = giaPhong;
    }

    public BigDecimal getThanhTien() {
        this.thanhTien = tinhThanhTien();
        return thanhTien;
    }

    public boolean isDaHuy() {
        return daHuy;
    }

    public LocalDate getNgayHuy() {
        return ngayHuy;
    }


    public void setDaHuy(boolean daHuy) {
        this.daHuy = daHuy;
    }


    public void setNgayHuy(LocalDate ngayHuy) {
        this.ngayHuy = ngayHuy;
    }


    // ===== NGHIỆP VỤ =====

    /**
     * Hủy (xóa mềm) phòng khỏi phiếu
     */
    public void huyPhong() {
        this.daHuy = true;
        this.ngayHuy = LocalDate.now();
    }

    /**
     * Tính tiền theo số đêm
     */
    public BigDecimal tinhThanhTien() {
        if (ngayDen == null || ngayDi == null || giaPhong == null) {
            return BigDecimal.ZERO;
        }

        long soDem = ChronoUnit.DAYS.between(ngayDen, ngayDi);
        if (soDem <= 0) soDem = 1;

        return giaPhong.multiply(BigDecimal.valueOf(soDem));
    }

    /**
     * Tiện cho UI
     */
    public boolean isConHieuLuc() {
        return !daHuy;
    }
}
