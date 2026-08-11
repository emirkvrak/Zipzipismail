package state;

import java.awt.Graphics2D;
import java.awt.Point;

import core.InputState;
import resource.AudioManager;
import resource.GameAssets;
import resource.SoundEffect;
import resource.ProgressStore;
import ui.ControlsRenderer;
import ui.MenuRenderer;

public final class MenuState extends AbstractGameState {

    private static final int CONTROLS_OPTION = 3;
    private static final int EXIT_OPTION = 4;
    private final MenuRenderer menuRenderer;
    private final AudioManager audioManager;
    private final ProgressStore progressStore;
    private final Runnable exitAction;
    private final ControlsRenderer controlsRenderer;
    private boolean controlsVisible;
    private int selectedOption;
    private Point mousePosition = new Point(-1, -1);

    public MenuState(GameStateManager stateManager, GameAssets assets, AudioManager audioManager,
            ProgressStore progressStore, Runnable exitAction) {
        super(stateManager, assets);
        this.audioManager = audioManager;
        this.progressStore = progressStore;
        this.exitAction = exitAction;
        menuRenderer = new MenuRenderer(assets);
        controlsRenderer = new ControlsRenderer();
    }

    @Override
    public void update(double deltaSeconds, InputState input) {
        mousePosition = input.mousePosition();
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
            selectedOption = EXIT_OPTION;
        }
        if (input.consumeConfirm()) {
            selectCurrentOption();
        }
        if (input.consumeUp()) {
            selectedOption = (selectedOption - 1 + options().length) % options().length;
            audioManager.play(SoundEffect.MENU_MOVE);
        }
        if (input.consumeDown()) {
            selectedOption = (selectedOption + 1) % options().length;
            audioManager.play(SoundEffect.MENU_MOVE);
        }

        int hoveredOption = menuRenderer.optionAt(mousePosition.x, mousePosition.y, options().length);
        if (hoveredOption >= 0 && hoveredOption != selectedOption) {
            selectedOption = hoveredOption;
            audioManager.play(SoundEffect.MENU_MOVE);
        }

        Point click = input.consumeMouseClick();
        if (click != null) {
            int clickedOption = menuRenderer.optionAt(click.x, click.y, options().length);
            if (clickedOption >= 0) {
                selectedOption = clickedOption;
                selectCurrentOption();
            }
        }
    }

    private void selectCurrentOption() {
        audioManager.play(SoundEffect.MENU_CONFIRM);
        switch (selectedOption) {
            case 0 -> stateManager.startLevel(1);
            case 1 -> stateManager.startLevel(2);
            case 2 -> stateManager.startLevel(3);
            case CONTROLS_OPTION -> controlsVisible = true;
            case EXIT_OPTION -> exitAction.run();
            default -> throw new IllegalStateException("Geçersiz menü seçimi: " + selectedOption);
        }
    }

    private String[] options() {
        return new String[] {
                "1. Bölüm", "2. Bölüm", "3. Bölüm", "Kontroller", "Çıkış"
        };
    }

    @Override
    public void render(Graphics2D graphics) {
        Graphics2D canvas = (Graphics2D) graphics.create();
        try {
            if (controlsVisible) {
                controlsRenderer.render(canvas, audioManager.isMuted(),
                        mousePosition.x, mousePosition.y);
            } else {
                menuRenderer.render(canvas, options(), selectedOption, progressStore);
            }
        } finally {
            canvas.dispose();
        }
    }

}
