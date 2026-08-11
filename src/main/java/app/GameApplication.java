package app;

import java.awt.EventQueue;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import resource.AudioManager;
import resource.AudioPlayer;
import resource.ProgressStore;

public final class GameApplication {

    private static AudioPlayer music;
    private static AudioManager audioManager;
    private static ProgressStore progressStore;

    private GameApplication() {
    }

    public static void main(String[] args) {
        progressStore = new ProgressStore();
        audioManager = new AudioManager();
        audioManager.setMuted(progressStore.isSoundMuted());
        audioManager.setMuteListener(muted -> {
            if (music != null) {
                music.setMuted(muted);
            }
        });
        startMusic();
        EventQueue.invokeLater(() -> new GameWindow(GameApplication::stopAudio,
                GameApplication::exit, audioManager, progressStore).start());
    }

    private static void startMusic() {
        try {
            music = new AudioPlayer("/Ses/zipzipses.wav");
            music.loop();
            music.setMuted(audioManager.isMuted());
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            System.err.println("Oyun müziği başlatılamadı: " + e.getMessage());
        }
    }

    static void stopAudio() {
        if (music != null) {
            music.close();
            music = null;
        }
        if (audioManager != null) {
            audioManager.close();
            audioManager = null;
        }
    }

    private static void exit() {
        stopAudio();
        System.exit(0);
    }
}
