package resource;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class AudioPlayer implements AutoCloseable {

    private final Clip clip;
    private boolean muted;
    private boolean looping;

    public AudioPlayer(String resourcePath) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        try (AudioInputStream input = AudioSystem.getAudioInputStream(ResourceLoader.url(resourcePath))) {
            clip = AudioSystem.getClip();
            clip.open(input);
        }
    }

    public void loop() {
        looping = true;
        if (!muted) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if (muted) {
            clip.stop();
        } else if (looping) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    @Override
    public void close() {
        looping = false;
        clip.close();
    }
}
