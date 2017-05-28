package ru.mikroacse.rolespell.app.model.game.inventory;

import ru.mikroacse.engine.listeners.Listener;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.rolespell.app.model.game.items.Item;

/**
 * Created by MikroAcse on 28-Apr-17.
 */
public class Inventory {
    private ItemList items;
    private ItemList hotbar;

    private int selected;

    private ItemListListener itemListListener;
    private ItemListListener hotbarListener;

    private Listener listeners;

    public Inventory(int size, int hotbarSize) {
        listeners = ListenerSupportFactory.create(Listener.class);

        items = new ItemList(size);
        hotbar = new ItemList(hotbarSize);

        selected = 0;

        itemListListener = new ItemListListener() {
            @Override
            public void itemRemoved(ItemList itemList, Item item, int index) {
                hotbar.removeItem(item);
            }
        };

        hotbarListener = new ItemListListener() {
            @Override
            public void itemSet(ItemList itemList, int index, Item item) {
                if(index == selected) {
                    listeners.selected(Inventory.this, index, item);
                }
            }
        };

        items.addListener(itemListListener);
        hotbar.addListener(hotbarListener);
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

    public ItemList getItems() {
        return items;
    }

    public ItemList getHotbar() {
        return hotbar;
    }

    public Item getSelectedItem() {
        return hotbar.getItem(selected);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        if(this.selected == selected) {
            return;
        }

        this.selected = selected;

        listeners.selected(this, selected, hotbar.getItem(selected));
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void selected(Inventory inventory, int index, Item item);
    }
}
