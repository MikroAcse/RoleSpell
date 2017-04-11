package ru.mikroacse.rolespell.model.entities.components.drawable;

import ru.mikroacse.rolespell.model.entities.components.core.Component;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class DrawableComponent extends Component {
    public DrawableComponent(Entity entity) {
        super(entity);
    }
}
