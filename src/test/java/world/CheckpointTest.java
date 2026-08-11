package world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Rectangle;

import org.junit.jupiter.api.Test;

import core.GameConfig;

class CheckpointTest {

    @Test
    void activatesWhenPlayerTouchesItAndProvidesRespawnPosition() {
        Checkpoint checkpoint = new Checkpoint(400, 0);

        assertFalse(checkpoint.active());
        assertTrue(checkpoint.touches(new Rectangle(410, -10, 30, 30)));
        assertTrue(checkpoint.touches(new Rectangle(410, -30, 30, 30)));

        checkpoint.activate();

        assertTrue(checkpoint.active());
        assertEquals(405, checkpoint.respawnX());
        assertEquals(-GameConfig.PLAYER_HEIGHT, checkpoint.respawnY());
    }
}
