package ru.mikroacse.rolespell.app.model.game;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.AbstractListener;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.model.game.world.WorldManager;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameModel {
    private Listener listeners;

    public GameModel() {
        listeners = ListenerSupportFactory.create(Listener.class);

        WorldManager.instance.addListener(new WorldManager.Listener() {
            @Override
            public void worldChanged(String prevId, String currentId) {
                listeners.worldChanged(prevId, currentId);
            }
        });
    }

    public void update(float delta) {
        WorldManager.instance.getCurrentWorld().update(delta);
    }

    public void tryAttack(int x, int y) {
        World world = WorldManager.instance.getCurrentWorld();

        if (!world.getMap().isValidPosition(x, y)) {
            return;
        }

        Array<Entity> entities = world.getEntitiesAt(x, y);

        AttackAi attackAi = getControllable().getComponent(AttackAi.class);

        if (entities.size == 0) {
            attackAi.clearTargets();
        } else {
            attackAi.setTarget(entities.get(0));
        }
    }

    public void tryRouteTo(int x, int y) {
        PathMovementComponent movement = getControllable().getComponent(PathMovementComponent.class);

        // TODO: magic numbers
        movement.tryRouteTo(
                new IntVector2(x, y),
                Priority.HIGH,
                10, 15,
                0, 15);
    }

    public void stopAttacking() {
        AttackAi attackAi = getControllable().getComponent(AttackAi.class);

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

    // TODO: optimize
    public Entity getControllable() {
        return WorldManager.instance.getCurrentWorld().getPlayer();
    }

    // TODO: optimize
    public Entity getObservable() {
        return WorldManager.instance.getCurrentWorld().getPlayer();
    }

    public World getWorld() {
        return WorldManager.instance.getCurrentWorld();
    }

    public interface Listener extends AbstractListener {
        void worldChanged(String prevId, String currentId);
    }
}
