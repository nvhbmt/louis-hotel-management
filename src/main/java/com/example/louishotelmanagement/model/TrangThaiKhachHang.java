package com.example.louishotelmanagement.model;

public enum TrangThaiKhachHang {
    DANG_LUU_TRU("Đang lưu trú"),
    CHECK_OUT("Check-out"),
    DA_DAT("Đã đặt");

    private final String tenHienThi;

    TrangThaiKhachHang(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }

    public static TrangThaiKhachHang fromString(String text) {
        if (text != null) {
            for (TrangThaiKhachHang t : TrangThaiKhachHang.values()) {
                if (text.equalsIgnoreCase(t.getTenHienThi())) {
                    return t;
                }
            }
        }
        return null;
    }
}