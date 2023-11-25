package com.chazwinter.minesweeper.model;

import javafx.scene.control.Button;

public class Cell {
    private int row;
    private int col;
    private Button cellButton;
    private boolean isBomb;
    private boolean isRevealed;
    private boolean isFlagged;
    private int neighborMines;

    public Cell(int row, int col, Button cellButton) {
        this.row = row;
        this.col = col;
        this.cellButton = cellButton;
        this.isBomb = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.neighborMines = 0;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Button getCellButton() {
        return cellButton;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setIsBomb(boolean mine) {
        isBomb = mine;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setIsRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getNeighborMines() {
        return neighborMines;
    }

    public void setNeighborMines(int neighborMines) {
        this.neighborMines = neighborMines;
    }
}
