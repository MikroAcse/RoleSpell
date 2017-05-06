package ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.ParameterType;

/**
 * Created by MikroAcse on 09-Apr-17.
 */
public abstract class NumericParameter extends Parameter {
    private Interval interval;
    private double speed;

    public NumericParameter(StatusComponent status, ParameterType type, Interval interval, double speed) {
        super(status, type);

        this.interval = interval;
        this.speed = speed;
    }

    public NumericParameter(StatusComponent status, ParameterType type, Interval interval) {
        this(status, type, interval, 0.0);
    }

    @Override
    public boolean update(float delta) {
        interval.setValue(interval.getValue() + speed * delta);

        return super.update(delta);
    }

    public void randomize() {
        interval.randomize();
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
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

    }
}
