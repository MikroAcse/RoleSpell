package ru.mikroacse.rolespell.app.model.game;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameModel {
    private Entity controllable;
    private Entity observable;
    private World world;

    public GameModel() {

    }

    private void initializeWorld() {
        observable = world.getPlayer();
        controllable = world.getPlayer();
    }

    public void update(float delta) {
        List<Entity> entities = new ArrayList<>(world.getEntities());

        for (Entity entity : entities) {
            entity.update(delta);
        }
    }

    public boolean tryAttack(int x, int y) {
        if (world.isValidPosition(x, y)) {
            List<Entity> entities = world.getEntitiesAt(x, y);

            BehaviorAi behaviorAi = controllable.getComponent(BehaviorAi.class);

            if (entities.isEmpty()) {
                behaviorAi.clearTargets();
            } else {
                behaviorAi.setTargetSelectors(entities.get(0));
            }

            return true;
        }

        return false;
    }

    public boolean tryRouteTo(int x, int y) {
        if (world.isValidPosition(x, y)) {
            // TODO: bad and magic
            // looking for the nearest passable cells
            List<IntVector2> passableCells = world.getPassableCells(
                    x,
                    y,
                    false,
                    0,
                    5,
                    false);

            // checking passable cells for available paths
            return tryRouteTo(passableCells, Priority.HIGH);
        }

        return false;
    }

    private boolean tryRouteTo(List<IntVector2> destinations, Priority priority) {
        PathMovementComponent movement = controllable.getComponent(PathMovementComponent.class);

        // TODO: magic numbers
        return movement.tryRouteTo(destinations, priority, 10, 15);
    }

    public Entity getControllable() {
        return controllable;
    }

    public Entity getObservable() {
        return observable;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;

        initializeWorld();
    }
}
