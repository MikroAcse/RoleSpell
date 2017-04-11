package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.components.core.Component;
import ru.mikroacse.rolespell.model.entities.components.core.IntervalComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.ArrayUtil;
import ru.mikroacse.util.LimitedDouble;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.Priority;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class SimpleBehaviorAi extends IntervalComponent implements MovementComponent.Listener {
    private Type type;
    private Entity target;
    private int radius;

    public SimpleBehaviorAi(Entity entity, LimitedDouble interval, Type type, Entity target, int radius) {
        super(entity, interval);
        this.type = type;
        this.radius = radius;

        setTarget(target);
    }

    @Override
    public boolean action() {
        if (target == null) return false;

        Entity entity = getEntity();
        World world = entity.getWorld();
        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);

        MovementComponent targetMovement = target.getComponent(MovementComponent.class);
        Position targetPosition = targetMovement.getPosition().copy();

        // TODO: beautify
        if (movement.getPosition().distance(targetMovement.getPosition()) <= radius) {
            return false;
        }

        switch (type) {
            case RUN_AWAY:

                break;
            case AVOID:
                break;
            case FOLLOW:
                targetPosition.translate(
                        ArrayUtil.getRandom(Arrays.asList(-1, 1)),
                        ArrayUtil.getRandom(Arrays.asList(-1, 0, 1))
                );

                // TODO: magic number
                movement.routeTo(targetPosition, Priority.NORMAL, 10, 15);
                break;
            case NONE:
                // do nothing
                break;
        }

        return false;
    }

    @Override
    public void originChanged(MovementComponent movement, Position previous, Position current) {

    }

    @Override
    public void positionChanged(MovementComponent movement, Position previous, Position current) {
        action();
    }

    /**
     * Subscribes to specific target events.
     */
    protected void attachTarget(Entity target) {
        target
                .getComponent(MovementComponent.class)
                .addListener(this);
    }

    /**
     * Unsubscribes from specific target events.
     */
    protected void detachTarget(Entity target) {
        target
                .getComponent(MovementComponent.class)
                .removeListener(this);
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        if(this.target != null) {
            detachTarget(target);
        }

        this.target = target;

        if(target != null) {
            attachTarget(target);
        }
    }

    public enum Type {
        RUN_AWAY,
        AVOID,
        FOLLOW,
        NONE
    }
}
