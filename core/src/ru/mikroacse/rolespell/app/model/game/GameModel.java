package ru.mikroacse.rolespell.app.model.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemRepository;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.media.AssetManager;

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

        ItemRepository itemRepository = ItemRepository.getInstance();

        JsonValue items = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME).getConfig("items");

        for (JsonValue value : items.iterator()) {
            itemRepository.addItemConfig(value.name, value);
        }
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

    public boolean tryPickup(Entity entity) {
        // TODO: magic number (maximum pickup distance)
        if (controllable.getPosition().distance(entity.getPosition()) > 2) {
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
