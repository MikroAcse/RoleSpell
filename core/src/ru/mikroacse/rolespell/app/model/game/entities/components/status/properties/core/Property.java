package ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.core;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.PropertyType;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public abstract class Property {
    private StatusComponent status;
    private PropertyType type;

    private Listener listeners;

    public Property(StatusComponent status, PropertyType type) {
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

    public PropertyType getType() {
        return type;
    }

    public StatusComponent getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "status=" + status +
                ", type=" + type +
                '}';
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void updated(Property property);
    }
}
