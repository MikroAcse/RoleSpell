package ru.mikroacse.rolespell.model.entities.ai.core;

import ru.mikroacse.rolespell.model.entities.core.Entity;

import java.awt.*;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public abstract class EntityAI {
    protected Entity entity;

    protected EntityAIState state;

    protected float updateInterval; // seconds between states change
    protected float time;

    public boolean enabled = true;

    public EntityAI(Entity entity) {
        this.entity = entity;

        state = EntityAIState.STAYING;
        time = 0f;
        updateInterval = Float.POSITIVE_INFINITY;
    }

    public final void update(float delta) {
        if(!enabled) {
            return;
        }

        time += delta;

        while(time >= updateInterval) {
            update();

            time -= updateInterval;
        }
    }

    public abstract void update();

    public void resetTime() {
        time = 0f;
    }

    public EntityAIState getState() {
        return state;
    }

    public void setState(EntityAIState state) {
        this.state = state;
    }

    public float getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(float updateInterval) {
        this.updateInterval = updateInterval;
    }
}
