package ru.mikroacse.rolespell.app.model.game.entities.mobs;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.controllers.MobController;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Created by Vitaly Rudenko on 19-May-17.
 */
public abstract class Mob extends Entity {
    private InventoryComponent inventory;
    private PathMovementComponent movement;
    private StatusComponent status;
    private MobController mobController;

    public Mob(EntityType type, World world, int x, int y, float speed) {
        super(type, world);

        setParameters(EnumSet.of(Parameter.SOLID));

        movement = new PathMovementComponent(this, x, y, speed);
        addComponent(movement);

        movement.setType(MovementComponent.UpdateType.POSITION);

        status = new StatusComponent(this);
        addComponent(status);

        mobController = new MobController(this);
        addComponent(mobController);

        // TODO: magic numbers
        inventory = new InventoryComponent(this, new Inventory(12, 3));
        addComponent(inventory);
    }

    @Override
    public void setPosition(int x, int y) {
        movement.setPosition(x, y);
    }

    @Override
    public IntVector2 getPosition() {
        return movement.getPosition();
    }

    @Override
    public void setOrigin(int x, int y) {
        movement.setOrigin(x, y);
    }

    @Override
    public IntVector2 getOrigin() {
        return movement.getPosition();
    }

    public PathMovementComponent getMovement() {
        return movement;
    }

    public InventoryComponent getInventory() {
        return inventory;
    }

    public StatusComponent getStatus() {
        return status;
    }

    public MobController getController() {
        return mobController;
    }
}
