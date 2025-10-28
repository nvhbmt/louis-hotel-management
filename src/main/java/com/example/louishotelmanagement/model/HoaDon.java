package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HoaDon {
    private String maHD;
    private LocalDate ngayLap;
    private PhuongThucThanhToan phuongThuc;
    private TrangThaiHoaDon trangThai;
    private BigDecimal tongTien;
    private String maKH;
    private String maNV;
    private String maGG; // Có thể null
    private KhachHang khachHang;

    private String soPhong;
    private LocalDate ngayCheckOut;

    // Constructor không tham số
    public HoaDon() {
    }

    public HoaDon(String maHD) {
        this.maHD = maHD;
    }

    // Constructor đầy đủ tham số
    public HoaDon(String maHD, LocalDate ngayLap, PhuongThucThanhToan phuongThuc,TrangThaiHoaDon trangThai,
                  BigDecimal tongTien, String maKH, String maNV, String maGG) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.phuongThuc = phuongThuc;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.maKH = maKH;
        this.maNV = maNV;
        this.maGG = maGG;
        this.khachHang = new KhachHang();
    }

    // Getters & Setters
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

    public PhuongThucThanhToan getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(PhuongThucThanhToan phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
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

    public TrangThaiHoaDon getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiHoaDon trangThai) {
        this.trangThai = trangThai;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public LocalDate getNgayCheckOut() {
        return ngayCheckOut;
    }

    public void setNgayCheckOut(LocalDate ngayCheckOut) {
        this.ngayCheckOut = ngayCheckOut;
    }

    @Override
    public String toString() {
        return String.format(
                "HoaDon{maHD='%s', ngayLap=%s, phuongThuc='%s', tongTien=%,.0f, maKH='%s', maNV='%s', maGG='%s'}",
                maHD,
                ngayLap,
                phuongThuc,
                tongTien != null ? tongTien.doubleValue() : 0,
                maKH,
                maNV,
                maGG
        );
    }
}
