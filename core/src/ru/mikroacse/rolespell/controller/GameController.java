package ru.mikroacse.rolespell.controller;

import com.badlogic.gdx.Gdx;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.entities.Player;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.rolespell.view.game.GameRenderer;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.Priority;

import java.util.List;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameController {
    private GameRenderer renderer;
    private GameModel model;

    private GameInputAdapter inputAdapter;

    public GameController(GameRenderer renderer, GameModel model) {
        this.renderer = renderer;
        this.model = model;

        inputAdapter = new GameInputAdapter();
        Gdx.input.setInputProcessor(inputAdapter);
    }

    public void update(float delta) {
        if (inputAdapter.isJustTouched()) {
            cellTouched();
        }

        model.update(delta);
        inputAdapter.update();
    }

    // TODO: it should be in model
    private void cellTouched() {
        // TODO: change player to 'controllable'
        Player player = model.getPlayer();
        World world = model.getWorld();
        PathMovementComponent movement = player.getComponent(PathMovementComponent.class);

        movement.clearPath();

        int mouseX = inputAdapter.getMouseX();
        int mouseY = inputAdapter.getMouseY();

        Position coordinates = renderer.globalToLocal(mouseX, mouseY);
        Position touchedCell = world.getCellPosition(coordinates.x, coordinates.y);

        if (world.isValidPosition(touchedCell)) {
            // TODO: bad and magic
            // looking for the nearest passable cells
            List<Position> passableCells = world.getPassableCells(
                    touchedCell.x,
                    touchedCell.y,
                    false,
                    0,
                    5,
                    false);

            Position destination = null;

            // checking passable cells for available paths
            for (Position passableCell : passableCells) {
                if (moveTo(passableCell, Priority.NORMAL)) {
                    destination = passableCell;
                    break;
                }
            }

            // no path found to any nearest cell
            if (destination == null) {
                System.out.println("Path not found!");
            }
        }
    }

    public boolean moveTo(Position destination, Priority priority) {
        Player player = model.getPlayer();
        PathMovementComponent movement = player.getComponent(PathMovementComponent.class);

        // TODO: magic numbers
        return movement.routeTo(destination, priority, 10, 15);
    }
}
