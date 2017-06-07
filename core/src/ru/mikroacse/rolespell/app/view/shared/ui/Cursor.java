package ru.mikroacse.rolespell.app.view.shared.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.media.AssetManager.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.getAssetManager;

/**
 * Created by MikroAcse on 06-May-17.
 */
public class Cursor extends Image {
    private Type type;

    public Cursor() {
        super(getTexture(Type.POINTER));

        type = Type.POINTER;
        setScale(2f); // TODO: magic number
    }

    private static Texture getTexture(Type type) {
        AssetBundle bundle = getAssetManager().getBundle(Bundle.GLOBAL);

        return bundle.getTexture("ui/cursor/" + type.getName());
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if (this.type == type) {
            return;
        }

        this.type = type;

        setDrawable(new SpriteDrawable(new Sprite(getTexture(type))));
    }

    public enum Type {
        POINTER("pointer"),
        TAKE("take"),
        DRAG("drag"),
        ATTACK("attack"),
        QUESTION("question");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
