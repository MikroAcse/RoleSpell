package ru.mikroacse.rolespell.app.view.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.ItemStack;
import ru.mikroacse.rolespell.app.model.game.items.weapons.WoodenSword;

/**
 * Created by MikroAcse on 27-Apr-17.
 */
public class HotbarView {
    private static final int CELL_OFFSET = 5;
    private static final int CELL_WIDTH = 32;
    private static final int CELL_HEIGHT = 32;
    
    private AssetBundle bundle;
    
    private Texture cell;
    private Texture woodenSwordTexture;
    
    private Inventory inventory;
    
    public HotbarView() {
        bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);
        
        this.woodenSwordTexture = bundle.getTexture("items/weapons/wooden-sword");
        this.cell = bundle.getTexture("items/inventory-cell");
    }
    
    public boolean draw(SpriteBatch batch, float x, float y) {
        if (inventory == null) {
            return false;
        }
        
        ItemStack[] items = inventory.getHotbarItems();
        
        for (int i = 0; i < inventory.getHotbarSize(); i++) {
            float cellX = x + i * (CELL_WIDTH + CELL_OFFSET);
            float cellY = y;
            
            batch.draw(cell, cellX, cellY, CELL_WIDTH, CELL_HEIGHT);
            
            ItemStack itemStack = items[i];
            
            if (itemStack == null) {
                continue;
            }
            
            if (itemStack.getItemClass().equals(WoodenSword.class)) {
                batch.draw(woodenSwordTexture, cellX, cellY, CELL_WIDTH, CELL_HEIGHT);
            }
        }
        
        return true;
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    
    // TODO: ???
    
    public float getWidth() {
        if (inventory == null) {
            return 0;
        }
        
        return inventory.getHotbarSize() * (CELL_WIDTH + CELL_OFFSET) - CELL_OFFSET;
    }
    
    public float getHeight() {
        if (inventory == null) {
            return 0;
        }
        
        return CELL_HEIGHT;
    }
}
