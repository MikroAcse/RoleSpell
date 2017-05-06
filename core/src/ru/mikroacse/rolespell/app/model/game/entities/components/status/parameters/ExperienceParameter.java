package ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class ExperienceParameter extends NumericParameter {
    public ExperienceParameter(StatusComponent status, Interval interval) {
        super(status, ParameterType.EXPERIENCE, interval);
    }
}
