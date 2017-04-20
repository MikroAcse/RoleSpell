package ru.mikroacse.rolespell.model.entities.components.status.parameters.core;

import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.ParameterType;
import ru.mikroacse.util.LimitedDouble;

/**
 * Created by MikroAcse on 09-Apr-17.
 */
public abstract class NumericParameter extends Parameter {
    protected LimitedDouble value;
    protected double speed;

    public NumericParameter(StatusComponent status, ParameterType type) {
        super(status, type);

        value = new LimitedDouble();
        value.setMin(0);
    }

    @Override
    public boolean update(float delta) {
        value.setValue(value.getValue() + speed * delta);

        return true;
    }

    public LimitedDouble getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value.setValue(value);
    }

    public double getCurrentValue() {
        return value.getValue();
    }

    public double getPercentage() {
        return value.getPercentage();
    }
}
