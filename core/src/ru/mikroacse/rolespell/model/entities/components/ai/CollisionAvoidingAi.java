package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.GuidedEntity;
import ru.mikroacse.rolespell.model.entities.core.MovableEntity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.ArrayUtil;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.Position;

import java.util.List;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class CollisionAvoidingAi extends AiComponent {
    private PathMovementComponent movement;

    private int minRadius;
    private int maxRadius;

    private int pathFindRadius;

    public CollisionAvoidingAi(PathMovementComponent movement, Interval interval, int minRadius, int maxRadius) {
        super(interval);

        this.movement = movement;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;

        // TODO: magic
        pathFindRadius = 5;
    }

    @Override
    public boolean apply(Entity entity, World world) {
        if(entity instanceof GuidedEntity) {
            if(!((GuidedEntity) entity).getMovement().isPathEmpty()) {
                return false;
            }
        }

        MovableEntity movableEntity = entity;

        List<Entity> entities = world.getEntitiesAt(movement.getPosition());
        entities.remove(entity);

        if(entities.isEmpty()) {
            return false;
        }

        List<Position> passableCells = world.getPassableCells(
                movement.getOrigin().x,
                movement.getOrigin().y,
                true,
                minRadius,
                maxRadius,
                false);

        Position destination = null;

        // checking passable cells for available paths
        while (!passableCells.isEmpty()) {
            Position passableCell = ArrayUtil.getRandom(passableCells);
            passableCells.remove(passableCell);

            if (movement.moveTo(movableEntity, world, passableCell, pathFindRadius)) {
                destination = passableCell;
                break;
            }
        }

        return destination != null;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public int getPathFindRadius() {
        return pathFindRadius;
    }

    public void setPathFindRadius(int pathFindRadius) {
        this.pathFindRadius = pathFindRadius;
    }
}
