package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.components.core.IntervalComponent;
import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.DamageParameter;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.util.Interval;

/**
 * Created by MikroAcse on 13-Apr-17.
 */
// TODO: Convert to behavior
public class AttackAi extends IntervalComponent {
    private Target target;

    private int randomTargetCount;

    public AttackAi(Entity entity, Target target) {
        // TODO: magic number
        super(entity, new Interval(1));

        this.target = target;

        randomTargetCount = 1; // TODO: magic number
    }

    @Override
    public boolean action() {
        /*Entity entity = getEntity();
        World world = entity.getWorld();
        MovementComponent movement = entity.getComponent(MovementComponent.class);

        StatusComponent status = entity.getComponent(StatusComponent.class);
        DamageParameter damage = status.getParameter(DamageParameter.class);

        ArrayList<Entity> targets = new ArrayList<>();

        if(target == Target.BEHAVIOR) {
            Entity target = getEntity().getComponent(BehaviorAi.class).getTarget();

            if(target != null) {
                targets.add(target);
            }
        } else {
            List<Entity> nearestEntities = world.getEntitiesAt(movement.getPosition(), damage.getAttackDistance());

            if(target == Target.ALL) {
                targets = nearestEntities;
            } else {
                for (int i = 0; i < randomTargetCount; i++) {
                    Entity randomEntity = ArrayUtil.getRandom(nearestEntities);

                    targets.add(randomEntity);
                    nearestEntities.remove(randomEntity);
                }
            }

            targets.remove(entity);
        }

        if(targets.isEmpty()) {
            return false;
        }

        for (Entity target : targets) {
            damage.bump(target);
        }*/

        return false;
    }

    @Override
    protected void attachEntity(Entity entity) {
        super.attachEntity(entity);

        StatusComponent status = entity.getComponent(StatusComponent.class);
        DamageParameter damage = status.getParameter(DamageParameter.class);

        // TODO: somehow 'link' interval to attack speed
        getInterval().set(damage.getAttackSpeed());
    }

    @Override
    protected void detachEntity(Entity entity) {
        super.detachEntity(entity);
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public int getRandomTargetCount() {
        return randomTargetCount;
    }

    public void setRandomTargetCount(int randomTargetCount) {
        this.randomTargetCount = randomTargetCount;
    }

    public enum Target {
        BEHAVIOR, // using BehaviorAi's target
        RANDOM, // using target count
        NEAREST,
        ALL
    }
}
