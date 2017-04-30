package ru.mikroacse.engine.util;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;

/**
 * Created by MikroAcse on 14-Apr-17.
 */
public class Interval {
    private LimitedDouble interval;
    
    private float speed;
    private double time;
    
    private boolean randomized;
    private boolean enabled;
    
    private Listener listeners;
    
    public Interval(LimitedDouble interval, boolean randomized, float speed) {
        this.interval = interval;
        this.speed = speed;
        
        listeners = ListenerSupportFactory.create(Listener.class);
        
        enabled = true;
        this.randomized = randomized;
    }
    
    public Interval(LimitedDouble interval, boolean randomized) {
        this(interval, randomized, 1f);
    }
    
    public Interval(LimitedDouble interval) {
        this(interval, false);
    }
    
    public Interval(double interval, boolean randomized) {
        this(new LimitedDouble(interval), randomized);
    }
    
    public Interval(double interval) {
        this(interval, false);
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
        
        if (actionPerformed && randomized) {
            interval.randomize();
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
    
    public LimitedDouble get() {
        return interval;
    }
    
    public void set(LimitedDouble interval) {
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
    
    public boolean isRandomized() {
        return randomized;
    }
    
    public void setRandomized(boolean randomized) {
        this.randomized = randomized;
    }
    
    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void action(Interval interval);
    }
}
