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

    public MouseEventHandler(Minesweeper minesweeper, MinesweeperUIBuilder uiBuilder) {
        this.minesweeper = minesweeper;
        this.uiBuilder = uiBuilder;
    }

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

    private void checkBothButtonsDown(Button cellButton) {
        if (leftButtonDown && rightButtonDown) {
            handleBothMouseButtonsAction(cellButton);
        }
    }

    private void handleLeftMouseButtonAction(Button cellButton) {
        minesweeper.processCellLeftClick(cellButton);
    }

    private void handleRightMouseButtonAction(Button cellButton) {
        minesweeper.processCellRightClick(cellButton);
    }

    private void handleBothMouseButtonsAction(Button cellButton) {
        minesweeper.processCellMiddleClick(cellButton);
    }
}
