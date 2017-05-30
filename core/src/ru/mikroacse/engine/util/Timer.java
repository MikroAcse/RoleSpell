package ru.mikroacse.engine.util;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;

/**
 * Created by MikroAcse on 14-Apr-17.
 */
public class Timer {
    private Interval interval;

    private float speed;
    private double time;

    private boolean enabled;

    private Listener listeners;

    public Timer(Interval interval, float speed) {
        this.interval = interval;
        this.speed = speed;

        listeners = ListenerSupportFactory.create(Listener.class);

        enabled = true;
    }

    public Timer(Interval interval) {
        this(interval, 1f);
    }

    public Timer(double interval) {
        this(new Interval(interval));
    }

    public boolean update(float delta) {
        if (!enabled) {
            return false;
        }

        boolean actionPerformed = false;
        double intervalValue = interval.getValue();

        time += delta * speed;

        while (time >= intervalValue) {
            actionPerformed = true;

            listeners.action(this);

            time -= intervalValue;
        }

        return actionPerformed;
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

    public void pause() {
        enabled = false;
    }

    public void resume() {
        enabled = true;
    }

    public void reset() {
        time = 0;
    }

    public Interval get() {
        return interval;
    }

    public void set(Interval interval) {
        this.interval = interval;
    }

    public void set(double interval) {
        this.interval.setValue(interval);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Timer{" +
                "interval=" + interval +
                ", speed=" + speed +
                ", time=" + time +
                ", enabled=" + enabled +
                '}';
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void action(Timer timer);
    }
}
