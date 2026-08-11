package world;

public enum TileType {
    SOLID(1),
    GOAL(7),
    HAZARD(9),
    BOUNCY(4);

    private final int mapId;

    TileType(int mapId) {
        this.mapId = mapId;
    }

    public static TileType fromMapId(int mapId) {
        for (TileType type : values()) {
            if (type.mapId == mapId) {
                return type;
            }
        }
        throw new IllegalArgumentException("Bilinmeyen tile id: " + mapId);
    }
}
