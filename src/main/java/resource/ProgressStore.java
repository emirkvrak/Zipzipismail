package resource;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/** Persists completion, best-time and star progress between game sessions. */
public final class ProgressStore {

    private static final double NO_BEST_TIME = Double.POSITIVE_INFINITY;
    private final Preferences preferences;

    public ProgressStore() {
        this(Preferences.userNodeForPackage(ProgressStore.class));
    }

    ProgressStore(Preferences preferences) {
        this.preferences = preferences;
    }

    public boolean recordCompletion(int levelNumber, double elapsedSeconds, int stars) {
        double previousBest = bestTime(levelNumber);
        boolean newRecord = elapsedSeconds < previousBest;
        if (newRecord) {
            preferences.putDouble(bestTimeKey(levelNumber), elapsedSeconds);
        }

        int previousStars = bestStars(levelNumber);
        preferences.putInt(bestStarsKey(levelNumber), Math.max(previousStars, stars));
        preferences.putBoolean(completedKey(levelNumber), true);
        flushSafely();
        return newRecord;
    }

    public boolean isCompleted(int levelNumber) {
        return preferences.getBoolean(completedKey(levelNumber), false);
    }

    public double bestTime(int levelNumber) {
        return preferences.getDouble(bestTimeKey(levelNumber), NO_BEST_TIME);
    }

    public int bestStars(int levelNumber) {
        return preferences.getInt(bestStarsKey(levelNumber), 0);
    }

    public boolean isSoundMuted() {
        return preferences.getBoolean("sound.muted", false);
    }

    public void setSoundMuted(boolean muted) {
        preferences.putBoolean("sound.muted", muted);
        flushSafely();
    }

    public boolean hasBestTime(int levelNumber) {
        return bestTime(levelNumber) != NO_BEST_TIME;
    }

    private void flushSafely() {
        try {
            preferences.flush();
        } catch (BackingStoreException exception) {
            System.err.println("İlerleme kaydedilemedi: " + exception.getMessage());
        }
    }

    private String completedKey(int levelNumber) {
        return "level." + levelNumber + ".completed";
    }

    private String bestTimeKey(int levelNumber) {
        return "level." + levelNumber + ".bestTime";
    }

    private String bestStarsKey(int levelNumber) {
        return "level." + levelNumber + ".bestStars";
    }
}
