package state;

import java.awt.Graphics2D;

import core.InputState;
import resource.AudioManager;
import resource.GameAssets;
import resource.ProgressStore;

public final class GameStateManager {

    private final GameAssets assets;
    private final AudioManager audioManager;
    private final ProgressStore progressStore;
    private final Runnable exitAction;
    private GameState currentState;

    public GameStateManager(GameAssets assets, AudioManager audioManager,
            ProgressStore progressStore, Runnable exitAction) {
        this.assets = assets;
        this.audioManager = audioManager;
        this.progressStore = progressStore;
        this.exitAction = exitAction;
        showMenu();
    }

    public synchronized void update(double deltaSeconds, InputState input) {
        if (input.consumeMuteToggle()) {
            toggleSound();
        }
        currentState.update(deltaSeconds, input);
    }

    public synchronized void toggleSound() {
        audioManager.toggleMuted();
        progressStore.setSoundMuted(audioManager.isMuted());
    }

    public synchronized void render(Graphics2D graphics) {
        currentState.render(graphics);
    }

    public synchronized void showMenu() {
        currentState = new MenuState(this, assets, audioManager, progressStore, exitAction);
    }

    public synchronized void startLevel(int levelNumber) {
        currentState = new LevelState(this, assets, audioManager, progressStore, levelNumber);
    }

    GameState currentState() {
        return currentState;
    }
}
