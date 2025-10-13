package com.example.louishotelmanagement.model;

public class Phong {
    private String maPhong;
    private Integer tang;
    private TrangThaiPhong trangThai;
    private String moTa;
    private LoaiPhong loaiPhong;

    public Phong() {
        this.maPhong = "";
        this.tang = null;
        this.trangThai = TrangThaiPhong.TRONG;
        this.moTa = "";
        this.loaiPhong = null;
    }

    public Phong(String maPhong, Integer tang, TrangThaiPhong trangThai, String moTa, LoaiPhong loaiPhong) {
        this.maPhong = maPhong;
        this.tang = tang;
        this.trangThai = trangThai;
        this.moTa = moTa;
        this.loaiPhong = loaiPhong;
    }

    public Phong(String maPhong) {
        this.maPhong = maPhong;
        this.tang = null;
        this.trangThai = TrangThaiPhong.TRONG;
        this.moTa = "";
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

    public TrangThaiPhong getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiPhong trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
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
                ", loaiPhong=" + (loaiPhong != null ? loaiPhong.getTenLoai() : "null") +
                '}';
    }
}