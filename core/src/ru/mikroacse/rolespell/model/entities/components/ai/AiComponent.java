package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.components.core.UpdatableComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

import java.util.Random;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class AiComponent implements UpdatableComponent {
    private Random random;

    // TODO: make interval separate class and customizable
    private double interval;
    private double time;

    public AiComponent() {
        interval = Double.POSITIVE_INFINITY;
        time = 0.0;
    }

    @Override
    public boolean update(Entity entity, World world, float delta) {
        time += delta;

        while (time >= interval) {
            action(entity, world);

            time -= interval;
        }

        return true;
    }

    public abstract boolean action(Entity entity, World world);

    public void resetTime() {
        time = 0.0;
    }

    public double getInterval() {
        return interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }
}
