package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

class InputStateTest {

    @Test
    void tracksHeldMovementAndConsumesOneShotActions() {
        InputState input = new InputState();
        JPanel source = new JPanel();

        input.keyPressed(new KeyEvent(source, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_RIGHT, ' '));
        input.keyPressed(new KeyEvent(source, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER, '\n'));

        assertEquals(1, input.horizontalAxis());
        assertTrue(input.consumeConfirm());
        assertFalse(input.consumeConfirm());

        input.keyReleased(new KeyEvent(source, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_RIGHT, ' '));
        assertEquals(0, input.horizontalAxis());

        input.keyPressed(new KeyEvent(source, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ESCAPE, '\u001b'));
        assertTrue(input.consumeBack());
        assertFalse(input.consumeBack());

        input.mouseMoved(new MouseEvent(source, MouseEvent.MOUSE_MOVED, 0, 0, 450, 150, 0, false));
        assertEquals(450, input.mousePosition().x);
        assertEquals(150, input.mousePosition().y);

        input.mousePressed(new MouseEvent(source, MouseEvent.MOUSE_PRESSED, 0, 0,
                450, 150, 1, false, MouseEvent.BUTTON1));
        Point click = input.consumeMouseClick();
        assertEquals(new Point(450, 150), click);
        assertNull(input.consumeMouseClick());

        input.keyPressed(new KeyEvent(source, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_F11, '\0'));
        assertTrue(input.consumeFullscreenToggle());
        assertFalse(input.consumeFullscreenToggle());

        input.keyPressed(new KeyEvent(source, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_F10, '\0'));
        assertTrue(input.consumeMuteToggle());
        assertFalse(input.consumeMuteToggle());
    }

    @Test
    void mapsFullscreenMouseCoordinatesBackToLogicalViewport() {
        InputState input = new InputState();
        JPanel source = new JPanel();
        source.setSize(1800, 1100);

        input.mouseMoved(new MouseEvent(source, MouseEvent.MOUSE_MOVED, 0, 0,
                900, 300, 0, false));

        assertEquals(new Point(450, 150), input.mousePosition());
    }
}
