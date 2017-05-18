package ru.mikroacse.rolespell.app.view.game.world;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 07-May-17.
 */
public class CellSelector extends Image {
    public CellSelector() {
        super(RoleSpell
                .getAssetManager()
                .getBundle(AssetManager.Bundle.GAME)
                .getTexture("ui/cell-selector"));
    }
}
