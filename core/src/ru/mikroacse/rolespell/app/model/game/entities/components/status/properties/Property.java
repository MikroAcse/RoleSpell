package ru.mikroacse.rolespell.app.model.game.entities.components.status.properties;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;

/**
 * Created by MikroAcse on 09-Apr-17.
 */
public abstract class Property implements Interval.Listener {
    private StatusComponent status;
    private PropertyType type;

    private Interval interval;
    private double speed;

    private Listener listeners;

    private boolean paused;

    public Property(StatusComponent status, PropertyType type, Interval interval, double speed) {
        this.status = status;
        this.type = type;
        this.speed = speed;

        listeners = ListenerSupportFactory.create(Listener.class);

        setInterval(interval);
    }

    public Property(StatusComponent status, PropertyType type, Interval interval) {
        this(status, type, interval, 0.0);
    }

    public boolean update(float delta) {
        if (paused) {
            return false;
        }

        interval.setValue(interval.getValue() + speed * delta);

        return true;
    }

    @Override
    public void valueChanged(Interval interval, double previousValue, double currentValue) {
        listeners.updated(Property.this, previousValue, currentValue);
    }

    public void randomize() {
        interval.randomize();
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

    private void attachInterval(Interval interval) {
        interval.addListener(this);
    }

    private void detachInterval(Interval interval) {
        interval.removeListener(this);
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        if (this.interval != null) {
            detachInterval(this.interval);
        }

        this.interval = interval;

        if (interval != null) {
            attachInterval(interval);
        }
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getValue() {
        return interval.getValue();
    }

    public void setValue(double value) {
        this.interval.setValue(value);
    }

    public double getPercentage() {
        return interval.getPercentage();
    }

    public StatusComponent getStatus() {
        return status;
    }

    public PropertyType getType() {
        return type;
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void updated(Property property, double previousValue, double currentValue);
    }
}
