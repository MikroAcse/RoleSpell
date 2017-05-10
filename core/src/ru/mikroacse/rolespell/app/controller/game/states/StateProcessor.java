package ru.mikroacse.rolespell.app.controller.game.states;

import ru.mikroacse.rolespell.app.controller.game.GameController;

/**
 * Created by MikroAcse on 01-May-17.
 */
public abstract class StateProcessor {
    private GameController controller;

    private boolean paused;

    public StateProcessor(GameController controller) {
        this.controller = controller;
    }

    public abstract void process();

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public GameController getController() {
        return controller;
    }

    public boolean isPaused() {
        return paused;
    }
}
