package ru.mikroacse.rolespell.app.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mikroacse.rolespell.app.controller.game.states.GameStateProcessor;
import ru.mikroacse.rolespell.app.controller.game.states.InventoryStateProcessor;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameController {
    private GameRenderer renderer;
    private GameModel model;

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
    }

    public void update(float delta) {
        GameRenderer.State state = renderer.getState();

        InputAdapter.Button inventoryButton = input.getButton(Input.Keys.I);

        if (inventoryButton.justPressed) {
            if (state == GameRenderer.State.GAME) {
                state = GameRenderer.State.INVENTORY;
            } else if (state == GameRenderer.State.INVENTORY) {
                state = GameRenderer.State.GAME;
            }

            renderer.setState(state);
        }

        switch (state) {
            case GAME:
                gameState.process();
                break;
            case INVENTORY:
                inventoryState.process();
                break;
            case QUESTS:
                break;
        }


        model.update(delta);
        input.update();
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
