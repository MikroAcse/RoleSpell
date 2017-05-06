package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.AttackBehavior;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;

/**
 * Created by MikroAcse on 02-May-17.
 */
public class AttackAi extends BehaviorAi {
    private AttackBehavior attackBehavior;

    public AttackAi(Entity entity, Timer timer) {
        super(entity, Integer.MAX_VALUE);

        attackBehavior = new AttackBehavior(timer);
        addBehavior(attackBehavior);
    }
}
