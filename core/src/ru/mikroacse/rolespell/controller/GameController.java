package ru.mikroacse.rolespell.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
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

    private GameInputAdapter input;

    public GameController(GameRenderer renderer, GameModel model) {
        this.renderer = renderer;
        this.model = model;

        input = new GameInputAdapter();
        Gdx.input.setInputProcessor(input);
    }

    public void update(float delta) {
        GameInputAdapter.Button mouseLeft = input.getButton(Input.Buttons.LEFT);
        GameInputAdapter.Button mouseRight = input.getButton(Input.Buttons.RIGHT);

        if (mouseLeft.justPressed) {
            routeAction(input.getMouseX(), input.getMouseY());
        }

        if (mouseRight.justPressed) {
            attackAction(input.getMouseX(), input.getMouseY());
        }

        model.update(delta);
        input.update();
    }

    // TODO: it should be in model?
    private void attackAction(int x, int y) {
        Entity controllable = model.getControllable();
        World world = model.getWorld();
        PathMovementComponent movement = controllable.getComponent(PathMovementComponent.class);

        Position coordinates = renderer.globalToLocal(x, y);
        Position cell = world.getCellPosition(coordinates.x, coordinates.y);

        if (world.isValidPosition(cell)) {
            List<Entity> entities = world.getEntitiesAt(cell);

            BehaviorAi behaviorAi = controllable.getComponent(BehaviorAi.class);

            if (entities.isEmpty()) {
                behaviorAi.clearTargets();
            } else {
                behaviorAi.setTarget(entities.get(0));
            }
        }
    }

    private void routeAction(int x, int y) {
        // TODO: change player to 'controllable'
        Entity controllable = model.getControllable();
        World world = model.getWorld();
        PathMovementComponent movement = controllable.getComponent(PathMovementComponent.class);

        Position coordinates = renderer.globalToLocal(x, y);
        Position cell = world.getCellPosition(coordinates.x, coordinates.y);

        if (world.isValidPosition(cell)) {
            // TODO: bad and magic
            // looking for the nearest passable cells
            List<Position> passableCells = world.getPassableCells(
                    cell.x,
                    cell.y,
                    false,
                    0,
                    5,
                    false);

            Position destination = null;

            // checking passable cells for available paths
            for (Position passableCell : passableCells) {
                if (routeTo(passableCell, Priority.NORMAL)) {
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

    public boolean routeTo(Position destination, Priority priority) {
        Entity entity = model.getControllable();
        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);

        // TODO: magic numbers
        return movement.routeTo(destination, priority, 10, 15);
    }
}
