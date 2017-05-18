package ru.mikroacse.rolespell.app.model.game.entities.components.status;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.core.Property;

/**
 * Created by MikroAcse on 03.04.2017.
 */
public class StatusComponent extends Component {
    private Array<Property> parameters;

    private Listener listeners;

    private Property.Listener parameterListener;

    public StatusComponent(Entity entity) {
        super(entity);

        listeners = ListenerSupportFactory.create(Listener.class);

        parameters = new Array<>();

        parameterListener = listeners::parameterUpdated;
    }

    @Override
    public boolean update(float delta) {
        boolean updated = false;
        for (Property property : parameters) {
            updated |= property.update(delta);
        }

        return updated;
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

    public void addParameter(Property property) {
        parameters.add(property);

        property.addListener(parameterListener);
    }

    public boolean removeParameter(Property property) {
        property.removeListener(parameterListener);

        return parameters.removeValue(property, true);
    }

    /**
     * @return True if status has at least one parameter of given class.
     */
    public boolean hasParameter(Class<? extends Component> parameterClass) {
        for (Property property : parameters) {
            if (parameterClass.isInstance(property)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return First matching status parameter of given class.
     */
    public <T extends Property> T getParameter(Class<T> parameterClass) {
        for (Property property : parameters) {
            if (parameterClass.isInstance(property)) {
                return (T) property;
            }
        }
        return null;
    }

    /**
     * @return All status properties of given class.
     */
    public <T extends Property> Array<T> getParameters(Class<T> parameterClass) {
        Array<T> result = new Array<T>();

        for (Property property : parameters) {
            if (parameterClass.isInstance(property)) {
                result.add((T) property);
            }
        }

        return result;
    }

    public Array<Property> getParameters() {
        return parameters;
    }

    @Override
    public void dispose() {

    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void parameterUpdated(Property property);
    }
}
