package world;

import java.awt.Rectangle;

import core.GameConfig;

/** A respawn point that can be activated by touching it. */
public final class Checkpoint {

    private final Rectangle bounds;
    private boolean active;

    public Checkpoint(int x, int y) {
        bounds = new Rectangle(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
    }

    public Rectangle bounds() {
        return new Rectangle(bounds);
    }

    public boolean touches(Rectangle playerBounds) {
        Rectangle activationArea = new Rectangle(bounds.x,
                bounds.y - GameConfig.PLAYER_HEIGHT,
                bounds.width, bounds.height + GameConfig.PLAYER_HEIGHT);
        return playerBounds.intersects(activationArea);
    }

    public void activate() {
        active = true;
    }

    public boolean active() {
        return active;
    }

    public double respawnX() {
        return bounds.x + (bounds.width - GameConfig.PLAYER_WIDTH) / 2.0;
    }

    public double respawnY() {
        return bounds.y - GameConfig.PLAYER_HEIGHT;
    }
}
