package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.components.core.Component;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.ArrayUtil;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.Priority;

import java.util.List;

/**
 * Created by MikroAcse on 29.03.2017.
 */
// TODO: Convert it to behavior
// TODO: It is the heir of the "avoid" behavior
public class CollisionAvoidingAi extends Component implements World.Listener, MovementComponent.Listener {
    private int minRadius;
    private int maxRadius;

    private int pathFindRadius;

    // TODO: one variable for all AIs?
    private boolean stickToOrigin;

    public CollisionAvoidingAi(Entity entity, int minRadius, int maxRadius, boolean stickToOrigin) {
        super(entity);

        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.stickToOrigin = stickToOrigin;

        // TODO: magic number
        pathFindRadius = 5;

        entity.getWorld().addListener(this);
    }

    @Override
    public boolean action() {
        Entity entity = getEntity();
        World world = entity.getWorld();
        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);

        if (movement == null) {
            return false;
        }

        List<Entity> entities = world.getEntitiesAt(movement.getPosition());
        entities.remove(entity);

        if (entities.isEmpty()) {
            return false;
        }

        Position position = stickToOrigin ? movement.getOrigin() : movement.getPosition();

        List<Position> passableCells = world.getPassableCells(
                position.x,
                position.y,
                true,
                minRadius,
                maxRadius,
                false);

        Position destination = null;

        // checking passable cells for available paths
        while (!passableCells.isEmpty()) {
            Position passableCell = ArrayUtil.getRandom(passableCells);
            passableCells.remove(passableCell);

            if (movement.routeTo(passableCell, Priority.HIGH, pathFindRadius, maxRadius)) {
                destination = passableCell;
                break;
            }
        }

        return destination != null;
    }

    @Override
    public void originChanged(MovementComponent movement, Position previous, Position current) {

    }

    @Override
    public void positionChanged(MovementComponent movement, Position previous, Position current) {
        action();
    }

    @Override
    public void entityMoved(Entity entity, Position previous, Position current) {
        action();
    }

    @Override
    protected void attachEntity(Entity entity) {
        super.attachEntity(entity);

        entity
                .getComponent(MovementComponent.class)
                .addListener(this);
    }

    @Override
    protected void detachEntity(Entity entity) {
        super.detachEntity(entity);

        entity
                .getComponent(MovementComponent.class)
                .removeListener(this);
    }

    public int getPathFindRadius() {
        return pathFindRadius;
    }

    public void setPathFindRadius(int pathFindRadius) {
        this.pathFindRadius = pathFindRadius;
    }
}
