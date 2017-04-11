package ru.mikroacse.rolespell.model.entities.components.movement;

import ru.mikroacse.rolespell.model.entities.components.core.IntervalComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.util.LimitedDouble;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.listeners.ListenerSupport;
import ru.mikroacse.util.listeners.ListenerSupportFactory;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class MovementComponent extends IntervalComponent {
    private Position position;
    private Position origin;

    private UpdateType type;

    private Listener listeners;

    public MovementComponent(Entity entity, int x, int y, double speed) {
        super(entity, new LimitedDouble(1.0));
        listeners = ListenerSupportFactory.create(Listener.class);

        origin = new Position(x, y);
        position = origin.copy();

        super.setSpeed(speed);

        type = UpdateType.BOTH;
    }

    public void moveTo(Position position, UpdateType type) {
        switch (type) {
            case ORIGIN:
                setOrigin(position);
                break;
            case CURRENT:
                setPosition(position);
                break;
            case BOTH:
                setBoth(position);
                break;
        }
    }

    public void moveTo(Position position) {
        moveTo(position, type);
    }

    public void teleportToOrigin() {
        setPosition(origin);
    }

    public void currentAsOrigin() {
        setOrigin(position);
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        Position previous = this.origin;

        this.position.set(position);
        listeners.positionChanged(this, previous, position);
    }

    public Position getOrigin() {
        return origin;
    }

    public void setOrigin(Position origin) {
        Position previous = this.origin;

        this.origin.set(origin);
        listeners.originChanged(this, previous, origin);
    }

    public void setBoth(Position position) {
        setOrigin(position);
        setPosition(position);
    }

    @Override
    public void setSpeed(double speed) {
        super.setSpeed(speed);
    }

    public UpdateType getType() {
        return type;
    }

    public void setType(UpdateType type) {
        this.type = type;
    }

    public enum UpdateType {
        ORIGIN,
        CURRENT,
        BOTH
    }

    public interface Listener extends ru.mikroacse.util.listeners.Listener {
        public void originChanged(MovementComponent movement, Position previous, Position current);
        public void positionChanged(MovementComponent movement, Position previous, Position current);
    }
}
