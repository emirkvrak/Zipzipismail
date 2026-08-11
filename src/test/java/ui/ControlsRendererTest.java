package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

import state.PauseOption;

class ControlsRendererTest {

    @Test
    void rendersControlsAndExposesPauseOptions() {
        BufferedImage image = new BufferedImage(900, 550, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            new ControlsRenderer().render(graphics, false);
            new ControlsRenderer().render(graphics, true);
            assertEquals(PauseOption.CONTROLS.ordinal(),
                    new PauseMenuRenderer().optionAt(450, 320));
        } finally {
            graphics.dispose();
        }
    }
}
