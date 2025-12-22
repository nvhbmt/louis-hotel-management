package com.example.louishotelmanagement.model;

import java.time.LocalDate;

public class KhuyenMai {
    private String maKM;
    private String code;
    private double giamGia;
    private KieuGiamGia kieuGiamGia;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private double tongTienToiThieu;
    private String moTa;
    private String trangThai;
    private String maNhanVien;


    // Constructor mặc định
    public KhuyenMai() {
    }

    public KhuyenMai(String maKM, String code, double giamGia, KieuGiamGia kieuGiamGia, LocalDate ngayBatDau, LocalDate ngayKetThuc, double tongTienToiThieu, String moTa, String trangThai, String maNhanVien) {
        this.maKM = maKM;
        this.code = code;
        this.giamGia = giamGia;
        this.kieuGiamGia = kieuGiamGia;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.tongTienToiThieu = tongTienToiThieu;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.maNhanVien = maNhanVien;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
    }

    public KieuGiamGia getKieuGiamGia() {
        return kieuGiamGia;
    }

    public void setKieuGiamGia(KieuGiamGia kieuGiamGia) {
        this.kieuGiamGia = kieuGiamGia;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public double getTongTienToiThieu() {
        return tongTienToiThieu;
    }

    public void setTongTienToiThieu(double tongTienToiThieu) {
        this.tongTienToiThieu = tongTienToiThieu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    @Override
    public String toString() {
        return "KhuyenMai{" +
                "maKM='" + maKM + '\'' +
                ", code='" + code + '\'' +
                ", giamGia=" + giamGia +
                ", kieuGiamGia='" + kieuGiamGia + '\'' +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", tongTienToiThieu=" + tongTienToiThieu +
                ", moTa='" + moTa + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", maNhanVien=" + maNhanVien +
                '}';
    }
}