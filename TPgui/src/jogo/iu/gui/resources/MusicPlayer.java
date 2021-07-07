package jogo.iu.gui.resources;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {
    static MediaPlayer mp;
    public static void playMusic(String name) {
        String path = Resources.getResourceFilename("sounds/"+name);
        if(path == null)
            return;
        Media music = new Media(path);
        mp = new MediaPlayer(music);
        mp.setStartTime(Duration.seconds(1.7));
        mp.setStopTime(music.getDuration());
        /*
        mp.setOnReady(() -> {
                mp.play();
        };
         */
        mp.setAutoPlay(true);
    }
}
