package ru.mikroacse.rolespell.model.entities.components.ai;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class SimpleBehaviorAi extends AiComponent {
    public enum Type {
        RUN_AWAY,
        AVOID,
        FOLLOW,
        NONE
    }

    @Override
    public boolean apply(Entity entity, World world) {
        return false;
    }
}
