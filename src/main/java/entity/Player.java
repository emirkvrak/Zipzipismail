package entity;

import java.awt.Rectangle;

import core.GameConfig;
import core.InputState;
import physics.CollisionService;
import world.GameMap;
import world.MovingPlatform;
import world.Tile;
import world.TileType;

public final class Player {

    private final int width;
    private final int height;
    private double x;
    private double y;
    private double velocityY;
    private boolean grounded;
    private boolean bounceEvent;
    private double bounceFeedbackRemaining;
    private double landingJumpSpeed = GameConfig.PLAYER_JUMP_SPEED;
    private PlayerStatus status = PlayerStatus.PLAYING;

    public Player(double spawnX, double spawnY, int width, int height) {
        this.width = width;
        this.height = height;
        reset(spawnX, spawnY);
    }

    public void reset(double spawnX, double spawnY) {
        x = spawnX;
        y = spawnY;
        velocityY = 0;
        grounded = false;
        bounceEvent = false;
        bounceFeedbackRemaining = 0;
        landingJumpSpeed = GameConfig.PLAYER_JUMP_SPEED;
        status = PlayerStatus.PLAYING;
    }

    public void update(double deltaSeconds, InputState input, GameMap map) {
        if (status != PlayerStatus.PLAYING) {
            return;
        }

        bounceEvent = false;
        bounceFeedbackRemaining = Math.max(0,
                bounceFeedbackRemaining - deltaSeconds);

        // Sonuç nesneleri fiziksel olarak oyuncudan ayrıştırılmadan önce kontrol edilir.
        checkSpecialTiles(map);
        if (status != PlayerStatus.PLAYING) {
            return;
        }

        double horizontalMovement = input.horizontalAxis() * GameConfig.PLAYER_MOVE_SPEED * deltaSeconds;
        x += horizontalMovement;
        checkSpecialTiles(map);
        if (status != PlayerStatus.PLAYING) {
            return;
        }
        resolveHorizontalCollision(horizontalMovement, map);

        velocityY = Math.min(velocityY + GameConfig.PLAYER_GRAVITY * deltaSeconds,
                GameConfig.PLAYER_MAX_FALL_SPEED);
        double verticalMovement = velocityY * deltaSeconds;
        y += verticalMovement;
        checkSpecialTiles(map);
        if (status != PlayerStatus.PLAYING) {
            return;
        }
        grounded = false;
        resolveVerticalCollision(verticalMovement, map);
        checkSpecialTiles(map);

        // Oyunun temel mekaniği: top zemine değdiğinde otomatik olarak tekrar seker.
        if (grounded && status == PlayerStatus.PLAYING) {
            velocityY = landingJumpSpeed;
            grounded = false;
            bounceEvent = true;
            bounceFeedbackRemaining = GameConfig.PLAYER_BOUNCE_FEEDBACK_SECONDS;
            landingJumpSpeed = GameConfig.PLAYER_JUMP_SPEED;
        }

        x = Math.max(0, Math.min(x, map.widthInPixels() - width));
        if (y > map.heightInPixels() + GameConfig.SCREEN_HEIGHT) {
            status = PlayerStatus.LOST;
        }
    }

    private void resolveHorizontalCollision(double movement, GameMap map) {
        Rectangle playerBounds = bounds();
        for (Tile tile : map.tiles()) {
            if (!isPhysicalObstacle(tile.type())) {
                continue;
            }
            if (CollisionService.overlaps(playerBounds, tile.bounds())) {
                resolveHorizontalPosition(movement, tile.bounds());
                playerBounds = bounds();
            }
        }
        for (MovingPlatform platform : map.movingPlatforms()) {
            if (!isPhysicalObstacle(platform.type())) {
                continue;
            }
            if (CollisionService.overlaps(playerBounds, platform.bounds())) {
                resolveHorizontalPosition(movement, platform.bounds());
                playerBounds = bounds();
            }
        }
    }

    private void resolveHorizontalPosition(double movement, Rectangle obstacle) {
        if (movement > 0) {
            x = obstacle.x - width;
        } else if (movement < 0) {
            x = obstacle.x + obstacle.width;
        }
    }

    private void resolveVerticalCollision(double movement, GameMap map) {
        Rectangle playerBounds = bounds();
        for (Tile tile : map.tiles()) {
            if (!isPhysicalObstacle(tile.type())) {
                continue;
            }
            if (CollisionService.overlaps(playerBounds, tile.bounds())) {
                resolveVerticalPosition(movement, tile.bounds(), tile.type());
                playerBounds = bounds();
            }
        }
        for (MovingPlatform platform : map.movingPlatforms()) {
            if (!isPhysicalObstacle(platform.type())) {
                continue;
            }
            if (CollisionService.overlaps(playerBounds, platform.bounds())) {
                resolveVerticalPosition(movement, platform.bounds(), platform.type());
                playerBounds = bounds();
            }
        }
    }

    private void resolveVerticalPosition(double movement, Rectangle obstacle, TileType type) {
        if (movement > 0) {
            y = obstacle.y - height;
            velocityY = 0;
            grounded = true;
            landingJumpSpeed = type == TileType.BOUNCY
                    ? GameConfig.BOUNCY_BLOCK_JUMP_SPEED
                    : GameConfig.PLAYER_JUMP_SPEED;
        } else if (movement < 0) {
            y = obstacle.y + obstacle.height;
            velocityY = 0;
        }
    }

    private void checkSpecialTiles(GameMap map) {
        Rectangle playerBounds = bounds();
        for (Tile tile : map.tiles()) {
            if (touchesSpecialTile(playerBounds, tile.bounds(), tile.type())) {
                applyTileResult(tile.type());
            }
        }
        for (MovingPlatform platform : map.movingPlatforms()) {
            if (touchesSpecialTile(playerBounds, platform.bounds(), platform.type())) {
                applyTileResult(platform.type());
            }
        }
    }

    private boolean touchesSpecialTile(Rectangle playerBounds, Rectangle tileBounds, TileType type) {
        if (type == TileType.HAZARD) {
            return CollisionService.circleIntersects(playerBounds, tileBounds);
        }
        return CollisionService.overlaps(playerBounds, tileBounds);
    }

    private void applyTileResult(TileType type) {
        if (status != PlayerStatus.PLAYING) {
            return;
        }
        if (type == TileType.HAZARD) {
            status = PlayerStatus.LOST;
        } else if (type == TileType.GOAL) {
            status = PlayerStatus.WON;
        }
    }

    private boolean isPhysicalObstacle(TileType type) {
        return type == TileType.SOLID || type == TileType.BOUNCY;
    }

    public Rectangle bounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), width, height);
    }

    public PlayerStatus status() {
        return status;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double verticalVelocity() {
        return velocityY;
    }

    public boolean consumeBounceEvent() {
        boolean bounced = bounceEvent;
        bounceEvent = false;
        return bounced;
    }

    public double bounceFeedbackProgress() {
        return bounceFeedbackRemaining / GameConfig.PLAYER_BOUNCE_FEEDBACK_SECONDS;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

}
