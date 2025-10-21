package com.example.louishotelmanagement.model;

public enum TrangThaiPhieuDatPhong {
    HOAN_THANH("Hoàn thành"),
    DA_DAT("Đã đặt"),
    DANG_SU_DUNG("Đang sử dụng");

    private final String trangThai;

    TrangThaiPhieuDatPhong(String trangThai) {
        this.trangThai = trangThai;
    }


    public static TrangThaiPhieuDatPhong fromString(String text) {
        if (text == null) return null;
        text = text.trim().toLowerCase();
        for (TrangThaiPhieuDatPhong tt : values()) {
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
