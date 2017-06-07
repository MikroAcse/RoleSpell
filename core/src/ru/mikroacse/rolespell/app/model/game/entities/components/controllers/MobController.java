package ru.mikroacse.rolespell.app.model.game.entities.components.controllers;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementListener;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusListener;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.DamageProperty;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.HealthProperty;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.Property;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.PropertyType;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.ItemType;
import ru.mikroacse.rolespell.app.model.game.items.weapons.Weapon;

/**
 * Created by Vitaly Rudenko on 19-May-17.
 */
public class MobController extends Component {
    private State state;

    private StatusListener statusListener;

    private MovementListener movementListener;

    private Inventory.Listener inventoryListener;

    private Listener listeners;

    public MobController(Entity entity) {
        super(entity, true);

        listeners = ListenerSupportFactory.create(Listener.class);

        state = State.ALIVE;
    }

    @Override
    protected void initListeners() {
        statusListener = new StatusListener() {
            @Override
            public void propertyUpdated(StatusComponent status, Property property, double previousValue, double currentValue) {
                listeners.propertyUpdated(MobController.this, property, previousValue, currentValue);

                if (property.getType() == PropertyType.HEALTH) {
                    if (isAlive() && currentValue <= 0) {
                        die();
                    }

                    if (isDead() && currentValue > 0) {
                        resurrect();
                    }
                }
            }
        };

        movementListener = new MovementListener() {
            @Override
            public void originChanged(MovementComponent movement, int prevX, int prevY, IntVector2 current) {
                listeners.originChanged(MobController.this, prevX, prevY, current);
            }

            @Override
            public void positionChanged(MovementComponent movement, int prevX, int prevY, IntVector2 current) {
                listeners.positionChanged(MobController.this, prevX, prevY, current);
            }
        };

        inventoryListener = new Inventory.Listener() {
            @Override
            public void selected(Inventory inventory, int index, Item item) {
                reselectItem();
            }
        };
    }

    public void reselectItem() {
        if(!getEntity().hasComponent(InventoryComponent.class)) {
            return;
        }

        StatusComponent status = getStatus();

        if(!status.hasProperty(DamageProperty.class)) {
            return;
        }

        Inventory inventory = getInventory().getInventory();
        Item item = inventory.getSelectedItem();

        AttackAi attackAi = getEntity().getComponent(AttackAi.class);
        DamageProperty damage = status.getProperty(DamageProperty.class);

        if(item != null && item.getType() == ItemType.WEAPON) {
            Weapon weapon = (Weapon) item;

            damage.setInterval(weapon.getDamage());

            damage.setAttackDistance(weapon.getAttackDistance());
            attackAi.setAttackDistance(weapon.getAttackDistance());

            attackAi.setAttackTimer(weapon.getAttackTimer());

            System.out.println("weapon: " + weapon.getName());
            System.out.println("timer: " + weapon.getAttackTimer());

            damage.resume();
        } else {
            System.out.println("damage paused");
            damage.pause();
        }
    }

    public void die() {
        if(getEntity().getType() == EntityType.PLAYER) {
            System.out.println("Player nearly died");
            return;
        }

        StatusComponent status = getStatus();

        if (status.hasProperty(HealthProperty.class)) {
            HealthProperty health = status.getProperty(HealthProperty.class);
            health.pause();
        }

        listeners.died(this);
    }

    public void resurrect() {
        StatusComponent status = getStatus();

        if (status.hasProperty(HealthProperty.class)) {
            HealthProperty health = status.getProperty(HealthProperty.class);
            health.resume();
        }

        listeners.resurrected(this);
    }

    public boolean isAlive() {
        return state == State.ALIVE;
    }

    public boolean isDead() {
        return state == State.DEAD;
    }

    @Override
    protected void attachEntity(Entity entity) {
        super.attachEntity(entity);

        getStatus().addListener(statusListener);
        getMovement().addListener(movementListener);

        if(entity.hasComponent(InventoryComponent.class)) {
            getInventory().getInventory().addListener(inventoryListener);
        }

        reselectItem();
    }

    @Override
    protected void detachEntity(Entity entity) {
        super.detachEntity(entity);

        getStatus().removeListener(statusListener);
        getMovement().removeListener(movementListener);

        if(entity.hasComponent(InventoryComponent.class)) {
            getInventory().getInventory().removeListener(inventoryListener);
        }
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

    public StatusComponent getStatus() {
        return getEntity().getComponent(StatusComponent.class);
    }

    public MovementComponent getMovement() {
        return getEntity().getComponent(MovementComponent.class);
    }

    public InventoryComponent getInventory() { return getEntity().getComponent(InventoryComponent.class); }

    public enum State {
        ALIVE,
        DEAD
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        // MovementComponent.ActionListener
        void positionChanged(MobController controller, int prevX, int prevY, IntVector2 current);

        void originChanged(MobController controller, int prevX, int prevY, IntVector2 current);

        // Original events
        void died(MobController controller);

        void resurrected(MobController controller);

        void propertyUpdated(MobController controller, Property property, double previousValue, double currentValue);
    }
}
