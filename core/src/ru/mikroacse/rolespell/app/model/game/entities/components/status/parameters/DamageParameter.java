package ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.NumericParameter;

/**
 * Created by MikroAcse on 13-Apr-17.
 */
public class DamageParameter extends NumericParameter {
    private double attackDistance;

    private boolean randomized;

    public DamageParameter(StatusComponent status, Interval interval, double attackDistance, boolean randomized) {
        super(status, ParameterType.DAMAGE, interval);

        this.attackDistance = attackDistance;
        this.randomized = randomized;
    }

    public boolean bump(Entity entity) {
        StatusComponent status = entity.getComponent(StatusComponent.class);
        HealthParameter health = status.getParameter(HealthParameter.class);

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
