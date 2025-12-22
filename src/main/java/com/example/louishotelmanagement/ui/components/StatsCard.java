package com.example.louishotelmanagement.ui.components;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Reusable statistics card component for displaying metrics with icons
 */
public class StatsCard extends VBox {

    private final Label titleLabel;
    private final Label valueLabel;
    private final ImageView iconImageView;

    /**
     * Create a statistics card with title, value, and icon
     *
     * @param title                   The title text for the card
     * @param value                   The value text for the card
     * @param valueStyleClass         The CSS style class for the value label
     * @param iconPath                The resource path to the icon image
     * @param iconContainerStyleClass The CSS style class for the icon container
     */
    public StatsCard(String title, String value, String valueStyleClass, String iconPath, String iconContainerStyleClass) {
        super();

        // Initialize components
        titleLabel = new Label(title);
        valueLabel = new Label(value);
        iconImageView = new ImageView();

        // Set up card container
        this.getStyleClass().addAll("stats-card");

        // Create content layout
        HBox contentHBox = new HBox();
        contentHBox.getStyleClass().addAll("stats-card-content");

        // Create text container
        VBox textVBox = new VBox();
        textVBox.getStyleClass().addAll("stats-card-text");

        titleLabel.getStyleClass().addAll("stats-card-title");
        valueLabel.getStyleClass().addAll(valueStyleClass);

        textVBox.getChildren().addAll(titleLabel, valueLabel);
        HBox.setHgrow(textVBox, Priority.ALWAYS);

        // Create icon container
        VBox iconVBox = new VBox();
        iconVBox.getStyleClass().addAll(iconContainerStyleClass);

        iconImageView.getStyleClass().addAll("stats-card-icon");
        iconImageView.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        iconVBox.getChildren().addAll(iconImageView);

        contentHBox.getChildren().addAll(textVBox, iconVBox);
        this.getChildren().addAll(contentHBox);
    }

    /**
     * Update the value displayed on the card
     *
     * @param newValue The new value to display
     */
    public void setValue(String newValue) {
        valueLabel.setText(newValue);
    }

    /**
     * Get the current value displayed on the card
     *
     * @return The current value text
     */
    public String getValue() {
        return valueLabel.getText();
    }

    /**
     * Update the title displayed on the card
     *
     * @param newTitle The new title to display
     */
    public void setTitle(String newTitle) {
        titleLabel.setText(newTitle);
    }

    /**
     * Get the current title displayed on the card
     *
     * @return The current title text
     */
    public String getTitle() {
        return titleLabel.getText();
    }

    /**
     * Get the title label for direct styling or manipulation
     *
     * @return The title label
     */
    public Label getTitleLabel() {
        return titleLabel;
    }

    /**
     * Get the value label for direct styling or manipulation
     *
     * @return The value label
     */
    public Label getValueLabel() {
        return valueLabel;
    }

    /**
     * Get the icon image view for direct styling or manipulation
     *
     * @return The icon image view
     */
    public ImageView getIconImageView() {
        return iconImageView;
    }
}
