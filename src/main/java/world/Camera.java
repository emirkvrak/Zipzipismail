package world;

import core.GameConfig;
import entity.Player;

/** Controls the world-to-screen offset while following the player. */
public final class Camera {

    private double x;
    private double y;

    public Camera(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void follow(Player player, GameMap map) {
        follow(player, map, 1.0);
    }

    public void follow(Player player, GameMap map, double deltaSeconds) {
        double playerScreenX = player.x() - x;
        double targetX = x;
        if (playerScreenX > GameConfig.CAMERA_RIGHT_DEAD_ZONE) {
            targetX = player.x() - GameConfig.CAMERA_RIGHT_DEAD_ZONE;
        } else if (playerScreenX < GameConfig.CAMERA_LEFT_DEAD_ZONE) {
            targetX = player.x() - GameConfig.CAMERA_LEFT_DEAD_ZONE;
        }

        double maximumX = Math.max(GameConfig.INITIAL_CAMERA_X,
                map.widthInPixels() - GameConfig.SCREEN_WIDTH);
        targetX = clamp(targetX, GameConfig.INITIAL_CAMERA_X, maximumX);
        double smoothing = Math.min(1.0, deltaSeconds * 10.0);
        x += (targetX - x) * smoothing;
        x = clamp(x, GameConfig.INITIAL_CAMERA_X, maximumX);
        y = GameConfig.INITIAL_CAMERA_Y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    private double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
