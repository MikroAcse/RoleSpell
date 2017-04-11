package ru.mikroacse.rolespell.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class ManaParameter extends NumericParameter {
    public ManaParameter(StatusComponent status) {
        super(status, ParameterType.MANA);

        value.setValue(0);
        value.setMax(100);
        change = 2.0;
    }
}
