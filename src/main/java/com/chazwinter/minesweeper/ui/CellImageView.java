package com.chazwinter.minesweeper.ui;

import com.chazwinter.minesweeper.util.UIProperties;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CellImageView extends ImageView {
    private static final double FIT_HEIGHT = UIProperties.CELL_SIZE;
    private static final double FIT_WIDTH = UIProperties.CELL_SIZE;
    private static final boolean PRESERVE_RATIO = true;

    /**
     * Constructor for adding an image to a cell on the game grid.
     * @param image The image that goes on that cell.
     */
    public CellImageView(Image image) {
        super(image);
        setFitHeight(FIT_HEIGHT);
        setFitWidth(FIT_WIDTH);
        setPreserveRatio(PRESERVE_RATIO);
    }
}
