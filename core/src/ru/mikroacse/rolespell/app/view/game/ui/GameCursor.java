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
    private Type type;

    public GameCursor() {
        super(RoleSpell
                .getAssetManager()
                .getBundle(AssetManager.Bundle.GAME)
                .getTexture("ui/cursor/pointer"));

        setScale(2f);
        setType(Type.POINTER);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if (this.type == type) {
            return;
        }

        this.type = type;

        AssetBundle bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);

        Texture texture = bundle.getTexture("ui/cursor/" + type.getName());

        setDrawable(new SpriteDrawable(new Sprite(texture)));
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
