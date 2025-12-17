package com.example.louishotelmanagement.util;

import javafx.scene.Parent;

public interface ContentSwitcher {
    /**
     * Chuyển đổi nội dung hiển thị
     * @param fxmlPath Đường dẫn đến file FXML
     */
    void switchContent(String fxmlPath);
    void switchContent(Parent root);
    /**
     * Chuyển đổi nội dung và cập nhật trạng thái active trong menu
     * @param fxmlPath Đường dẫn đến file FXML
     * @param updateMenuActive Có cập nhật trạng thái active trong menu không
     */
    default void switchContent(String fxmlPath, boolean updateMenuActive) {
        switchContent(fxmlPath);
    }
    
    /**
     * Chuyển về trang chủ
     */
    default void switchToHome() {
        switchContent("/com/example/louishotelmanagement/fxml/trang-chu-view.fxml");
    }
}

