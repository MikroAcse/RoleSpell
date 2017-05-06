package ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class HealthParameter extends NumericParameter {
    private long lastTimeDamaged;
    private long lastTimeHealed;

    public HealthParameter(StatusComponent status, Interval interval, double speed) {
        super(status, ParameterType.HEALTH, interval, speed);

        lastTimeDamaged = 0;
        lastTimeHealed = 0;
    }

    public boolean heal(double value) {
        if (getInterval().isMax()) {
            return false;
        }

        getInterval().add(value);

        lastTimeHealed = System.currentTimeMillis();

        return true;
    }

    public boolean damage(double value) {
        if (getInterval().isMin()) {
            return false;
        }

        getInterval().add(-value);

        lastTimeDamaged = System.currentTimeMillis();

        return true;
    }

    public long getLastTimeDamaged() {
        return lastTimeDamaged;
    }

    public long getLastTimeHealed() {
        return lastTimeHealed;
    }
}
