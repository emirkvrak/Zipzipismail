package ui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import core.GameConfig;

/** Displays the controls and the meaning of the game's actions. */
public final class ControlsRenderer {

    private static final int PANEL_WIDTH = 680;
    private static final int PANEL_HEIGHT = 500;
    private static final int PANEL_X = (GameConfig.SCREEN_WIDTH - PANEL_WIDTH) / 2;
    private static final int PANEL_Y = (GameConfig.SCREEN_HEIGHT - PANEL_HEIGHT) / 2;
    private static final Rectangle SOUND_BUTTON = new Rectangle(
            (GameConfig.SCREEN_WIDTH - 300) / 2, PANEL_Y + 370, 300, 42);

    public void render(Graphics2D graphics, boolean soundMuted) {
        render(graphics, soundMuted, -1, -1);
    }

    public void render(Graphics2D graphics, boolean soundMuted,
            int mouseX, int mouseY) {
        Graphics2D canvas = (Graphics2D) graphics.create();
        try {
            canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            canvas.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.78f));
            canvas.setColor(UiTheme.DARK_OVERLAY);
            canvas.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
            canvas.setComposite(AlphaComposite.SrcOver);

            canvas.setColor(UiTheme.PANEL_SOLID);
            canvas.fillRoundRect(PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT, 24, 24);
            canvas.setColor(UiTheme.ACCENT);
            canvas.drawRoundRect(PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT, 24, 24);

            canvas.setColor(UiTheme.ACCENT);
            canvas.setFont(UiTheme.CONTROLS_TITLE_FONT);
            drawCentered(canvas, "KONTROLLER", GameConfig.SCREEN_WIDTH / 2, PANEL_Y + 58);

            canvas.setFont(UiTheme.CONTROLS_ROW_FONT);
            canvas.setColor(UiTheme.TEXT);
            int rowY = PANEL_Y + 112;
            drawRow(canvas, "A / D veya ← / →", "Topu sağa ve sola hareket ettir", rowY);
            drawRow(canvas, "Otomatik zıplama", "Top zemine değince kendiliğinden seker", rowY + 38);
            drawRow(canvas, "Esc", "Oyunu duraklatır; duraklatma ekranından ana menüye döner", rowY + 76);
            drawRow(canvas, "Enter", "Seçimi onaylar, yeniden başlatır veya sonraki bölüme geçer", rowY + 114);
            drawRow(canvas, "F10", "Ses efektleri ve müzik: " + (soundMuted ? "KAPALI" : "AÇIK"), rowY + 152);
            drawRow(canvas, "F11", "Tam ekranı açar veya kapatır", rowY + 190);
            drawRow(canvas, "Mouse", "Menü butonlarına tıkla; ses butonuyla sesi aç/kapat", rowY + 228);

            boolean soundHovered = soundToggleAt(mouseX, mouseY);
            canvas.setColor(soundHovered ? UiTheme.ACCENT : UiTheme.BUTTON_GREEN);
            canvas.fillRoundRect(SOUND_BUTTON.x, SOUND_BUTTON.y,
                    SOUND_BUTTON.width, SOUND_BUTTON.height, 14, 14);
            canvas.setColor(soundHovered ? new java.awt.Color(35, 27, 10) : UiTheme.TEXT);
            canvas.setFont(UiTheme.CONTROLS_ROW_FONT);
            drawCentered(canvas, soundMuted ? "SESİ AÇ" : "SESİ KAPAT",
                    GameConfig.SCREEN_WIDTH / 2, SOUND_BUTTON.y + 27);

            canvas.setFont(UiTheme.CONTROLS_HINT_FONT);
            canvas.setColor(UiTheme.MUTED_TEXT);
            drawCentered(canvas, "Esc veya Enter ile geri dön",
                    GameConfig.SCREEN_WIDTH / 2, PANEL_Y + PANEL_HEIGHT - 20);
        } finally {
            canvas.dispose();
        }
    }

    public boolean soundToggleAt(int x, int y) {
        return SOUND_BUTTON.contains(x, y);
    }

    private void drawRow(Graphics2D graphics, String key, String description, int baselineY) {
        graphics.setColor(UiTheme.ACCENT_HIGHLIGHT);
        graphics.drawString(key, PANEL_X + 42, baselineY);
        graphics.setColor(UiTheme.TEXT);
        graphics.drawString(description, PANEL_X + 210, baselineY);
    }

    private void drawCentered(Graphics2D graphics, String text, int centerX, int baselineY) {
        int textWidth = graphics.getFontMetrics().stringWidth(text);
        graphics.drawString(text, centerX - textWidth / 2, baselineY);
    }
}
