package ru.mikroacse.rolespell.app.model.game.inventory;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.rolespell.app.model.game.items.Item;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class ItemList {
    private Item[] items;

    private Listener listeners;

    /**
     * @param size Capacity of the item list.
     */
    public ItemList(int size) {
        items = new Item[size];

        listeners = ListenerSupportFactory.create(Listener.class);
    }

    /**
     * Sets an item at the first empty index.
     */
    public boolean addItem(Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                setItem(i, item);
                return true;
            }
        }

        return false;
    }

    /**
     * Removes appearances of an item in the list.
     */
    public boolean removeItem(Item removableItem) {
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];

            if (item == removableItem) {
                items[i] = null;

                listeners.itemRemoved(this, item, i);
                return true;
            }
        }

        return false;
    }

    public boolean swapItems(int index1, int index2) {
        Item temp = items[index1];

        setItem(index1, items[index2]);
        setItem(index2, temp);

        listeners.itemsSwapped(this, index1, index2);

        return true;
    }

    public boolean swapItems(Item item1, Item item2) {
        int index1 = indexOf(item1);
        int index2 = indexOf(item2);

        if (index1 != -1 && index2 != -1) {
            return swapItems(index1, index2);
        }

        return false;
    }

    public void setItem(int index, Item item) {
        int prevIndex = -1;
        for (int i = 0; i < items.length; i++) {
            if (items[i] == item) {
                items[i] = null;

                prevIndex = i;
                break;
            }
        }

        Item prev = items[index];

        items[index] = item;

        if (prevIndex != -1) {
            listeners.itemMoved(this, index, prevIndex);
        } else {
            listeners.itemAdded(this, index);
        }
    }

    public Item getItem(int index) {
        return items[index];
    }

    public int indexOf(Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == item) {
                return i;
            }
        }

        return -1;
    }

    public boolean hasItem(Item item) {
        return indexOf(item) != -1;
    }

    /**
     * @return Item array without null items.
     */
    public Array<Item> asArray() {
        Array<Item> itemArray = new Array<>();

        for (Item item : items) {
            if (item != null) {
                itemArray.add(item);
            }
        }

        return itemArray;
    }

    /**
     * @return Capacity of the item list.
     */
    public int getSize() {
        return items.length;
    }

    /**
     * Changes the capacity of the item list.
     */
    public void setSize(int size) {
        Item[] old = items;

        items = new Item[size];

        System.arraycopy(old, 0, items, 0, items.length);

        listeners.sizeChanged(this, size);
    }

    public void clear() {
        while (items.length > 0) {
            removeItem(items[0]);
        }
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

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void itemAdded(ItemList itemList, int index);

        void itemRemoved(ItemList itemList, Item item, int index);

        void itemMoved(ItemList itemList, int index, int prevIndex);

        void sizeChanged(ItemList itemList, int size);

        void itemsSwapped(ItemList itemList, int index1, int index2);
    }
}
