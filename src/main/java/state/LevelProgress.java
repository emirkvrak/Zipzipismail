package state;

import entity.PlayerStatus;
import resource.ProgressStore;

/** Tracks time, collected stars and the completion record of one level run. */
public final class LevelProgress {

    private final int levelNumber;
    private final ProgressStore progressStore;
    private double elapsedSeconds;
    private int collectedCount;
    private boolean resultSaved;
    private boolean newRecord;

    public LevelProgress(int levelNumber, ProgressStore progressStore) {
        this.levelNumber = levelNumber;
        this.progressStore = progressStore;
    }

    public void reset() {
        elapsedSeconds = 0;
        collectedCount = 0;
        resultSaved = false;
        newRecord = false;
    }

    public void update(double deltaSeconds) {
        elapsedSeconds += Math.max(0, deltaSeconds);
    }

    public void collectStar() {
        collectedCount++;
    }

    public void saveCompletionIfNeeded(PlayerStatus status) {
        if (resultSaved || status != PlayerStatus.WON) {
            return;
        }
        newRecord = progressStore.recordCompletion(levelNumber, elapsedSeconds, collectedCount);
        resultSaved = true;
    }

    public double elapsedSeconds() {
        return elapsedSeconds;
    }

    public int collectedCount() {
        return collectedCount;
    }

    public boolean newRecord() {
        return newRecord;
    }
}
