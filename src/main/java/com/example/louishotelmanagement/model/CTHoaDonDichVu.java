package com.example.louishotelmanagement.model;

import java.math.BigDecimal;

public class CTHoaDonDichVu {
    private String maHD;
    private String maPhieuDV;
    private String maDV;
    private int soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;

    public CTHoaDonDichVu() {
    }

    public CTHoaDonDichVu(String maHD, String maPhieuDV, String maDV, int soLuong, BigDecimal donGia) {
        this.maHD = maHD;
        this.maPhieuDV = maPhieuDV;
        this.maDV = maDV;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = tinhThanhTien();
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaPhieuDV() {
        return maPhieuDV;
    }

    public void setMaPhieuDV(String maPhieuDV) {
        this.maPhieuDV = maPhieuDV;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public BigDecimal getThanhTien() {
        this.thanhTien = tinhThanhTien();
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }

    /**
     * Tính tiền = số lượng * đơn giá.
     */
    public BigDecimal tinhThanhTien() {
        if (donGia != null) {
            return donGia.multiply(BigDecimal.valueOf(soLuong));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return String.format(
                "CTHoaDonDichVu{maHD='%s', maPhieuDV='%s', maDV='%s', soLuong=%d, donGia=%,.0f, thanhTien=%,.0f}",
                maHD, maPhieuDV, maDV,
                soLuong,
                donGia != null ? donGia.doubleValue() : 0,
                thanhTien != null ? thanhTien.doubleValue() : 0
        );
    }
}
