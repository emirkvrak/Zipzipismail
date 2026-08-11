package resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.jupiter.api.Test;

class ProgressStoreTest {

    @Test
    void keepsBestTimeAndHighestStarCount() throws BackingStoreException {
        Preferences preferences = Preferences.userRoot()
                .node("zipzipismail-test-" + UUID.randomUUID());
        try {
            ProgressStore store = new ProgressStore(preferences);

            assertTrue(store.recordCompletion(3, 42.5, 3));
            assertFalse(store.recordCompletion(3, 50.0, 1));
            assertTrue(store.isCompleted(3));
            assertEquals(42.5, store.bestTime(3));
            assertEquals(3, store.bestStars(3));
        } finally {
            preferences.removeNode();
        }
    }
}
