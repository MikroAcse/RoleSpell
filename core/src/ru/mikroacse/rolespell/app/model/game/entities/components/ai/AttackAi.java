package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.AttackBehavior;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;

/**
 * Created by MikroAcse on 02-May-17.
 */
public class AttackAi extends BehaviorAi {
    private AttackBehavior attackBehavior;
    
    public AttackAi(Entity entity, Interval interval) {
        super(entity, Integer.MAX_VALUE);
    
        attackBehavior = new AttackBehavior(interval);
        addBehavior(attackBehavior);
    }
    
    public AttackBehavior getAttackBehavior() {
        return attackBehavior;
    }
}
