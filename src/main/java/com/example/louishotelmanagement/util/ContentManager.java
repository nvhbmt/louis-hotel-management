package com.example.louishotelmanagement.util;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class để quản lý việc chuyển đổi nội dung trong ứng dụng
 */
public class ContentManager {
    
    private static final Map<String, Parent> cachedViews = new HashMap<>();
    private static final Map<String, Object> cachedControllers = new HashMap<>();
    private static boolean reload;

    /**
     * Load FXML với caching để tăng performance
     */
    public static Parent loadFXML(String fxmlPath, BorderPane container, ContentSwitcher switcher) {
        try {
            // Kiểm tra cache trước
            if (!reload&&cachedViews.containsKey(fxmlPath)) {
                System.out.println("Loading from cache for: " + fxmlPath);
                Parent cachedView = cachedViews.get(fxmlPath);
                Object cachedController = cachedControllers.get(fxmlPath);
                
                // Cập nhật ContentSwitcher cho controller
                updateControllerSwitcher(cachedController, switcher);
                
                container.setCenter(cachedView);
                return cachedView;
            }
            
            // Load mới nếu chưa có trong cache
            FXMLLoader loader = new FXMLLoader(ContentManager.class.getResource(fxmlPath));
            Parent view = loader.load();
            Object controller = loader.getController();
            
            // Cập nhật ContentSwitcher cho controller
            updateControllerSwitcher(controller, switcher);
            
            // Cache view và controller
            cachedViews.put(fxmlPath, view);
            cachedControllers.put(fxmlPath, controller);
            
            container.setCenter(view);
            return view;
            
        } catch (IOException e) {
            System.err.println("Lỗi khi load FXML: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Load FXML với custom controller đã được setup sẵn
     */
    public static Parent loadFXMLWithCustomController(String fxmlPath, Object customController, ContentSwitcher switcher) {
        try {
            FXMLLoader loader = new FXMLLoader(ContentManager.class.getResource(fxmlPath));
            loader.setController(customController);
            Parent view = loader.load();

            // Setup controller với switcher
            updateControllerSwitcher(customController, switcher);

            // Setup cho Refreshable
            if (customController instanceof Refreshable) {
                try {
                    ((Refreshable) customController).refreshData();
                } catch (Exception e) {
                    System.err.println("Lỗi khi refreshData cho custom controller: " + e.getMessage());
                }
            }

            return view;

        } catch (IOException e) {
            System.err.println("Lỗi khi load FXML với custom controller: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cập nhật ContentSwitcher cho controller
     */
    public static void updateControllerSwitcher(Object controller, ContentSwitcher switcher) {
        if (controller instanceof ContentSwitchable) {
            ((ContentSwitchable) controller).setContentSwitcher(switcher);
        }
        
        if (controller instanceof Refreshable) {
            try {
                ((Refreshable) controller).refreshData();
            } catch (Exception e) {
                System.err.println("Lỗi khi refreshData: " + e.getMessage());
                // Xử lý lỗi SQL nếu cần
            }
        }
        // Có thể thêm các controller khác ở đây
    }
    
    /**
     * Clear cache khi cần thiết
     */
    public static void clearCache() {
        cachedViews.clear();
        cachedControllers.clear();
    }
    
    /**
     * Clear cache cho một FXML cụ thể
     */
    public static void clearCache(String fxmlPath) {
        cachedViews.remove(fxmlPath);
        cachedControllers.remove(fxmlPath);
    }
    
    /**
     * Kiểm tra xem FXML có trong cache không
     */
    public static boolean isCached(String fxmlPath) {
        return cachedViews.containsKey(fxmlPath);
    }
}

