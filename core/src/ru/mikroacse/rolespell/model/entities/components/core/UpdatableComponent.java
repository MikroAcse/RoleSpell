package ru.mikroacse.rolespell.model.entities.components.core;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public interface UpdatableComponent extends Component {
    boolean update(Entity entity, World world, float delta);
}
