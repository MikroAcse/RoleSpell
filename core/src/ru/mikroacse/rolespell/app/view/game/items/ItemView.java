package ru.mikroacse.rolespell.app.view.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.bundle;

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
        return bundle(Bundle.GAME).getTexture(item.getConfig().getTexture(null));
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
