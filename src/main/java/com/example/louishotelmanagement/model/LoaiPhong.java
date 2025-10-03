package com.example.louishotelmanagement.model;

import java.math.BigDecimal;

public class LoaiPhong {
    private String maLoaiPhong;
    private String tenLoai;
    private String moTa;
    private BigDecimal donGia;

    public LoaiPhong() {
        this.maLoaiPhong = "";
        this.tenLoai = "";
        this.moTa = "";
        this.donGia = BigDecimal.ZERO;
    }

    public LoaiPhong(String maLoaiPhong, String tenLoai, String moTa, BigDecimal donGia) {
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoai = tenLoai;
        this.moTa = moTa;
        this.donGia = donGia;
    }

    public LoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoai = "";
        this.moTa = "";
        this.donGia = BigDecimal.ZERO;
    }

    public String getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public void setMaLoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    @Override
    public String toString() {
        return "LoaiPhong{" +
                "maLoaiPhong='" + maLoaiPhong + '\'' +
                ", tenLoai='" + tenLoai + '\'' +
                ", moTa='" + moTa + '\'' +
                ", donGia=" + donGia +
                '}';
    }
}
