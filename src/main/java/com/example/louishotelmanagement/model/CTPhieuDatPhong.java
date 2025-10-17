package com.example.louishotelmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CTPhieuDatPhong {
    private String maPhieu;
    private String maPhong;
    private LocalDate ngayDen;
    private LocalDate ngayDi;
    private LocalDate ngayNhan; // <--- THUỘC TÍNH MỚI
    private LocalDate ngayTra;  // <--- THUỘC TÍNH MỚI
    private BigDecimal giaPhong;
    private PhieuDatPhong phieuDatPhong;
    private Phong phong;

    public CTPhieuDatPhong() {
        this.maPhieu = "";
        this.maPhong = "";
        this.ngayDen = null;
        this.ngayDi = null;
        this.ngayNhan = null; // Khởi tạo thuộc tính mới
        this.ngayTra = null;  // Khởi tạo thuộc tính mới
        this.giaPhong = BigDecimal.ZERO;
        this.phieuDatPhong = null;
        this.phong = null;
    }

    // Constructor MỚI với 7 tham số (thêm ngayNhan và ngayTra)
    public CTPhieuDatPhong(String maPhieu, String maPhong, LocalDate ngayDen, LocalDate ngayDi, LocalDate ngayNhan, LocalDate ngayTra, BigDecimal giaPhong) {
        this.maPhieu = maPhieu;
        this.maPhong = maPhong;
        this.ngayDen = ngayDen;
        this.ngayDi = ngayDi;
        this.ngayNhan = ngayNhan;
        this.ngayTra = ngayTra;
        this.giaPhong = giaPhong;
        this.phieuDatPhong = null;
        this.phong = null;
    }

    // Constructor cũ (giữ lại nếu cần cho mục đích tương thích ngược, nhưng nên ưu tiên constructor mới)
    public CTPhieuDatPhong(String maPhieu, String maPhong, LocalDate ngayDen, LocalDate ngayDi, BigDecimal giaPhong) {
        this(maPhieu, maPhong, ngayDen, ngayDi, null, null, giaPhong); // Gọi constructor mới với null cho ngày nhận/trả
    }

    // --- GETTERS VÀ SETTERS CỦA THUỘC TÍNH MỚI ---

    public LocalDate getNgayNhan() {
        return ngayNhan;
    }

    public void setNgayNhan(LocalDate ngayNhan) {
        this.ngayNhan = ngayNhan;
    }

    public LocalDate getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDate ngayTra) {
        this.ngayTra = ngayTra;
    }

    // --- CÁC GETTERS VÀ SETTERS KHÁC (GIỮ NGUYÊN) ---

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

    public BigDecimal getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(BigDecimal giaPhong) {
        this.giaPhong = giaPhong;
    }

    public PhieuDatPhong getPhieuDatPhong() {
        return phieuDatPhong;
    }

    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        this.phieuDatPhong = phieuDatPhong;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    // --- CẬP NHẬT PHƯƠNG THỨC toString() ---

    @Override
    public String toString() {
        return "CTPhieuDatPhong{" +
                "maPhieu='" + maPhieu + '\'' +
                ", maPhong='" + maPhong + '\'' +
                ", ngayDen=" + ngayDen +
                ", ngayDi=" + ngayDi +
                ", ngayNhan=" + ngayNhan +  // <--- THÊM vào toString
                ", ngayTra=" + ngayTra +   // <--- THÊM vào toString
                ", giaPhong=" + giaPhong +
                ", phieuDatPhong=" + (phieuDatPhong != null ? phieuDatPhong.getMaPhieu() : "null") +
                ", phong=" + (phong != null ? phong.getMaPhong() : "null") +
                '}';
    }
}