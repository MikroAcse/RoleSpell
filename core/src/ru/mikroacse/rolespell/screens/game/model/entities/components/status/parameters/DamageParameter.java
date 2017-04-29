package ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.screens.game.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters.core.Parameter;
import ru.mikroacse.rolespell.screens.game.model.entities.core.Entity;
import ru.mikroacse.util.LimitedDouble;

/**
 * Created by MikroAcse on 13-Apr-17.
 */
public class DamageParameter extends Parameter {
    private LimitedDouble damage;
    private int attackDistance;
    
    private boolean randomized;
    
    public DamageParameter(StatusComponent status, LimitedDouble damage, int attackDistance, boolean randomized) {
        super(status, ParameterType.DAMAGE);
        this.damage = damage;
        this.attackDistance = attackDistance;
        this.randomized = randomized;
    }
    
    public DamageParameter(StatusComponent status) {
        this(status, new LimitedDouble(0), 0, false);
    }
    
    public boolean bump(Entity entity) {
        StatusComponent status = entity.getComponent(StatusComponent.class);
        HealthParameter health = status.getParameter(HealthParameter.class);
        
        health.damage(damage.getValue());
        
        if (randomized) {
            damage.randomize();
        }
        
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
    
    public boolean isRandomized() {
        return randomized;
    }
    
    public void setRandomized(boolean randomized) {
        this.randomized = randomized;
    }
}
