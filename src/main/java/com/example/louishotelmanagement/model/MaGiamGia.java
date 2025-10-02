package com.example.louishotelmanagement.model;

import java.time.LocalDate;

public class MaGiamGia {
    private String maGG;
    private String code;
    private double giamGia;
    private String kieuGiamGia;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private double tongTienToiThieu;
    private String moTa;
    private String trangThai;
    private String maNhanVien;


    public MaGiamGia(String maGG, String code, double giamGia, String kieuGiamGia, LocalDate ngayBatDau, LocalDate ngayKetThuc, double tongTienToiThieu, String moTa, String trangThai, String maNhanVien) {
        this.maGG = maGG;
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

    public String getMaGG() {
        return maGG;
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

    public String getKieuGiamGia() {
        return kieuGiamGia;
    }

    public void setKieuGiamGia(String kieuGiamGia) {
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
        return "MaGiamGia{" +
                "maGG='" + maGG + '\'' +
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
