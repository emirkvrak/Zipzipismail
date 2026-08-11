package ui;

import java.awt.AlphaComposite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import core.GameConfig;
import state.PauseOption;

/** Draws the interactive pause menu independently from level logic. */
public final class PauseMenuRenderer {

    private static final int PANEL_WIDTH = 460;
    private static final int PANEL_HEIGHT = 410;
    private static final int PANEL_X = (GameConfig.SCREEN_WIDTH - PANEL_WIDTH) / 2;
    private static final int PANEL_Y = (GameConfig.SCREEN_HEIGHT - PANEL_HEIGHT) / 2;
    private static final int BUTTON_X = PANEL_X + 60;
    private static final int BUTTON_WIDTH = PANEL_WIDTH - 120;
    private static final int BUTTON_HEIGHT = 52;
    private static final int BUTTON_GAP = 14;
    private static final int BUTTON_START_Y = PANEL_Y + 105;
    public void render(Graphics2D graphics, PauseOption selectedOption) {
        Graphics2D canvas = (Graphics2D) graphics.create();
        try {
            canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            canvas.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.72f));
            canvas.setColor(UiTheme.DARK_OVERLAY);
            canvas.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
            canvas.setComposite(AlphaComposite.SrcOver);

            canvas.setPaint(new GradientPaint(PANEL_X, PANEL_Y, UiTheme.PANEL_TOP,
                    PANEL_X, PANEL_Y + PANEL_HEIGHT, UiTheme.PANEL_BOTTOM));
            canvas.fillRoundRect(PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT, 24, 24);
            canvas.setColor(UiTheme.ACCENT);
            canvas.drawRoundRect(PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT, 24, 24);

            canvas.setFont(UiTheme.PAUSE_TITLE_FONT);
            drawCentered(canvas, "OYUN DURAKLATILDI", GameConfig.SCREEN_WIDTH / 2, PANEL_Y + 70);

            canvas.setFont(UiTheme.PAUSE_BUTTON_FONT);
            PauseOption[] options = PauseOption.values();
            for (int index = 0; index < options.length; index++) {
                PauseOption option = options[index];
                int buttonY = BUTTON_START_Y + index * (BUTTON_HEIGHT + BUTTON_GAP);
                boolean selected = option == selectedOption;
                canvas.setColor(selected ? UiTheme.ACCENT : UiTheme.BUTTON);
                canvas.fillRoundRect(BUTTON_X, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, 14, 14);
                canvas.setColor(selected ? new java.awt.Color(35, 27, 10) : UiTheme.TEXT);
                drawCentered(canvas, option.label(), GameConfig.SCREEN_WIDTH / 2,
                        buttonY + centeredBaseline(canvas, BUTTON_HEIGHT));
            }

            canvas.setFont(UiTheme.PAUSE_HINT_FONT);
            canvas.setColor(UiTheme.MUTED_TEXT);
            drawCentered(canvas, "↑ / ↓ seç   Enter onayla   Mouse tıkla   Esc ana menü",
                    GameConfig.SCREEN_WIDTH / 2, PANEL_Y + PANEL_HEIGHT - 18);
        } finally {
            canvas.dispose();
        }
    }

    public int optionAt(int x, int y) {
        if (x < BUTTON_X || x > BUTTON_X + BUTTON_WIDTH) {
            return -1;
        }
        PauseOption[] options = PauseOption.values();
        for (int index = 0; index < options.length; index++) {
            int buttonY = BUTTON_START_Y + index * (BUTTON_HEIGHT + BUTTON_GAP);
            if (y >= buttonY && y <= buttonY + BUTTON_HEIGHT) {
                return index;
            }
        }
        return -1;
    }

    private int centeredBaseline(Graphics2D graphics, int height) {
        return (height - graphics.getFontMetrics().getHeight()) / 2
                + graphics.getFontMetrics().getAscent();
    }

    private void drawCentered(Graphics2D graphics, String text, int centerX, int baselineY) {
        int textWidth = graphics.getFontMetrics().stringWidth(text);
        graphics.drawString(text, centerX - textWidth / 2, baselineY);
    }
}
