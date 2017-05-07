package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;

/**
 * Move towards a target (land exactly on the target).
 */
public class ArriveBehavior extends Behavior {
    public ArriveBehavior(Priority priority, Timer timer) {
        super(priority, false, Trigger.ALL);

        setTimer(timer);
    }

    @Override
    public boolean process(Entity entity, Array<Entity> targets) {
        targets.removeValue(entity, true);
        if (targets.size == 0) {
            return false;
        }

        // get centroid of target positions
        IntVector2 destination = new IntVector2(0, 0);

        int targetCount = 0;

        for (Entity target : targets) {
            if (isTargetActivated(entity, target)) {
                MovementComponent targetMovement = target.getComponent(MovementComponent.class);

                destination.translate(targetMovement.getPosition());

                targetCount++;
            }
        }

        if (targetCount == 0) {
            return false;
        }

        destination.multiply(1 / targetCount);

        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);

        // TODO: magic number
        return movement.tryRouteTo(
                destination,
                getPriority(),
                5,
                15,
                0,
                (int) Math.ceil(getDeactivationDistance())) != null;
    }
}
