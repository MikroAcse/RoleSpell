package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.Behavior;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.Behavior.Trigger;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.*;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class BehaviorAi extends Component {
    private List<Entity> targets;

    private int activationDistance;
    private int deactivationDistance;

    private int maxTargets;

    private List<Behavior> behaviors;
    private EnumSet<TargetSelector> targetSelectors;

    private MovementComponent.Listener movementListener;
    private Timer.Listener intervalListener;

    /**
     * @param entity               Entity to which behavior is being applied
     * @param activationDistance   Global target activation distance (use 0 to customize it for every behavior)
     * @param deactivationDistance Global target deactivation distance (smaller values — better performance)
     */
    public BehaviorAi(Entity entity, int activationDistance, int deactivationDistance) {
        super(entity);
        this.activationDistance = activationDistance;
        this.deactivationDistance = deactivationDistance;

        maxTargets = Integer.MAX_VALUE;

        behaviors = new ArrayList<>();

        targets = new ArrayList<>();
        targetSelectors = TargetSelector.ALL;

        World world = entity.getWorld();

        world.addListener(new World.Listener() {
            @Override
            public void entityMoved(Entity entity, IntVector2 previous, IntVector2 current) {
                process(EnumSet.of(Trigger.MOVEMENT), null);
            }
        });

        movementListener = new MovementComponent.Listener() {
            @Override
            public void originChanged(MovementComponent movement, IntVector2 previous, IntVector2 current) {

            }

            @Override
            public void positionChanged(MovementComponent movement, IntVector2 previous, IntVector2 current) {
                process(EnumSet.of(Trigger.MOVEMENT), null);
            }
        };

        intervalListener = interval -> process(EnumSet.of(Trigger.INTERVAL), interval);
    }

    public BehaviorAi(Entity entity, int deactivationDistance) {
        this(entity, 0, deactivationDistance);
    }

    @Override
    public boolean action() {
        return process(Trigger.ALL, null);
    }

    @Override
    public boolean update(float delta) {
        boolean updated = false;

        for (Behavior behavior : behaviors) {
            updated |= behavior.update(delta);
        }

        return updated;
    }

    public boolean process(EnumSet<Trigger> triggers, Timer timer) {
        if (behaviors.isEmpty()) {
            return false;
        }

        Entity entity = getEntity();
        World world = entity.getWorld();

        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);
        IntVector2 position = movement.getPosition();

        List<Entity> targets = new ArrayList<>();

        if (targetSelectors.contains(TargetSelector.NEAREST)) {
            targets.addAll(world.getEntitiesAt(position, deactivationDistance));
            targets.remove(entity);
        }

        if (targetSelectors.contains(TargetSelector.CUSTOM)) {
            targets.addAll(this.targets);
        }

        // remove inactivated targets
        targets.removeIf(target -> {
            MovementComponent targetMovement = target.getComponent(MovementComponent.class);
            IntVector2 targetPosition = targetMovement.getPosition();

            double distance = position.distance(targetPosition);

            return distance < activationDistance || distance > deactivationDistance;
        });

        // sort targets by distance
        targets.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return Double.compare(getDistance(o1), getDistance(o2));
            }

            public double getDistance(Entity target) {
                MovementComponent movement = entity.getComponent(MovementComponent.class);
                IntVector2 position = movement.getPosition();

                MovementComponent targetMovement = target.getComponent(MovementComponent.class);
                IntVector2 targetPosition = targetMovement.getPosition();

                return position.distance(targetPosition);
            }
        });

        // truncate target list
        if (targets.size() > maxTargets) {
            targets = targets.subList(0, maxTargets);
        }

        boolean actionPerformed = false;
        boolean soloistPerformed = false;
        for (Behavior behavior : behaviors) {
            if (triggers != null) {
                if (!behavior.getTriggers().containsAll(triggers)) {
                    continue;
                }

                // if intervals aren't equal
                if (triggers.contains(Trigger.INTERVAL)) {
                    if (timer != null && behavior.getTimer() != timer) {
                        continue;
                    }
                }
            }
            if (soloistPerformed && !behavior.isIndependent()) {
                continue;
            }

            if (behavior.process(entity, targets)) {
                actionPerformed = true;
                soloistPerformed |= !behavior.isIndependent();
            }
        }

        return actionPerformed;
    }

    /**
     * Subscribes to specific target events.
     */
    private void attachTarget(Entity target) {
        target.getComponent(MovementComponent.class)
                .addListener(movementListener);
    }

    /**
     * Unsubscribes from specific target events.
     */
    private void detachTarget(Entity target) {
        target.getComponent(MovementComponent.class)
                .removeListener(movementListener);
    }

    /**
     * Subscribes to specific behavior events.
     */
    private void attachBehavior(Behavior behavior) {
        if (behavior.getTriggers().contains(Trigger.INTERVAL)) {
            behavior.getTimer()
                    .addListener(intervalListener);
        }
    }

    /**
     * Unsubscribes from specific behavior events.
     */
    private void detachBehavior(Behavior behavior) {
        if (behavior.getTriggers().contains(Trigger.INTERVAL)) {
            behavior.getTimer()
                    .removeListener(intervalListener);
        }
    }

    public void setTargetSelectors(Entity targetSelectors) {
        clearTargets();
        addTarget(targetSelectors);
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

        // TODO: move to list implementation?
        Collections.sort(behaviors, Collections.reverseOrder());
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

    public int getActivationDistance() {
        return activationDistance;
    }

    public void setActivationDistance(int activationDistance) {
        this.activationDistance = activationDistance;
    }

    public int getDeactivationDistance() {
        return deactivationDistance;
    }

    public void setDeactivationDistance(int deactivationDistance) {
        this.deactivationDistance = deactivationDistance;
    }

    public EnumSet<TargetSelector> getTargetSelectors() {
        return targetSelectors;
    }

    public void setTargetSelectors(EnumSet<TargetSelector> targetSelector) {
        this.targetSelectors = targetSelector;
    }

    public int getMaxTargets() {
        return maxTargets;
    }

    public void setMaxTargets(int maxTargets) {
        this.maxTargets = maxTargets;
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    /**
     * @return First behavior in the behaviors list.
     */
    public Behavior getBehavior() {
        return behaviors.get(0);
    }

    public enum TargetSelector {
        CUSTOM, // targets from custom list
        NEAREST; // nearest target

        public static final EnumSet<TargetSelector> ALL = EnumSet.allOf(TargetSelector.class);
    }
}
