package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.AbstractListener;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;

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

    private Listener listeners;

    private Timer.Listener timerListener;

    private boolean independent;

    private boolean enabled;

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

        listeners = ListenerSupportFactory.create(Listener.class);

        timerListener = timer -> listeners.timer(Behavior.this);

        enabled = true;
    }

    public boolean update(float delta) {
        return enabled && timer != null && timer.update(delta);
    }

    public abstract boolean process(Entity entity, Array<Entity> targets);

    public boolean isTargetActivated(Entity entity, Entity target) {
        double distance = entity.getPosition().distance(target.getPosition());

        return distance >= activationDistance && distance <= deactivationDistance;
    }

    public void addListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).addListener(listener);
    }

    public void removeListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<Listener>) listeners).clearListeners();
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
        if (this.timer != null) {
            this.timer.removeListener(timerListener);
        }

        this.timer = timer;

        if (timer != null) {
            timer.addListener(timerListener);
        }
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int compareTo(Behavior o) {
        return getPriority().compare(o.getPriority());
    }

    public enum Trigger {
        MOVEMENT, // update when one of the targets has moved
        TIMER; // update on timer event

        public static final EnumSet<Trigger> ALL = EnumSet.allOf(Trigger.class);
    }

    public interface Listener extends AbstractListener {
        void timer(Behavior behavior);
    }
}
