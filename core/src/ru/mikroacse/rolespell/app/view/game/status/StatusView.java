package ru.mikroacse.rolespell.app.view.game.status;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.actors.MeasurableActor;
import ru.mikroacse.engine.util.GroupUtil;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.Property;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class StatusView extends Group implements MeasurableActor {
    private static final int PROPERTY_WIDTH = 100;
    private static final int PROPERTY_HEIGHT = 24;
    private static final int PROPERTY_OFFSET = 5;

    private Image background;

    private StatusComponent status;
    private Array<PropertyView> propertyViews;

    private StatusComponent.Listener statusListener;

    public StatusView() {
        super();

        Pixmap pm = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pm.setColor(0x00000077);
        pm.fill();

        background = new Image(new Texture(pm));

        pm.dispose();

        propertyViews = new Array<>(0);

        statusListener = new StatusComponent.Listener() {
            @Override
            public void propertyUpdated(StatusComponent status, Property property, double previousValue, double currentValue) {
                update();
            }

            @Override
            public void propertyAdded(StatusComponent status, Property property) {
                // TODO: refresh properties
            }

            @Override
            public void propertyRemoved(StatusComponent status, Property property) {
                // TODO: refresh properties
            }
        };
    }

    public void update() {
        for (int i = 0; i < propertyViews.size; i++) {
            PropertyView propertyView = propertyViews.get(i);

            if (propertyView == null) {
                continue;
            }

            Property parameter = propertyView.getProperty();

            propertyView.setMaxWidth(PROPERTY_WIDTH);
            propertyView.setHeight(PROPERTY_HEIGHT);

            propertyView.setY(i * (PROPERTY_HEIGHT + PROPERTY_OFFSET));

            propertyView.update();
        }
    }

    private void attachStatus(StatusComponent status) {
        status.addListener(statusListener);

        for (Property property : status.getProperties()) {
            if (!PropertyView.canBeRendered(property)) {
                continue;
            }

            PropertyView propertyView = new PropertyView((Property) property);
            propertyViews.add(propertyView);

            addActor(propertyView);
        }

        update();
    }

    private void detachStatus(StatusComponent status) {
        status.removeListener(statusListener);

        clearChildren();

        propertyViews.clear();
    }

    public StatusComponent getStatus() {
        return status;
    }

    public void setStatus(StatusComponent status) {
        if (this.status != null) {
            detachStatus(status);
        }

        this.status = status;

        if (status != null) {
            attachStatus(status);
        }
    }

    @Override
    public float getRealWidth() {
        return GroupUtil.getWidth(this);
    }

    @Override
    public float getRealHeight() {
        return GroupUtil.getHeight(this);
    }
}
