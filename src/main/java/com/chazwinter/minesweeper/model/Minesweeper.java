package com.chazwinter.minesweeper.model;

import com.chazwinter.minesweeper.ui.MinesweeperUIBuilder;
import com.chazwinter.minesweeper.util.SoundManager;
import javafx.scene.control.Button;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Minesweeper {
    private final MinesweeperUIBuilder uiBuilder;
    private final int width;
    private final int height;
    private int numBombs;
    private int numReveals;
    private boolean youWon;
    private final Cell[][] cellGrid;
    private final Map<Button, Cell> buttonCellMap = new HashMap<>();
    private boolean weAreDebugging = false;

    /* Two arrays to represent where a neighbor cell is relative to the current one.
        For example, the top-left cell is row-1 and col-1. */
    int[] dRow = new int[] {-1, -1, -1,
                             0,      0,
                             1,  1,  1};
    int[] dCol = new int[] {-1,  0,  1,
                            -1,      1,
                            -1,  0,  1};

    /**
     * Constructor for starting a new game of Minesweeper.
     * @param height The height of the game grid.
     * @param width The width of the game grid.
     * @param numBombs The number of bombs within the grid.
     * @param uiBuilder updates the appearance of the game grid as you play.
     */
    public Minesweeper(int height, int width, int numBombs, MinesweeperUIBuilder uiBuilder) {
        this.height = height;
        this.width = width;
        this.numBombs = numBombs;
        this.numReveals = width * height - numBombs;
        this.uiBuilder = uiBuilder;
        this.cellGrid = new Cell[height][width];
    }

    /**
     * Sets the starting parameters of the new game board.
     */
    public void initializeBoard() {
        randomizeAndPlaceBombs();
        calculateNeighbors();
        showAllBombsForDebugging(weAreDebugging);
    }

    /**
     * Randomly place bombs on the new game board.
     */
    public void randomizeAndPlaceBombs() {
        int totalCells = width * height;
        /* Obtain a Set of random numbers == the number of expected mines
            It is done in such a way that the four corners can never
            contain a bomb. This is intentional. */
        Set<Integer> randomNums = new HashSet<>();
        while (randomNums.size() < numBombs) {
            int randomNum = (int) (Math.random() * totalCells);
            if (!isACornerCell(randomNum)) {
                randomNums.add(randomNum);
            }
        }
        for (int num : randomNums) {
            int row = num / width;
            int col = num % width;
            Cell cellToPlaceBomb = cellGrid[row][col];
            cellToPlaceBomb.setIsBomb(true);
        }
    }

    private boolean isACornerCell(int cellNumber) {
        int row = cellNumber / width;
        int col = cellNumber % width;
        boolean rowIsOnEdge = row == 0 || row == height - 1;
        boolean colIsOnEdge = col == 0 || col == width - 1;
        return rowIsOnEdge && colIsOnEdge;
    }

    /**
     * Private method used to determine the number of bombs surrounding each Cell.
     */
    private void calculateNeighbors() {
        int numNeighbors = 0;
        // For each cell, check each neighbor, and increment count if it's a bomb.
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                for (int i = 0; i < dRow.length; i++) {
                    int newRow = row + dRow[i];
                    int newCol = col + dCol[i];
                    if (isInBounds(newRow, newCol) && cellGrid[newRow][newCol].isBomb()) {
                        numNeighbors++;
                    }
                }
                cellGrid[row][col].setNeighborMines(numNeighbors);
                numNeighbors = 0;
            }
        }
    }

    private void showAllBombsForDebugging(boolean weAreDebugging) {
        if (weAreDebugging) {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    Cell cell = cellGrid[row][col];
                    if (cell.isBomb()) {
                        uiBuilder.updateCellAppearance(cell.getCellButton(), cell, true);
                    }
                }
            }
        }
    }

    /**
     * Change the game state and UI after you click on a Cell Button.
     * @param cellButton The Button you clicked on the game board.
     */
    public void processCellLeftClick(Button cellButton) {
        Cell cell = buttonCellMap.get(cellButton);
        /* Don't try to reveal flagged cells, or cells that are already revealed. */
        if (cell.isRevealed() || cell.isFlagged()) {
            return;
        }
        boolean didWeFloodFill = false;
        if (cell.getNeighborMines() == 0) {
            didWeFloodFill = true;
            floodFill(cell);
        }
        cell.setIsRevealed(true);
        // Reveals are also handled by Flood Fill, so don't count them twice.
        if (!didWeFloodFill) {
            numReveals--;
        }
        uiBuilder.updateCellAppearance(cellButton, cell, true);
        if (cell.isBomb()) {
            youWon = false;
            initializeGameOverState(youWon);
        }
        System.out.println("Number of reveals remaining: " + numReveals);
        if (numReveals == 0) {
            youWon = true;
            initializeGameOverState(youWon);
        }
    }

    public void processCellRightClick(Button cellButton) {
        Cell cell = buttonCellMap.get(cellButton);
        // Right-clicking on a revealed cell, or when you used all your bombs, should do nothing.
        if (cell.isRevealed() || numBombs == 0) {
            return;
        }
        if (cell.isFlagged()) { // Cell was already flagged; unflag it.
            cell.setIsFlagged(false);
            numBombs++;
        } else { // Newly flagged cell.
            cell.setIsFlagged(true);
            numBombs--;
        }
        uiBuilder.updateMineCounter(numBombs);
        uiBuilder.updateCellAppearance(cellButton, cell, false);
    }

    public void processCellMiddleClick(Button cellButton) {
        Cell cell = buttonCellMap.get(cellButton);
        if (!cell.isRevealed()) {
            return;
        }
        int flaggedNeighbors = countFlaggedNeighbors(cell);
        if (cell.getNeighborMines() ==  flaggedNeighbors) {
            for (int i = 0; i < dRow.length; i++) {
                int newRow = cell.getRow() + dRow[i];
                int newCol = cell.getCol() + dCol[i];
                if (isInBounds(newRow, newCol)) {
                    processCellLeftClick(cellGrid[newRow][newCol].getCellButton());
                }
            }
        }
    }

    private void floodFill(Cell cell) {
        // Base case: Don't process this cell if it was already processed.
        if (cell.isRevealed()) {
            return;
        }
        cell.setIsRevealed(true);
        numReveals--;
        Button button = cell.getCellButton();
        uiBuilder.updateCellAppearance(button, cell, true);

        // Recursive case: If the current cell has no mines as neighbors, flood fill its neighbors.
        if (cell.getNeighborMines() == 0) {
            for (int i = 0; i < dRow.length; i++) {
                int newRow = cell.getRow() + dRow[i];
                int newCol = cell.getCol() + dCol[i];
                if (isInBounds(newRow, newCol)) {
                    Cell cellToFill = cellGrid[newRow][newCol];
                    floodFill(cellToFill);
                }
            }
        }
    }

    private int countFlaggedNeighbors(Cell cell) {
        int count = 0;
        for (int i = 0; i < dRow.length; i++) {
            int newRow = cell.getRow() + dRow[i];
            int newCol = cell.getCol() + dCol[i];
            if (isInBounds(newRow, newCol) && cellGrid[newRow][newCol].isFlagged()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Helper method to determine if a cell is in bounds before taking action on it.
     * @param row The row of the cell to check.
     * @param col the column of the cell the check.
     * @return true if the cell is in bounds, false if it is not.
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < height
                && col >= 0 && col < width;
    }

    /**
     * Register each Button and Cell into the game grid, so they can be checked as you play.
     * @param cellButton The Button to be registered in the game grid.
     * @param cell The Cell to be registered in the game grid.
     */
    public void registerCellButton(Button cellButton, Cell cell) {
        buttonCellMap.put(cellButton, cell);
        cellGrid[cell.getRow()][cell.getCol()] = cell;
    }

    /**
     * Pain and suffering (aka you clicked a mine). Reveal the entire board
     * and remind you which cell you screwed up at.
     */
    private void initializeGameOverState(boolean youWon) {
        if (youWon) {
            // Show the good ending
            SoundManager.playWinnerSound();
        } else {
            // Show the bad ending
            SoundManager.playLoserSound();
        }
        uiBuilder.setGameOver();
        numReveals = -1;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = cellGrid[row][col];
                if (!cell.isRevealed()) {
                    Button cellButton = cell.getCellButton();
                    uiBuilder.updateCellAppearance(cellButton, cell, true);
                    cell.setIsRevealed(true);
                }
            }
        }
    }
}
