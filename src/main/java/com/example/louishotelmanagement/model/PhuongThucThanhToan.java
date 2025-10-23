package com.example.louishotelmanagement.model;

public enum PhuongThucThanhToan {
        TIEN_MAT("Tiền mặt"),
        VI_DIEN_TU("Ví điện tử"),
        CHUYEN_KHOAN("Chuyển khoản");

        private final String trangThai;

        PhuongThucThanhToan(String trangThai) {
            this.trangThai = trangThai;
        }


        public static PhuongThucThanhToan fromString(String text) {
            if (text == null) return null;
            text = text.trim().toLowerCase();
            for (PhuongThucThanhToan tt : values()) {
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
