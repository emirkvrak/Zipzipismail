package world;

import java.awt.Rectangle;

import core.GameConfig;

/** A collectible star placed in a level. */
public final class Collectible {

    private static final int SIZE = 24;

    private final Rectangle bounds;
    private boolean collected;

    public Collectible(int x, int y) {
        bounds = new Rectangle(x + (GameConfig.TILE_SIZE - SIZE) / 2,
                y + (GameConfig.TILE_SIZE - SIZE) / 2, SIZE, SIZE);
    }

    public Rectangle bounds() {
        return new Rectangle(bounds);
    }

    public boolean touches(Rectangle playerBounds) {
        return !collected && playerBounds.intersects(bounds);
    }

    public void collect() {
        collected = true;
    }

    public boolean collected() {
        return collected;
    }
}
