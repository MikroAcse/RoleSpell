package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.Behavior;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.Behavior.Trigger;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementListener;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.model.game.world.WorldListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public abstract class BehaviorAi extends Component {
    private EnumSet<EntityType> targetTypes; // limit target types
    private boolean blacklist; // use target type list as blacklist

    private Array<Entity> targets;

    private double activationDistance;
    private double deactivationDistance;

    private int maxTargets;

    private Array<Behavior> behaviors;
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

        behaviors = new Array<>();

        targets = new Array<>();
        targetSelectors = TargetSelector.ALL;

        targetTypes = EntityType.ALL;
        blacklist = false;

        World world = entity.getWorld();

        world.addListener(new WorldListener() {
            @Override
            public void entityMoved(World world, Entity entity, int prevX, int prevY, IntVector2 current) {
                process(EnumSet.of(Trigger.MOVEMENT), null);
            }
        });

        movementListener = new MovementListener() {
            @Override
            public void positionChanged(MovementComponent movement, int prevX, int prevY, IntVector2 current) {
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

        for (int i = behaviors.size - 1; i >= 0; i--) {
            updated |= behaviors.get(i).update(delta);
        }

        return updated;
    }

    public boolean process(EnumSet<Trigger> triggers, Timer timer) {
        if (behaviors.size == 0) {
            return false;
        }

        Entity entity = getEntity();
        World world = entity.getWorld();

        IntVector2 position = entity.getPosition();

        Array<Entity> targets = new Array<>();

        if (targetSelectors.contains(TargetSelector.NEAREST)) {
            targets.addAll(world.getEntitiesAt(position, (int) deactivationDistance));
            targets.removeValue(entity, true);
        }

        if (targetSelectors.contains(TargetSelector.CUSTOM)) {
            targets.addAll(this.targets);
        }

        // remove inactivate targets and targets with disabled types
        for (int i = targets.size - 1; i >= 0; i--) {
            Entity target = targets.get(i);

            if(targetTypes.contains(target.getType()) == blacklist) {
                targets.removeValue(target, true);
                continue;
            }

            IntVector2 targetPosition = target.getPosition();

            double distance = position.distance(targetPosition);

            if(distance < activationDistance || distance > deactivationDistance) {
                targets.removeValue(target, true);
            }
        }

        // TODO: separate comparator
        // sort targets by distance
        targets.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return Double.compare(getDistance(o1), getDistance(o2));
            }

            public double getDistance(Entity target) {
                IntVector2 position = entity.getPosition();

                IntVector2 targetPosition = target.getPosition();

                return position.distance(targetPosition);
            }
        });

        // truncate target list
        if (targets.size > maxTargets) {
            targets.truncate(maxTargets);
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

    public void setTarget(Entity targetSelectors) {
        clearTargets();
        addTarget(targetSelectors);
    }

    public void addTarget(Entity target) {
        targets.add(target);
        attachTarget(target);
    }

    public boolean removeTarget(Entity target) {
        detachTarget(target);
        return targets.removeValue(target, true);
    }

    public void clearTargets() {
        // detach each target
        targets.forEach(this::detachTarget);

        targets.clear();
    }

    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
        attachBehavior(behavior);

        // TODO: move to list implementation?
        behaviors.sort();
        behaviors.sort(Collections.reverseOrder());
    }

    public boolean removeBehavior(Behavior behavior) {
        detachBehavior(behavior);
        return behaviors.removeValue(behavior, true);
    }

    public void clearBehaviors() {
        behaviors.forEach(this::detachBehavior);

        behaviors.clear();
    }

    public Array<Entity> getTargets() {
        return targets;
    }

    public double getActivationDistance() {
        return activationDistance;
    }

    public void setActivationDistance(double activationDistance) {
        this.activationDistance = activationDistance;
    }

    public double getDeactivationDistance() {
        return deactivationDistance;
    }

    public void setDeactivationDistance(double deactivationDistance) {
        this.deactivationDistance = deactivationDistance;
    }

    public EnumSet<TargetSelector> getTargetSelectors() {
        return targetSelectors;
    }

    public void setTargetSelectors(EnumSet<TargetSelector> targetSelectors) {
        this.targetSelectors = targetSelectors;
    }

    public EnumSet<EntityType> getTargetTypes() {
        return targetTypes;
    }

    public void setTargetTypes(EnumSet<EntityType> targetTypes) {
        this.targetTypes = targetTypes;
    }

    public boolean isBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    public int getMaxTargets() {
        return maxTargets;
    }

    public void setMaxTargets(int maxTargets) {
        this.maxTargets = maxTargets;
    }

    public Array<Behavior> getBehaviors() {
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
