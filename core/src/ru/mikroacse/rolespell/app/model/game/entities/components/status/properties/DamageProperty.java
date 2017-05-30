package ru.mikroacse.rolespell.app.model.game.entities.components.status.properties;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;

/**
 * Created by MikroAcse on 13-Apr-17.
 */
public class DamageProperty extends Property {
    private double attackDistance;

    public DamageProperty(StatusComponent status, Interval interval, double attackDistance) {
        super(status, PropertyType.DAMAGE, interval);

        this.attackDistance = attackDistance;
    }

    public boolean bump(Entity entity) {
        if(isPaused()) {
            return false;
        }

        StatusComponent status = entity.getComponent(StatusComponent.class);
        HealthProperty health = status.getProperty(HealthProperty.class);

        if(entity.getType() == EntityType.OGREMAGI) {
            System.out.println(getValue());
        }
        health.damage(getValue());
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
