package ru.mikroacse.rolespell.app.model.game.entities.components.movement;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.components.IntervalComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.engine.util.Interval;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class MovementComponent extends IntervalComponent {
    private float speed;
    
    private IntVector2 position;
    private IntVector2 origin;
    
    private UpdateType type;
    
    private Listener listeners;
    
    public MovementComponent(Entity entity, int x, int y, float speed) {
        super(entity, new Interval(1.0));
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
        IntVector2 previous = this.origin;
        
        this.position.set(position);
        listeners.positionChanged(this, previous, position);
    }
    
    public IntVector2 getOrigin() {
        return origin;
    }
    
    public void setOrigin(IntVector2 origin) {
        IntVector2 previous = this.origin;
        
        this.origin.set(origin);
        listeners.originChanged(this, previous, origin);
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
    
    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        public void originChanged(MovementComponent movement, IntVector2 previous, IntVector2 current);
        
        public void positionChanged(MovementComponent movement, IntVector2 previous, IntVector2 current);
    }
}
