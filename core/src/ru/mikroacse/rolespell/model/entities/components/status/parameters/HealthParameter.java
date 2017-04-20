package ru.mikroacse.rolespell.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class HealthParameter extends NumericParameter {
    private long lastTimeDamaged;

    public HealthParameter(StatusComponent status) {
        super(status, ParameterType.HEALTH);

        value.setValue(0);
        value.setMax(100);
        speed = 3.0;

        lastTimeDamaged = 0;
    }

    public boolean damage(double value) {
        if (getValue().isMin()) {
            return false;
        }

        getValue().add(-value);

        lastTimeDamaged = System.currentTimeMillis();

        return true;
    }

    public long getLastTimeDamaged() {
        return lastTimeDamaged;
    }
}
