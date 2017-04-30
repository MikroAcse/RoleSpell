package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import ru.mikroacse.engine.util.Vector2;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.ListUtil;
import ru.mikroacse.engine.util.Priority;

import java.util.ArrayList;
import java.util.List;

/**
 * Move towards a target (land exactly on the target).
 */
public class ArriveBehavior extends Behavior {
    public ArriveBehavior(Priority priority, Interval interval) {
        super(priority, Type.SOMETIMES, true, Trigger.BOTH);
        
        setInterval(interval);
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
        
        // TODO: better solution
        List<Vector2> translate = new ArrayList<>();
        translate.add(new Vector2(-1, -1));
        translate.add(new Vector2(-1, 0));
        translate.add(new Vector2(0, -1));
        translate.add(new Vector2(1, 0));
        translate.add(new Vector2(0, 1));
        translate.add(new Vector2(1, 1));
        
        destination.translate(ListUtil.getRandom(translate));
        
        // TODO: magic number
        return entity
                .getComponent(PathMovementComponent.class)
                .tryRouteTo(destination, getPriority(), 5, 15);
    }
}
