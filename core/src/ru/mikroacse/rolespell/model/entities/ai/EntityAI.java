package ru.mikroacse.rolespell.model.entities.ai;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

import java.awt.*;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public class EntityAI {
    private EntityAIState state;
    private Point stayingPosition;

    public EntityAI() {
        state = EntityAIState.STAYING;
    }

    public void update(Entity entity, World world) {
        
    }

    public EntityAIState getState() {
        return state;
    }

    public void setState(EntityAIState state) {
        this.state = state;
    }

    public Point getStayingPosition() {
        return stayingPosition;
    }
}
