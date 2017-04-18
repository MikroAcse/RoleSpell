package ru.mikroacse.rolespell.model.entities.components.ai.behaviors;

import ru.mikroacse.rolespell.model.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.util.ArrayUtil;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.Priority;

import java.util.ArrayList;
import java.util.List;

/**
 * Move towards a target (land near target)
 */
public class SeekBehavior extends Behavior {
    public SeekBehavior(Priority priority, Interval interval) {
        super(priority, Type.SOMETIMES, true, Trigger.BOTH);

        setInterval(interval);
    }

    @Override
    public boolean process(Entity entity, List<Entity> targets) {
        if(targets.isEmpty()) {
            return false;
        }

        Position destination = getCentroid(targets);

        // TODO: better solution
        List<Position> translate = new ArrayList<>();
        translate.add(new Position(-1, -1));
        translate.add(new Position(-1, 0));
        translate.add(new Position(0, -1));
        translate.add(new Position(1, 0));
        translate.add(new Position(0, 1));
        translate.add(new Position(1, 1));

        destination.translate(ArrayUtil.getRandom(translate));

        // TODO: magic number
        return entity
                .getComponent(PathMovementComponent.class)
                .routeTo(destination, Priority.NORMAL, 5, 15);
    }
}
