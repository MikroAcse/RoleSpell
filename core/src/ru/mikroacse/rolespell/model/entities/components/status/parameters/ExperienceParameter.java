package ru.mikroacse.rolespell.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class ExperienceParameter extends NumericParameter {
    public ExperienceParameter(StatusComponent status) {
        super(status, ParameterType.EXPERIENCE);

        value.setValue(10);
        value.setMax(100);
    }
}
