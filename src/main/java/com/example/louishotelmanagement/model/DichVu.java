package com.example.louishotelmanagement.model;

public class DichVu {
    private String maDV;
    private String tenDV;
    private int soLuong;
    private double donGia;
    private String moTa;
    private boolean conKinhDoanh;


    public DichVu() {
    }


    public DichVu(String maDV, String tenDV, int soLuong, double donGia, String moTa, boolean conKinhDoanh) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.moTa = moTa;
        this.conKinhDoanh = conKinhDoanh;
    }



    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
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

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public boolean isConKinhDoanh() {
        return conKinhDoanh;
    }

    public void setConKinhDoanh(boolean conKinhDoanh) {
        this.conKinhDoanh = conKinhDoanh;
    }
    
    @Override
    public String toString() {
        return "DichVu{" +
                "maDV='" + maDV + '\'' +
                ", tenDV='" + tenDV + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", moTa='" + moTa + '\'' +
                ", conKinhDoanh=" + conKinhDoanh +
                '}';
    }
}
