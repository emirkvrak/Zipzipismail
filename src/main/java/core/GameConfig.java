package core;

public final class GameConfig {

    public static final int SCREEN_WIDTH = 900;
    public static final int SCREEN_HEIGHT = 550;
    public static final int TARGET_FPS = 60;
    public static final double FIXED_DELTA_SECONDS = 1.0 / TARGET_FPS;
    public static final int TILE_SIZE = 40;
    public static final int PLAYER_WIDTH = 30;
    public static final int PLAYER_HEIGHT = 30;
    public static final double PLAYER_MOVE_SPEED = 240.0;
    public static final double PLAYER_GRAVITY = 700.0;
    public static final double PLAYER_JUMP_SPEED = -360.0;
    public static final double BOUNCY_BLOCK_JUMP_SPEED = -500.0;
    public static final double PLAYER_MAX_FALL_SPEED = 500.0;
    public static final double PLAYER_BOUNCE_FEEDBACK_SECONDS = 0.12;
    public static final double MOVING_PLATFORM_SPEED = 160.0;

    public static final double CAMERA_LEFT_DEAD_ZONE = 100.0;
    public static final double CAMERA_RIGHT_DEAD_ZONE = 700.0;
    public static final double INITIAL_CAMERA_X = -200.0;
    public static final double INITIAL_CAMERA_Y = -300.0;

    private GameConfig() {
    }
}
