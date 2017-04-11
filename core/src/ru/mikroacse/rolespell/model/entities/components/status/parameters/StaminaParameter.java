package ru.mikroacse.rolespell.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class StaminaParameter extends NumericParameter {
    public StaminaParameter(StatusComponent status) {
        super(status, ParameterType.STAMINA);

        value.setValue(0);
        value.setMax(100);
        change = 10.0;
    }
}
