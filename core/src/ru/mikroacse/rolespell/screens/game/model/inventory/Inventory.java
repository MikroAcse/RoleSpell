package ru.mikroacse.rolespell.screens.game.model.inventory;

import ru.mikroacse.rolespell.screens.game.model.items.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikroAcse on 28-Apr-17.
 */
public class Inventory {
    protected ItemStack[][] items;
    protected ItemStack[] hotbar;
    protected int hotbarSize;
    private int width;
    private int height;
    
    public Inventory(int width, int height, int hotbarSize) {
        this.width = width;
        this.height = height;
        this.hotbarSize = hotbarSize;
        
        this.items = new ItemStack[width][height];
        this.hotbar = new ItemStack[hotbarSize];
    }
    
    public ItemStack getItem(int x, int y) {
        return items[x][y];
    }
    
    public void setItem(int x, int y, ItemStack item) {
        items[x][y] = item;
    }
    
    public ItemStack getHotbarItem(int index) {
        return hotbar[index];
    }
    
    public void setHotbarItem(int index, ItemStack item) {
        hotbar[index] = item;
    }
    
    public List<ItemStack> getItemList() {
        List<ItemStack> result = new ArrayList<>();
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (items[i][j] != null) {
                    result.add(items[i][j]);
                }
            }
        }
        
        return result;
    }
    
    public ItemStack[][] getItems() {
        return items;
    }
    
    public List<ItemStack> getHotbarItemList() {
        List<ItemStack> result = new ArrayList<>();
        
        for (int i = 0; i < hotbarSize; i++) {
            if (hotbar[i] != null) {
                result.add(hotbar[i]);
            }
        }
        
        return result;
    }
    
    public ItemStack[] getHotbarItems() {
        return hotbar;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getHotbarSize() {
        return hotbarSize;
    }
}
