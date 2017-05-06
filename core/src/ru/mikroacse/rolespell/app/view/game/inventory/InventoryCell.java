package ru.mikroacse.rolespell.app.view.game.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class InventoryCell extends Image {
    public InventoryCell() {
        super(RoleSpell.getAssetManager()
                .getBundle(AssetManager.Bundle.GAME)
                .getTexture("inventory/inventory-cell"));
    }
}
