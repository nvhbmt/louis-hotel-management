package com.example.louishotelmanagement.model;

import java.time.LocalDate;

public class PhieuDichVu {
    private String maPhieuDV;
    private String maHD;
    private LocalDate ngayLap;
    private String maNV;
    private String ghiChu;
    private NhanVien nhanVien;
    private HoaDon hoaDon;
    public PhieuDichVu() {
        this.maPhieuDV = "";
        this.maHD = "";
        this.ngayLap = null;
        this.maNV = "";
        this.ghiChu = "";
        this.nhanVien = null;
        this.hoaDon = null;
    }

    public PhieuDichVu(String maPhieuDV, String maHD, LocalDate ngayLap, String maNV, String ghiChu, NhanVien nhanVien, HoaDon hoaDon) {
        this.maPhieuDV = maPhieuDV;
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.maNV = maNV;
        this.ghiChu = ghiChu;
        this.nhanVien = nhanVien;
        this.hoaDon = hoaDon;
    }
    public PhieuDichVu( String maPhieuDV) {
        this.maPhieuDV = maPhieuDV;
        this.maHD = "";
        this.ngayLap = null;
        this.maNV = "";
        this.ghiChu = "";
        this.nhanVien = null;
        this.hoaDon = null;
    }
    public String getMaPhieuDV() {
        return maPhieuDV;
    }

    public void setMaPhieuDV(String maPhieuDV) {
        this.maPhieuDV = maPhieuDV;
    }

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

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    @Override
    public String toString() {
        return "PhieuDichVu{" +
                "maPhieuDV='" + maPhieuDV + '\'' +
                ", maHD='" + maHD + '\'' +
                ", ngayLap=" + ngayLap +
                ", maNV='" + maNV + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                ", nhanVien=" + nhanVien +
                ", hoaDon=" + hoaDon +
                '}';
    }
}
