package ru.mikroacse.rolespell.app.view.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mikroacse.engine.util.Vector2;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.ItemStack;

/**
 * Created by MikroAcse on 27-Apr-17.
 */
public class InventoryView {
    private static final int CELL_OFFSET = 5;
    
    private static final int ITEM_WIDTH = 32;
    private static final int ITEM_HEIGHT = 32;
    
    private AssetBundle bundle;
    
    private Texture cell;
    private Texture woodenSwordTexture;
    
    private Inventory inventory;
    
    private ItemStack dragItem;
    private Vector2 dragPosition;
    
    public InventoryView() {
        bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);
        
        this.woodenSwordTexture = bundle.getTexture("items/weapons/wooden-sword");
        this.cell = bundle.getTexture("items/inventory-cell");
        
        dragItem = null;
        dragPosition = new Vector2();
    }
    
    public boolean draw(SpriteBatch batch, float x, float y) {
        if (inventory == null) {
            return false;
        }
        
        ItemStack[][] items = inventory.getItems();
        
        for (int i = 0; i < inventory.getWidth(); i++) {
            for (int j = 0; j < inventory.getHeight(); j++) {
                float cellX = x + i * (ITEM_WIDTH + CELL_OFFSET);
                float cellY = y + j * (ITEM_HEIGHT + CELL_OFFSET);
                
                batch.draw(cell, cellX, cellY, ITEM_WIDTH, ITEM_HEIGHT);
                
                ItemStack itemStack = items[i][j];
                
                if (itemStack == null) {
                    continue;
                }
                
                batch.draw(woodenSwordTexture, cellX, cellY, ITEM_WIDTH, ITEM_HEIGHT);
            }
        }
        
        if (dragItem != null) {
            batch.draw(woodenSwordTexture, x + dragPosition.x, y + dragPosition.y, ITEM_WIDTH, ITEM_HEIGHT);
        }
        
        return true;
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public ItemStack getDragItem() {
        return dragItem;
    }
    
    public void setDragItem(ItemStack dragItem) {
        this.dragItem = dragItem;
    }
    
    public Vector2 getDragPosition() {
        return dragPosition;
    }
    
    public void setDragPosition(Vector2 dragPosition) {
        this.dragPosition = dragPosition;
    }
    
    // TODO: ???
    
    public float getWidth() {
        if (inventory == null) {
            return 0;
        }
        
        return inventory.getWidth() * (ITEM_WIDTH + CELL_OFFSET) - CELL_OFFSET;
    }
    
    public float getHeight() {
        if (inventory == null) {
            return 0;
        }
        
        return inventory.getHeight() * (ITEM_HEIGHT + CELL_OFFSET) - CELL_OFFSET;
    }
}
