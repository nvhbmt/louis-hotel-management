package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PhieuDatPhong {
    private String maPhieu;
    private LocalDate ngayDat;
    private LocalDate ngayDen;
    private LocalDate ngayDi;
    private TrangThaiPhieuDatPhong trangThai;
    private String ghiChu;
    private String maKH;
    private String maNV;
    private BigDecimal tienCoc;

    public PhieuDatPhong() {
        this.maPhieu = "";
        this.ngayDat = null;
        this.ngayDen = null;
        this.ngayDi = null;
        this.ghiChu = "";
        this.trangThai = null;
        this.maKH = "";
        this.maNV = "";
        this.tienCoc = null;
    }

    public PhieuDatPhong(String maPhieu, LocalDate ngayDat, LocalDate ngayDen, LocalDate ngayDi, TrangThaiPhieuDatPhong trangThai, String ghiChu, String maKH, String maNV,BigDecimal tienCoc) {
        this.maPhieu = maPhieu;
        this.ngayDat = ngayDat;
        this.ngayDen = ngayDen;
        this.ngayDi = ngayDi;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
        this.maKH = maKH;
        this.maNV = maNV;
        this.tienCoc = tienCoc;
    }

    public PhieuDatPhong(String maPhieu) {
        this.maPhieu = maPhieu;
        this.ngayDat = null;
        this.ngayDen = null;
        this.ngayDi = null;
        this.trangThai = null;
        this.ghiChu = "";
        this.maKH = "";
        this.maNV = "";
        this.tienCoc = null;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public LocalDate getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDate ngayDat) {
        this.ngayDat = ngayDat;
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

    public TrangThaiPhieuDatPhong getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiPhieuDatPhong trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    public BigDecimal getTienCoc() {
        return tienCoc;
    }
    public void setTienCoc(BigDecimal tienCoc) {
        this.tienCoc = tienCoc;
    }
    @Override
    public String toString() {
        return "PhieuDatPhong{" +
                "maPhieu='" + maPhieu + '\'' +
                ", ngayDat=" + ngayDat +
                ", ngayDen=" + ngayDen +
                ", ngayDi=" + ngayDi +
                ", trangThai='" + trangThai + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                ", maKH='" + maKH + '\'' +
                ", maNV='" + maNV + '\'' +
                '}';
    }
}
