package com.example.louishotelmanagement.ui.models;

public enum ButtonVariant {
    PRIMARY("#007bff", "#ffffff", "#007bff"),
    SUCCESS("#28a745", "#ffffff", "#28a745"),
    SECONDARY("#6c757d", "#ffffff", "#6c757d"),
    OUTLINE("#ffffff", "#000000", "#c0c0c0"),
    DANGER("#dc3545", "#ffffff", "#dc3545"),
    WARNING("#ffc107", "#000000", "#ffc107"),
    INFO("#17a2b8", "#ffffff", "#17a2b8");

    public final String bg;
    public final String text;
    public final String border;

    ButtonVariant(String bg, String text, String border) {
        this.bg = bg;
        this.text = text;
        this.border = border;
    }
}
