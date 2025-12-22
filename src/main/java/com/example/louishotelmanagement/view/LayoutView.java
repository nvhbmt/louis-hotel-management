package com.example.louishotelmanagement.view;

import com.example.louishotelmanagement.controller.LayoutController;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class LayoutView {
  private static LayoutView instance;

  // Main container
  private BorderPane mainBorderPane;

  // Sidebar components
  private VBox menuContainer;
  private Label userInfoLabel;
  private Button logoutBtn;

  private Parent root;
  public LayoutController layoutController;

  public static LayoutView getInstance() {
    if (instance == null) {
      instance = new LayoutView();
    }
    return instance;
  }

  public static String getIdentifier() {
    return "layout-chinh";
  }

  public LayoutView() {
    // ============================================
    // MAIN BORDER PANE SETUP
    // ============================================
    mainBorderPane = new BorderPane();
    mainBorderPane.getStylesheets().addAll(
        getClass().getResource("/com/example/louishotelmanagement/css/main.css").toExternalForm()
    );

    // ============================================
    // SIDEBAR SETUP
    // ============================================
    BorderPane sidebarBorderPane = new BorderPane();
    sidebarBorderPane.getStyleClass().addAll("sidebar");

    // ============================================
    // LOGO SECTION (TOP)
    // ============================================
    VBox logoVBox = new VBox(12.0);
    logoVBox.setAlignment(javafx.geometry.Pos.CENTER);

    ImageView logoImageView = new ImageView();
    logoImageView.setFitHeight(72.0);
    logoImageView.setFitWidth(94.0);
    logoImageView.setPickOnBounds(true);
    logoImageView.setPreserveRatio(true);
    logoImageView.setImage(new Image(getClass().getResource("/com/example/louishotelmanagement/image/unnamed.jpg").toExternalForm()));

    Label logoLabel = new Label("LOIUS HOTEL");
    logoLabel.getStyleClass().addAll("logo-text");

    logoVBox.getChildren().addAll(logoImageView, logoLabel);
    sidebarBorderPane.setTop(logoVBox);
    BorderPane.setAlignment(logoVBox, javafx.geometry.Pos.CENTER);

    // ============================================
    // USER INFO SECTION (BOTTOM)
    // ============================================
    VBox userInfoVBox = new VBox();
    userInfoVBox.setPrefHeight(108.0);
    userInfoVBox.setPrefWidth(250.0);
    userInfoVBox.getStyleClass().addAll("user-section");

    Label userInfoTitleLabel = new Label("THÔNG TIN NGƯỜI DÙNG");
    userInfoTitleLabel.setAlignment(javafx.geometry.Pos.CENTER);
    userInfoTitleLabel.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
    userInfoTitleLabel.setPrefHeight(14.0);
    userInfoTitleLabel.setPrefWidth(243.0);
    userInfoTitleLabel.getStyleClass().addAll("user-label");
    userInfoTitleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

    VBox userInfoContentVBox = new VBox(4.0);
    userInfoContentVBox.setAlignment(javafx.geometry.Pos.CENTER);
    userInfoContentVBox.getStyleClass().addAll("user-info");

    userInfoLabel = new Label("admin (Admin)");
    userInfoLabel.getStyleClass().addAll("user-name");

    logoutBtn = new Button("ĐĂNG XUẤT");
    logoutBtn.getStyleClass().addAll("btn-danger");
    logoutBtn.setMnemonicParsing(false);

    userInfoContentVBox.getChildren().addAll(userInfoLabel, logoutBtn);
    userInfoVBox.getChildren().addAll(userInfoTitleLabel, userInfoContentVBox);

    sidebarBorderPane.setBottom(userInfoVBox);
    BorderPane.setAlignment(userInfoVBox, javafx.geometry.Pos.CENTER);

    // ============================================
    // MENU SECTION (CENTER)
    // ============================================
    VBox menuVBox = new VBox();
    menuVBox.setPrefWidth(250.0);
    menuVBox.getStyleClass().addAll("sidebar");
    menuVBox.setPadding(new Insets(20.0, 16.0, 20.0, 16.0));

    Label menuLabel = new Label("MENU");
    menuLabel.setPrefHeight(26.0);
    menuLabel.setPrefWidth(210.0);
    menuLabel.getStyleClass().addAll("menu-label");

    ScrollPane menuScrollPane = new ScrollPane();
    menuScrollPane.setFitToWidth(true);
    menuScrollPane.getStyleClass().addAll("menu-scroll-pane");
    VBox.setVgrow(menuScrollPane, javafx.scene.layout.Priority.ALWAYS);

    menuContainer = new VBox();
    menuContainer.getStyleClass().addAll("menu-container");

    menuScrollPane.setContent(menuContainer);
    menuVBox.getChildren().addAll(menuLabel, menuScrollPane);

    sidebarBorderPane.setCenter(menuVBox);
    BorderPane.setAlignment(menuVBox, javafx.geometry.Pos.CENTER);

    // Set sidebar to left of main border pane
    mainBorderPane.setLeft(sidebarBorderPane);

    this.root = mainBorderPane;
    this.layoutController = new LayoutController(this);
  }

  // Getters for controller
  public BorderPane getMainBorderPane() {
    return mainBorderPane;
  }

  public VBox getMenuContainer() {
    return menuContainer;
  }

  public Label getUserInfoLabel() {
    return userInfoLabel;
  }

  public Button getLogoutBtn() {
    return logoutBtn;
  }

  public Parent getRoot() {
    return root;
  }
}
