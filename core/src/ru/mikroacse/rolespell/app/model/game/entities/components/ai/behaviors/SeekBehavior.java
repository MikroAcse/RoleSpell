package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Priority;

import java.util.List;

/**
 * Move towards a target (land near target)
 */
public class SeekBehavior extends Behavior {
    private int randomDistance;
    
    public SeekBehavior(Priority priority, Interval interval, int activationDistance) {
        super(priority, false, Trigger.ALL);
        
        setInterval(interval);
        setActivationDistance(activationDistance);
        
        // TODO: magic number
        randomDistance = 2;
    }
    
    public SeekBehavior(Priority priority, Interval interval, int activationDistance, int deactivationDistance) {
        this(priority, interval, activationDistance);
        
        setDeactivationDistance(deactivationDistance);
    }
    
    @Override
    public boolean process(Entity entity, List<Entity> targets) {
        targets.remove(entity);
        if (targets.isEmpty()) {
            return false;
        }
        
        // get centroid of target positions
        IntVector2 destination = new IntVector2(0, 0);
        
        int targetCount = 0;
        
        for (Entity target : targets) {
            if (isTargetActivated(entity, target)) {
                MovementComponent targetMovement = target.getComponent(MovementComponent.class);
                
                destination.translate(targetMovement.getPosition());
                
                targetCount++;
            }
        }
        
        if (targetCount == 0) {
            return false;
        }
        
        destination.multiply(1 / targetCount);
        
        World world = entity.getWorld();
        List<IntVector2> passableCells = world.getPassableCells(
                destination.x,
                destination.y,
                true,
                1,
                randomDistance,
                false
        );
        
        IntVector2 position = entity.getComponent(MovementComponent.class).getPosition();
        
        if (passableCells.isEmpty()) {
            return false;
        }
        
        // TODO: magic numbers
        return entity
                .getComponent(PathMovementComponent.class)
                .tryRouteTo(
                        passableCells,
                        getPriority(),
                        5,
                        15
                );
    }
}
