package ru.mikroacse.rolespell.app.model.game.entities.components;

import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;

/**
 * Created by MikroAcse on 09-Apr-17.
 */
public abstract class TimerComponent extends Component {
    private Timer timer;
    private Timer.Listener listener;

    public TimerComponent(Entity entity, Timer timer, boolean single) {
        super(entity, single);

        listener = this::action;

        setTimer(timer);
    }

    @Override
    public boolean update(float delta) {
        if (timer == null) {
            return false;
        }
        return timer.update(delta);
    }

    protected void action(Timer timer) {
        action();
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        if (this.timer != null) {
            this.timer.removeListener(listener);
        }

        this.timer = timer;

        if (timer != null) {
            timer.addListener(listener);
        }
    }
}
