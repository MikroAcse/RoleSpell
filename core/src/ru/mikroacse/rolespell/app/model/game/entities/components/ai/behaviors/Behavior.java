package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Priority;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by MikroAcse on 14-Apr-17.
 */
public abstract class Behavior implements Comparable<Behavior> {
    private EnumSet<Trigger> triggers;
    
    private List<Entity> targets;
    
    private int activationDistance;
    private int deactivationDistance;
    
    private Interval interval;
    private Priority priority;
    
    private boolean independent;
    
    /**
     * @param priority Behavior priority.
     * @param independent     If true, stops processing other behaviors after processing this.
     */
    public Behavior(Priority priority, boolean independent, EnumSet<Trigger> triggers) {
        this.priority = priority;
        this.independent = independent;
        this.triggers = triggers;
        
        this.activationDistance = 0;
        this.deactivationDistance = Integer.MAX_VALUE;
    }
    
    public boolean update(float delta) {
        if (interval == null) {
            return false;
        }
        
        return interval.update(delta);
    }
    
    public abstract boolean process(Entity entity, List<Entity> targets);
    
    public boolean isTargetActivated(Entity entity, Entity target) {
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        IntVector2 position = movement.getPosition();
        
        MovementComponent targetMovement = target.getComponent(MovementComponent.class);
        IntVector2 targetPosition = targetMovement.getPosition();
        
        double distance = position.distance(targetPosition);
        
        return distance >= activationDistance && distance <= deactivationDistance;
    }
    
    public EnumSet<Trigger> getTriggers() {
        return triggers;
    }
    
    public void setTriggers(EnumSet<Trigger> triggers) {
        this.triggers = triggers;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public boolean isIndependent() {
        return independent;
    }
    
    public void setIndependent(boolean independent) {
        this.independent = independent;
    }
    
    public Interval getInterval() {
        return interval;
    }
    
    public void setInterval(Interval interval) {
        this.interval = interval;
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
    
    public List<Entity> getTargets() {
        return targets;
    }
    
    public void setTargets(List<Entity> targets) {
        this.targets = targets;
    }
    
    @Override
    public int compareTo(Behavior o) {
        return getPriority().compare(o.getPriority());
    }
    
    @Override
    public boolean equals(Object o) {
        return false;
    }
    
    public enum Trigger {
        MOVEMENT, // update when one of the targets has moved
        INTERVAL; // update on interval event
        
        public static final EnumSet<Trigger> ALL = EnumSet.allOf(Trigger.class);
        public static final EnumSet<Trigger> NONE = EnumSet.noneOf(Trigger.class);
    }
}
