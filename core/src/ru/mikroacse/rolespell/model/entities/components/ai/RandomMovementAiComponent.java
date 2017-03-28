package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.MovableEntity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.ArrayUtil;
import ru.mikroacse.util.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by MikroAcse on 26.03.2017.
 */
public class RandomMovementAiComponent extends AiComponent {
    private PathMovementComponent movement;

    private int minRadius;
    private int maxRadius;

    private int pathFindRadius;

    private boolean inverse;

    private Random random;

    public RandomMovementAiComponent(PathMovementComponent movement, int minRadius, int maxRadius) {
        super();

        this.movement = movement;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;

        random = new Random();

        inverse = true; // looking for empty cell from maxRadius to minRadius

        pathFindRadius = 5; // TODO: magic
    }

    @Override
    public boolean action(Entity entity, World world) {
        LinkedList<Position> path = movement.getPath();

        // don't do anything while moving
        if (!movement.isPathEmpty()) {
            return false;
        }

        int x = movement.getOrigin().x;
        int y = movement.getOrigin().y;

        // looking for empty cells to move
        ArrayList<Position> passableCells = world.getPassableCells(x, y, minRadius, maxRadius, inverse);

        if (!passableCells.isEmpty()) {
            Position destination = ArrayUtil.getRandom(passableCells);

            // TODO: unchecked casting
            return movement.moveTo((MovableEntity) entity, world, destination, pathFindRadius);
        }

        // TODO: if can't find path, do something else
        return false;
    }

    public PathMovementComponent getMovement() {
        return movement;
    }

    public void setMovement(PathMovementComponent movement) {
        this.movement = movement;
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

    public boolean isInverse() {
        return inverse;
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public int getPathFindRadius() {
        return pathFindRadius;
    }

    public void setPathFindRadius(int pathFindRadius) {
        this.pathFindRadius = pathFindRadius;
    }
}
