package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementListener;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.model.game.world.WorldListener;

/**
 * Created by MikroAcse on 29.03.2017.
 */
// TODO: Convert it to behavior
// TODO: It is the heir of the "avoid" behavior
public class CollisionAvoidingAi extends Component {
    private int minRadius;
    private int maxRadius;

    private int pathFindRadius;

    private WorldListener worldListener;
    private MovementListener movementListener;

    // TODO: one variable for all AIs?
    private boolean stickToOrigin;

    public CollisionAvoidingAi(Entity entity, int minRadius, int maxRadius, boolean stickToOrigin) {
        super(entity, true);

        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.stickToOrigin = stickToOrigin;

        // TODO: magic number
        pathFindRadius = 5;

        // TODO: update listener if world changed
        entity.getWorld().addListener(worldListener);
    }

    @Override
    protected void initListeners() {
        worldListener = new WorldListener() {
            @Override
            public void positionChanged(World world, MovementComponent movement, int prevX, int prevY, IntVector2 current) {
                action();
            }
        };

        movementListener = new MovementListener() {
            @Override
            public void positionChanged(MovementComponent movement, int prevX, int prevY, IntVector2 current) {
                action();
            }
        };
    }

    @Override
    public boolean action() {
        Entity entity = getEntity();
        World world = entity.getWorld();

        if(world == null) {
            return false;
        }

        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);

        if (movement == null) {
            return false;
        }

        Array<Entity> entities = world.getEntitiesAt(movement.getPosition());
        entities.removeValue(entity, true);

        if (entities.size == 0) {
            return false;
        }

        IntVector2 position = stickToOrigin ? movement.getOrigin() : movement.getPosition();

        Array<IntVector2> passableCells = world.getPassableCells(
                position.x, position.y,
                true,
                minRadius, maxRadius,
                false);

        IntVector2 destination = null;

        // checking passable cells for available paths
        while (passableCells.size != 0) {
            IntVector2 passableCell = passableCells.random();
            passableCells.removeValue(passableCell, true);

            // TODO: magic numbers
            if (movement.tryRouteTo(passableCell, Priority.NORMAL, pathFindRadius, maxRadius, 0, 15) != null) {
                destination = passableCell;
                break;
            }
        }

        return destination != null;
    }

    @Override
    protected void attachEntity(Entity entity) {
        super.attachEntity(entity);

        entity
                .getComponent(MovementComponent.class)
                .addListener(movementListener);
    }

    @Override
    protected void detachEntity(Entity entity) {
        super.detachEntity(entity);

        entity
                .getComponent(MovementComponent.class)
                .removeListener(movementListener);
    }
}
