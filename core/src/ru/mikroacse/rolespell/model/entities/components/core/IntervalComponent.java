package ru.mikroacse.rolespell.model.entities.components.core;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.util.Interval;

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
