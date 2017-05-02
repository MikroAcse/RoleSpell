package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.PickupBehavior;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 02-May-17.
 */
public class PickupAi extends BehaviorAi<PickupBehavior> {
    public PickupAi(Entity entity) {
        super(entity, 0);
        
        // pick up only one item at the time
        setMaxTargets(1);
        
        addBehavior(new PickupBehavior());
    }
}
