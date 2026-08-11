package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HudRendererTest {

    @Test
    void formatsLevelTimeWithTenths() {
        assertEquals("00:00.0", HudRenderer.formatTime(0));
        assertEquals("01:05.7", HudRenderer.formatTime(65.7));
        assertEquals("12:34.5", HudRenderer.formatTime(754.5));
    }
}
