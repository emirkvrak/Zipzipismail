package world;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Rectangle;

import org.junit.jupiter.api.Test;

class CollectibleTest {

    @Test
    void canBeCollectedOnlyOnce() {
        Collectible collectible = new Collectible(0, 0);

        assertTrue(collectible.touches(new Rectangle(8, 8, 24, 24)));
        collectible.collect();
        assertTrue(collectible.collected());
        assertFalse(collectible.touches(new Rectangle(8, 8, 24, 24)));
    }
}
