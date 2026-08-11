package app;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import core.GameCanvas;
import resource.AudioManager;
import resource.ProgressStore;

public final class GameWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private final GameCanvas canvas;

    public GameWindow(Runnable shutdownAction, Runnable exitAction,
            AudioManager audioManager, ProgressStore progressStore) {
        super("Zipzipismail");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        canvas = new GameCanvas(audioManager, progressStore, this::toggleFullscreen, exitAction);
        add(canvas, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                canvas.stop();
                shutdownAction.run();
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    public void start() {
        setVisible(true);
        canvas.start();
    }

    private void toggleFullscreen() {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        dispose();
        if (device.getFullScreenWindow() == this) {
            device.setFullScreenWindow(null);
            setUndecorated(false);
            setResizable(false);
            pack();
            setLocationRelativeTo(null);
        } else {
            setUndecorated(true);
            device.setFullScreenWindow(this);
        }
        setVisible(true);
        canvas.requestFocusInWindow();
    }
}
