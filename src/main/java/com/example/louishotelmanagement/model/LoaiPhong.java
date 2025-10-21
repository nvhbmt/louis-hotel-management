package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.util.Objects;

public class LoaiPhong {
    private String maLoaiPhong;
    private String tenLoai;
    private String moTa;
    private Double donGia;

    public LoaiPhong() {
        this.maLoaiPhong = "";
        this.tenLoai = "";
        this.moTa = "";
        this.donGia = 0.0;
    }

    public LoaiPhong(String maLoaiPhong, String tenLoai, String moTa, Double donGia) {
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoai = tenLoai;
        this.moTa = moTa;
        this.donGia = donGia;
    }

    public LoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoai = "";
        this.moTa = "";
        this.donGia = 0.0;
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

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    @Override
    public String toString() {
        return tenLoai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoaiPhong loaiPhong = (LoaiPhong) o;
        return Objects.equals(maLoaiPhong, loaiPhong.maLoaiPhong);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maLoaiPhong);
    }
}
