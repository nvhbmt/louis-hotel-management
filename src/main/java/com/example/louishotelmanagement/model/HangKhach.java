package com.example.louishotelmanagement.model;

public enum HangKhach {
    KHACH_THUONG("Khách thường"),
    KHACH_QUEN("Khách quen"),
    KHACH_VIP("Khách VIP"),
    KHACH_DOANH_NGHIEP("Khách doanh nghiệp");

    private final String hangKhach;

    HangKhach(String hangKhach) {
        this.hangKhach = hangKhach;
    }


    public static HangKhach fromString(String text) {
        if (text == null) return null;
        text = text.trim().toLowerCase();
        for (HangKhach tt : values()) {
            if (tt.toString().toLowerCase().equals(text) || tt.name().toLowerCase().equals(text)) {
                return tt;
            }
        }
        return null; // hoặc throw exception
    }

    @Override
    public String toString() {
        return hangKhach;
    }
}
