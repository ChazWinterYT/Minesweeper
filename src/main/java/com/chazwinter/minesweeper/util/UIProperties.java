package com.chazwinter.minesweeper.util;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Map;

/**
 * This class contains various UI constants that are used in the rest of the app.
 * To change UI parameters, you should change them in this class.
 */
public class UIProperties {
    // General UI properties that apply to multiple places
    public static final String BACKGROUND_COLOR = "-fx-background-color: #111111; ";
    public static final int PADDING = 4;
    public static final Insets INSETS = new Insets(PADDING);
    public static final int BOX_SPACING = 10;

    // Game grid cell graphics
    public static final int CELL_SIZE = 20;
    private static final String MULTI_IMAGE_PATH = "/com/chazwinter/minesweeper/image/";
    public static final Image CLICKED_CELL_IMG = new Image(
            UIProperties.class.getResourceAsStream(MULTI_IMAGE_PATH + "clicked-cell.gif"));
    public static final Image CLICKED_BOMB_IMG = new Image(
            UIProperties.class.getResourceAsStream(MULTI_IMAGE_PATH + "clicked-bomb-cell.gif"));
    public static final Image REVEALED_BOMB_IMG = new Image(
            UIProperties.class.getResourceAsStream(MULTI_IMAGE_PATH + "chaz48.png"));
    public static final Image FLAGGED_CELL_IMG = new Image(
            UIProperties.class.getResourceAsStream(MULTI_IMAGE_PATH + "flagged-cell.gif"));
    public static final Image NO_CELL_IMG = new Image(
            UIProperties.class.getResourceAsStream(MULTI_IMAGE_PATH + "default-cell.gif"));
    public static final String CLICKED_CELL_BG_COLOR = "-fx-background-color: #c0c0c0; ";
    public static final String CELL_BORDER_STYLE = "-fx-border-color: #888888; -fx-border-width: 0.75; ";

    // Game grid text
    public static final int CELL_FONT_SIZE = 10;
    public static final Font CELL_FONT = Font.loadFont(UIProperties.class.getResourceAsStream(
            "/com/chazwinter/minesweeper/font/mine-sweeper.ttf"), CELL_FONT_SIZE);
    public static final Color CELL_TEXT_STROKE_COLOR = Color.BLACK;
    public static final double CELL_TEXT_STROKE_WIDTH = 0.3;
    public static final Map<Integer, Color> COLOR_MAP = Map.of(
            1, Color.BLUE,
            2, Color.MAGENTA,
            3, Color.PURPLE,
            4, Color.RED,
            5, Color.GREEN,
            6, Color.AQUA,
            7, Color.YELLOW,
            8, Color.MAROON,
            9, Color.ANTIQUEWHITE,      // Impossible value
            0, Color.ANTIQUEWHITE   // Test value - should not display
    );

    // Mine Counter and Timer on main pane
    public static final int COUNTER_FONT_SIZE = 40;
    public static final Font COUNTER_FONT = Font.loadFont(UIProperties.class.getResourceAsStream(
            "/com/chazwinter/minesweeper/font/neon_pixel-7.ttf"), COUNTER_FONT_SIZE);
    public static final Color COUNTER_COLOR = Color.MAGENTA;

    // Settings menu
    public static final Color SETTINGS_TEXT_COLOR = Color.ANTIQUEWHITE;
    public static final Color USER_MESSAGE_NEUTRAL_COLOR = Color.CADETBLUE;
    public static final Color USER_MESSAGE_GOOD_COLOR = Color.GREEN;
    public static final Color USER_MESSAGE_EVIL_COLOR = Color.RED;
    public static final int USER_MESSAGE_GOOD_WIDTH = 200;
    public static final int USER_MESSAGE_EVIL_WIDTH = 200;
    public static final String USER_MESSAGE_DEFAULT = "Pro Tip: If you find and right-click all the mines around " +
            "a cell, you can left+right click (or middle click) to clear all surrounding cells.";
    public static final int USER_MESSAGE_DEFAULT_WIDTH = 200;
}
