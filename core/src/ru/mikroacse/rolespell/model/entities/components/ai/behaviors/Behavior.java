package ru.mikroacse.rolespell.model.entities.components.ai.behaviors;

import ru.mikroacse.rolespell.model.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.Priority;

import java.util.List;

/**
 * Created by MikroAcse on 14-Apr-17.
 */
public abstract class Behavior {
    private Trigger trigger;

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
    }

    public boolean update(float delta) {
        if(interval == null) {
            return false;
        }

        return interval.update(delta);
    }

    public abstract boolean process(Entity entity, List<Entity> targets);

    // TODO: does it belong here?
    public Position getCentroid(List<Entity> entities) {
        Position centroid = new Position(0, 0);

        for (Entity target : entities) {
            MovementComponent targetMovement = target.getComponent(MovementComponent.class);
            Position targetPosition = targetMovement.getPosition();

            centroid.translate(targetPosition);
        }

        int positions = entities.size();
        centroid.set(centroid.x / positions, centroid.y / positions);

        return centroid;
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
