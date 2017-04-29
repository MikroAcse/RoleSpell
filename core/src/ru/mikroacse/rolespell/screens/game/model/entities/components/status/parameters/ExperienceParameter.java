package ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.screens.game.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class ExperienceParameter extends NumericParameter {
    public ExperienceParameter(StatusComponent status) {
        super(status, ParameterType.EXPERIENCE);
        
        // TODO: magic numbers
        value.setValue(10);
        value.setMax(100);
    }
}
