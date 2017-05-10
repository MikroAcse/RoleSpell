package ru.mikroacse.rolespell.app.view.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 06-May-17.
 */
public class GameCursor extends Image {
    private Cursor cursor;

    public GameCursor() {
        super(getCursorTexture(Cursor.POINTER));

        cursor = Cursor.POINTER;
        setScale(2f); // TODO: magic number
    }

    private static Texture getCursorTexture(Cursor cursor) {
        AssetBundle bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);

        return bundle.getTexture("ui/cursor/" + cursor.getName());
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        if (this.cursor == cursor) {
            return;
        }

        this.cursor = cursor;

        setDrawable(new SpriteDrawable(new Sprite(getCursorTexture(cursor))));
    }

    public enum Cursor {
        POINTER("pointer"),
        TAKE("take"),
        DRAG("drag"),
        ATTACK("attack"),
        QUESTION("question");

        private String name;

        Cursor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
