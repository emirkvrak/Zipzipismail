package ui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.Locale;

import core.GameConfig;

public final class HudRenderer {

    private HudRenderer() {
    }

    public static void draw(Graphics2D graphics, int levelNumber, boolean paused,
            double elapsedSeconds, int collectedCount, int collectibleCount) {
        Graphics2D hud = (Graphics2D) graphics.create();
        try {
            hud.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.78f));
            hud.setColor(new java.awt.Color(10, 14, 30));
            hud.fillRect(0, 0, GameConfig.SCREEN_WIDTH, 96);
            hud.setComposite(AlphaComposite.SrcOver);
            hud.setColor(UiTheme.TEXT);
            hud.setFont(UiTheme.HUD_HEADER_FONT);
            hud.drawString("BÖLÜM " + levelNumber, 24, 33);
            hud.setColor(UiTheme.ACCENT);
            hud.drawString("ZIPZIPISMAIL", 370, 33);
            if (collectibleCount > 0) {
                hud.setColor(new java.awt.Color(255, 218, 65));
                hud.drawString("YILDIZ " + collectedCount + "/" + collectibleCount, 520, 33);
            }
            hud.setColor(new java.awt.Color(220, 225, 240));
            hud.drawString(paused ? "DURAKLATILDI" : "SÜRE " + formatTime(elapsedSeconds), 700, 33);
            hud.setFont(UiTheme.HUD_HINT_FONT);
            hud.drawString("A/D veya ←/→ hareket   Otomatik zıpla   Sarı yıldızları topla   Esc duraklat", 24, 78);
        } finally {
            hud.dispose();
        }
    }

    public static void drawOverlay(Graphics2D graphics, String title, String subtitle) {
        Graphics2D overlay = (Graphics2D) graphics.create();
        try {
            overlay.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.72f));
            overlay.setColor(UiTheme.DARK_OVERLAY);
            overlay.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
            overlay.setComposite(AlphaComposite.SrcOver);
            overlay.setColor(UiTheme.ACCENT);
            overlay.setFont(UiTheme.HUD_OVERLAY_TITLE_FONT);
            int titleWidth = overlay.getFontMetrics().stringWidth(title);
            overlay.drawString(title, (GameConfig.SCREEN_WIDTH - titleWidth) / 2, 285);
            overlay.setColor(UiTheme.TEXT);
            overlay.setFont(UiTheme.HUD_OVERLAY_SUBTITLE_FONT);
            int subtitleWidth = overlay.getFontMetrics().stringWidth(subtitle);
            overlay.drawString(subtitle, (GameConfig.SCREEN_WIDTH - subtitleWidth) / 2, 330);
        } finally {
            overlay.dispose();
        }
    }

    public static String formatTime(double elapsedSeconds) {
        int totalTenths = Math.max(0, (int) Math.floor(elapsedSeconds * 10));
        int minutes = totalTenths / 600;
        int seconds = (totalTenths / 10) % 60;
        int tenths = totalTenths % 10;
        return String.format(Locale.ROOT, "%02d:%02d.%d", minutes, seconds, tenths);
    }
}
