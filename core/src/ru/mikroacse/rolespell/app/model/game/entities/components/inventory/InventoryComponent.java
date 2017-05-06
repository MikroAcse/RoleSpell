package ru.mikroacse.rolespell.app.model.game.entities.components.inventory;

import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;

/**
 * Created by MikroAcse on 28-Apr-17.
 */
public class InventoryComponent extends Component {
    private Inventory inventory;

    public InventoryComponent(Entity entity, Inventory inventory) {
        super(entity);

        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
