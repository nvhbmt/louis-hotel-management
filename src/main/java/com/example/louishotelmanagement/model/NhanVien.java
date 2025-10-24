package com.example.louishotelmanagement.model;

import java.time.LocalDate;

public class NhanVien {
    private String maNV;
    private String hoTen;
    private String soDT;
    private String diaChi;
    private String chucVu;
    private LocalDate ngaySinh;

    public NhanVien(String maNV) {
        this.maNV = maNV;
    }

    public NhanVien(String maNV, String hoTen, String soDT, String diaChi, String chucVu, LocalDate ngaySinh) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.soDT = soDT;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
        this.ngaySinh = ngaySinh;
    }

    public NhanVien() {
        this.maNV = "";
        this.hoTen = "";
        this.soDT = "";
        this.diaChi = "";
        this.chucVu = "";
        this.ngaySinh = null;
    }

    public String getMaNV() {
        return maNV;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    @Override
    public String toString() {
        return maNV + " - " + hoTen;
    }
}
