package ru.mikroacse.rolespell.app.model.game.entities.components;

import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.engine.util.Interval;

/**
 * Created by MikroAcse on 09-Apr-17.
 */
public abstract class IntervalComponent extends Component {
    private Interval interval;
    private Interval.Listener listener;
    
    public IntervalComponent(Entity entity, Interval interval) {
        super(entity);
        
        listener = this::action;
        
        setInterval(interval);
    }
    
    public IntervalComponent(Entity entity) {
        this(entity, null);
    }
    
    @Override
    public boolean update(float delta) {
        if (interval == null) {
            return false;
        }
        return interval.update(delta);
    }
    
    protected void action(Interval interval) {
        action();
    }
    
    public Interval getInterval() {
        return interval;
    }
    
    public void setInterval(Interval interval) {
        if (this.interval != null) {
            this.interval.removeListener(listener);
        }
        
        this.interval = interval;
        
        if (interval != null) {
            interval.addListener(listener);
        }
    }
}
