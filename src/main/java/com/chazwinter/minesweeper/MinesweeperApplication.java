package com.chazwinter.minesweeper;

import com.chazwinter.minesweeper.ui.MinesweeperUIBuilder;
import com.chazwinter.minesweeper.settings.GameSettings;
import com.chazwinter.minesweeper.util.SoundManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MinesweeperApplication extends Application {
    private GameSettings gameSettings;
    private Stage settingsStage = new Stage();
    private MinesweeperUIBuilder uiBuilder;

    /**
     * Start the Minesweeper Application. Runs automatically when app is launched.
     * @param primaryStage The primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        this.gameSettings = new GameSettings(settingsStage, this::applyNewSettings);
        uiBuilder = new MinesweeperUIBuilder(primaryStage, gameSettings);
        SoundManager.loadSounds();

        // Display everything on screen
        Scene scene = uiBuilder.buildScene();
        primaryStage.setTitle("Chazsweeper!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void applyNewSettings() {
        System.out.printf("Applying new settings: [H: %d, W: %d, M: %d]\n",
                gameSettings.getHeight(), gameSettings.getWidth(), gameSettings.getNumMines());
        uiBuilder.resetGame();
    }



    public static void main(String[] args) {
        launch(args);
    }
}