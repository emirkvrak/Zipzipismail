package world;

import java.awt.Rectangle;

import core.GameConfig;

public final class Tile {

    private final Rectangle bounds;
    private final TileType type;

    public Tile(int x, int y, TileType type) {
        bounds = new Rectangle(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
        this.type = type;
    }

    public Rectangle bounds() {
        return new Rectangle(bounds);
    }

    public TileType type() {
        return type;
    }

}
