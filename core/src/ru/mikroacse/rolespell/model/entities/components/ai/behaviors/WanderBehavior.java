package ru.mikroacse.rolespell.model.entities.components.ai.behaviors;

import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.ListUtil;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.Priority;

import java.util.LinkedList;
import java.util.List;

/**
 * Move around [target/origin/position] randomly.
 */
public class WanderBehavior extends Behavior {
    private Guide guide;

    private int minRadius;
    private int maxRadius;

    public WanderBehavior(Priority priority, Guide guide, int minRadius, int maxRadius, Interval interval) {
        super(priority, Type.SOMETIMES, true, Trigger.INTERVAL);
        this.guide = guide;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;

        this.guide = guide;
        setInterval(interval);
    }

    @Override
    public boolean process(Entity entity, List<Entity> targets) {
        World world = entity.getWorld();
        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);
        LinkedList<Position> path = movement.getPath();

        Position destination = null;

        switch (guide) {
            case TARGET:
                destination = new Position(0, 0);

                for (Entity target : targets) {
                    if (!isTargetActivated(entity, target)) {
                        continue;
                    }

                    MovementComponent targetMovement = target.getComponent(MovementComponent.class);

                    destination.translate(targetMovement.getPosition());
                }
                break;
            case ORIGIN:
                destination = movement.getOrigin();
                break;
            case POSITION:
                destination = movement.getPosition();
                break;
        }

        if (destination == null) {
            return false;
        }

        int x = destination.x;
        int y = destination.y;

        // looking for empty cells to routeTo
        List<Position> passableCells = world.getPassableCells(
                x, y,
                false,
                minRadius, maxRadius,
                false);

        if (passableCells.isEmpty()) {
            return false;
        }

        destination = ListUtil.getRandom(passableCells);

        // TODO: magic numbers
        return movement.routeTo(destination, Priority.LOW, 5, 15);
    }

    public enum Guide {
        TARGET, // move around target
        ORIGIN, // move around entity's origin
        POSITION // move around entity's current position
    }
}
