package ru.mikroacse.rolespell.model.entities.components.core;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.util.LimitedDouble;

/**
 * Created by MikroAcse on 09-Apr-17.
 */
public abstract class IntervalComponent extends Component {
    private LimitedDouble interval;

    private double speed;
    private double time;

    public IntervalComponent(Entity entity, LimitedDouble interval) {
        super(entity);
        this.interval = interval;

        speed = 1.0;
        time = 0.0;
    }

    @Override
    public boolean update(float delta) {
        if (interval == null) {
            return false;
        }

        time += speed * delta;

        double intervalValue = interval.getValue();

        boolean updated = false;
        while (time >= intervalValue) {
            updated |= action();

            time -= intervalValue;
        }

        return updated;
    }

    public void resetTime() {
        time = 0.0;
    }

    public LimitedDouble getInterval() {
        return interval;
    }

    public void setInterval(LimitedDouble interval) {
        this.interval = interval;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
