package world;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import core.GameConfig;
import entity.Player;

class CameraTest {

    @Test
    void followsPlayerAfterLeavingRightDeadZone() {
        Camera camera = new Camera(0, GameConfig.INITIAL_CAMERA_Y);
        Player player = new Player(900, 0, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        GameMap map = emptyMap(50, 10);

        camera.follow(player, map);

        assertEquals(200, camera.x());
        assertEquals(GameConfig.INITIAL_CAMERA_Y, camera.y());
    }

    @Test
    void doesNotMoveBeyondMapBounds() {
        Camera camera = new Camera(GameConfig.INITIAL_CAMERA_X, GameConfig.INITIAL_CAMERA_Y);
        Player player = new Player(3_000, 0, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        GameMap map = emptyMap(50, 10);

        camera.follow(player, map);

        assertEquals(map.widthInPixels() - GameConfig.SCREEN_WIDTH, camera.x());
    }

    private GameMap emptyMap(int width, int height) {
        return new GameMap(width, height, List.of(), List.of());
    }
}
