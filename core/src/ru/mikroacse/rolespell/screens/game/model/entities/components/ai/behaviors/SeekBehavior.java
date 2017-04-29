package ru.mikroacse.rolespell.screens.game.model.entities.components.ai.behaviors;

import ru.mikroacse.engine.utils.Vector2;
import ru.mikroacse.rolespell.screens.game.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.core.Entity;
import ru.mikroacse.rolespell.screens.game.model.world.World;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.Priority;

import java.util.List;

/**
 * Move towards a target (land near target)
 */
public class SeekBehavior extends Behavior {
    private int randomDistance;
    
    public SeekBehavior(Priority priority, Interval interval, int activationDistance) {
        super(priority, Type.SOMETIMES, true, Trigger.BOTH);
        
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
        if (targets.isEmpty()) {
            return false;
        }
        
        // get centroid of target positions
        Vector2 destination = new Vector2(0, 0);
        
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
        List<Vector2> passableCells = world.getPassableCells(
                destination.x,
                destination.y,
                true,
                1,
                randomDistance,
                false
        );
        
        Vector2 position = entity.getComponent(MovementComponent.class).getPosition();
        
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
