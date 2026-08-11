package core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.EventQueue;
import java.awt.Color;
import java.util.Objects;

import javax.swing.JPanel;

import resource.GameAssets;
import resource.AudioManager;
import resource.ProgressStore;
import state.GameStateManager;

public final class GameCanvas extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    private final InputState inputState = new InputState();
    private final GameStateManager stateManager;
    private final Runnable fullscreenToggle;
    private volatile boolean running;
    private Thread gameThread;

    public GameCanvas(AudioManager audioManager, ProgressStore progressStore,
            Runnable fullscreenToggle, Runnable exitAction) {
        this.fullscreenToggle = fullscreenToggle;
        setPreferredSize(new java.awt.Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        setFocusable(true);
        setBackground(Color.BLACK);
        setIgnoreRepaint(true);
        addKeyListener(inputState);
        addMouseListener(inputState);
        addMouseMotionListener(inputState);
        stateManager = new GameStateManager(new GameAssets(), audioManager, progressStore,
                Objects.requireNonNull(exitAction, "exitAction"));
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        gameThread = new Thread(this, "zipzipismail-game-loop");
        gameThread.start();
        requestFocusInWindow();
    }

    public synchronized void stop() {
        running = false;
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    @Override
    public void run() {
        long previousTime = System.nanoTime();
        double accumulator = 0;
        long updateNanos = (long) (GameConfig.FIXED_DELTA_SECONDS * 1_000_000_000L);

        while (running) {
            long currentTime = System.nanoTime();
            long elapsedNanos = Math.min(currentTime - previousTime, 250_000_000L);
            previousTime = currentTime;
            accumulator += elapsedNanos;

            while (accumulator >= updateNanos) {
                if (inputState.consumeFullscreenToggle()) {
                    EventQueue.invokeLater(fullscreenToggle);
                }
                stateManager.update(GameConfig.FIXED_DELTA_SECONDS, inputState);
                accumulator -= updateNanos;
            }

            repaint();
            sleepBriefly();
        }
        running = false;
    }

    private void sleepBriefly() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            running = false;
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D canvas = (Graphics2D) graphics.create();
        try {
            double scale = Math.min((double) getWidth() / GameConfig.SCREEN_WIDTH,
                    (double) getHeight() / GameConfig.SCREEN_HEIGHT);
            if (scale <= 0) {
                return;
            }
            int viewportWidth = (int) Math.round(GameConfig.SCREEN_WIDTH * scale);
            int viewportHeight = (int) Math.round(GameConfig.SCREEN_HEIGHT * scale);
            canvas.translate((getWidth() - viewportWidth) / 2.0,
                    (getHeight() - viewportHeight) / 2.0);
            canvas.scale(scale, scale);
            canvas.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            canvas.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            stateManager.render(canvas);
        } finally {
            canvas.dispose();
        }
    }
}
