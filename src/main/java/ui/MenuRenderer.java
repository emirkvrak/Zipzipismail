package ui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import core.GameConfig;
import resource.GameAssets;
import resource.ProgressStore;

/** Renders the main menu and keeps its layout rules in one place. */
public final class MenuRenderer {

    private static final int MENU_OPTION_SPACING = 85;
    private static final int MENU_CENTER_X = GameConfig.SCREEN_WIDTH / 2;
    private static final int BUTTON_WIDTH = 340;
    private static final int BUTTON_HEIGHT = 52;
    private static final int BUTTON_X = MENU_CENTER_X - BUTTON_WIDTH / 2;
    private static final int TEXT_BASELINE_OFFSET = 38;
    private final GameAssets assets;

    public MenuRenderer(GameAssets assets) {
        this.assets = assets;
    }

    public void render(Graphics2D graphics, String[] options, int selectedOption,
            ProgressStore progressStore) {
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawImage(assets.menuBackground(), 0, 0,
                GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, null);

        graphics.setFont(UiTheme.MENU_OPTION_FONT);
        int buttonStartY = buttonStartY(options.length);
        for (int index = 0; index < options.length; index++) {
            int buttonY = buttonStartY + index * optionSpacing(options.length);
            boolean selected = index == selectedOption;
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    selected ? 0.92f : 0.78f));
            graphics.setColor(selected ? UiTheme.ACCENT : UiTheme.TEXT);
            graphics.fillRoundRect(BUTTON_X, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, 14, 14);
            graphics.setComposite(AlphaComposite.SrcOver);
            graphics.setColor(selected ? new java.awt.Color(45, 31, 8) : java.awt.Color.BLACK);
            graphics.setFont(UiTheme.MENU_OPTION_FONT);
            int baselineY = buttonY + TEXT_BASELINE_OFFSET;
            drawCenteredString(graphics, options[index], MENU_CENTER_X, baselineY);

            if (index < 3 && progressStore.isCompleted(index + 1)) {
                graphics.setFont(UiTheme.MENU_STATUS_FONT);
                graphics.setColor(new java.awt.Color(255, 245, 190));
                String status = "Tamamlandı  •  "
                        + HudRenderer.formatTime(progressStore.bestTime(index + 1))
                        + "  •  " + progressStore.bestStars(index + 1) + "★";
                drawCenteredString(graphics, status, MENU_CENTER_X, buttonY + 68);
            }
        }
    }

    public int optionAt(int x, int y, int optionCount) {
        if (x < BUTTON_X || x > BUTTON_X + BUTTON_WIDTH) {
            return -1;
        }
        int buttonStartY = buttonStartY(optionCount);
        for (int index = 0; index < optionCount; index++) {
            int buttonY = buttonStartY + index * optionSpacing(optionCount);
            if (y >= buttonY && y <= buttonY + BUTTON_HEIGHT) {
                return index;
            }
        }
        return -1;
    }

    private int buttonStartY(int optionCount) {
        if (optionCount <= 3) {
            return 142;
        }
        int totalHeight = BUTTON_HEIGHT + (optionCount - 1) * optionSpacing(optionCount);
        return (GameConfig.SCREEN_HEIGHT - totalHeight) / 2;
    }

    private int optionSpacing(int optionCount) {
        return optionCount >= 6 ? 72 : MENU_OPTION_SPACING;
    }

    private void drawCenteredString(Graphics2D graphics, String text,
            int centerX, int baselineY) {
        int textWidth = graphics.getFontMetrics().stringWidth(text);
        graphics.drawString(text, centerX - textWidth / 2, baselineY);
    }
}
