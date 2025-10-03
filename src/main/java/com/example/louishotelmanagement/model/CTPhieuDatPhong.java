package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CTPhieuDatPhong {
    private String maPhieu;
    private String maPhong;
    private LocalDate ngayDen;
    private LocalDate ngayDi;
    private BigDecimal giaPhong;
    private PhieuDatPhong phieuDatPhong;
    private Phong phong;

    public CTPhieuDatPhong() {
        this.maPhieu = "";
        this.maPhong = "";
        this.ngayDen = null;
        this.ngayDi = null;
        this.giaPhong = BigDecimal.ZERO;
        this.phieuDatPhong = null;
        this.phong = null;
    }

    public CTPhieuDatPhong(String maPhieu, String maPhong, LocalDate ngayDen, LocalDate ngayDi, BigDecimal giaPhong) {
        this.maPhieu = maPhieu;
        this.maPhong = maPhong;
        this.ngayDen = ngayDen;
        this.ngayDi = ngayDi;
        this.giaPhong = giaPhong;
        this.phieuDatPhong = null;
        this.phong = null;
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

    public PhieuDatPhong getPhieuDatPhong() {
        return phieuDatPhong;
    }

    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        this.phieuDatPhong = phieuDatPhong;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    @Override
    public String toString() {
        return "CTPhieuDatPhong{" +
                "maPhieu='" + maPhieu + '\'' +
                ", maPhong='" + maPhong + '\'' +
                ", ngayDen=" + ngayDen +
                ", ngayDi=" + ngayDi +
                ", giaPhong=" + giaPhong +
                ", phieuDatPhong=" + (phieuDatPhong != null ? phieuDatPhong.getMaPhieu() : "null") +
                ", phong=" + (phong != null ? phong.getMaPhong() : "null") +
                '}';
    }
}
