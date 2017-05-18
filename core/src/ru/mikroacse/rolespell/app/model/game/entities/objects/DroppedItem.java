package ru.mikroacse.rolespell.app.model.game.entities.objects;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class DroppedItem<T extends Item> extends Entity {
    private MovementComponent movement;

    private T item;

    public DroppedItem(World world, T item) {
        this(world, item, 0, 0);
    }

    public DroppedItem(World world, T item, int x, int y) {
        super(EntityType.DROPPED_ITEM, world);

        this.item = item;

        movement = new MovementComponent(this, x, y, 0);
        addComponent(movement);
    }

    public T getItem() {
        return item;
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
        return movement.getOrigin();
    }
}
