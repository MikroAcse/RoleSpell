package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.DamageParameter;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by MikroAcse on 18-Apr-17.
 */
public class AttackBehavior extends Behavior {
    public AttackBehavior(Timer timer) {
        super(Priority.IMMEDIATELY, true, EnumSet.of(Trigger.INTERVAL));

        setTimer(timer);
    }

    @Override
    public boolean process(Entity entity, List<Entity> targets) {
        targets.remove(entity);
        if (targets.isEmpty()) {
            return false;
        }

        MovementComponent movement = entity.getComponent(MovementComponent.class);
        IntVector2 position = movement.getPosition();

        StatusComponent status = entity.getComponent(StatusComponent.class);
        DamageParameter damage = status.getParameter(DamageParameter.class);

        boolean bumped = false;

        for (Entity target : targets) {
            if (isTargetActivated(entity, target)) {
                MovementComponent targetMovement = target.getComponent(MovementComponent.class);
                IntVector2 targetPosition = targetMovement.getPosition();

                if (position.distance(targetPosition) <= damage.getAttackDistance()) {
                    bumped |= damage.bump(target);
                }
            }
        }

        return bumped;
    }
}
