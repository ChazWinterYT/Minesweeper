package com.chazwinter.minesweeper.settings;

import com.chazwinter.minesweeper.ui.SettingsUIBuilder;
import com.chazwinter.minesweeper.util.UIProperties;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameSettings {
    private int height = 10;    // Default setting on game start
    private int width = 10;     // Default setting on game start
    private int numMines = 15;  // Default setting on game start

    private final GameSettingsValidator validator;
    private final Runnable onSettingsApplied;
    private Stage settingsStage;

    /**
     * Constructor to initialize game settings.
     * @param settingsStage The stage that will display the game settings
     * @param onSettingsApplied Runnable to reset the game on first launch, or when new settings are accepted.
     */
    public GameSettings(Stage settingsStage, Runnable onSettingsApplied) {
        this.settingsStage = settingsStage;
        this.onSettingsApplied = onSettingsApplied;
        this.validator = new GameSettingsValidator(height, width, numMines);
    }



    /**
     * Build the Settings UI window Pane for display on screen.
     * @return The Scene containing the display Pane.
     */
    public Scene getSettingsScene() {
        SettingsUIBuilder uiBuilder = new SettingsUIBuilder(this);
        Pane settingsPane = uiBuilder.buildSettingsPane();
        return new Scene(settingsPane);
    }

    /**
     * Update game settings when valid settings are entered. This will
     * also start a new game.
     * @param newHeight The new grid height.
     * @param newWidth The new grid width.
     * @param newMines The new number of mines in the grid.
     */
    public void updateSettings(int newHeight, int newWidth, int newMines) {
        setHeight(newHeight);
        setWidth(newWidth);
        setNumMines(newMines);
        validator.updateSettings(newHeight, newWidth, newMines);
        onSettingsApplied.run();
    }

    /**
     * Text to display to the user (via UI) or developer (via console)
     * when candidate game settings are invalid.
     * @param height The candidate height, needed to calculate the valid number of mines.
     * @param width The candidate width, needed to calculate the valid number of mines.
     * @return the text to display.
     */
    public String getUserErrorMessageText(int height, int width) {
        String message = String.format("Game settings not within valid range!\n" +
                "Height and Width must be between %d and %d.\n" +
                "Assuming your new grid size is valid, Mines must be between %d and %d.",
                validator.getMinGridSize(), validator.getMaxGridSize(),
                validator.calcMinMines(height, width, "min"),
                validator.calcMinMines(height, width, "max"));
        return message;
    }

    /**
     * Calculate the minimum window size needed to display the game with current settings.
     * @return An array containing the minimum {width, height} required for the game window.
     */
    public double[] calculateMinGameDimensions() {
        double topButtonGroupWidth = 350;
        double topButtonGroupHeight = 92;
        double minWidth = Math.max(
                width * UIProperties.CELL_SIZE + UIProperties.BOX_SPACING + UIProperties.PADDING * 4,
                topButtonGroupWidth);
        double minHeight = height * UIProperties.CELL_SIZE + topButtonGroupHeight;
        return new double[] {minWidth, minHeight};
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getNumMines() {
        return numMines;
    }

    public String getNumMinesAsString() {
        return String.format("%03d", numMines);
    }

    public void setNumMines(int numMines) {
        this.numMines = numMines;
    }

    public GameSettingsValidator getValidator() {
        return validator;
    }

    public Stage getSettingsStage() {
        return settingsStage;
    }

    public void setSettingsStage(Stage settingsStage) {
        this.settingsStage = settingsStage;
    }
}
