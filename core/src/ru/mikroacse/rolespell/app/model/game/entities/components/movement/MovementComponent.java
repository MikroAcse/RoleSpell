package ru.mikroacse.rolespell.app.model.game.entities.components.movement;

import ru.mikroacse.engine.listeners.AbstractListener;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.TimerComponent;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class MovementComponent extends TimerComponent {
    private float speed;

    private IntVector2 position;
    private IntVector2 origin;

    private UpdateType type;

    private Listener listeners;

    public MovementComponent(Entity entity, int x, int y, float speed) {
        super(entity, new Timer(1.0), true);
        this.speed = speed;

        listeners = ListenerSupportFactory.create(Listener.class);

        origin = new IntVector2(x, y);
        position = origin.copy();

        type = UpdateType.BOTH;
    }

    @Override
    public boolean update(float delta) {
        return super.update(delta * speed);
    }

    public void moveTo(IntVector2 position, UpdateType type) {
        switch (type) {
            case ORIGIN:
                setOrigin(position);
                break;
            case POSITION:
                setPosition(position);
                break;
            case BOTH:
                setBoth(position);
                break;
        }
    }

    public void moveTo(IntVector2 position) {
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

    public IntVector2 getPosition() {
        return position;
    }

    public void setPosition(IntVector2 position) {
        setPosition(position.x, position.y);
    }

    public void setPosition(int x, int y) {
        int prevX = position.x;
        int prevY = position.y;

        position.set(x, y);

        listeners.positionChanged(this, prevX, prevY, position);
    }

    public IntVector2 getOrigin() {
        return origin;
    }

    public void setOrigin(IntVector2 origin) {
        setOrigin(origin.x, origin.y);
    }

    public void setOrigin(int x, int y) {
        int prevX = origin.x;
        int prevY = origin.y;

        origin.set(x, y);

        listeners.originChanged(this, prevX, prevY, origin);
    }

    public void setBoth(IntVector2 position) {
        setOrigin(position);
        setPosition(position);
    }

    public UpdateType getType() {
        return type;
    }

    public void setType(UpdateType type) {
        this.type = type;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public enum UpdateType {
        ORIGIN,
        POSITION,
        BOTH
    }

    public interface Listener extends AbstractListener {
        void originChanged(MovementComponent movement, int prevX, int prevY, IntVector2 current);

        void positionChanged(MovementComponent movement, int prevX, int prevY, IntVector2 current);
    }
}
