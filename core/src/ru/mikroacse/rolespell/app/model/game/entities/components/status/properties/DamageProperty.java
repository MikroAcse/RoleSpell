package ru.mikroacse.rolespell.app.model.game.entities.components.status.properties;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.core.NumericProperty;

/**
 * Created by MikroAcse on 13-Apr-17.
 */
public class DamageProperty extends NumericProperty {
    private double attackDistance;

    private boolean randomized;

    public DamageProperty(StatusComponent status, Interval interval, double attackDistance, boolean randomized) {
        super(status, PropertyType.DAMAGE, interval);

        this.attackDistance = attackDistance;
        this.randomized = randomized;
    }

    public boolean bump(Entity entity) {
        StatusComponent status = entity.getComponent(StatusComponent.class);
        HealthProperty health = status.getParameter(HealthProperty.class);

        health.damage(getInterval().getValue());

        if (randomized) {
            randomize();
        }

        return true;
    }

    @Override
    public boolean update(float delta) {
        return false;
    }

    public double getAttackDistance() {
        return attackDistance;
    }
}
