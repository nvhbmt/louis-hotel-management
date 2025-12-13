package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CTHoaDonPhong {
    private String maHD;
    private String maPhieu;
    private String maPhong;
    private LocalDate ngayDen;
    private LocalDate ngayDi;
    private BigDecimal giaPhong;
    private BigDecimal thanhTien;

    public CTHoaDonPhong() {
    }

    public CTHoaDonPhong(String maHD, String maPhieu, String maPhong,
                         LocalDate ngayDen, LocalDate ngayDi, BigDecimal giaPhong) {
        this.maHD = maHD;
        this.maPhieu = maPhieu;
        this.maPhong = maPhong;
        this.ngayDen = ngayDen;
        this.ngayDi = ngayDi;
        this.giaPhong = giaPhong;
        this.thanhTien = tinhThanhTien();
    }

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

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }

    /**
     * Tính tiền tự động theo số ngày ở và giá phòng.
     */
    public BigDecimal tinhThanhTien() {
        if (ngayDen != null && giaPhong != null && ngayDi!=null) {
            long soNgay = java.time.temporal.ChronoUnit.DAYS.between(ngayDen, ngayDi);
            if (soNgay <= 0) soNgay = 1;
            return giaPhong.multiply(BigDecimal.valueOf(soNgay));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return String.format("CTHoaDonPhong{maHD='%s', maPhieu='%s', maPhong='%s', ngayDen=%s, ngayDi=%s, giaPhong=%,.0f, thanhTien=%,.0f}",
                maHD, maPhieu, maPhong,
                ngayDen, ngayDi,
                giaPhong != null ? giaPhong.doubleValue() : 0,
                thanhTien != null ? thanhTien.doubleValue() : 0);
    }
}