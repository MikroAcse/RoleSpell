package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.DamageProperty;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 18-Apr-17.
 */
public class AttackBehavior extends Behavior {
    public AttackBehavior(Timer timer) {
        super(Priority.IMMEDIATELY, true, EnumSet.of(Trigger.INTERVAL));

        setTimer(timer);
    }

    @Override
    public boolean process(Entity entity, Array<Entity> targets) {
        targets.removeValue(entity, true);
        if (targets.size == 0) {
            return false;
        }

        IntVector2 position = entity.getPosition();

        StatusComponent status = entity.getComponent(StatusComponent.class);
        DamageProperty damage = status.getProperty(DamageProperty.class);

        boolean bumped = false;

        for (Entity target : targets) {
            if (isTargetActivated(entity, target)) {
                if (position.distance(target.getPosition()) <= damage.getAttackDistance()) {
                    bumped |= damage.bump(target);
                }
            }
        }

        return bumped;
    }
}
