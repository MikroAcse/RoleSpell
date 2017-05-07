package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.AttackBehavior;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.SeekBehavior;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 02-May-17.
 */
public class AttackAi extends BehaviorAi {
    private AttackBehavior attackBehavior;
    private SeekBehavior seekBehavior;

    public AttackAi(Entity entity, Timer timer) {
        super(entity, Integer.MAX_VALUE);

        setTargetSelectors(EnumSet.of(TargetSelector.CUSTOM));
        setMaxTargets(1);

        attackBehavior = new AttackBehavior(timer);
        addBehavior(attackBehavior);

        seekBehavior = new SeekBehavior(Priority.NORMAL,
                new Timer(0.2),
                1,
                20);

        addBehavior(seekBehavior);
    }
}
