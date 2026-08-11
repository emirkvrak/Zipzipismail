package state;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import resource.AudioManager;
import resource.GameAssets;
import resource.ProgressStore;

class GameStateManagerTest {

    @Test
    void transitionsBetweenMenuAndLevels() {
        AudioManager audioManager = new AudioManager();
        try {
            GameStateManager manager = new GameStateManager(new GameAssets(), audioManager,
                    new ProgressStore(), () -> { });

            assertInstanceOf(MenuState.class, manager.currentState());

            manager.startLevel(1);
            assertInstanceOf(LevelState.class, manager.currentState());

            manager.showMenu();
            assertInstanceOf(MenuState.class, manager.currentState());
        } finally {
            audioManager.close();
        }
    }
}
