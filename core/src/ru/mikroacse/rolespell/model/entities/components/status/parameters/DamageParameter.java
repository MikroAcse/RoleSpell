package ru.mikroacse.rolespell.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.core.Parameter;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.util.LimitedDouble;

/**
 * Created by MikroAcse on 13-Apr-17.
 */
public class DamageParameter extends Parameter {
    private LimitedDouble damage;
    private int attackDistance;
    private double attackSpeed;

    public DamageParameter(StatusComponent status, LimitedDouble damage, int attackDistance, double attackSpeed) {
        super(status, ParameterType.DAMAGE);
        this.damage = damage;
        this.attackDistance = attackDistance;
        this.attackSpeed = attackSpeed;
    }

    public DamageParameter(StatusComponent status) {
        this(status, new LimitedDouble(0), 0, 0);
    }

    public boolean bump(Entity entity) {
        StatusComponent status = entity.getComponent(StatusComponent.class);
        HealthParameter health = status.getParameter(HealthParameter.class);

        health.setValue(health.getCurrentValue() - damage.getValue());
        damage.randomize();
        return true;
    }

    @Override
    public boolean update(float delta) {
        return false;
    }

    public LimitedDouble getDamage() {
        return damage;
    }

    public int getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(int attackDistance) {
        this.attackDistance = attackDistance;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
}
