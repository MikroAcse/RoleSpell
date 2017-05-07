package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class DroppedItem<T extends Item> extends Entity {
    private MovementComponent movement;

    private T item;

    public DroppedItem(T item, World world) {
        super(EntityType.DROPPED_ITEM, world);

        this.item = item;

        movement = new MovementComponent(this, 0, 0, 0);
        addComponent(movement);
    }

    @Override
    public void dispose() {

    }

    public T getItem() {
        return item;
    }
}
