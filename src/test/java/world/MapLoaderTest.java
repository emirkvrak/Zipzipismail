package world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import core.GameConfig;

class MapLoaderTest {

    @Test
    void loadsAllPlayableMaps() {
        GameMap firstMap = MapLoader.load("/Maps/map1.map");
        GameMap secondMap = MapLoader.load("/Maps/map2.map");
        GameMap thirdMap = MapLoader.load("/Maps/map3.map");

        assertEquals(54 * 40, firstMap.widthInPixels());
        assertEquals(47 * 40, secondMap.widthInPixels());
        assertEquals(1, firstMap.movingPlatforms().size());
        assertEquals(1, secondMap.movingPlatforms().size());
        assertEquals(47 * 40, thirdMap.widthInPixels());
        assertEquals(4, thirdMap.movingPlatforms().size());
        assertEquals(2, thirdMap.checkpoints().size());
        assertEquals(5, thirdMap.collectibles().size());
        assertEquals(-GameConfig.TILE_SIZE, thirdMap.checkpoints().get(0).bounds().y);
        assertEquals(-GameConfig.TILE_SIZE + (GameConfig.TILE_SIZE - 24) / 2,
                thirdMap.collectibles().get(0).bounds().y);
        assertEquals(3, thirdMap.tiles().stream()
                .filter(tile -> tile.type() == TileType.BOUNCY)
                .count());
    }

    @Test
    void rejectsMissingMapResource() {
        assertThrows(IllegalStateException.class, () -> MapLoader.load("/Maps/missing.map"));
    }
}
