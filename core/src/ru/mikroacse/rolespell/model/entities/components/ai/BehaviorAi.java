package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.components.ai.behaviors.Behavior;
import ru.mikroacse.rolespell.model.entities.components.core.Component;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.Position;

import java.util.*;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class BehaviorAi extends Component {
    // TODO: Target Selectors
    private List<Entity> targets;

    private int activationDistance;
    private int deactivationDistance;

    private int maxTargets;

    private Set<Behavior> behaviors;
    private Target target;

    private Interval globalInterval;

    private MovementComponent.Listener movementListener;
    private Interval.Listener intervalListener;

    public BehaviorAi(Entity entity, int activationDistance, int deactivationDistance) {
        super(entity);
        this.activationDistance = activationDistance;
        this.deactivationDistance = deactivationDistance;

        maxTargets = Integer.MAX_VALUE;

        // descending order, sorting by priority
        behaviors = new TreeSet<>((o1, o2) -> {
            return -o1.getPriority().compare(o2.getPriority());
        });

        targets = new ArrayList<>();
        target = Target.CUSTOM;

        // TODO:
        movementListener = new MovementComponent.Listener() {
            @Override
            public void originChanged(MovementComponent movement, Position previous, Position current) {

            }

            @Override
            public void positionChanged(MovementComponent movement, Position previous, Position current) {
                process(movement);
            }
        };

        intervalListener = this::process;
    }

    @Override
    public boolean action() {
        return process();
    }

    @Override
    public boolean update(float delta) {
        boolean result = false;

        for (Behavior behavior : behaviors) {
            result |= behavior.update(delta);
        }

        return result;
    }

    private boolean process(Behavior.Trigger trigger, Interval interval) {
        Entity entity = getEntity();
        World world = entity.getWorld();

        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);
        Position position = movement.getPosition();

        List<Entity> targets = new ArrayList<>();

        // TODO: CODE DUPLICATION

        // get targets from custom list
        if (target == Target.ALL || target == Target.CUSTOM) {
            for (Entity target : this.targets) {
                MovementComponent targetMovement = target.getComponent(MovementComponent.class);
                Position targetPosition = targetMovement.getPosition();

                double distance = position.distance(targetPosition);

                if (distance >= activationDistance && distance <= deactivationDistance) {
                    targets.add(target);
                }
            }
        }

        // get targets from world
        if (target == Target.ALL || target == Target.NEAREST) {
            List<Entity> worldEntities = world.getEntitiesAt(position, deactivationDistance);

            for (Entity target : worldEntities) {
                MovementComponent targetMovement = target.getComponent(MovementComponent.class);
                Position targetPosition = targetMovement.getPosition();

                double distance = position.distance(targetPosition);

                if (distance >= activationDistance && distance <= deactivationDistance) {
                    targets.add(target);
                }
            }
        }

        // truncate targets list to needed count
        if (targets.size() > maxTargets) {
            Collections.shuffle(targets);
            targets = targets.subList(0, maxTargets);
        }

        boolean actionPerformed = false;
        boolean soloistPerformed = false;
        for (Behavior behavior : behaviors) {

            if(trigger != null) {
                Behavior.Trigger behaviorTrigger = behavior.getTrigger();

                if(behaviorTrigger != trigger) {
                    if(behaviorTrigger != Behavior.Trigger.BOTH) {
                        continue;
                    }
                }

                if(behaviorTrigger == Behavior.Trigger.INTERVAL) {
                    if(interval != null && behavior.getInterval() != interval) {
                        continue;
                    }
                }
            }

            if (soloistPerformed && behavior.getType() == Behavior.Type.SOMETIMES) {
                continue;
            }

            if (behavior.process(entity, targets)) {
                actionPerformed = true;
                soloistPerformed |= behavior.isSolo();
            }
        }

        return actionPerformed;
    }

    public boolean process(Interval interval) {
        return process(Behavior.Trigger.INTERVAL, interval);
    }

    public boolean process(MovementComponent movement) {
        return process(Behavior.Trigger.MOVEMENT, null);
    }

    public boolean process() {
        return process(Behavior.Trigger.BOTH, null);
    }

    /**
     * Subscribes to specific target events.
     */
    private void attachTarget(Entity target) {
        target
                .getComponent(MovementComponent.class)
                .addListener(movementListener);
    }

    /**
     * Unsubscribes from specific target events.
     */
    private void detachTarget(Entity target) {
        target
                .getComponent(MovementComponent.class)
                .removeListener(movementListener);
    }

    /**
     * Subscribes to specific behavior events.
     */
    private void attachBehavior(Behavior behavior) {
        behavior
                .getInterval()
                .addListener(intervalListener);
    }

    /**
     * Unsubscribes from specific behavior events.
     */
    private void detachBehavior(Behavior behavior) {
        behavior
                .getInterval()
                .removeListener(intervalListener);
    }

    public void addTarget(Entity target) {
        targets.add(target);
        attachTarget(target);
    }

    public boolean removeTarget(Entity target) {
        detachTarget(target);
        return targets.remove(target);
    }

    public void clearTargets() {
        // detach each entity
        targets.forEach(this::detachTarget);

        targets.clear();
    }

    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
        attachBehavior(behavior);
    }

    public boolean removeBehavior(Behavior behavior) {
        detachBehavior(behavior);
        return behaviors.remove(behavior);
    }

    public void clearBehaviors() {
        behaviors.forEach(this::detachBehavior);

        behaviors.clear();
    }

    public List<Entity> getTargets() {
        return targets;
    }

    public enum Target {
        CUSTOM, // targets from custom list
        NEAREST, // nearest target
        ALL // all targets in radius + from custom list
    }
}
