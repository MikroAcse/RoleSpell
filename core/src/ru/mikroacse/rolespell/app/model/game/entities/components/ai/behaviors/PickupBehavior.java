package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 02-May-17.
 */
public class PickupBehavior extends Behavior {
    public PickupBehavior() {
        super(Priority.IMMEDIATELY, true, EnumSet.of(Trigger.MOVEMENT));
    }

    @Override
    public boolean process(Entity entity, Array<Entity> targets) {
        if (targets.size == 0) {
            return false;
        }

        if (!entity.hasComponent(InventoryComponent.class)) {
            return false;
        }

        World world = entity.getWorld();
        InventoryComponent inventory = entity.getComponent(InventoryComponent.class);

        boolean pickedUp = false;

        for (Entity target : targets) {
            pickedUp |= inventory.pickup(target);
        }

        return pickedUp;
    }
}
