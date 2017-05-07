package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.DamageParameter;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 18-Apr-17.
 */
public class AttackBehavior extends Behavior {
    public AttackBehavior(Timer timer) {
        super(Priority.IMMEDIATELY, true, EnumSet.of(Trigger.INTERVAL));

        setTimer(timer);
    }

    @Override
    public boolean process(Entity entity, Array<Entity> targets) {
        targets.removeValue(entity, true);
        if (targets.size == 0) {
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
