package com.example.louishotelmanagement.model;

public enum TrangThaiHoaDon {
    DA_THANH_TOAN("Đã thanh toán"),
    CHUA_THANH_TOAN("Chưa thanh toán"),
    DA_HUY("Đã hủy");

    private final String trangThai;

    TrangThaiHoaDon(String trangThai) {
        this.trangThai = trangThai;
    }


    public static TrangThaiHoaDon fromString(String text) {
        if (text == null) return null;
        text = text.trim().toLowerCase();
        for (TrangThaiHoaDon tt : values()) {
            if (tt.toString().toLowerCase().equals(text) || tt.name().toLowerCase().equals(text)) {
                return tt;
            }
        }
        return null; // hoặc throw exception
    }

    @Override
    public String toString() {
        return trangThai;
    }
}
