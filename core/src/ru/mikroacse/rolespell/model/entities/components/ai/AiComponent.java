package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.components.core.UpdatableComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Interval;

import java.util.Random;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class AiComponent implements UpdatableComponent {
    private Random random;

    // TODO: make interval separate class and customizable
    private Interval interval;
    private double time;

    public AiComponent(Interval interval) {
        this.interval = interval;

        time = 0.0;
    }

    public AiComponent() {
        this(null);
    }

    @Override
    public boolean update(Entity entity, World world, float delta) {
        if(interval == null) {
            return false;
        }

        time += delta;

        double intervalValue = interval.getValue();

        while (time >= intervalValue) {
            apply(entity, world);

            time -= intervalValue;
        }

        return true;
    }

    public abstract boolean apply(Entity entity, World world);

    public void resetTime() {
        time = 0.0;
    }

    @Override
    public void dispose() {

    }

    public Interval getInterval() {
        return interval;
    }
}
