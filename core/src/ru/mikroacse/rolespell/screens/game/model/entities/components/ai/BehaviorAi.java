package ru.mikroacse.rolespell.screens.game.model.entities.components.ai;

import ru.mikroacse.engine.utils.Vector2;
import ru.mikroacse.rolespell.screens.game.model.entities.components.Component;
import ru.mikroacse.rolespell.screens.game.model.entities.components.ai.behaviors.Behavior;
import ru.mikroacse.rolespell.screens.game.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.core.Entity;
import ru.mikroacse.rolespell.screens.game.model.world.World;
import ru.mikroacse.util.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class BehaviorAi extends Component {
    // TODO: Target Selectors
    private List<Entity> targets;
    
    private int activationDistance;
    private int deactivationDistance;
    
    private int maxTargets;
    
    private List<Behavior> behaviors;
    private Target target;
    
    private MovementComponent.Listener movementListener;
    private Interval.Listener intervalListener;
    
    /**
     * @param entity               Entity to which behavior is being applied
     * @param activationDistance   Global target activation distance (use 0 to customize it for every behavior)
     * @param deactivationDistance Global target deactivation distance (smaller values — better performance)
     */
    public BehaviorAi(Entity entity, int activationDistance, int deactivationDistance) {
        super(entity);
        this.activationDistance = activationDistance;
        this.deactivationDistance = deactivationDistance;
        
        maxTargets = Integer.MAX_VALUE;
        
        behaviors = new ArrayList<>();
        
        targets = new ArrayList<>();
        target = Target.CUSTOM;
        
        // TODO:
        movementListener = new MovementComponent.Listener() {
            @Override
            public void originChanged(MovementComponent movement, Vector2 previous, Vector2 current) {
            
            }
            
            @Override
            public void positionChanged(MovementComponent movement, Vector2 previous, Vector2 current) {
                process(movement);
            }
        };
        
        intervalListener = this::process;
    }
    
    public BehaviorAi(Entity entity, int deactivationDistance) {
        this(entity, 0, deactivationDistance);
    }
    
    @Override
    public boolean action() {
        return process();
    }
    
    @Override
    public boolean update(float delta) {
        boolean updated = false;
        
        for (Behavior behavior : behaviors) {
            updated |= behavior.update(delta);
        }
        
        return updated;
    }
    
    private boolean process(Behavior.Trigger trigger, Interval interval) {
        if (behaviors.isEmpty()) {
            return false;
        }
        
        Entity entity = getEntity();
        World world = entity.getWorld();
        
        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);
        Vector2 position = movement.getPosition();
        
        List<Entity> targets = new ArrayList<>();
        
        // TODO: optimize
        if (target == Target.ALL || target == Target.NEAREST) {
            targets.addAll(world.getEntitiesAt(position, deactivationDistance));
            targets.remove(entity);
        }
        if (target == Target.ALL || target == Target.CUSTOM) {
            targets.addAll(this.targets);
        }
        
        targets.removeIf(target -> {
            MovementComponent targetMovement = target.getComponent(MovementComponent.class);
            Vector2 targetPosition = targetMovement.getPosition();
            
            double distance = position.distance(targetPosition);
            
            return distance < activationDistance || distance > deactivationDistance;
        });
        
        targets.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return Double.compare(getDistance(o1), getDistance(o2));
            }
            
            public double getDistance(Entity target) {
                MovementComponent movement = entity.getComponent(MovementComponent.class);
                Vector2 position = movement.getPosition();
                
                MovementComponent targetMovement = target.getComponent(MovementComponent.class);
                Vector2 targetPosition = targetMovement.getPosition();
                
                return position.distance(targetPosition);
            }
        });
        
        // truncate target list
        if (targets.size() > maxTargets) {
            targets = targets.subList(0, maxTargets);
        }
        
        boolean actionPerformed = false;
        boolean soloistPerformed = false;
        for (Behavior behavior : behaviors) {
            if (trigger != null) {
                Behavior.Trigger behaviorTrigger = behavior.getTrigger();
                
                if (behaviorTrigger != trigger) {
                    if (behaviorTrigger != Behavior.Trigger.BOTH) {
                        continue;
                    }
                }
                
                if (behaviorTrigger == Behavior.Trigger.INTERVAL) {
                    if (interval != null && behavior.getInterval() != interval) {
                        continue;
                    }
                }
            }
            
            if (soloistPerformed && behavior.getType() == Behavior.Type.SOMETIMES) {
                continue;
            }
            
            if (behavior.process(entity, targets)) {
                actionPerformed = true;
                soloistPerformed |= behavior.isSolo();
            }
        }
        
        return actionPerformed;
    }
    
    public boolean process(Interval interval) {
        return process(Behavior.Trigger.INTERVAL, interval);
    }
    
    public boolean process(MovementComponent movement) {
        return process(Behavior.Trigger.MOVEMENT, null);
    }
    
    public boolean process() {
        return process(Behavior.Trigger.BOTH, null);
    }
    
    /**
     * Subscribes to specific target events.
     */
    private void attachTarget(Entity target) {
        target
                .getComponent(MovementComponent.class)
                .addListener(movementListener);
    }
    
    /**
     * Unsubscribes from specific target events.
     */
    private void detachTarget(Entity target) {
        target
                .getComponent(MovementComponent.class)
                .removeListener(movementListener);
    }
    
    /**
     * Subscribes to specific behavior events.
     */
    private void attachBehavior(Behavior behavior) {
        behavior
                .getInterval()
                .addListener(intervalListener);
    }
    
    /**
     * Unsubscribes from specific behavior events.
     */
    private void detachBehavior(Behavior behavior) {
        behavior
                .getInterval()
                .removeListener(intervalListener);
    }
    
    public void setTarget(Entity target) {
        clearTargets();
        addTarget(target);
    }
    
    public void addTarget(Entity target) {
        targets.add(target);
        attachTarget(target);
    }
    
    public boolean removeTarget(Entity target) {
        detachTarget(target);
        return targets.remove(target);
    }
    
    public void clearTargets() {
        // detach each entity
        targets.forEach(this::detachTarget);
        
        targets.clear();
    }
    
    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
        attachBehavior(behavior);
        
        // TODO: move to list implementation?
        Collections.sort(behaviors, Collections.reverseOrder());
    }
    
    public boolean removeBehavior(Behavior behavior) {
        detachBehavior(behavior);
        return behaviors.remove(behavior);
    }
    
    public void clearBehaviors() {
        behaviors.forEach(this::detachBehavior);
        
        behaviors.clear();
    }
    
    public List<Entity> getTargets() {
        return targets;
    }
    
    public int getActivationDistance() {
        return activationDistance;
    }
    
    public void setActivationDistance(int activationDistance) {
        this.activationDistance = activationDistance;
    }
    
    public int getDeactivationDistance() {
        return deactivationDistance;
    }
    
    public void setDeactivationDistance(int deactivationDistance) {
        this.deactivationDistance = deactivationDistance;
    }
    
    public Target getTargetType() {
        return target;
    }
    
    public void setTargetType(Target target) {
        this.target = target;
    }
    
    public int getMaxTargets() {
        return maxTargets;
    }
    
    public void setMaxTargets(int maxTargets) {
        this.maxTargets = maxTargets;
    }
    
    public enum Target {
        CUSTOM, // targets from custom list
        NEAREST, // nearest target
        ALL // all targets in radius + from custom list
    }
}
