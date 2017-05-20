package ru.mikroacse.rolespell.app.model.game.entities.components.status.properties;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class HealthProperty extends Property {
    private long lastTimeDamaged;
    private long lastTimeHealed;

    public HealthProperty(StatusComponent status, Interval interval, double speed) {
        super(status, PropertyType.HEALTH, interval, speed);

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

        getInterval().subtract(value);

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
