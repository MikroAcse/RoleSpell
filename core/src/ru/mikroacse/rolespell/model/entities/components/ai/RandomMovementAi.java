package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.components.core.IntervalComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.ArrayUtil;
import ru.mikroacse.util.LimitedDouble;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.Priority;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by MikroAcse on 26.03.2017.
 */
public class RandomMovementAi extends IntervalComponent {
    private int minRadius;
    private int maxRadius;

    private int pathFindRadius;

    private boolean stickToOrigin;

    public RandomMovementAi(Entity entity, LimitedDouble interval, int minRadius, int maxRadius, boolean stickToOrigin) {
        super(entity, interval);

        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.stickToOrigin = stickToOrigin;

        pathFindRadius = 5; // TODO: magic

        interval.randomize();
    }

    @Override
    public boolean action() {
        Entity entity = getEntity();
        World world = entity.getWorld();
        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);

        getInterval().randomize();

        LinkedList<Position> path = movement.getPath();

        // don't do anything while moving
        if (!movement.isPathEmpty()) {
            return false;
        }

        Position position = stickToOrigin? movement.getOrigin() : movement.getPosition();

        int x = position.x;
        int y = position.y;

        // looking for empty cells to moveTo
        ArrayList<Position> passableCells = world.getPassableCells(x, y, false, minRadius, maxRadius, false);

        if (!passableCells.isEmpty()) {
            Position destination = ArrayUtil.getRandom(passableCells);

            // TODO: magic number
            if(movement.routeTo(destination, Priority.LOW, pathFindRadius, 15)) {
                return true;
            }
        }

        // TODO: if can't find path, do something else
        return false;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public int getPathFindRadius() {
        return pathFindRadius;
    }

    public void setPathFindRadius(int pathFindRadius) {
        this.pathFindRadius = pathFindRadius;
    }
}
