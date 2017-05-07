package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 14-Apr-17.
 */
public abstract class Behavior implements Comparable<Behavior> {
    private EnumSet<Trigger> triggers;

    private double activationDistance;
    private double deactivationDistance;

    private Timer timer;
    private Priority priority;

    private boolean independent;

    /**
     * @param priority    Behavior priority.
     * @param independent If true, stops processing other behaviors after processing this.
     */
    public Behavior(Priority priority, boolean independent, EnumSet<Trigger> triggers) {
        this.priority = priority;
        this.independent = independent;
        this.triggers = triggers;

        this.activationDistance = 0;
        this.deactivationDistance = Double.POSITIVE_INFINITY;
    }

    public boolean update(float delta) {
        if (timer == null) {
            return false;
        }

        return timer.update(delta);
    }

    public abstract boolean process(Entity entity, Array<Entity> targets);

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

    public Priority getPriority() {
        return priority;
    }

    public boolean isIndependent() {
        return independent;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public double getActivationDistance() {
        return activationDistance;
    }

    public void setActivationDistance(double activationDistance) {
        this.activationDistance = activationDistance;
    }

    public double getDeactivationDistance() {
        return deactivationDistance;
    }

    public void setDeactivationDistance(double deactivationDistance) {
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

    public enum Trigger {
        MOVEMENT, // update when one of the targets has moved
        INTERVAL; // update on interval event

        public static final EnumSet<Trigger> ALL = EnumSet.allOf(Trigger.class);
    }
}
