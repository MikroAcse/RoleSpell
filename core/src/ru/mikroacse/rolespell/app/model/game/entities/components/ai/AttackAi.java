package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.AttackBehavior;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.FleeBehavior;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.SeekBehavior;

/**
 * Created by MikroAcse on 02-May-17.
 */
public class AttackAi extends BehaviorAi {
    private AttackBehavior attackBehavior;
    private SeekBehavior seekBehavior;
    private FleeBehavior fleeBehavior;

    public AttackAi(Entity entity, Timer attackTimer) {
        super(entity, Integer.MAX_VALUE);

        setMaxTargets(1);

        attackBehavior = new AttackBehavior(attackTimer);
        addBehavior(attackBehavior);

        seekBehavior = new SeekBehavior(Priority.NORMAL,
                new Timer(0.1),
                1,
                20);

        addBehavior(seekBehavior);

        fleeBehavior = new FleeBehavior(Priority.NORMAL,
                new Timer(0.5),
                0);

        fleeBehavior.setEnabled(false);

        addBehavior(fleeBehavior);
    }

    public void setAttackTimer(Timer attackTimer) {
        attackBehavior.setTimer(attackTimer);
    }

    public void setAttackDistance(double attackDistance) {
        seekBehavior.setActivationDistance(attackDistance);
        seekBehavior.setDeactivationDistance(attackDistance + 20);

        if(attackDistance > 1) {
            fleeBehavior.setDeactivationDistance(attackDistance);
            fleeBehavior.setEnabled(true);
        } else {
            fleeBehavior.setEnabled(false);
        }
    }
}
