package ru.mikroacse.rolespell.app.model.game;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameModel {
    private Entity controllable;
    private Entity observable;
    private World world;

    public GameModel() {

    }

    public void update(float delta) {
        Array<Entity> entities = new Array<>(world.getEntities());

        for (Entity entity : entities) {
            entity.update(delta);
        }
    }

    public boolean tryAttack(int x, int y) {
        if (world.getMap().isValidPosition(x, y)) {
            Array<Entity> entities = world.getEntitiesAt(x, y);

            AttackAi attackAi = controllable.getComponent(AttackAi.class);

            if (entities.size == 0) {
                attackAi.clearTargets();
            } else {
                attackAi.setTarget(entities.get(0));
            }

            return true;
        }

        return false;
    }

    public boolean tryPickup(Entity entity) {
        MovementComponent movement = controllable.getComponent(MovementComponent.class);
        MovementComponent entityMovement = entity.getComponent(MovementComponent.class);

        // TODO: magic number (maximum pickup distance)
        if (movement.getPosition().distance(entityMovement.getPosition()) > 2) {
            return false;
        }

        InventoryComponent inventory = controllable.getComponent(InventoryComponent.class);

        return inventory.pickup(entity);
    }

    public boolean tryRouteTo(int x, int y) {
        PathMovementComponent movement = controllable.getComponent(PathMovementComponent.class);

        // TODO: magic numbers
        return movement.tryRouteTo(
                new IntVector2(x, y),
                Priority.HIGH,
                10,
                15,
                0,
                15) != null;
    }

    public void stopAttacking() {
        AttackAi attackAi = controllable.getComponent(AttackAi.class);

        attackAi.clearTargets();
    }

    private void attachWorld(World world) {
        observable = world.getPlayer();
        controllable = world.getPlayer();
    }

    private void detachWorld(World world) {

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
        if (this.world != null) {
            detachWorld(this.world);
        }

        this.world = world;

        if (world != null) {
            attachWorld(world);
        }
    }
}
