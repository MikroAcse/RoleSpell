package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.ListUtil;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Move around [target/origin/position] randomly.
 */
public class WanderBehavior extends Behavior {
    private Guide guide;

    private int minRadius;
    private int maxRadius;

    public WanderBehavior(Priority priority, Guide guide, int minRadius, int maxRadius, Timer timer) {
        super(priority, false, EnumSet.of(Trigger.INTERVAL));
        this.guide = guide;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;

        this.guide = guide;
        setTimer(timer);
    }

    @Override
    public boolean process(Entity entity, Array<Entity> targets) {
        targets.removeValue(entity, true);

        World world = entity.getWorld();
        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);
        Array<IntVector2> path = movement.getPath();

        IntVector2 destination = null;

        switch (guide) {
            case TARGET:
                if (targets.size == 0) {
                    return false;
                }

                destination = new IntVector2(0, 0);

                for (Entity target : targets) {
                    if (!isTargetActivated(entity, target)) {
                        continue;
                    }

                    destination.translate(target.getPosition());
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

        // looking for empty cells to tryRouteTo
        Array<IntVector2> passableCells = world.getPassableCells(
                x, y,
                false,
                minRadius, maxRadius,
                false);

        if (passableCells.size == 0) {
            return false;
        }

        destination = passableCells.random();

        // TODO: magic numbers
        return movement.tryRouteTo(destination,
                getPriority(),
                5,
                15,
                0,
                (int) Math.ceil(getDeactivationDistance())) != null;
    }

    public enum Guide {
        TARGET, // move around target
        ORIGIN, // move around entity's origin
        POSITION // move around entity's current position
    }
}
