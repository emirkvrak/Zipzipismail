package state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import entity.PlayerStatus;
import resource.ProgressStore;

class LevelProgressTest {

    @Test
    void tracksRunTimeAndCollectedStarsBeforeCompletion() {
        LevelProgress progress = new LevelProgress(999, new ProgressStore());

        progress.update(1.25);
        progress.update(-1.0);
        progress.collectStar();
        progress.saveCompletionIfNeeded(PlayerStatus.PLAYING);

        assertEquals(1.25, progress.elapsedSeconds());
        assertEquals(1, progress.collectedCount());
        assertFalse(progress.newRecord());
    }
}
