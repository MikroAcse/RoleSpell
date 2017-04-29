package ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.screens.game.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class StaminaParameter extends NumericParameter {
    public StaminaParameter(StatusComponent status) {
        super(status, ParameterType.STAMINA);
        
        // TODO: magic numbers
        value.setValue(0);
        value.setMax(100);
        speed = 10.0;
    }
}
