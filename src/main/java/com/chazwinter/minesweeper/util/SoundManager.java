package com.chazwinter.minesweeper.util;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SoundManager {
    private static List<MediaPlayer> loserSounds;
    private static List<MediaPlayer> winnerSounds;
    private static final int LOSER_SOUNDS_LIST_SIZE = 15;
    private static final int WINNER_SOUNDS_LIST_SIZE = 13;
    private static final String MULTI_SOUND_PATH = "/com/chazwinter/minesweeper/sound/";
    private static Random random;

    /**
     * Method to dynamically load all sounds when the app first launches.
     * The List Sizes must be updated manually above, based on how many sounds there are.
     */
    public static void loadSounds() {
        loserSounds = new ArrayList<>();
        for (int i = 0; i < LOSER_SOUNDS_LIST_SIZE; i++) {
            String resourcePath = buildFileName(i, "loser");
            Media sound = new Media(SoundManager.class.getResource(resourcePath).toExternalForm());
            loserSounds.add(new MediaPlayer(sound));
        }
        winnerSounds = new ArrayList<>();
        for (int i = 0; i < WINNER_SOUNDS_LIST_SIZE; i++) {
            String resourcePath = buildFileName(i, "winner");
            Media sound = new Media(SoundManager.class.getResource(resourcePath).toExternalForm());
            winnerSounds.add(new MediaPlayer(sound));
        }
    }

    private static String buildFileName(int i, String loserOrWinner) {
        return MULTI_SOUND_PATH + loserOrWinner + i + ".mp3";
    }

    public static void playLoserSound() {
        random = new Random();
        int index = random.nextInt(loserSounds.size());
        MediaPlayer player = loserSounds.get(index);
        player.stop();
        player.play();
    }

    public static void playWinnerSound(){
        random = new Random();
        int index = random.nextInt(winnerSounds.size());
        MediaPlayer player = winnerSounds.get(index);
        player.stop();
        player.play();
    }
}
