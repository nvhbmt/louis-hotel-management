package com.example.louishotelmanagement.ui.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Model đại diện cho 1 item trong menu
 * Hỗ trợ hiển thị icon (ảnh hoặc text) và mở file FXML tương ứng.
 */
public class MenuItemModel {
    private final String title;      // Tên hiển thị
    private final String iconText;   // Icon dạng text hoặc emoji (tùy chọn)
    private final String iconPath;   // Đường dẫn ảnh icon (tùy chọn)
    private final String fxmlPath;   // Đường dẫn tới file FXML

    /**
     * Constructor cơ bản (dùng icon text)
     */
    public MenuItemModel(String title, String iconText, String fxmlPath) {
        this(title, iconText, null, fxmlPath, false);
    }

    /**
     * Constructor cho icon dạng ảnh
     */
    public MenuItemModel(String title, String iconPath, String fxmlPath, boolean isImageIcon) {
        this(title, null, iconPath, fxmlPath, false);
    }

    /**
     * Constructor đầy đủ
     */
    public MenuItemModel(String title, String iconText, String iconPath, String fxmlPath, boolean _isParent) {
        this.title = title;
        this.iconText = iconText;
        this.iconPath = iconPath;
        this.fxmlPath = fxmlPath;
    }

    // Getter
    public String getTitle() { return title; }
    public String getFxmlPath() { return fxmlPath; }

    public javafx.scene.Node getIconNode() {
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
