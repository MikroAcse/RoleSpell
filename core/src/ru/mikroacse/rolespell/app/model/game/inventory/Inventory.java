package ru.mikroacse.rolespell.app.model.game.inventory;

import ru.mikroacse.rolespell.app.model.game.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikroAcse on 28-Apr-17.
 */
public class Inventory {
    private ItemList items;
    private ItemList hotbar;
    
    public Inventory(int size, int hotbarSize) {
        items = new ItemList(size);
        hotbar = new ItemList(hotbarSize);
    }
    
    public void removeItem(Item item) {
        items.removeItem(item);
        hotbar.removeItem(item);
    }
    
    public ItemList getItems() {
        return items;
    }
    
    public ItemList getHotbar() {
        return hotbar;
    }
}
