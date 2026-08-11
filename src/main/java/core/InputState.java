package core;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public final class InputState implements KeyListener, MouseListener, MouseMotionListener {

    private boolean left;
    private boolean right;
    private boolean upPressed;
    private boolean downPressed;
    private boolean confirmPressed;
    private boolean backPressed;
    private boolean fullscreenPressed;
    private boolean mutePressed;
    private int mouseX;
    private int mouseY;
    private Point mouseClick;

    public synchronized int horizontalAxis() {
        return (right ? 1 : 0) - (left ? 1 : 0);
    }

    public synchronized boolean consumeConfirm() {
        boolean value = confirmPressed;
        confirmPressed = false;
        return value;
    }

    public synchronized boolean consumeBack() {
        boolean value = backPressed;
        backPressed = false;
        return value;
    }

    public synchronized boolean consumeFullscreenToggle() {
        boolean value = fullscreenPressed;
        fullscreenPressed = false;
        return value;
    }

    public synchronized boolean consumeMuteToggle() {
        boolean value = mutePressed;
        mutePressed = false;
        return value;
    }

    public synchronized boolean consumeUp() {
        boolean value = upPressed;
        upPressed = false;
        return value;
    }

    public synchronized boolean consumeDown() {
        boolean value = downPressed;
        downPressed = false;
        return value;
    }

    public synchronized Point mousePosition() {
        return new Point(mouseX, mouseY);
    }

    public synchronized Point consumeMouseClick() {
        Point value = mouseClick;
        mouseClick = null;
        return value;
    }

    @Override
    public synchronized void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_UP -> upPressed = true;
            case KeyEvent.VK_DOWN -> downPressed = true;
            case KeyEvent.VK_ENTER -> confirmPressed = true;
            case KeyEvent.VK_ESCAPE -> backPressed = true;
            case KeyEvent.VK_F11 -> fullscreenPressed = true;
            case KeyEvent.VK_F10 -> mutePressed = true;
            default -> {
                // Kullanılmayan tuşlar oyun durumunu değiştirmez.
            }
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = false;
            default -> {
                // Kenar tetiklemeli tuşların bırakılması ayrıca izlenmez.
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
        // Karakter girişi kullanılmıyor.
    }

    @Override
    public synchronized void mousePressed(MouseEvent event) {
        updateMousePosition(event);
        if (event.getButton() == MouseEvent.BUTTON1) {
            mouseClick = new Point(mouseX, mouseY);
        }
    }

    @Override
    public synchronized void mouseMoved(MouseEvent event) {
        updateMousePosition(event);
    }

    @Override
    public synchronized void mouseDragged(MouseEvent event) {
        updateMousePosition(event);
    }

    private void updateMousePosition(MouseEvent event) {
        Point logicalPoint = toLogicalPoint(event);
        mouseX = logicalPoint.x;
        mouseY = logicalPoint.y;
    }

    private Point toLogicalPoint(MouseEvent event) {
        int componentWidth = event.getComponent().getWidth();
        int componentHeight = event.getComponent().getHeight();
        if (componentWidth <= 0 || componentHeight <= 0) {
            return new Point(event.getX(), event.getY());
        }

        double scale = Math.min((double) componentWidth / GameConfig.SCREEN_WIDTH,
                (double) componentHeight / GameConfig.SCREEN_HEIGHT);
        int viewportWidth = (int) Math.round(GameConfig.SCREEN_WIDTH * scale);
        int viewportHeight = (int) Math.round(GameConfig.SCREEN_HEIGHT * scale);
        int offsetX = (componentWidth - viewportWidth) / 2;
        int offsetY = (componentHeight - viewportHeight) / 2;
        return new Point((int) Math.round((event.getX() - offsetX) / scale),
                (int) Math.round((event.getY() - offsetY) / scale));
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        // Tıklama mousePressed üzerinden tek seferlik olarak işlenir.
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        // Mouse bırakılması oyun durumunu değiştirmez.
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        updateMousePosition(event);
    }

    @Override
    public void mouseExited(MouseEvent event) {
        // Son konum korunur; menü dışındaki konum seçim üretmez.
    }
}
