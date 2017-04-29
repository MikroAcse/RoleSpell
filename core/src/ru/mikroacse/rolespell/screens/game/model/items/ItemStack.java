package ru.mikroacse.rolespell.screens.game.model.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikroAcse on 28-Apr-17.
 */
public class ItemStack<T extends Item> {
    private Class<T> itemClass;
    private List<T> items;
    
    public ItemStack(Class<T> itemClass) {
        this.items = new ArrayList<>();
        this.itemClass = itemClass;
    }
    
    public ItemStack(Class<T> itemClass, T item) {
        this(itemClass);
        
        addItem(item);
    }
    
    public ItemStack(Class<T> itemClass, List<T> items) {
        this(itemClass);
        
        addItems(items);
    }
    
    public void addItems(List<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }
    
    public boolean addItem(T item) {
        if (!isEmpty() && !item.isStackable()) {
            return false;
        }
        
        return items.add(item);
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public boolean removeItem(T item) {
        return items.remove(item);
    }
    
    public void clearItems() {
        items.clear();
    }
    
    public List<T> getItems() {
        return items;
    }
    
    public int getAmount() {
        return items.size();
    }
    
    public Class<T> getItemClass() {
        return itemClass;
    }
}
