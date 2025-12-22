package com.example.louishotelmanagement.ui.models;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Model đại diện cho 1 item trong menu
 * Hỗ trợ hiển thị icon (ảnh hoặc text) và lưu trữ Parent component.
 */
public class MenuItemModel {
    private final String title;      // Tên hiển thị
    private final String iconText;   // Icon dạng text hoặc emoji (tùy chọn)
    private final String iconPath;   // Đường dẫn ảnh icon (tùy chọn)
    private final Parent root;       // Parent component của menu item

    /**
     * Constructor cơ bản (dùng icon text)
     */
    public MenuItemModel(String title, String iconText, Parent root) {
        this(title, iconText, null, root);
    }

    /**
     * Constructor cho icon dạng ảnh
     */
    public MenuItemModel(String title, String iconPath, Parent root, boolean isImageIcon) {
        this(title, null, iconPath, root);
    }

    /**
     * Constructor đầy đủ
     */
    public MenuItemModel(String title, String iconText, String iconPath, Parent root) {
        this.title = title;
        this.iconText = iconText;
        this.iconPath = iconPath;
        this.root = root;
    }

    // Getter
    public String getTitle() { return title; }
    public Parent getRoot() { return root; }

    public Node getIconNode() {
        if (iconPath != null) {
            Image img = new Image(getClass().getResourceAsStream(iconPath), 20, 20, true, true);
            return new ImageView(img);
        }
        if (iconText != null && !iconText.isEmpty()) {
            FontIcon icon = new FontIcon(iconText);
            icon.setIconSize(18);
            return icon;
        }
        return null;
    }

    @Override
    public String toString() {
        return title;
    }
}
