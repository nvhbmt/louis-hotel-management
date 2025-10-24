package com.example.louishotelmanagement.model;

public enum KieuGiamGia {
    PERCENT("PERCENT"),
    AMOUNT("AMOUNT");

    private final String kieuGiamGia;

    KieuGiamGia(String kieuGiamGia) {
        this.kieuGiamGia = kieuGiamGia;
    }
    
    public static KieuGiamGia fromString(String text) {
        if (text == null) return null;
        text = text.trim().toLowerCase();
        for (KieuGiamGia tt : values()) {
            if (tt.toString().toLowerCase().equals(text) || tt.name().toLowerCase().equals(text)) {
                return tt;
            }
        }
        return null; // hoặc throw exception
    }

    @Override
    public String toString() {
        return kieuGiamGia;
    }
}
