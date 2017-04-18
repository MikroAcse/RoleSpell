package ru.mikroacse.rolespell.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class HealthParameter extends NumericParameter {
    public HealthParameter(StatusComponent status) {
        super(status, ParameterType.HEALTH);

        value.setValue(0);
        value.setMax(100);
        change = 5.0;
    }
}
