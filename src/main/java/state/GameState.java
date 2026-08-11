package state;

import java.awt.Graphics2D;

import core.InputState;

public interface GameState {

    void update(double deltaSeconds, InputState input);

    void render(Graphics2D graphics);
}
