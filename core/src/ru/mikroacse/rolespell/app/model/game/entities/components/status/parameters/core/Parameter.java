package ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.ParameterType;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public abstract class Parameter {
    private StatusComponent status;
    private ParameterType type;

    private Listener listeners;

    public Parameter(StatusComponent status, ParameterType type) {
        this.status = status;
        this.type = type;

        listeners = ListenerSupportFactory.create(Listener.class);
    }

    public boolean update(float delta) {
        listeners.updated(this);

        return true;
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

    public ParameterType getType() {
        return type;
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void updated(Parameter parameter);
    }
}
