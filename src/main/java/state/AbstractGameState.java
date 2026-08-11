package state;

import resource.GameAssets;

public abstract class AbstractGameState implements GameState {

    protected final GameStateManager stateManager;
    protected final GameAssets assets;

    protected AbstractGameState(GameStateManager stateManager, GameAssets assets) {
        this.stateManager = stateManager;
        this.assets = assets;
    }
}
