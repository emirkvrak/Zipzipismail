package world;

import java.util.List;

import core.GameConfig;

public final class GameMap {

    private final int widthInTiles;
    private final int heightInTiles;
    private final List<Tile> tiles;
    private final List<MovingPlatform> movingPlatforms;
    private final List<Checkpoint> checkpoints;
    private final List<Collectible> collectibles;

    public GameMap(int widthInTiles, int heightInTiles, List<Tile> tiles,
            List<MovingPlatform> movingPlatforms) {
        this(widthInTiles, heightInTiles, tiles, movingPlatforms, List.of());
    }

    public GameMap(int widthInTiles, int heightInTiles, List<Tile> tiles,
            List<MovingPlatform> movingPlatforms, List<Checkpoint> checkpoints) {
        this(widthInTiles, heightInTiles, tiles, movingPlatforms, checkpoints, List.of());
    }

    public GameMap(int widthInTiles, int heightInTiles, List<Tile> tiles,
            List<MovingPlatform> movingPlatforms, List<Checkpoint> checkpoints,
            List<Collectible> collectibles) {
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.tiles = List.copyOf(tiles);
        this.movingPlatforms = List.copyOf(movingPlatforms);
        this.checkpoints = List.copyOf(checkpoints);
        this.collectibles = List.copyOf(collectibles);
    }

    public void update(double deltaSeconds) {
        movingPlatforms.forEach(platform -> platform.update(deltaSeconds));
    }

    public List<Tile> tiles() {
        return tiles;
    }

    public List<MovingPlatform> movingPlatforms() {
        return movingPlatforms;
    }

    public List<Checkpoint> checkpoints() {
        return checkpoints;
    }

    public List<Collectible> collectibles() {
        return collectibles;
    }

    public int widthInPixels() {
        return widthInTiles * GameConfig.TILE_SIZE;
    }

    public int heightInPixels() {
        return heightInTiles * GameConfig.TILE_SIZE;
    }
}
