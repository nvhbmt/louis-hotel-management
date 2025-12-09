package com.example.louishotelmanagement.util;

import java.math.BigDecimal;

public class HoaDonCalculationResult {
    public BigDecimal tienPhongTong; // Đã bao gồm phí phạt
    public BigDecimal tienDichVu;
    public BigDecimal vat;
    public BigDecimal tongGiamGia; // Tổng GG Mã GG + GG Hạng Khách
    public BigDecimal tienCoc;
    public BigDecimal tienPhat;
    public BigDecimal tongThanhToan; // Tổng tiền cuối cùng

    public HoaDonCalculationResult() {
        this.tienPhongTong = BigDecimal.ZERO;
        this.tienDichVu = BigDecimal.ZERO;
        this.vat = BigDecimal.ZERO;
        this.tongGiamGia = BigDecimal.ZERO;
        this.tienCoc = BigDecimal.ZERO;
        this.tienPhat = BigDecimal.ZERO;
        this.tongThanhToan = BigDecimal.ZERO;
    }

    public HoaDonCalculationResult(BigDecimal tienPhongTong, BigDecimal tienDichVu, BigDecimal vat, BigDecimal tongGiamGia, BigDecimal tienCoc, BigDecimal tienPhat, BigDecimal tongThanhToan) {
        this.tienPhongTong = tienPhongTong;
        this.tienDichVu = tienDichVu;
        this.vat = vat;
        this.tongGiamGia = tongGiamGia;
        this.tienCoc = tienCoc;
        this.tienPhat = tienPhat;
        this.tongThanhToan = tongThanhToan;
    }

    public BigDecimal getTienPhongTong() {
        return tienPhongTong;
    }

    public void setTienPhongTong(BigDecimal tienPhongTong) {
        this.tienPhongTong = tienPhongTong;
    }

    public BigDecimal getTienDichVu() {
        return tienDichVu;
    }

    public void setTienDichVu(BigDecimal tienDichVu) {
        this.tienDichVu = tienDichVu;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getTongGiamGia() {
        return tongGiamGia;
    }

    public void setTongGiamGia(BigDecimal tongGiamGia) {
        this.tongGiamGia = tongGiamGia;
    }

    public BigDecimal getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(BigDecimal tienCoc) {
        this.tienCoc = tienCoc;
    }

    public BigDecimal getTienPhat() {
        return tienPhat;
    }

    public void setTienPhat(BigDecimal tienPhat) {
        this.tienPhat = tienPhat;
    }

    public BigDecimal getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(BigDecimal tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }
}
