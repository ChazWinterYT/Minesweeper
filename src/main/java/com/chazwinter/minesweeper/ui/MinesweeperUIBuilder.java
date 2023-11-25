package com.chazwinter.minesweeper.ui;

import com.chazwinter.minesweeper.util.UIProperties;
import com.chazwinter.minesweeper.model.Cell;
import com.chazwinter.minesweeper.model.Minesweeper;
import com.chazwinter.minesweeper.settings.GameSettings;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class MinesweeperUIBuilder {
    private Minesweeper minesweeper;
    private MouseEventHandler mouseEventHandler;
    private GameSettings gameSettings;
    private GridPane buttonGrid;
    private Stage primaryStage;
    private Stage settingsStage;

    private Label mineCounter = new Label();
    private Label timerLabel = new Label();
    private Timer timer;
    private int seconds;
    private boolean timerStarted;
    private boolean gameOver;

    /**
     * Constructor for initializing the UI Builder.
     * A new Minesweeper and MouseEventHandler are created when the
     * resetGame() method is called, which happens at program launch and
     * when starting a new game.
     * @param primaryStage
     * @param gameSettings
     */
    public MinesweeperUIBuilder(Stage primaryStage, GameSettings gameSettings) {
        this.primaryStage = primaryStage;
        this.gameSettings = gameSettings;
    }

    public void updateMineCounter(int numMines) {
        mineCounter.setText(String.format("%03d", numMines));
    }

    public void setGameOver() {
        gameOver = true;
    }

    /**
     * Build the Scene containing the main game layout.
     * @return The Scene to be displayed.
     */
    public Scene buildScene() {
        HBox topButtonGroup = new HBox(UIProperties.BOX_SPACING);
        topButtonGroup.setPadding(UIProperties.INSETS);
        topButtonGroup.setAlignment(Pos.CENTER);

        // Build the button grid
        buttonGrid = new GridPane();
        buttonGrid.setPadding(UIProperties.INSETS);
        resetGame();

        // Create mine counter and timer
        mineCounter = new Label(gameSettings.getNumMinesAsString());
        timerLabel = new Label("000");
        mineCounter.setFont(UIProperties.COUNTER_FONT);
        timerLabel.setFont(UIProperties.COUNTER_FONT);
        mineCounter.setTextFill(UIProperties.COUNTER_COLOR);
        timerLabel.setTextFill(UIProperties.COUNTER_COLOR);
        HBox.setHgrow(mineCounter, Priority.ALWAYS);
        HBox.setHgrow(timerLabel, Priority.ALWAYS);

        // Create Settings, New Game, and Exit buttons, and group them
        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(x -> {
            settingsStage = new Stage();
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.setTitle("Settings");
            gameSettings.setSettingsStage(settingsStage);
            settingsStage.setScene(gameSettings.getSettingsScene());
            settingsStage.show();
        });
        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> resetGame());
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> Platform.exit());

        // Add all top row elements to the top row group
        topButtonGroup.getChildren().addAll(mineCounter, settingsButton, newGameButton, exitButton, timerLabel);

        /* Create VBox and HBox wrappers to center the grid.
           Put the VBox in the HBox, and put the HBox in the mainLayout. */
        VBox vBoxWrapper = new VBox(buttonGrid);
        HBox hBoxWrapper = new HBox(vBoxWrapper);
        vBoxWrapper.setAlignment(Pos.CENTER);
        hBoxWrapper.setAlignment(Pos.CENTER);

        // Build the window to hold the grid and buttons
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle(UIProperties.BACKGROUND_COLOR);
        mainLayout.setTop(topButtonGroup);
        mainLayout.setCenter(hBoxWrapper);

        return new Scene(mainLayout);
    }

    /**
     * Start a new game and rebuild the grid using current game settings.
     */
    public void resetGame() {
        resetTimer();
        gameOver = false;
        int height = gameSettings.getHeight();
        int width = gameSettings.getWidth();
        int numMines = gameSettings.getNumMines();
        minesweeper = new Minesweeper(height, width, numMines, this);
        mouseEventHandler = new MouseEventHandler(minesweeper, this);
        mineCounter.setText(gameSettings.getNumMinesAsString());
        timerLabel.setText("000");
        buttonGrid.getChildren().clear();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Button cellButton = createCellButton();
                buttonGrid.add(cellButton, col, row);
                minesweeper.registerCellButton(cellButton, new Cell(row, col, cellButton));
            }
        }
        minesweeper.initializeBoard();

        primaryStage.setMinWidth(gameSettings.calculateMinGameDimensions()[0]);
        primaryStage.setMinHeight(gameSettings.calculateMinGameDimensions()[1] + 12);
        primaryStage.setWidth(gameSettings.calculateMinGameDimensions()[0]);
        primaryStage.setHeight(gameSettings.calculateMinGameDimensions()[1] + 12);
        System.out.println("A new game has been started.");
    }

    private Button createCellButton() {
        Button cellButton = new Button();
        cellButton.setMinSize(UIProperties.CELL_SIZE, UIProperties.CELL_SIZE);
        cellButton.setMaxSize(UIProperties.CELL_SIZE, UIProperties.CELL_SIZE);
        cellButton.setPrefSize(UIProperties.CELL_SIZE, UIProperties.CELL_SIZE);
        cellButton.setGraphic(buildStackPaneFromLeftClickOrDefault(cellButton, null));

        cellButton.addEventHandler(MouseEvent.MOUSE_PRESSED,
                e -> mouseEventHandler.handleMousePressed(e, cellButton));
        cellButton.addEventHandler(MouseEvent.MOUSE_RELEASED,
                e -> mouseEventHandler.handleMouseReleased(e, cellButton));
        return cellButton;
    }

    public void updateCellAppearance(Button cellButton, Cell cell, boolean isLeftClick) {
        // The timer starts when the player first interacts with the game board.
        if (!timerStarted) {
            startTimer();
            timerStarted = true;
        }
        StackPane stackPane;
        if (isLeftClick) {
            stackPane = buildStackPaneFromLeftClickOrDefault(cellButton, cell);
        } else {    // You right-clicked.
            stackPane = buildStackPaneFromRightClick(cellButton, cell);
        }
        cellButton.setGraphic(stackPane);
    }

    private StackPane buildStackPaneFromLeftClickOrDefault(Button cellButton, Cell cell) {
        StackPane stackPane = new StackPane();
        stackPane.setStyle(UIProperties.CELL_BORDER_STYLE);
        ImageView imageView;
        Text cellText = new Text("");
        cellText.setFont(UIProperties.CELL_FONT);
        cellText.setStroke(UIProperties.CELL_TEXT_STROKE_COLOR);
        cellText.setStrokeWidth(UIProperties.CELL_TEXT_STROKE_WIDTH);
        if (cell == null) { // Initial board setup
            imageView = new CellImageView(UIProperties.NO_CELL_IMG);
            stackPane.getChildren().addAll(imageView);
            return stackPane;
        }
        if (cell.isBomb() && cell.isRevealed()) { // Endgame only - show which cell you screwed up on.
            imageView = new CellImageView(UIProperties.CLICKED_BOMB_IMG);
        } else if (cell.isBomb() && !cell.isRevealed()) { // Endgame only - show where the bombs were.
            imageView = new CellImageView(UIProperties.REVEALED_BOMB_IMG);
        } else {    // Cell is not a Bomb.
            imageView = new CellImageView(UIProperties.NO_CELL_IMG);
            stackPane.setStyle(UIProperties.CLICKED_CELL_BG_COLOR + UIProperties.CELL_BORDER_STYLE);
            if (cell.getNeighborMines() != 0) {
                cellText.setText(String.valueOf(cell.getNeighborMines()));
                cellText.setFill(UIProperties.COLOR_MAP.get(cell.getNeighborMines()));
            }
        }
        stackPane.getChildren().addAll(imageView, cellText);
        return stackPane;
    }

    private StackPane buildStackPaneFromRightClick(Button cellButton, Cell cell) {
        StackPane stackPane = new StackPane();
        stackPane.setStyle(UIProperties.CELL_BORDER_STYLE);
        ImageView imageView;
        if (cell.isFlagged()) {
            imageView = new CellImageView(UIProperties.FLAGGED_CELL_IMG);
        } else {
            imageView = new CellImageView(UIProperties.NO_CELL_IMG);
        }
        stackPane.getChildren().addAll(imageView);
        return stackPane;
    }

    private void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (seconds == 999 || gameOver) {
                    // Stop incrementing the timer
                    timer.cancel();
                    timer.purge();
                } else {
                    seconds++;
                }
                Platform.runLater(() -> updateTimerDisplay(seconds));
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public void resetTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timerStarted = false;
        seconds = 0;
    }

    public void updateTimerDisplay(int seconds) {
        timerLabel.setText(String.format("%03d", seconds));
    }
}
