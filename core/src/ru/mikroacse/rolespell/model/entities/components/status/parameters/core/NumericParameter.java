package ru.mikroacse.rolespell.model.entities.components.status.parameters.core;

import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.ParameterType;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.LimitedDouble;

/**
 * Created by MikroAcse on 09-Apr-17.
 */
public abstract class NumericParameter extends Parameter {
    protected LimitedDouble value;
    protected double change;

    public NumericParameter(StatusComponent status, ParameterType type) {
        super(status, type);

        value = new LimitedDouble();
        value.setMin(0);
    }

    @Override
    public boolean update(float delta) {
        value.setValue(value.getValue() + change * delta);

        return true;
    }

    public double getValue() {
        return value.getValue();
    }

    public double getPercentage() {
        return value.getPercentage();
    }
}
