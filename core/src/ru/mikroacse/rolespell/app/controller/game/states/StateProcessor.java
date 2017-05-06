package ru.mikroacse.rolespell.app.controller.game.states;

import ru.mikroacse.rolespell.app.controller.game.GameController;

/**
 * Created by MikroAcse on 01-May-17.
 */
public abstract class StateProcessor {
    private GameController controller;

    public StateProcessor(GameController controller) {
        this.controller = controller;
    }

    public abstract void process();

    public GameController getController() {
        return controller;
    }
}
