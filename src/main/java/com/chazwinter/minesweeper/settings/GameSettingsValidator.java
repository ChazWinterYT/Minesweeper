package com.chazwinter.minesweeper.settings;

import java.util.function.Function;
import java.util.function.Predicate;

public class GameSettingsValidator {
    private int height;
    private int width;
    private int mines;

    private final int minGridSize = 5;
    private final int maxGridSize = 30;
    private final int minMinesPercentage = 5;
    private final int maxMinesPercentage = 25;
    private final String minMaxGridText = String.format("(min: %d  max %d)", minGridSize, maxGridSize);
    private final Function<Integer, Integer> minMaxMinesLimit = x -> height * width * x / 100;
    private final Predicate<Integer> minMaxMinesLimitTest = x ->
            x >= minMaxMinesLimit.apply(minMinesPercentage) && x <= minMaxMinesLimit.apply(maxMinesPercentage);

    /**
     * Constructor to accept initial values for the Validator.
     * @param initialHeight Default height on game launch.
     * @param initialWidth Default width on game launch.
     * @param initialNumMines Default width on game launch.
     */
    public GameSettingsValidator(int initialHeight, int initialWidth, int initialNumMines) {
        this.height = initialHeight;
        this.width = initialWidth;
        this.mines = initialNumMines;
    }

    /**
     * Update game parameters after new settings are validated, so
     * validation text displays properly in menus and new valid ranges are
     * calculated correctly.
     * @param newHeight New validated height from updated game settings.
     * @param newWidth New validated width from updated game settings.
     * @param newMines New validated mine count from updated game settings.
     */
    public void updateSettings(int newHeight, int newWidth, int newMines) {
        this.height = newHeight;
        this.width = newWidth;
        this.mines = newMines;
    }

    /**
     * Calculate the valid mine range before sending it to the UI.
     * @return String containing UI text and the valid calculated min/max mine count.
     */
    public String getMinesLimitText() {
        return String.format("Mines:   (min: %d  max: %d)",
                minMaxMinesLimit.apply(minMinesPercentage),  minMaxMinesLimit.apply(maxMinesPercentage));
    }

    /**
     * Determine if new user-entered game settings are valid before applying them to a new game.
     * @param height New candidate height from game settings.
     * @param width New candidate height from game settings.
     * @param mines New candidate mine count from game settings.
     * @return true if the candidate values are within valid game parameters.
     */
    public boolean validate(int height, int width, int mines) {
        boolean dimensionsCorrect = height >= minGridSize && height <= maxGridSize
                && width >= minGridSize && width <= maxGridSize;
        /* numMinesCorrect must be calculated from candidate values (not the existing ones),
            or else it will cause broken mine count settings to be validated. */
        boolean numMinesCorrect = mines >= height * width * minMinesPercentage / 100
                && mines <= height * width * maxMinesPercentage / 100;
        return dimensionsCorrect && numMinesCorrect;
    }

    /**
     * Show the calculation for min/max mine count based on candidate values,
     * so the user knows what values are valid for the new settings.
     * @param height Candidate height to be used for the mine count calculation.
     * @param width Candidate width to be used for the mine count calculation.
     * @param minOrMax Whether to display the min or max mine count.
     * @return the number of mines which would be considered valid for these candidate grid dimensions.
     */
    public int calcMinMines(int height, int width, String minOrMax) {
        if (minOrMax.equals("min")) {
            return height * width * minMinesPercentage / 100;
        } else {
            return height * width * maxMinesPercentage / 100;
        }
    }

    public int getMinGridSize() {
        return minGridSize;
    }

    public int getMaxGridSize() {
        return maxGridSize;
    }

    public String getMinMaxGridText() {
        return minMaxGridText;
    }
}
