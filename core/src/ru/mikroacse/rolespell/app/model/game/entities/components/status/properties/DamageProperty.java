package ru.mikroacse.rolespell.app.model.game.entities.components.status.properties;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;

/**
 * Created by MikroAcse on 13-Apr-17.
 */
public class DamageProperty extends Property {
    private double attackDistance;

    private boolean randomized;

    public DamageProperty(StatusComponent status, Interval interval, double attackDistance, boolean randomized) {
        super(status, PropertyType.DAMAGE, interval);

        this.attackDistance = attackDistance;
        this.randomized = randomized;
    }

    public boolean bump(Entity entity) {
        System.out.println("bump " + entity + ": " + !isPaused());

        if(isPaused()) {
            return false;
        }

        StatusComponent status = entity.getComponent(StatusComponent.class);
        HealthProperty health = status.getProperty(HealthProperty.class);

        if (randomized) {
            randomize();
        }

        health.damage(getInterval().getValue());
        return true;
    }

    @Override
    public boolean update(float delta) {
        return false;
    }

    public void setAttackDistance(double attackDistance) {
        this.attackDistance = attackDistance;
    }

    public double getAttackDistance() {
        return attackDistance;
    }
}
