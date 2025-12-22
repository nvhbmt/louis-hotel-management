package com.example.louishotelmanagement.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kordamp.ikonli.javafx.FontIcon;

import com.example.louishotelmanagement.ui.data.MenuData;
import com.example.louishotelmanagement.ui.models.MenuItemModel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Class chịu trách nhiệm xây dựng menu từ TreeItem structure.
 * Tách logic tạo menu ra khỏi LayoutController để dễ quản lý và bảo trì.
 */
public class MenuBuilder {
    
    /**
     * Kết quả xây dựng menu, chứa các components và mappings cần thiết
     */
    public static class MenuBuildResult {
        private final VBox menuContainer;
        private final Map<Parent, Button> rootToButton;
        private final Map<Button, VBox> groupButtonToSubmenu;

        public MenuBuildResult(VBox menuContainer, Map<Parent, Button> rootToButton,
                              Map<Button, VBox> groupButtonToSubmenu) {
            this.menuContainer = menuContainer;
            this.rootToButton = rootToButton;
            this.groupButtonToSubmenu = groupButtonToSubmenu;
        }
        
        public VBox getMenuContainer() {
            return menuContainer;
        }
        
        public Map<Parent, Button> getRootToButton() {
            return rootToButton;
        }

        public Map<Button, VBox> getGroupButtonToSubmenu() {
            return groupButtonToSubmenu;
        }
    }
    
    /**
     * Interface để xử lý sự kiện khi click vào menu item
     */
    public interface MenuItemActionHandler {
        void onMenuItemClick(Parent root, Button button);
    }
    
    /**
     * Interface để xử lý sự kiện toggle submenu
     */
    public interface SubmenuToggleHandler {
        void onSubmenuToggle(Button groupHeader, VBox submenu);
    }
    
    /**
     * Xây dựng menu từ MenuData và trả về kết quả
     * 
     * @param menuItemActionHandler Handler để xử lý khi click vào menu item
     * @param submenuToggleHandler Handler để xử lý khi toggle submenu
     * @return MenuBuildResult chứa menu container và các mappings
     */
    public static MenuBuildResult buildMenu(MenuItemActionHandler menuItemActionHandler,
                                            SubmenuToggleHandler submenuToggleHandler) {
        VBox menuContainer = new VBox();
        menuContainer.getStyleClass().add("menu-container");

        Map<Parent, Button> rootToButton = new HashMap<>();
        Map<Button, VBox> groupButtonToSubmenu = new HashMap<>();
        
        TreeItem<MenuItemModel> root = MenuData.createMenuTree();
        
        // Build menu từ TreeItem structure
        for (TreeItem<MenuItemModel> item : root.getChildren()) {
            MenuItemModel model = item.getValue();
            
            if (item.getChildren().isEmpty()) {
                // Leaf item - single menu button
                Button menuButton = createMenuButton(model, menuItemActionHandler);
                menuContainer.getChildren().add(menuButton);
                
                if (model.getRoot() != null) {
                    rootToButton.put(model.getRoot(), menuButton);
                }
            } else {
                // Parent item - expandable group
                Button groupHeader = createGroupHeader(model);
                VBox submenu = createSubmenu(item.getChildren(), menuItemActionHandler, rootToButton);
                groupButtonToSubmenu.put(groupHeader, submenu);
                
                // Initially hide submenu
                submenu.setVisible(false);
                submenu.setManaged(false);
                
                menuContainer.getChildren().add(groupHeader);
                menuContainer.getChildren().add(submenu);
                
                // Toggle submenu on click
                groupHeader.setOnAction(_ -> {
                    if (submenuToggleHandler != null) {
                        submenuToggleHandler.onSubmenuToggle(groupHeader, submenu);
                    }
                });
            }
        }
        
        return new MenuBuildResult(menuContainer, rootToButton, groupButtonToSubmenu);
    }
    
    /**
     * Tạo menu button cho leaf item
     */
    private static Button createMenuButton(MenuItemModel model, MenuItemActionHandler handler) {
        Button button = new Button(model.getTitle());
        button.getStyleClass().add("menu-item");
        
        if (model.getIconNode() != null) {
            button.setGraphic(model.getIconNode());
        }
        
        if (model.getRoot() != null && handler != null) {
            button.setOnAction(_ -> handler.onMenuItemClick(model.getRoot(), button));
        }
        
        return button;
    }
    
    /**
     * Tạo group header cho parent item
     */
    private static Button createGroupHeader(MenuItemModel model) {
        Button button = new Button();
        button.getStyleClass().add("menu-group-header");
        
        // Create HBox to hold icon, text, and arrow (arrow ở bên phải)
        HBox contentBox = new HBox(12);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        
        // Add icon nếu có
        if (model.getIconNode() != null) {
            contentBox.getChildren().add(model.getIconNode());
        }
        
        // Add text label
        Label textLabel = new Label(model.getTitle());
        contentBox.getChildren().add(textLabel);
        
        // Add spacer để đẩy arrow sang phải
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        contentBox.getChildren().add(spacer);
        
        // Add arrow icon ở bên phải
        FontIcon arrow = new FontIcon("mdi2c-chevron-right");
        arrow.getStyleClass().add("menu-arrow");
        contentBox.getChildren().add(arrow);
        
        button.setGraphic(contentBox);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        
        return button;
    }
    
    /**
     * Tạo submenu container cho children
     */
    private static VBox createSubmenu(List<TreeItem<MenuItemModel>> children,
                                     MenuItemActionHandler handler,
                                     Map<Parent, Button> rootToButton) {
        VBox submenu = new VBox();
        submenu.getStyleClass().add("menu-submenu");
        
        for (TreeItem<MenuItemModel> child : children) {
            MenuItemModel model = child.getValue();
            Button submenuButton = new Button(model.getTitle());
            submenuButton.getStyleClass().add("menu-submenu-item");
            
            if (model.getIconNode() != null) {
                submenuButton.setGraphic(model.getIconNode());
            }
            
            if (model.getRoot() != null) {
                if (handler != null) {
                    submenuButton.setOnAction(_ -> handler.onMenuItemClick(model.getRoot(), submenuButton));
                }
                rootToButton.put(model.getRoot(), submenuButton);
            }
            
            submenu.getChildren().add(submenuButton);
        }
        
        return submenu;
    }
    
    /**
     * Helper method để xoay arrow icon
     */
    public static void rotateArrow(Button button, double angle) {
        if (button.getGraphic() instanceof HBox) {
            HBox hbox = (HBox) button.getGraphic();
            // Arrow ở vị trí cuối cùng
            if (!hbox.getChildren().isEmpty()) {
                Node lastNode = hbox.getChildren().get(hbox.getChildren().size() - 1);
                if (lastNode instanceof FontIcon) {
                    ((FontIcon) lastNode).setRotate(angle);
                }
            }
        }
    }
    
    /**
     * Helper method để reset arrow rotation
     */
    public static void resetArrowRotation(Button button) {
        rotateArrow(button, 0);
    }
}

