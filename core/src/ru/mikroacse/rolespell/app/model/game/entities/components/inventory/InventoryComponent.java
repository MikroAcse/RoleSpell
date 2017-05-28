package ru.mikroacse.rolespell.app.model.game.entities.components.inventory;

import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.objects.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.Item;

/**
 * Created by MikroAcse on 28-Apr-17.
 */
public class InventoryComponent extends Component {
    private Inventory inventory;

    public InventoryComponent(Entity entity, Inventory inventory) {
        super(entity, true);

        this.inventory = inventory;
    }

    public boolean pickup(Entity entity) {
        if (entity.getType() != EntityType.DROPPED_ITEM) {
            return false;
        }

        DroppedItem droppedItem = (DroppedItem) entity;
        Item item = droppedItem.getItem();

        if (inventory.getItems().addItem(item)) {
            droppedItem.remove();
            return true;
        }

        return false;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
