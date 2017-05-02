package ru.mikroacse.rolespell.app.view.game.items;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 30-Apr-17.
 */
public class ItemView extends Image {
    private Item item;
    
    public ItemView(Item item) {
        super(RoleSpell.getAssetManager()
                       .getBundle(AssetManager.Bundle.GAME)
                       .getTexture("items/weapons/wooden-sword"));
        
        this.item = item;
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
}
