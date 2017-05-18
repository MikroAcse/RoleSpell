package ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.core;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.PropertyType;

/**
 * Created by MikroAcse on 09-Apr-17.
 */
public abstract class NumericProperty extends Property implements Interval.Listener {
    private Interval interval;
    private double speed;

    private Listener listeners;

    public NumericProperty(StatusComponent status, PropertyType type, Interval interval, double speed) {
        super(status, type);

        this.speed = speed;

        listeners = ListenerSupportFactory.create(Listener.class);

        setInterval(interval);
    }

    public NumericProperty(StatusComponent status, PropertyType type, Interval interval) {
        this(status, type, interval, 0.0);
    }

    @Override
    public boolean update(float delta) {
        interval.setValue(interval.getValue() + speed * delta);

        return super.update(delta);
    }

    @Override
    public void valueChanged(Interval interval, double previousValue, double currentValue) {
        listeners.valueChanged(NumericProperty.this, previousValue, currentValue);
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
        if(this.interval != null) {
            detachInterval(this.interval);
        }

        this.interval = interval;

        if(interval != null) {
            attachInterval(interval);
        }
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

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void valueChanged(Property property, double previousValue, double currentValue);
    }
}
