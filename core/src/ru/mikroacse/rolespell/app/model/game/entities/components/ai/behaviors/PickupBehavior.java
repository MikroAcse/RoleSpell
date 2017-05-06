package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.app.model.game.entities.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by MikroAcse on 02-May-17.
 */
public class PickupBehavior extends Behavior {
    public PickupBehavior() {
        super(Priority.IMMEDIATELY, true, EnumSet.of(Trigger.MOVEMENT));
    }

    @Override
    public boolean process(Entity entity, List<Entity> targets) {
        if (targets.isEmpty()) {
            return false;
        }

        if (!entity.hasComponent(InventoryComponent.class)) {
            return false;
        }

        World world = entity.getWorld();
        Inventory inventory = entity.getComponent(InventoryComponent.class).getInventory();

        boolean pickedUp = false;

        for (Entity target : targets) {
            if (target.getType() != EntityType.DROPPED_ITEM) {
                continue;
            }

            DroppedItem droppedItem = (DroppedItem) target;
            Item item = droppedItem.getItem();

            world.removeEntity(droppedItem);

            inventory.getItems().addItem(item);

            pickedUp = true;
        }

        return pickedUp;
    }
}
