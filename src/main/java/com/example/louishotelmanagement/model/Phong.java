package com.example.louishotelmanagement.model;

public class Phong {
    private String maPhong;
    private Integer tang;
    private String trangThai;
    private String moTa;
    private String maLoaiPhong;
    private LoaiPhong loaiPhong;

    public Phong() {
        this.maPhong = "";
        this.tang = null;
        this.trangThai = "";
        this.moTa = "";
        this.maLoaiPhong = "";
        this.loaiPhong = null;
    }

    public Phong(String maPhong, Integer tang, String trangThai, String moTa, String maLoaiPhong) {
        this.maPhong = maPhong;
        this.tang = tang;
        this.trangThai = trangThai;
        this.moTa = moTa;
        this.maLoaiPhong = maLoaiPhong;
        this.loaiPhong = null;
    }

    public Phong(String maPhong) {
        this.maPhong = maPhong;
        this.tang = null;
        this.trangThai = "";
        this.moTa = "";
        this.maLoaiPhong = "";
        this.loaiPhong = null;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public Integer getTang() {
        return tang;
    }

    public void setTang(Integer tang) {
        this.tang = tang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public void setMaLoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    public LoaiPhong getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(LoaiPhong loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    @Override
    public String toString() {
        return "Phong{" +
                "maPhong='" + maPhong + '\'' +
                ", tang=" + tang +
                ", trangThai='" + trangThai + '\'' +
                ", moTa='" + moTa + '\'' +
                ", maLoaiPhong='" + maLoaiPhong + '\'' +
                ", loaiPhong=" + (loaiPhong != null ? loaiPhong.getTenLoai() : "null") +
                '}';
    }
}