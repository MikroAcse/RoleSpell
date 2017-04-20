package ru.mikroacse.rolespell.model.entities.components.ai.behaviors;

import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.Priority;

import java.util.List;
import java.util.Random;

/**
 * Created by MikroAcse on 14-Apr-17.
 */
public abstract class Behavior implements Comparable<Behavior> {
    private Trigger trigger;

    private int activationDistance;
    private int deactivationDistance;

    private Interval interval;
    private Priority priority;

    private boolean solo;
    private Type type;

    /**
     * @param priority Behavior priority.
     * @param solo     If true, stops processing other behaviors after processing this.
     */
    public Behavior(Priority priority, Type type, boolean solo, Trigger trigger) {
        this.priority = priority;
        this.type = type;
        this.solo = solo;
        this.trigger = trigger;

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
        Position position = movement.getPosition();

        MovementComponent targetMovement = target.getComponent(MovementComponent.class);
        Position targetPosition = targetMovement.getPosition();

        double distance = position.distance(targetPosition);

        return distance >= activationDistance && distance <= deactivationDistance;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isSolo() {
        return solo;
    }

    public void setSolo(boolean solo) {
        this.solo = solo;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    @Override
    public int compareTo(Behavior o) {
        return getPriority().compare(o.getPriority());
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        int result = new Random().nextInt();
        return result;
    }

    public enum Trigger {
        NONE, // TODO: deactivate behavior ai
        MOVEMENT, // update when one of the targets has moved
        INTERVAL, // update on interval event
        BOTH // update both on movement and interval event
    }

    public enum Type {
        SOMETIMES, // don't action behavior if being overridden by solo behavior
        ALWAYS // action behavior even if there is a solo behavior
    }
}
