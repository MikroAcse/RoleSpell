package ru.mikroacse.rolespell.app.model.game.inventory;

import ru.mikroacse.rolespell.app.model.game.items.Item;

/**
 * Created by MikroAcse on 28-Apr-17.
 */
public class Inventory {
    private ItemList items;
    private ItemList hotbar;

    private ItemListListener itemListListener;

    public Inventory(int size, int hotbarSize) {
        items = new ItemList(size);
        hotbar = new ItemList(hotbarSize);

        itemListListener = new ItemListListener() {
            @Override
            public void itemRemoved(ItemList itemList, Item item, int index) {
                hotbar.removeItem(item);
            }
        };

        items.addListener(itemListListener);
    }

    public ItemList getItems() {
        return items;
    }

    public ItemList getHotbar() {
        return hotbar;
    }
}
