package com.example.louishotelmanagement.model;

public enum TrangThaiPhong {
    TRONG("Trống"),
    DA_DAT("Đã đặt"),
    DANG_SU_DUNG("Đang sử dụng"),
    BAO_TRI("Bảo trì");

    private final String trangThai;

    TrangThaiPhong(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public static TrangThaiPhong fromString(String text) {
        if (text == null) return null;
        text = text.trim().toLowerCase();
        for (TrangThaiPhong tt : values()) {
            if (tt.getTrangThai().toLowerCase().equals(text) || tt.name().toLowerCase().equals(text)) {
                return tt;
            }
        }
        return null; // hoặc throw exception
    }
}
