package world;

import java.awt.Rectangle;

import core.GameConfig;

public final class MovingPlatform {

    private final TileType type;
    private final double leftBound;
    private final double rightBound;
    private double x;
    private final double y;
    private double direction = 1;
    private final double speed = GameConfig.MOVING_PLATFORM_SPEED;

    public MovingPlatform(int x, int y, TileType type, int leftBound, int rightBound) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    public void update(double deltaSeconds) {
        x += direction * speed * deltaSeconds;
        if (x <= leftBound) {
            x = leftBound;
            direction = 1;
        } else if (x + GameConfig.TILE_SIZE >= rightBound) {
            x = rightBound - GameConfig.TILE_SIZE;
            direction = -1;
        }
    }

    public Rectangle bounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y),
                GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
    }

    public TileType type() {
        return type;
    }

}
