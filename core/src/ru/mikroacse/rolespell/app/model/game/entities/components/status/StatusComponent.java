package ru.mikroacse.rolespell.app.model.game.entities.components.status;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.Property;

/**
 * Created by MikroAcse on 03.04.2017.
 */
public class StatusComponent extends Component {
    private Array<Property> properties;

    private Listener listeners;

    private Property.Listener propertyListener;

    private boolean paused;

    public StatusComponent(Entity entity) {
        super(entity, true);

        listeners = ListenerSupportFactory.create(Listener.class);

        properties = new Array<>();

        propertyListener = new Property.Listener() {
            @Override
            public void updated(Property property, double previousValue, double currentValue) {
                listeners.propertyUpdated(StatusComponent.this, property, previousValue, currentValue);
            }
        };
    }

    @Override
    public boolean update(float delta) {
        if (paused) {
            return false;
        }

        boolean updated = false;

        for (Property property : properties) {
            updated |= property.update(delta);
        }

        return updated;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void addListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).addListener(listener);
    }

    public void removeListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<Listener>) listeners).clearListeners();
    }

    public void addProperty(Property property) {
        properties.add(property);

        property.addListener(propertyListener);

        listeners.propertyAdded(this, property);
    }

    public boolean removeProperty(Property property) {
        property.removeListener(propertyListener);

        boolean result = properties.removeValue(property, true);

        if (result) {
            listeners.propertyRemoved(this, property);
            return true;
        }

        return false;
    }

    /**
     * @return True if status has at least one parameter of given class.
     */
    public <T extends Property> boolean hasProperty(Class<?> parameterClass) {
        for (Property property : properties) {
            if (parameterClass.isInstance(property)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return First matching status parameter of given class.
     */
    public <T extends Property> T getProperty(Class<T> parameterClass) {
        for (Property property : properties) {
            if (parameterClass.isInstance(property)) {
                return (T) property;
            }
        }
        return null;
    }

    /**
     * @return All status properties of given class.
     */
    public <T extends Property> Array<T> getProperties(Class<T> parameterClass) {
        Array<T> result = new Array<T>();

        for (Property property : properties) {
            if (parameterClass.isInstance(property)) {
                result.add((T) property);
            }
        }

        return result;
    }

    public Array<Property> getProperties() {
        return properties;
    }

    @Override
    public void dispose() {

    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void propertyUpdated(StatusComponent status, Property property, double previousValue, double currentValue);

        void propertyAdded(StatusComponent status, Property property);

        void propertyRemoved(StatusComponent status, Property property);
    }
}
