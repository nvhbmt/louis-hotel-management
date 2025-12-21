package com.example.louishotelmanagement.ui.components;

import com.example.louishotelmanagement.ui.models.ButtonVariant;
import javafx.scene.control.Button;

public class CustomButton {

    /**
     * Tạo button với variant được chỉ định
     */
    public static Button createButton(String text, ButtonVariant variant) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + variant.bg + ";" +
                        "-fx-text-fill: " + variant.text + ";" +
                        "-fx-border-color: " + variant.border + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-font-size: 13px;"
        );
        return button;
    }

    /**
     * Tạo button với variant được chỉ định và kích thước tùy chỉnh
     */
    public static Button createButton(String text, ButtonVariant variant, double width) {
        Button button = createButton(text, variant);
        button.setPrefWidth(width);
        return button;
    }

    /**
     * Tạo button với variant được chỉ định và padding tùy chỉnh
     */
    public static Button createButton(String text, ButtonVariant variant, String padding) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + variant.bg + ";" +
                        "-fx-text-fill: " + variant.text + ";" +
                        "-fx-border-color: " + variant.border + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: " + padding + ";" +
                        "-fx-font-size: 13px;"
        );
        return button;
    }

    /**
     * Tạo button với variant được chỉ định, kích thước và padding tùy chỉnh
     */
    public static Button createButton(String text, ButtonVariant variant, double width, String padding) {
        Button button = createButton(text, variant, padding);
        button.setPrefWidth(width);
        return button;
    }

}
