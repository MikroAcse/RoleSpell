package ru.mikroacse.rolespell.app.model.game;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameModel {
    private Entity controllable;
    private Entity observable;
    private World world;

    private Listener listeners;

    public GameModel() {
        listeners = ListenerSupportFactory.create(Listener.class);
    }

    public void update(float delta) {
        Array<Entity> entities = new Array<>(world.getEntities());

        for (Entity entity : entities) {
            entity.update(delta);
        }
    }

    public boolean tryAttack(int x, int y) {
        if (!world.getMap().isValidPosition(x, y)) {
            return false;
        }

        Array<Entity> entities = world.getEntitiesAt(x, y);

        AttackAi attackAi = controllable.getComponent(AttackAi.class);

        if (entities.size == 0) {
            attackAi.clearTargets();
        } else {
            attackAi.setTarget(entities.get(0));
        }

        return true;
    }

    public boolean tryRouteTo(int x, int y) {
        PathMovementComponent movement = controllable.getComponent(PathMovementComponent.class);

        // TODO: magic numbers
        return movement.tryRouteTo(
                new IntVector2(x, y),
                Priority.HIGH,
                10, 15,
                0, 15) != null;
    }

    public void stopAttacking() {
        AttackAi attackAi = controllable.getComponent(AttackAi.class);

        attackAi.clearTargets();
    }

    public void addListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).addListener(listener);
    }

    public void removeListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<Listener>) listeners).clearListeners();
    }

    private void attachWorld(World world) {
        observable = world.getPlayer();
        controllable = world.getPlayer();
    }

    private void detachWorld(World world) {
        observable = null;
        controllable = null;
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
        World previous = this.world;

        if (this.world != null) {
            detachWorld(this.world);
        }

        this.world = world;

        if (world != null) {
            attachWorld(world);
        }

        listeners.worldChanged(this, previous, world);
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void worldChanged(GameModel model, World previous, World current);
    }
}
