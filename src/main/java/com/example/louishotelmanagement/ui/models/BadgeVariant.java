package com.example.louishotelmanagement.ui.models;

public enum BadgeVariant {
    SUCCESS("#d1fae5", "#065f46"),
    WARNING("#fef3c7", "#92400e"),
    DANGER("#fce7f3", "#9f1239"),
    INFO("#dbeafe", "#1e40af"),
    DEFAULT("#f3f4f6", "#6b7280");

    public final String bg;
    public final String text;

    BadgeVariant(String bg, String text) {
        this.bg = bg;
        this.text = text;
    }
}
