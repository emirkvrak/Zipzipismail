package ui;

import java.awt.Color;
import java.awt.Font;

/** Shared visual constants for menus, HUD and overlays. */
public final class UiTheme {

    public static final Color ACCENT = new Color(255, 190, 70);
    public static final Color ACCENT_HIGHLIGHT = new Color(255, 218, 120);
    public static final Color DARK_OVERLAY = new Color(5, 7, 18);
    public static final Color PANEL_TOP = new Color(26, 35, 70);
    public static final Color PANEL_BOTTOM = new Color(12, 17, 38);
    public static final Color PANEL_SOLID = new Color(18, 28, 58);
    public static final Color BUTTON = new Color(48, 59, 96);
    public static final Color BUTTON_GREEN = new Color(48, 150, 105);
    public static final Color TEXT = Color.WHITE;
    public static final Color MUTED_TEXT = new Color(205, 212, 230);

    public static final Font MENU_OPTION_FONT = new Font("SansSerif", Font.PLAIN, 46);
    public static final Font MENU_STATUS_FONT = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font HUD_HEADER_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font HUD_HINT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font HUD_OVERLAY_TITLE_FONT = new Font("SansSerif", Font.BOLD, 42);
    public static final Font HUD_OVERLAY_SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 20);
    public static final Font PAUSE_TITLE_FONT = new Font("SansSerif", Font.BOLD, 36);
    public static final Font PAUSE_BUTTON_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font PAUSE_HINT_FONT = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font CONTROLS_TITLE_FONT = new Font("SansSerif", Font.BOLD, 34);
    public static final Font CONTROLS_ROW_FONT = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font CONTROLS_HINT_FONT = new Font("SansSerif", Font.PLAIN, 13);

    private UiTheme() {
    }
}
