package resource;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

/** Plays short generated effects without blocking the game loop. */
public final class AudioManager implements AutoCloseable {

    private static final float SAMPLE_RATE = 44_100;
    private static final AudioFormat FORMAT = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);

    private final Map<SoundEffect, Clip> clips = new EnumMap<>(SoundEffect.class);
    private Consumer<Boolean> muteListener = ignored -> { };
    private boolean muted;

    public AudioManager() {
        try {
            for (SoundEffect effect : SoundEffect.values()) {
                clips.put(effect, createClip(effect));
            }
        } catch (LineUnavailableException | IllegalArgumentException exception) {
            close();
            System.err.println("Ses efektleri kullanılamıyor: " + exception.getMessage());
        }
    }

    public void play(SoundEffect effect) {
        if (muted) {
            return;
        }
        Clip clip = clips.get(effect);
        if (clip == null) {
            return;
        }
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if (muted) {
            clips.values().forEach(Clip::stop);
        }
        muteListener.accept(muted);
    }

    public void setMuteListener(Consumer<Boolean> muteListener) {
        this.muteListener = muteListener == null ? ignored -> { } : muteListener;
        this.muteListener.accept(muted);
    }

    public void toggleMuted() {
        setMuted(!muted);
    }

    public boolean isMuted() {
        return muted;
    }

    @Override
    public void close() {
        clips.values().forEach(Clip::close);
        clips.clear();
    }

    private Clip createClip(SoundEffect effect) throws LineUnavailableException {
        Tone tone = toneFor(effect);
        Clip clip = AudioSystem.getClip();
        byte[] audio = createTone(tone.startFrequency(), tone.endFrequency(),
                tone.durationSeconds(), tone.volume());
        clip.open(FORMAT, audio, 0, audio.length);
        return clip;
    }

    private Tone toneFor(SoundEffect effect) {
        return switch (effect) {
            case MENU_MOVE -> new Tone(520, 520, 0.045, 0.20);
            case MENU_CONFIRM -> new Tone(680, 900, 0.10, 0.24);
            case BOUNCE -> new Tone(240, 380, 0.08, 0.18);
            case HAZARD -> new Tone(180, 70, 0.20, 0.28);
            case GOAL -> new Tone(520, 1_040, 0.30, 0.25);
            case CHECKPOINT -> new Tone(440, 880, 0.18, 0.22);
            case COLLECT -> new Tone(760, 1_180, 0.10, 0.20);
        };
    }

    private byte[] createTone(double startFrequency, double endFrequency,
            double durationSeconds, double volume) {
        int sampleCount = (int) (SAMPLE_RATE * durationSeconds);
        byte[] audio = new byte[sampleCount * 2];
        for (int sample = 0; sample < sampleCount; sample++) {
            double progress = (double) sample / sampleCount;
            double frequency = startFrequency + (endFrequency - startFrequency) * progress;
            double envelope = Math.min(1.0, progress * 18.0)
                    * Math.min(1.0, (1.0 - progress) * 12.0);
            short value = (short) (Math.sin(2.0 * Math.PI * frequency * sample / SAMPLE_RATE)
                    * Short.MAX_VALUE * volume * envelope);
            audio[sample * 2] = (byte) (value & 0xff);
            audio[sample * 2 + 1] = (byte) ((value >> 8) & 0xff);
        }
        return audio;
    }

    private record Tone(double startFrequency, double endFrequency,
            double durationSeconds, double volume) {
    }
}
