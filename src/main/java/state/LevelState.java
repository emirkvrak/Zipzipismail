package state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;

import core.GameConfig;
import core.InputState;
import effect.ParticleSystem;
import entity.Player;
import entity.PlayerStatus;
import resource.GameAssets;
import resource.AudioManager;
import resource.SoundEffect;
import resource.ProgressStore;
import ui.ControlsRenderer;
import ui.HudRenderer;
import ui.PauseMenuRenderer;
import ui.PlayerRenderer;
import ui.WorldRenderer;
import world.Camera;
import world.Checkpoint;
import world.Collectible;
import world.GameMap;
import world.MapLoader;

public final class LevelState extends AbstractGameState {

    private final int levelNumber;
    private final String mapPath;
    private final Image background;
    private final AudioManager audioManager;
    private final Camera camera;
    private final LevelProgress progress;
    private final PlayerRenderer playerRenderer;
    private final WorldRenderer worldRenderer;
    private final PauseMenuRenderer pauseMenuRenderer;
    private final ControlsRenderer controlsRenderer;
    private final ParticleSystem particleSystem;
    private GameMap map;
    private Player player;
    private Checkpoint activeCheckpoint;
    private boolean paused;
    private boolean controlsVisible;
    private PauseOption pauseOption = PauseOption.RESUME;
    private boolean resultSoundPlayed;
    private Point mousePosition = new Point(-1, -1);

    public LevelState(GameStateManager stateManager, GameAssets assets,
            AudioManager audioManager, ProgressStore progressStore, int levelNumber) {
        super(stateManager, assets);
        this.levelNumber = levelNumber;
        mapPath = switch (levelNumber) {
            case 1 -> "/Maps/map1.map";
            case 2 -> "/Maps/map2.map";
            case 3 -> "/Maps/map3.map";
            default -> throw new IllegalArgumentException("Geçersiz bölüm: " + levelNumber);
        };
        background = assets.levelBackground(levelNumber);
        this.audioManager = audioManager;
        camera = new Camera(GameConfig.INITIAL_CAMERA_X, GameConfig.INITIAL_CAMERA_Y);
        progress = new LevelProgress(levelNumber, progressStore);
        playerRenderer = new PlayerRenderer(assets);
        worldRenderer = new WorldRenderer(assets);
        pauseMenuRenderer = new PauseMenuRenderer();
        controlsRenderer = new ControlsRenderer();
        particleSystem = new ParticleSystem();
        reset();
    }

    @Override
    public void update(double deltaSeconds, InputState input) {
        mousePosition = input.mousePosition();
        if (paused) {
            if (controlsVisible) {
                if (input.consumeBack() || input.consumeConfirm()) {
                    controlsVisible = false;
                } else {
                    Point click = input.consumeMouseClick();
                    if (click != null && controlsRenderer.soundToggleAt(click.x, click.y)) {
                        stateManager.toggleSound();
                    }
                }
                return;
            }
            if (input.consumeBack()) {
                stateManager.showMenu();
                return;
            }
            updatePauseMenu(input);
            return;
        }
        if (input.consumeBack()) {
            if (player.status() != PlayerStatus.PLAYING) {
                stateManager.showMenu();
            } else {
                paused = true;
                pauseOption = PauseOption.RESUME;
            }
            return;
        }
        particleSystem.update(deltaSeconds);
        if (player.status() != PlayerStatus.PLAYING) {
            if (input.consumeConfirm()) {
                if (player.status() == PlayerStatus.WON && levelNumber < 3) {
                    stateManager.startLevel(levelNumber + 1);
                } else if (player.status() == PlayerStatus.LOST && activeCheckpoint != null) {
                    respawnAtCheckpoint();
                } else {
                    reset();
                }
            }
            return;
        }

        worldRenderer.update(deltaSeconds);
        map.update(deltaSeconds);
        player.update(deltaSeconds, input, map);
        progress.update(deltaSeconds);
        updateCheckpoint();
        updateCollectibles();
        progress.saveCompletionIfNeeded(player.status());
        playResultSoundIfNeeded();
        if (player.consumeBounceEvent()) {
            particleSystem.emitBounce(player);
            audioManager.play(SoundEffect.BOUNCE);
        }
        updateCamera(deltaSeconds);
    }

    private void updateCheckpoint() {
        for (Checkpoint checkpoint : map.checkpoints()) {
            if (!checkpoint.active() && checkpoint.touches(player.bounds())) {
                checkpoint.activate();
                activeCheckpoint = checkpoint;
                audioManager.play(SoundEffect.CHECKPOINT);
            }
        }
    }

    private void updateCollectibles() {
        for (Collectible collectible : map.collectibles()) {
            if (collectible.touches(player.bounds())) {
                collectible.collect();
                progress.collectStar();
                audioManager.play(SoundEffect.COLLECT);
            }
        }
    }

