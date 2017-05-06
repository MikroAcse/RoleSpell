package ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class StaminaParameter extends NumericParameter {
    public StaminaParameter(StatusComponent status, Interval interval, double speed) {
        super(status, ParameterType.STAMINA, interval, speed);
    }
}
