package ui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import core.GameConfig;
import resource.GameAssets;
import world.GameMap;
import world.Checkpoint;
import world.Collectible;
import world.MovingPlatform;
import world.Tile;
import world.TileType;

/** Renders static tiles and moving world objects. */
public final class WorldRenderer {

    private final GameAssets assets;
    private double animationTime;

    public WorldRenderer(GameAssets assets) {
        this.assets = assets;
    }

    public void update(double deltaSeconds) {
        animationTime += deltaSeconds;
    }

    public void render(Graphics2D graphics, GameMap map, double cameraX, double cameraY) {
        Rectangle viewport = new Rectangle((int) cameraX, (int) cameraY,
                GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        for (Tile tile : map.tiles()) {
            if (viewport.intersects(tile.bounds())) {
                renderTile(graphics, tile, cameraX, cameraY);
            }
        }
        for (MovingPlatform platform : map.movingPlatforms()) {
            if (viewport.intersects(platform.bounds())) {
                renderMovingPlatform(graphics, platform, cameraX, cameraY);
            }
        }
        for (Checkpoint checkpoint : map.checkpoints()) {
            if (viewport.intersects(checkpoint.bounds())) {
                renderCheckpoint(graphics, checkpoint, cameraX, cameraY);
            }
        }
        for (Collectible collectible : map.collectibles()) {
            if (!collectible.collected() && viewport.intersects(collectible.bounds())) {
                renderCollectible(graphics, collectible, cameraX, cameraY);
            }
        }
    }

    private void renderTile(Graphics2D graphics, Tile tile, double cameraX, double cameraY) {
        Rectangle bounds = tile.bounds();
        renderWorldObject(graphics, imageFor(tile.type()), tile.type(), bounds, cameraX, cameraY);
    }

    private void renderMovingPlatform(Graphics2D graphics, MovingPlatform platform,
            double cameraX, double cameraY) {
        Rectangle bounds = platform.bounds();
        renderWorldObject(graphics, imageFor(platform.type()), platform.type(), bounds,
                cameraX, cameraY);
    }

    private void renderWorldObject(Graphics2D graphics, Image image, TileType type,
            Rectangle bounds, double cameraX, double cameraY) {
        if (type == TileType.GOAL) {
            drawGoalGlow(graphics, bounds, cameraX, cameraY);
        }
        if (type == TileType.BOUNCY) {
            drawBouncyOverlay(graphics, bounds, cameraX, cameraY);
        }
        if (type == TileType.HAZARD) {
            drawRotatedImage(graphics, image, bounds, cameraX, cameraY);
        } else {
            drawImage(graphics, image, bounds, cameraX, cameraY);
        }
    }

    private void drawImage(Graphics2D graphics, Image image, Rectangle bounds,
            double cameraX, double cameraY) {
        int drawX = (int) Math.round(bounds.x - cameraX);
        int drawY = (int) Math.round(bounds.y - cameraY);
        graphics.drawImage(image, drawX, drawY, bounds.width, bounds.height, null);
    }

    private void drawRotatedImage(Graphics2D graphics, Image image, Rectangle bounds,
            double cameraX, double cameraY) {
        Graphics2D rotated = (Graphics2D) graphics.create();
        try {
            double centerX = bounds.getCenterX() - cameraX;
            double centerY = bounds.getCenterY() - cameraY;
            rotated.rotate(animationTime * 4.5, centerX, centerY);
            drawImage(rotated, image, bounds, cameraX, cameraY);
        } finally {
            rotated.dispose();
        }
    }

    private void drawGoalGlow(Graphics2D graphics, Rectangle bounds,
            double cameraX, double cameraY) {
        Graphics2D glow = (Graphics2D) graphics.create();
        try {
            float alpha = (float) (0.18 + 0.10 * (1.0 + Math.sin(animationTime * 4.0)));
            glow.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            glow.setColor(new java.awt.Color(255, 205, 70));
            int drawX = (int) Math.round(bounds.x - cameraX);
            int drawY = (int) Math.round(bounds.y - cameraY);
            int padding = 7;
            glow.fillOval(drawX - padding, drawY - padding,
                    bounds.width + padding * 2, bounds.height + padding * 2);
        } finally {
            glow.dispose();
        }
    }

    private void drawBouncyOverlay(Graphics2D graphics, Rectangle bounds,
            double cameraX, double cameraY) {
        Graphics2D overlay = (Graphics2D) graphics.create();
        try {
            overlay.setColor(new java.awt.Color(80, 220, 130, 150));
            int drawX = (int) Math.round(bounds.x - cameraX);
            int drawY = (int) Math.round(bounds.y - cameraY);
            overlay.fillRoundRect(drawX + 3, drawY + 3,
                    bounds.width - 6, bounds.height - 6, 10, 10);
            overlay.setColor(new java.awt.Color(190, 255, 205));
            overlay.drawLine(drawX + 8, drawY + bounds.height / 2,
                    drawX + bounds.width / 2, drawY + 8);
            overlay.drawLine(drawX + bounds.width / 2, drawY + 8,
                    drawX + bounds.width - 8, drawY + bounds.height / 2);
        } finally {
            overlay.dispose();
        }
    }

    private void renderCheckpoint(Graphics2D graphics, Checkpoint checkpoint,
            double cameraX, double cameraY) {
        Rectangle bounds = checkpoint.bounds();
        int drawX = (int) Math.round(bounds.x - cameraX);
        int drawY = (int) Math.round(bounds.y - cameraY);
        Graphics2D flag = (Graphics2D) graphics.create();
        try {
            flag.setColor(checkpoint.active() ? new java.awt.Color(80, 230, 130)
                    : new java.awt.Color(90, 210, 255));
            flag.setStroke(new java.awt.BasicStroke(3));
            flag.drawLine(drawX + 10, drawY + 6, drawX + 10, drawY + 38);
            flag.fillPolygon(new int[] {drawX + 11, drawX + 31, drawX + 11},
                    new int[] {drawY + 7, drawY + 14, drawY + 21}, 3);
        } finally {
            flag.dispose();
        }
    }

    private void renderCollectible(Graphics2D graphics, Collectible collectible,
            double cameraX, double cameraY) {
        Rectangle bounds = collectible.bounds();
        int centerX = (int) Math.round(bounds.getCenterX() - cameraX);
        int centerY = (int) Math.round(bounds.getCenterY() - cameraY);
        double pulse = 1.0 + 0.10 * Math.sin(animationTime * 5.0);
        int outerRadius = (int) Math.round(bounds.width * pulse / 2.0);
        int innerRadius = Math.max(3, outerRadius / 2);
        int[] xPoints = new int[8];
        int[] yPoints = new int[8];
        for (int index = 0; index < 8; index++) {
            double angle = -Math.PI / 2.0 + index * Math.PI / 4.0;
            int radius = index % 2 == 0 ? outerRadius : innerRadius;
            xPoints[index] = centerX + (int) Math.round(Math.cos(angle) * radius);
            yPoints[index] = centerY + (int) Math.round(Math.sin(angle) * radius);
        }
        Graphics2D star = (Graphics2D) graphics.create();
        try {
            star.setColor(new java.awt.Color(255, 218, 65));
            star.fillPolygon(xPoints, yPoints, xPoints.length);
            star.setColor(new java.awt.Color(255, 250, 190));
            star.fillOval(centerX - 2, centerY - 2, 4, 4);
        } finally {
            star.dispose();
        }
    }

    private Image imageFor(TileType type) {
        return switch (type) {
            case SOLID -> assets.solidTile();
            case HAZARD -> assets.hazardTile();
            case GOAL -> assets.goalTile();
            case BOUNCY -> assets.solidTile();
        };
    }

}
