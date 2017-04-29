package ru.mikroacse.rolespell.screens.game.model.entities.components.ai.behaviors;

import ru.mikroacse.engine.utils.Vector2;
import ru.mikroacse.rolespell.screens.game.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters.DamageParameter;
import ru.mikroacse.rolespell.screens.game.model.entities.core.Entity;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.Priority;

import java.util.List;

/**
 * Created by MikroAcse on 18-Apr-17.
 */
public class AttackBehavior extends Behavior {
    public AttackBehavior(Priority priority, Interval interval) {
        super(priority, Type.ALWAYS, false, Trigger.INTERVAL);
        
        setInterval(interval);
    }
    
    @Override
    public boolean process(Entity entity, List<Entity> targets) {
        if (targets.isEmpty()) {
            return false;
        }
        
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        Vector2 position = movement.getPosition();
        
        StatusComponent status = entity.getComponent(StatusComponent.class);
        DamageParameter damage = status.getParameter(DamageParameter.class);
        
        boolean bumped = false;
        
        for (Entity target : targets) {
            if (isTargetActivated(entity, target)) {
                MovementComponent targetMovement = target.getComponent(MovementComponent.class);
                Vector2 targetPosition = targetMovement.getPosition();
                
                if (position.distance(targetPosition) <= damage.getAttackDistance()) {
                    bumped |= damage.bump(target);
                }
            }
        }
        
        return bumped;
    }
}
