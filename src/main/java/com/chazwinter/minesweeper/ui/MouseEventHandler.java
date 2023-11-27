package com.chazwinter.minesweeper.ui;

import com.chazwinter.minesweeper.model.Minesweeper;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MouseEventHandler {
    private final Minesweeper minesweeper;
    private final MinesweeperUIBuilder uiBuilder;
    private boolean leftButtonDown = false;
    private boolean rightButtonDown = false;

    /**
     * Constructor for handling a mouse event during a game of Minesweeper.
     * @param minesweeper The game of Minesweeper that requires mouse interaction.
     * @param uiBuilder The UI Builder that will update the display when the mouse has been clicked.
     */
    public MouseEventHandler(Minesweeper minesweeper, MinesweeperUIBuilder uiBuilder) {
        this.minesweeper = minesweeper;
        this.uiBuilder = uiBuilder;
    }

    /**
     * Initially handle a mouse event by determining if it was a left click,
     * right click, or middle click.
     * @param event
     * @param cellButton
     */
    public void handleMousePressed(MouseEvent event, Button cellButton) {
        if (event.isPrimaryButtonDown()) {
            leftButtonDown = true;
        }
        if (event.isSecondaryButtonDown()) {
            rightButtonDown = true;
        }
        if (event.isMiddleButtonDown()) {
            handleBothMouseButtonsAction(cellButton);
        } else {
            checkBothButtonsDown(cellButton);
        }
    }

    /**
     * Handle which action should take place when a mouse button is released.
     * Releasing the middle mouse button performs the same action as left
     * and right-clicking at the same time.
     * @param event The mouse event received.
     * @param cellButton The button on the game grid that was pressed with the mouse.
     */
    public void handleMouseReleased (MouseEvent event, Button cellButton) {
        if (cellButton.isHover()) {
            if (event.getButton() == MouseButton.PRIMARY && leftButtonDown && !rightButtonDown) {
                handleLeftMouseButtonAction(cellButton);
            } else if (event.getButton() == MouseButton.SECONDARY && rightButtonDown && !leftButtonDown) {
                handleRightMouseButtonAction(cellButton);
            } else if (event.getButton() == MouseButton.MIDDLE) {
                handleBothMouseButtonsAction(cellButton);
            }
        }

        if (event.getButton() == MouseButton.PRIMARY) {
            leftButtonDown = false;
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            rightButtonDown = false;
        }
    }

    /**
     * Helper method to determine if the user has pressed the left and
     * right mouse buttons at the same time, so we can take the correct action.
     * @param cellButton The button on the game grid that was pressed with the mouse.
     */
    private void checkBothButtonsDown(Button cellButton) {
        if (leftButtonDown && rightButtonDown) {
            handleBothMouseButtonsAction(cellButton);
        }
    }

    /**
     * Helper method to take appropriate action when a user left clicks the game grid.
     * @param cellButton The button on the game grid that was pressed with the mouse.
     */
    private void handleLeftMouseButtonAction(Button cellButton) {
        minesweeper.processCellLeftClick(cellButton);
    }

    /**
     * Helper method to take appropriate action when a user right clicks the game grid.
     * @param cellButton The button on the game grid that was pressed with the mouse.
     */
    private void handleRightMouseButtonAction(Button cellButton) {
        minesweeper.processCellRightClick(cellButton);
    }

    /**
     * Helper method to take appropriate action when a user middle clicks,
     * or left+right clicks, the game grid.
     * @param cellButton The button on the game grid that was pressed with the mouse.
     */
    private void handleBothMouseButtonsAction(Button cellButton) {
        minesweeper.processCellMiddleClick(cellButton);
    }
}
