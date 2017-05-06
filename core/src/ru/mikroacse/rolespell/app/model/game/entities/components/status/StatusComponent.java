package ru.mikroacse.rolespell.app.model.game.entities.components.status;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.Parameter;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikroAcse on 03.04.2017.
 */
public class StatusComponent extends Component {
    private List<Parameter> parameters;

    private Listener listeners;

    private Parameter.Listener parameterListener;

    public StatusComponent(Entity entity) {
        super(entity);

        listeners = ListenerSupportFactory.create(Listener.class);

        parameters = new ArrayList<>();

        parameterListener = listeners::parameterUpdated;
    }

    @Override
    public boolean update(float delta) {
        boolean updated = false;
        for (Parameter parameter : parameters) {
            updated |= parameter.update(delta);
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

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);

        parameter.addListener(parameterListener);
    }

    public boolean removeParameter(Parameter parameter) {
        parameter.removeListener(parameterListener);

        return parameters.remove(parameter);
    }

    /**
     * @return True if status has at least one parameter of given class.
     */
    public boolean hasParameter(Class<? extends Component> parameterClass) {
        for (Parameter parameter : parameters) {
            if (parameterClass.isInstance(parameter)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return First matching status parameter of given class.
     */
    public <T extends Parameter> T getParameter(Class<T> parameterClass) {
        for (Parameter parameter : parameters) {
            if (parameterClass.isInstance(parameter)) {
                return (T) parameter;
            }
        }
        return null;
    }

    /**
     * @return All status parameters of given class.
     */
    public <T extends Parameter> List<T> getParameters(Class<T> parameterClass) {
        List<T> result = new ArrayList<T>();

        for (Parameter parameter : parameters) {
            if (parameterClass.isInstance(parameter)) {
                result.add((T) parameter);
            }
        }

        return result;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public void dispose() {

    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void parameterUpdated(Parameter parameter);
    }
}
