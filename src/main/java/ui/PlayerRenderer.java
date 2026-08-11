package ui;

import java.awt.Graphics2D;

import core.GameConfig;
import entity.Player;
import resource.GameAssets;

/** Renders the player without exposing rendering concerns to the entity model. */
public final class PlayerRenderer {

    private final GameAssets assets;

    public PlayerRenderer(GameAssets assets) {
        this.assets = assets;
    }

    public void render(Graphics2D graphics, Player player, double cameraX, double cameraY) {
        double verticalScale = verticalScale(player);
        double horizontalScale = 1.0 / verticalScale;
        int drawWidth = (int) Math.round(player.width() * horizontalScale);
        int drawHeight = (int) Math.round(player.height() * verticalScale);
        int centerX = (int) Math.round(player.x() + player.width() / 2.0 - cameraX);
        int feetY = (int) Math.round(player.y() + player.height() - cameraY);
        int drawX = centerX - drawWidth / 2;
        int drawY = feetY - drawHeight;
        graphics.drawImage(assets.player(), drawX, drawY, drawWidth, drawHeight, null);
    }

    private double verticalScale(Player player) {
        double bounceProgress = player.bounceFeedbackProgress();
        if (bounceProgress > 0) {
            return 1.0 - 0.12 * bounceProgress;
        }
        if (player.verticalVelocity() < GameConfig.PLAYER_JUMP_SPEED * 0.35) {
            return 1.08;
        }
        if (player.verticalVelocity() > GameConfig.PLAYER_MAX_FALL_SPEED * 0.6) {
            return 0.96;
        }
        return 1.0;
    }
}
