package ru.mikroacse.rolespell.app.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mikroacse.rolespell.app.controller.game.states.GameStateProcessor;
import ru.mikroacse.rolespell.app.controller.game.states.InventoryStateProcessor;
import ru.mikroacse.rolespell.app.controller.game.states.StateProcessor;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;
import ru.mikroacse.rolespell.app.view.game.GameRenderer.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameController {
    private GameRenderer renderer;
    private GameModel model;

    private Map<State, StateProcessor> stateProcessors;

    private GameStateProcessor gameState;
    private InventoryStateProcessor inventoryState;

    private InputAdapter input;

    public GameController(GameRenderer renderer, GameModel model) {
        this.renderer = renderer;
        this.model = model;

        input = new InputAdapter();
        Gdx.input.setInputProcessor(input);

        // TODO: don't depend on concrete implementations of StateProcessor
        gameState = new GameStateProcessor(this);
        inventoryState = new InventoryStateProcessor(this);

        stateProcessors = new HashMap<>();
        stateProcessors.put(State.GAME, gameState);
        stateProcessors.put(State.INVENTORY, inventoryState);

        setState(State.GAME);
    }

    public void update(float delta) {
        State state = renderer.getState();

        renderer.setCursorPosition(input.getMouseX(), input.getMouseY());

        InputAdapter.Button inventoryButton = input.getButton(Input.Keys.I);
        InputAdapter.Button questsButton = input.getButton(Input.Keys.TAB);

        if (inventoryButton.justPressed) {
            if (state == State.INVENTORY) {
                state = State.GAME;
            } else {
                state = State.INVENTORY;
            }

            setState(state);
        }

        if(questsButton.justPressed) {
            if(state == State.QUESTS) {
                state = State.GAME;
            } else {
                state = State.QUESTS;
            }

            setState(state);
        }

        stateProcessors.get(state).process();

        model.update(delta);
        input.update();
    }

    public void setState(State newState) {
        State currentState = renderer.getState();

        if(newState == currentState) {
            return;
        }

        if(currentState != null) {
            stateProcessors.get(currentState).pause();
        }

        renderer.setState(newState);

        stateProcessors.get(newState).resume();
    }

    public GameStateProcessor getGameState() {
        return gameState;
    }

    public InventoryStateProcessor getInventoryState() {
        return inventoryState;
    }

    public GameRenderer getRenderer() {
        return renderer;
    }

    public GameModel getModel() {
        return model;
    }

    public InputAdapter getInput() {
        return input;
    }
}
