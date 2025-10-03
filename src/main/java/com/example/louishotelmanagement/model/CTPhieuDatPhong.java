package com.example.louishotelmanagement.model;

import java.time.LocalDate;

public class CTPhieuDatPhong {
    private String maPhieu;
    private String maPhong;
    private LocalDate ngayDen;
    private LocalDate ngayDi;
    private double giaPhong;

    // Constructor mặc định
    public CTPhieuDatPhong() {
    }

    // Constructor đầy đủ
    public CTPhieuDatPhong(String maPhieu, String maPhong, LocalDate ngayDen, LocalDate ngayDi, double giaPhong) {
        this.maPhieu = maPhieu;
        this.maPhong = maPhong;
        this.ngayDen = ngayDen;
        this.ngayDi = ngayDi;
        this.giaPhong = giaPhong;
    }

    // Getter & Setter
    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
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

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
    }

    // toString
    @Override
    public String toString() {
        return "CTPhieuDatPhong{" +
                "maPhieu='" + maPhieu + '\'' +
                ", maPhong='" + maPhong + '\'' +
                ", ngayDen=" + ngayDen +
                ", ngayDi=" + ngayDi +
                ", giaPhong=" + giaPhong +
                '}';
    }
}
