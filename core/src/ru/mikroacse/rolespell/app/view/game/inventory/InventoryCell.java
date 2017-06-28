package ru.mikroacse.rolespell.app.view.game.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class InventoryCell extends Image {
    private CellType cellType;

    public InventoryCell() {
        super(getCellTexture(CellType.DEFAULT));

        cellType = CellType.DEFAULT;
    }

    private static Texture getCellTexture(CellType cellType) {
        AssetBundle bundle = bundle(Bundle.GAME);

        return bundle.getTexture("inventory/cell-" + cellType.getName());
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        if (this.cellType == cellType) {
            return;
        }

        this.cellType = cellType;

        setDrawable(new SpriteDrawable(new Sprite(getCellTexture(cellType))));
    }

    public enum CellType {
        DEFAULT("default"),
        HIGHLIGHTED("highlighted"), // when mouse over cell
        SELECTED("selected"); // i.e. for hotbar cells, when they are selected by player ; ;

        private String name;

        CellType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
