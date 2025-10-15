package com.example.louishotelmanagement.model;

public class CTPhieuDichVu {
    private String maPhieuDV;
    private String maDV;
    private int soLuong;
    private double donGia;

    public CTPhieuDichVu() {
        this.maPhieuDV = "";
        this.donGia = 0;
        this.maDV = "";
        this.soLuong = 0;
    }
    public CTPhieuDichVu(String maPhieuDV) {
        this.maPhieuDV = maPhieuDV;
        this.maDV = "";
        this.donGia = 0;
        this.soLuong = 0;
    }

    public CTPhieuDichVu(String maPhieuDV, String maDV, int soLuong, double donGia) {
        this.maPhieuDV = maPhieuDV;
        this.maDV = maDV;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaPhieuDV() {
        return maPhieuDV;
    }

    public void setMaPhieuDV(String maPhieuDV) {
        this.maPhieuDV = maPhieuDV;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
    private double thanhTien(){
        return soLuong*donGia;
    }

    @Override
    public String toString() {
        return "CTPhieuDichVu{" +
                "maPhieuDV='" + maPhieuDV + '\'' +
                ", maDV='" + maDV + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien()=" + thanhTien() +
                '}';
    }
}

