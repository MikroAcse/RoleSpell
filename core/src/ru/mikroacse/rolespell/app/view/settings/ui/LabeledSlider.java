package ru.mikroacse.rolespell.app.view.settings.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import ru.mikroacse.engine.actors.MeasurableActor;
import ru.mikroacse.engine.actors.TextActor;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.assets;
import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by Vitaly Rudenko on 07-Jun-17.
 */
public class LabeledSlider extends Group implements MeasurableActor {
    private Button foreground;
    private Button background;

    private TextActor valueText;

    private TextActor label;

    private float value;

    private boolean down;

    public LabeledSlider() {
        super();

        // TODO: magic numbers (left x offset / nine patch size * 2)
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setValue((x - background.getX() - 20) / (background.getWidth() - 20));
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                setValue((x - background.getX() - 20) / (background.getWidth() - 20));
            }
        });

        // TODO: ↓
        NinePatchDrawable npdBg = new NinePatchDrawable(
                new NinePatch(
                        bundle(Bundle.SETTINGS).getTexture("slider-background"),
                        10, 10, 10, 10
                )
        );

        background = new Button(npdBg);

        // TODO: ↓
        NinePatchDrawable npdFg = new NinePatchDrawable(
                new NinePatch(
                        bundle(Bundle.SETTINGS).getTexture("slider-foreground"),
                        10, 10, 10, 10
                )
        );

        foreground = new Button(npdFg);

        valueText = new TextActor(assets().getGlobalFont("cg-24"));

        label = new TextActor(assets().getGlobalFont("cg-24"));

        addActor(background);
        addActor(foreground);
        addActor(valueText);
        addActor(label);

        value = -1f;
        setValue(0f);
    }

    private void update() {
        label.setY((int) (background.getHeight() / 2 - label.getRealHeight() / 2));

        // TODO: magic number (offset between label and slider
        float x = label.getRealWidth() + 20;

        background.setX(x);
        foreground.setX(x);

        // TODO: magic number (left x offset / nine patch size * 2)
        foreground.setWidth((int) (20 + (background.getWidth() - 20) * value));

        // TODO: magic number (label offset)
        float textSize = valueText.getRealWidth();
        float leftPart = foreground.getWidth();
        float rightPart = background.getWidth() - leftPart;

        // TODO: color/magic numbers
        if (leftPart < textSize + 20) {
            valueText.setX((int) (x + foreground.getWidth() + 10));
            valueText.setColor(new Color(0x495D98FF));
        } else {
            valueText.setX((int) (x + foreground.getWidth() - textSize - 10));
            valueText.setColor(Color.WHITE);
        }

        valueText.setY((int) (background.getHeight() / 2 - valueText.getRealHeight() / 2));
    }

    @Override
    public void setWidth(float width) {
        background.setWidth(width);

        update();
    }

    @Override
    public void setHeight(float height) {
        background.setHeight(height);
        foreground.setHeight(height);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        value = MathUtils.clamp(value, 0, 1);

        if (this.value == value) {
            return;
        }

        this.value = value;

        valueText.setText(String.valueOf((int) (value * 100)));

        update();
    }

    public void setLabel(String value) {
        label.setText(value);

        update();
    }

    @Override
    public float getRealWidth() {
        return (background.getX() + background.getWidth()) * getScaleX();
    }

    @Override
    public float getRealHeight() {
        return background.getHeight() * getScaleY();
    }
}
