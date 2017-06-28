package ru.mikroacse.rolespell.app.view.game.status;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import ru.mikroacse.engine.actors.MeasurableActor;
import ru.mikroacse.engine.actors.TextActor;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.Property;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.PropertyType;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.assets;
import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class PropertyView extends Group implements MeasurableActor {
    private Button background;

    private TextActor label;

    private Property property;

    private float maxWidth;

    public PropertyView(Property property) {
        super();

        // TODO: ↓
        NinePatchDrawable npd = new NinePatchDrawable(
                new NinePatch(
                        bundle(Bundle.GAME).getTexture("hud/statusbar"),
                        11, 11, 11, 11
                )
        );

        background = new Button(npd);

        label = new TextActor(assets().getGlobalFont("cg-24"));

        addActor(background);
        addActor(label);

        setTouchable(Touchable.disabled);

        setProperty(property);

        maxWidth = 0;

        update();
    }

    // TODO: better solution
    public static boolean canBeRendered(Property property) {
        return property.getType() != PropertyType.DAMAGE;
    }

    public void update() {
        double width = maxWidth * property.getPercentage();
        width = Math.max(22, width); // TODO: min width (nine patch)

        background.setWidth((int) width);

        // TODO: magic numbers (label offset)
        if (width < label.getRealWidth() + 10) {
            label.setX((int) (width + 5));
        } else {
            label.setX((int) (width - label.getRealWidth() - 5));
        }

        label.setY((int) (background.getHeight() / 2 - label.getRealHeight() / 2));

        label.setText(String.valueOf((int) property.getValue()));
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float value) {
        maxWidth = value;

        update();
    }

    @Override
    public void setHeight(float height) {
        background.setHeight(height);

        update();
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;

        switch (property.getType()) {
            case HEALTH:
                background.setColor(Color.SCARLET);
                break;
            case MANA:
                background.setColor(Color.CYAN);
                break;
            case STAMINA:
                background.setColor(Color.CHARTREUSE);
                break;
            case EXPERIENCE:
                background.setColor(Color.WHITE);
                break;
        }
    }

    @Override
    public float getRealWidth() {
        return Math.max(background.getWidth(), label.getX() + label.getRealWidth());
    }

    @Override
    public float getRealHeight() {
        return background.getHeight();
    }
}
