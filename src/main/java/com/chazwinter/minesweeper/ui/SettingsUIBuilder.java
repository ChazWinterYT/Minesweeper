package com.chazwinter.minesweeper.ui;

import com.chazwinter.minesweeper.util.UIProperties;
import com.chazwinter.minesweeper.settings.GameSettings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SettingsUIBuilder {
    private final GameSettings gameSettings;

    private Label minesLimitLabel;
    private Label userMessageLabel;

    /**
     * Constructor to initialize the Settings UI Builder
     * @param gameSettings Game settings to be used to build the Settings menu UI.
     */
   public SettingsUIBuilder(GameSettings gameSettings) {
       this.gameSettings = gameSettings;
   }

    /**
     * Build the settings window Pane for display to the user.
     * @return The completed Pane.
     */
   public Pane buildSettingsPane() {
       VBox settingsPane = new VBox(UIProperties.BOX_SPACING);
       settingsPane.setPadding(new Insets(UIProperties.PADDING));
       settingsPane.setStyle(UIProperties.BACKGROUND_COLOR);
       HBox bottomButtonGroup = new HBox(UIProperties.BOX_SPACING);
       bottomButtonGroup.setAlignment(Pos.CENTER);

       // Create items to display in the Pane
       TextField widthField = new TextField(Integer.toString(gameSettings.getWidth()));
       TextField heightField = new TextField(Integer.toString(gameSettings.getHeight()));
       TextField minesField = new TextField(Integer.toString(gameSettings.getNumMines()));
       Label widthLabel = new Label("Width:  " + gameSettings.getValidator().getMinMaxGridText());
       widthLabel.setTextFill(UIProperties.SETTINGS_TEXT_COLOR);
       Label heightLabel = new Label("Height:  " + gameSettings.getValidator().getMinMaxGridText());
       heightLabel.setTextFill(UIProperties.SETTINGS_TEXT_COLOR);
       minesLimitLabel = new Label(gameSettings.getValidator().getMinesLimitText());
       minesLimitLabel.setTextFill(UIProperties.SETTINGS_TEXT_COLOR);
       userMessageLabel = new Label("Game Settings.");
       userMessageLabel.setWrapText(true);
       userMessageLabel.setTextFill(UIProperties.USER_MESSAGE_NEUTRAL_COLOR);

       // Apply settings button
       Button applySettingsButton = new Button("Apply Settings");
       applySettingsButton.setOnAction(x -> {
           try {
               int newHeight = Integer.parseInt(heightField.getText());
               int newWidth = Integer.parseInt(widthField.getText());
               int newMines = Integer.parseInt(minesField.getText());
               if (gameSettings.getValidator().validate(newHeight, newWidth, newMines)) {
                   gameSettings.updateSettings(newHeight, newWidth, newMines);
                   userMessageLabel.setText("Game settings updated successfully!");
                   userMessageLabel.setMaxWidth(UIProperties.USER_MESSAGE_GOOD_WIDTH);
                   userMessageLabel.setTextFill(UIProperties.USER_MESSAGE_GOOD_COLOR);
                   gameSettings.getSettingsStage().sizeToScene();
                   minesLimitLabel.setText(gameSettings.getValidator().getMinesLimitText());
               } else {
                   userMessageLabel.setText(gameSettings.getUserErrorMessageText(newHeight, newWidth));
                   userMessageLabel.setMaxWidth(UIProperties.USER_MESSAGE_EVIL_WIDTH);
                   userMessageLabel.setTextFill(UIProperties.USER_MESSAGE_EVIL_COLOR);
                   gameSettings.getSettingsStage().sizeToScene();
                   System.out.println(gameSettings.getUserErrorMessageText(newHeight, newWidth));
               }
           } catch (NumberFormatException e) {
               userMessageLabel.setText("Bruh... actual numbers, please.");
               userMessageLabel.setMaxWidth(UIProperties.USER_MESSAGE_EVIL_WIDTH);
               userMessageLabel.setTextFill(UIProperties.USER_MESSAGE_EVIL_COLOR);
               gameSettings.getSettingsStage().sizeToScene();
               System.out.println("\nPlease enter actual numbers! gosh...");
           }
       });
       // Close button
       Button closeButton = new Button("Close");
       closeButton.setOnAction(e -> {
           if (gameSettings.getSettingsStage() != null) {
               gameSettings.getSettingsStage().close();
           }
       });

       // Add bottom buttons to HBox group
       bottomButtonGroup.getChildren().addAll(applySettingsButton, closeButton);

       // Place all items into the VBox Pane
       settingsPane.getChildren().addAll(
               heightLabel, heightField,
               widthLabel, widthField,
               minesLimitLabel, minesField,
               bottomButtonGroup, userMessageLabel);
       return settingsPane;
   }
}
