package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import core.GameConfig;
import core.InputState;
import world.GameMap;
import world.Tile;
import world.TileType;

class PlayerCollisionTest {

    @Test
    void touchingSawEndsTheGame() {
        Player player = new Player(0, 0, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        GameMap map = mapWith(TileType.HAZARD);

        player.update(1.0 / 60.0, new InputState(), map);

        assertEquals(PlayerStatus.LOST, player.status());
    }

    @Test
    void sawCornerDoesNotBehaveLikeSolidBlock() {
        Player player = new Player(-28, -28, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        GameMap map = mapWith(TileType.HAZARD);

        player.update(1.0 / 60.0, new InputState(), map);

        assertEquals(PlayerStatus.PLAYING, player.status());
        assertTrue(player.y() < 0);
    }

    @Test
    void touchingGoalFinishesTheLevel() {
        Player player = new Player(0, 0, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        GameMap map = mapWith(TileType.GOAL);

        player.update(1.0 / 60.0, new InputState(), map);

        assertEquals(PlayerStatus.WON, player.status());
    }

    @Test
    void landingProducesOneBounceEvent() {
        Player player = new Player(0, 0, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        GameMap map = new GameMap(3, 3,
                List.of(new Tile(0, GameConfig.PLAYER_HEIGHT, TileType.SOLID)),
                List.of());

        player.update(0.1, new InputState(), map);

        assertTrue(player.consumeBounceEvent());
        assertFalse(player.consumeBounceEvent());
    }

    @Test
    void bouncyBlockLaunchesPlayerHigher() {
        Player player = new Player(0, 0, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        GameMap map = new GameMap(3, 3,
                List.of(new Tile(0, GameConfig.PLAYER_HEIGHT, TileType.BOUNCY)),
                List.of());

        player.update(0.1, new InputState(), map);

        assertEquals(GameConfig.BOUNCY_BLOCK_JUMP_SPEED, player.verticalVelocity());
    }

    private GameMap mapWith(TileType type) {
        return new GameMap(3, 3,
                List.of(new Tile(0, 0, type)),
                List.of());
    }
}
