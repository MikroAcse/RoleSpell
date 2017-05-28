package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;

/**
 * Move away from a target.
 */
public class FleeBehavior extends Behavior {
    private int fleeDistance;

    public FleeBehavior(Priority priority, Timer timer, int deactivationDistance) {
        super(priority, false, Trigger.ALL);

        setTimer(timer);
        setDeactivationDistance(deactivationDistance);

        // TODO: magic number
        fleeDistance = 5;
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
                destination.translate(target.getPosition());

                targetCount++;
            }
        }

        if (targetCount == 0) {
            return false;
        }

        destination.multiply(1 / targetCount);

        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);
        IntVector2 position = movement.getPosition();

        // trying to move away in opposite direction
        destination = moveAway(destination, position, -5);

        // TODO: magic numbers
        return movement.tryRouteTo(
                destination,
                getPriority(),
                5,
                15,
                0,
                (int) Math.ceil(getDeactivationDistance())) != null;
    }

    private IntVector2 moveAway(IntVector2 position, IntVector2 origin, int distance) {
        double x2 = position.x - origin.x;
        double y2 = position.y - origin.y;
        double angle = Math.atan2(y2, x2);

        double x1 = Math.cos(angle) * distance;
        double y1 = Math.sin(angle) * distance;

        return new IntVector2(position.x + (int) (x1 - x2), position.y + (int) (y1 - y2));
    }
}
