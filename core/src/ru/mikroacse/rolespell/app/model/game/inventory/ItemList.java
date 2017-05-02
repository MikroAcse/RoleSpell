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
            if(items[i] == null) {
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
            
            if(item == removableItem) {
                items[i] = null;
    
                listeners.itemSet(this, i, null, item);
                return true;
            }
        }
        
        return false;
    }
    
    public void setItem(int index, Item item) {
        for (int i = 0; i < items.length; i++) {
            if(items[i] == item) {
                items[i] = null;
                break;
            }
        }
        
        Item prev = items[index];
        
        items[index] = item;
    
        listeners.itemSet(this, index, item, prev);
    }
    
    public Item getItem(int index) {
        return items[index];
    }
    
    /**
     * @return Item array without null items.
     */
    public Array<Item> asArray() {
        Array<Item> itemArray = new Array<>();
    
        for (Item item : items) {
            if(item != null) {
                itemArray.add(item);
            }
        }
        
        return itemArray;
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
    
    /**
     * @return Capacity of the item list.
     */
    public int getSize() {
        return items.length;
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
        void itemSet(ItemList itemList, int index, Item item, Item prev);
        void sizeChanged(ItemList itemList, int size);
    }
}
