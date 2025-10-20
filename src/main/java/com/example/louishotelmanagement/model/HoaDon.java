package com.example.louishotelmanagement.model;

import java.time.LocalDate;

public class HoaDon {
    private String maHD;
    private LocalDate ngayLap;
    private String phuongThuc;
    private LocalDate ngayDat;
    private LocalDate ngayDen;
    private LocalDate ngayDi;
    private String maKH;
    private String maNV;
    private String maGG;      // Có thể là null
    private String maPhieu;   // Có thể là null
    private String maPhieuDV; // Có thể là null

    // Constructor không tham số
    public HoaDon() {
    }

    // Constructor đầy đủ tham số
    public HoaDon(String maHD, LocalDate ngayLap, String phuongThuc, LocalDate ngayDat,
                  LocalDate ngayDen, LocalDate ngayDi, String maKH, String maNV,
                  String maGG, String maPhieu, String maPhieuDV) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.phuongThuc = phuongThuc;
        this.ngayDat = ngayDat;
        this.ngayDen = ngayDen;
        this.ngayDi = ngayDi;
        this.maKH = maKH;
        this.maNV = maNV;
        this.maGG = maGG;
        this.maPhieu = maPhieu;
        this.maPhieuDV = maPhieuDV;
    }

    // Getters and Setters

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
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

    public String getMaGG() {
        return maGG;
    }

    public void setMaGG(String maGG) {
        this.maGG = maGG;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaPhieuDV() {
        return maPhieuDV;
    }

    public void setMaPhieuDV(String maPhieuDV) {
        this.maPhieuDV = maPhieuDV;
    }

    // Ghi đè phương thức toString() để hiển thị thông tin đối tượng dễ dàng
    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", ngayLap=" + ngayLap +
                ", phuongThuc='" + phuongThuc + '\'' +
                ", ngayDat=" + ngayDat +
                ", ngayDen=" + ngayDen +
                ", ngayDi=" + ngayDi +
                ", maKH='" + maKH + '\'' +
                ", maNV='" + maNV + '\'' +
                ", maGG='" + maGG + '\'' +
                ", maPhieu='" + maPhieu + '\'' +
                ", maPhieuDV='" + maPhieuDV + '\'' +
                '}';
    }
}
