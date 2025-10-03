package com.example.louishotelmanagement.model;

import java.time.LocalDate;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String soDT;
    private String email;
    private String diaChi;
    private LocalDate ngaySinh;
    private String ghiChu;
    private String CCCD;

    public KhachHang() {
        this.maKH = "";
        this.hoTen = "";
        this.soDT = "";
        this.email = "";
        this.diaChi = "";
        this.ngaySinh = null;
        this.ghiChu = "";
        this.CCCD = "";
    }

    public KhachHang(String maKH, String hoTen, String soDT, String email, String diaChi, LocalDate ngaySinh, String ghiChu, String CCCD) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soDT = soDT;
        this.email = email;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.ghiChu = ghiChu;
        this.CCCD = CCCD;
    }

    public KhachHang(String maKH) {
        this.maKH = maKH;
        this.hoTen = "";
        this.soDT = "";
        this.email = "";
        this.diaChi = "";
        this.ngaySinh = null;
        this.ghiChu = "";
        this.CCCD = "";
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", soDT='" + soDT + '\'' +
                ", email='" + email + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", ghiChu='" + ghiChu + '\'' +
                ", CCCD='" + CCCD + '\'' +
                '}';
    }
}