    private void playResultSoundIfNeeded() {
        if (resultSoundPlayed || player.status() == PlayerStatus.PLAYING) {
            return;
        }
        audioManager.play(player.status() == PlayerStatus.WON
                ? SoundEffect.GOAL : SoundEffect.HAZARD);
        resultSoundPlayed = true;
    }

    private void updatePauseMenu(InputState input) {
        if (input.consumeUp()) {
            pauseOption = previousPauseOption();
        }
        if (input.consumeDown()) {
            pauseOption = nextPauseOption();
        }
        if (input.consumeConfirm()) {
            selectPauseOption();
        }

        Point mouse = input.mousePosition();
        int hoveredOption = pauseMenuRenderer.optionAt(mouse.x, mouse.y);
        if (hoveredOption >= 0 && hoveredOption != pauseOption.ordinal()) {
            pauseOption = PauseOption.values()[hoveredOption];
            audioManager.play(SoundEffect.MENU_MOVE);
        }

        Point click = input.consumeMouseClick();
        if (click != null) {
            int clickedOption = pauseMenuRenderer.optionAt(click.x, click.y);
            if (clickedOption >= 0) {
                pauseOption = PauseOption.values()[clickedOption];
                selectPauseOption();
            }
        }
    }

    private PauseOption previousPauseOption() {
        PauseOption[] options = PauseOption.values();
        int previousIndex = (pauseOption.ordinal() - 1 + options.length) % options.length;
        return options[previousIndex];
    }

    private PauseOption nextPauseOption() {
        PauseOption[] options = PauseOption.values();
        int nextIndex = (pauseOption.ordinal() + 1) % options.length;
        return options[nextIndex];
    }

    private void selectPauseOption() {
        switch (pauseOption) {
            case RESUME -> paused = false;
            case RESTART -> reset();
            case CONTROLS -> controlsVisible = true;
            case MENU -> stateManager.showMenu();
        }
    }

    private void reset() {
        map = MapLoader.load(mapPath);
        // Eski bölümlerin tasarlanan başlangıç kadrajı ve dünya koordinatları.
        player = new Player(50, -25, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        activeCheckpoint = null;
        camera.reset(GameConfig.INITIAL_CAMERA_X, GameConfig.INITIAL_CAMERA_Y);
        particleSystem.clear();
        progress.reset();
        paused = false;
        controlsVisible = false;
        pauseOption = PauseOption.RESUME;
        resultSoundPlayed = false;
    }

    private void respawnAtCheckpoint() {
        player.reset(activeCheckpoint.respawnX(), activeCheckpoint.respawnY());
        camera.reset(GameConfig.INITIAL_CAMERA_X, GameConfig.INITIAL_CAMERA_Y);
        particleSystem.clear();
        paused = false;
        resultSoundPlayed = false;
    }

    private void updateCamera(double deltaSeconds) {
        camera.follow(player, map, deltaSeconds);
    }

    @Override
    public void render(Graphics2D graphics) {
        Graphics2D canvas = (Graphics2D) graphics.create();
        try {
            canvas.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            canvas.drawImage(background, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, null);
            canvas.setColor(new Color(0, 0, 0, 45));
            canvas.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
            worldRenderer.render(canvas, map, camera.x(), camera.y());
            particleSystem.render(canvas, camera.x(), camera.y());
            playerRenderer.render(canvas, player, camera.x(), camera.y());
            HudRenderer.draw(canvas, levelNumber, paused, progress.elapsedSeconds(),
                    progress.collectedCount(), map.collectibles().size());
            if (paused) {
                if (controlsVisible) {
                    controlsRenderer.render(canvas, audioManager.isMuted(),
                            mousePosition.x, mousePosition.y);
                } else {
                    pauseMenuRenderer.render(canvas, pauseOption);
                }
            } else if (player.status() == PlayerStatus.WON) {
                String nextAction = levelNumber < 3
                        ? "Enter ile sonraki bölüme geç"
                        : "Enter ile tekrar oyna";
                String subtitle = resultSummary() + "  |  " + nextAction;
                HudRenderer.drawOverlay(canvas, "BÖLÜM TAMAMLANDI", subtitle);
            } else if (player.status() == PlayerStatus.LOST) {
                String retryAction = activeCheckpoint == null
                        ? "Enter ile tekrar dene"
                        : "Enter ile checkpoint'ten devam et";
                HudRenderer.drawOverlay(canvas, "OYUN BİTTİ",
                        resultSummary()
                                + "  |  " + retryAction);
            }
        } finally {
            canvas.dispose();
        }
    }

    private String resultSummary() {
        String time = "Süre " + HudRenderer.formatTime(progress.elapsedSeconds());
        if (map.collectibles().isEmpty()) {
            return progress.newRecord() ? time + "  |  YENİ REKOR" : time;
        }
        String result = time + "  |  Yıldız " + progress.collectedCount()
                + "/" + map.collectibles().size();
        return progress.newRecord() ? result + "  |  YENİ REKOR" : result;
    }
}
