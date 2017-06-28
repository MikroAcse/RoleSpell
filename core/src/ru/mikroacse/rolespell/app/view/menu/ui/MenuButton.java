package ru.mikroacse.rolespell.app.view.menu.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import ru.mikroacse.engine.actors.MeasurableActor;
import ru.mikroacse.engine.actors.TextActor;
import ru.mikroacse.rolespell.app.model.menu.MenuAction;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.assets;
import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class MenuButton extends Group implements MeasurableActor {
    private TextActor text;
    private Button background;

    private MenuAction action;

    public MenuButton(String label, MenuAction action) {
        super();

        this.action = action;

        // TODO: ↓
        NinePatchDrawable npd = new NinePatchDrawable(
                new NinePatch(
                        bundle(Bundle.MENU).getTexture("button-background"),
                        29, 29, 29, 29
                )
        );

        background = new Button(npd);

        text = new TextActor(assets()
                .getGlobalFont("cg-24"), label);

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

    public MenuAction getAction() {
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
