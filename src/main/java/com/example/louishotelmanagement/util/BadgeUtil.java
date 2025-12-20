package com.example.louishotelmanagement.util;

import javafx.scene.control.Label;

public class BadgeUtil {

    /**
     * Tạo badge với variant được chỉ định
     */
    public static Label createBadge(String text, BadgeVariant variant) {
        Label badge = new Label(text);
        badge.setStyle(
            "-fx-background-color: " + variant.bg + ";" +
            "-fx-text-fill: " + variant.text + ";" +
            "-fx-padding: 4px 12px;" +
            "-fx-background-radius: 12px;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-alignment: CENTER;"
        );
        return badge;
    }

    /**
     * Tạo badge với variant được chỉ định và padding tùy chỉnh
     */
    public static Label createBadge(String text, BadgeVariant variant, String padding) {
        Label badge = new Label(text);
        badge.setStyle(
            "-fx-background-color: " + variant.bg + ";" +
            "-fx-text-fill: " + variant.text + ";" +
            "-fx-padding: " + padding + ";" +
            "-fx-background-radius: 12px;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-alignment: CENTER;"
        );
        return badge;
    }

    /**
     * Tạo badge với variant được chỉ định và font weight tùy chỉnh
     */
    public static Label createBadge(String text, BadgeVariant variant, boolean isBold) {
        Label badge = new Label(text);
        String fontWeight = isBold ? "bold" : "600";
        badge.setStyle(
            "-fx-background-color: " + variant.bg + ";" +
            "-fx-text-fill: " + variant.text + ";" +
            "-fx-padding: 4px 12px;" +
            "-fx-background-radius: 12px;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: " + fontWeight + ";" +
            "-fx-alignment: CENTER;"
        );
        return badge;
    }

    /**
     * Tạo badge với variant được chỉ định và padding tùy chỉnh
     */
    public static Label createBadge(String text, BadgeVariant variant, String padding, boolean isBold) {
        Label badge = new Label(text);
        String fontWeight = isBold ? "bold" : "600";
        badge.setStyle(
            "-fx-background-color: " + variant.bg + ";" +
            "-fx-text-fill: " + variant.text + ";" +
            "-fx-padding: " + padding + ";" +
            "-fx-background-radius: 12px;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: " + fontWeight + ";" +
            "-fx-alignment: CENTER;"
        );
        return badge;
    }

}
