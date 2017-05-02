package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.PickupBehavior;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 02-May-17.
 */
public class PickupAi extends BehaviorAi {
    private PickupBehavior pickupBehavior;
    
    public PickupAi(Entity entity) {
        super(entity, 0);
        
        // pick up only one item at the time
        setMaxTargets(1);
    
        pickupBehavior = new PickupBehavior();
        addBehavior(pickupBehavior);
    }
    
    public PickupBehavior getPickupBehavior() {
        return pickupBehavior;
    }
}
