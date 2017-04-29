package ru.mikroacse.rolespell.screens.game.model.entities.components.inventory;

import ru.mikroacse.rolespell.screens.game.model.entities.components.Component;
import ru.mikroacse.rolespell.screens.game.model.entities.core.Entity;
import ru.mikroacse.rolespell.screens.game.model.inventory.Inventory;

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
    
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
