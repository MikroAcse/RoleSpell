package ru.mikroacse.rolespell.app.view.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 30-Apr-17.
 */
public class ItemView extends Image {
    private Item item;

    public ItemView(Item item) {
        super(getItemTexture(item));

        this.item = item;

        // TODO: magic numbers
        setWidth(32);
        setHeight(32);
    }

    public static Texture getItemTexture(Item item) {
        AssetBundle bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);

        return bundle.getTexture(item.getConfig().getTexture());
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
