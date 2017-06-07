package ru.mikroacse.rolespell.app.view.settings.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import ru.mikroacse.engine.actors.MeasurableActor;
import ru.mikroacse.engine.actors.TextActor;
import ru.mikroacse.rolespell.app.model.menu.MenuAction;
import ru.mikroacse.rolespell.app.model.settings.SettingsAction;
import ru.mikroacse.rolespell.media.AssetManager.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.getAssetManager;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class SettingsButton extends Group implements MeasurableActor {
    private TextActor text;
    private Button background;

    private SettingsAction action;

    public SettingsButton(String label, SettingsAction action) {
        super();

        this.action = action;

        // TODO: ↓
        NinePatchDrawable npd = new NinePatchDrawable(new NinePatch(getAssetManager()
                .getBundle(Bundle.SETTINGS)
                .getTexture("button-background"), 29,29,29,29));

        background = new Button(npd);

        text = new TextActor(getAssetManager()
                .getBundle(Bundle.GLOBAL).getFont("cg-24"), label);

        // TODO: magic number (menu button text color)
        text.setColor(new Color(0x583370FF));

        addActor(background);
        addActor(text);

        update();
    }

    private void update() {
        background.setWidth(text.getRealWidth() + 40);
        background.setHeight(text.getRealHeight() + 40);
        text.setX(20);
        text.setY(20);
    }

    public SettingsAction getAction() {
        return action;
    }

    @Override
    public float getRealWidth() {
        return background.getWidth() * getScaleX();
    }

    @Override
    public float getRealHeight() {
        return background.getHeight() * getScaleY();
    }
}
