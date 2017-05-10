package ru.mikroacse.rolespell.app.view.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import ru.mikroacse.engine.actors.RealActor;
import ru.mikroacse.engine.actors.TextActor;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 07-May-17.
 */
public class EntityName extends TextButton implements RealActor {
    private TextActor text;
    private Button background;

    public EntityName(String name) {
        super(name, new TextButtonStyle(null, null, null, new BitmapFont()));

        // TODO: ↓
        NinePatchDrawable npd = new NinePatchDrawable(new NinePatch(RoleSpell.getAssetManager()
                .getBundle(AssetManager.Bundle.GAME)
                .getTexture("entities/entity-name"), 17,17,17,17));

        getStyle().up = npd;
    }

    @Override
    public float getRealWidth() {
        return 0;
    }

    @Override
    public float getRealHeight() {
        return 0;
    }
}
