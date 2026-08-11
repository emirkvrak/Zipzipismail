package physics;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Rectangle;

import org.junit.jupiter.api.Test;

class CollisionServiceTest {

    @Test
    void sawCollisionUsesRoundShapeInsteadOfSquareCorners() {
        Rectangle playerAtSawCenter = new Rectangle(15, 15, 10, 10);
        Rectangle playerAtSawCorner = new Rectangle(-28, -28, 30, 30);
        Rectangle sawBounds = new Rectangle(0, 0, 40, 40);

        assertTrue(CollisionService.circleIntersects(playerAtSawCenter, sawBounds));
        assertFalse(CollisionService.circleIntersects(playerAtSawCorner, sawBounds));
    }
}
