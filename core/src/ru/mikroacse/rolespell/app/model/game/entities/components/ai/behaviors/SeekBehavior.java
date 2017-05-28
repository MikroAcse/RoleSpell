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

    public SeekBehavior(Priority priority, Timer timer, int activationDistance) {
        super(priority, false, Trigger.ALL);

        setTimer(timer);
        setActivationDistance(activationDistance);

        // TODO: magic number
        randomDistance = 2;
    }

    public SeekBehavior(Priority priority, Timer timer, int activationDistance, int deactivationDistance) {
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

        World world = entity.getWorld();
        Array<IntVector2> passableCells = world.getPassableCells(
                destination.x,
                destination.y,
                true,
                1,
                randomDistance,
                false);

        if (passableCells.size == 0) {
            return false;
        }

        // TODO: magic numbers
        return movement.tryRouteTo(
                destination,
                getPriority(),
                5,
                15,
                1,
                (int) Math.ceil(getDeactivationDistance())) != null;
    }
}
