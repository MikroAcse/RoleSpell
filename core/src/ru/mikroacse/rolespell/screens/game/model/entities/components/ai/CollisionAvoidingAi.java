package ru.mikroacse.rolespell.screens.game.model.entities.components.ai;

import ru.mikroacse.engine.utils.Vector2;
import ru.mikroacse.rolespell.screens.game.model.entities.components.Component;
import ru.mikroacse.rolespell.screens.game.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.core.Entity;
import ru.mikroacse.rolespell.screens.game.model.world.World;
import ru.mikroacse.util.ListUtil;
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
        
        Vector2 position = stickToOrigin ? movement.getOrigin() : movement.getPosition();
        
        List<Vector2> passableCells = world.getPassableCells(
                position.x,
                position.y,
                true,
                minRadius,
                maxRadius,
                false);
        
        Vector2 destination = null;
        
        // checking passable cells for available paths
        while (!passableCells.isEmpty()) {
            Vector2 passableCell = ListUtil.getRandom(passableCells);
            passableCells.remove(passableCell);
            
            if (movement.tryRouteTo(passableCell, Priority.HIGH, pathFindRadius, maxRadius)) {
                destination = passableCell;
                break;
            }
        }
        
        return destination != null;
    }
    
    @Override
    public void originChanged(MovementComponent movement, Vector2 previous, Vector2 current) {
    
    }
    
    @Override
    public void positionChanged(MovementComponent movement, Vector2 previous, Vector2 current) {
        action();
    }
    
    @Override
    public void entityMoved(Entity entity, Vector2 previous, Vector2 current) {
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
