package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Move towards a target (land near target)
 */
public class SeekBehavior extends Behavior {
    private int randomDistance;

    public SeekBehavior(Priority priority, Timer timer, double activationDistance) {
        super(priority, false, Trigger.ALL);

        setTimer(timer);
        setActivationDistance(activationDistance);

        // TODO: magic number
        randomDistance = 2;
    }

    public SeekBehavior(Priority priority, Timer timer, double activationDistance, double deactivationDistance) {
        this(priority, timer, activationDistance);

        setDeactivationDistance(deactivationDistance);
    }

    @Override
    public boolean process(Entity entity, Array<Entity> targets) {
        targets.removeValue(entity, true);
        if (targets.size == 0) {
            return false;
        }

        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);
        IntVector2 position = movement.getPosition();

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

        // TODO: magic numbers
        return movement.tryRouteTo(
                destination,
                getPriority(),
                5,
                15,
                (int) getActivationDistance(),
                (int) Math.ceil(getDeactivationDistance())) != null;
    }
}
