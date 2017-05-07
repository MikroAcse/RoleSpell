package ru.mikroacse.rolespell.app.model.game.entities.components;

import ru.mikroacse.rolespell.app.model.game.entities.Entity;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class Component {
    private Entity entity;

    public Component(Entity entity) {
        setEntity(entity);
    }

    public boolean update(float delta) {
        return false;
    }

    public boolean action() {
        return false;
    }

    public void dispose() {
        if (entity != null) {
            detachEntity(entity);
        }
    }

    /**
     * Subscribes to specific entity events.
     */
    protected void attachEntity(Entity entity) {

    }

    /**
     * Unsubscribes from specific entity events.
     */
    protected void detachEntity(Entity entity) {

    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        if (this.entity != null) {
            detachEntity(this.entity);
        }

        this.entity = entity;

        if (entity != null) {
            attachEntity(entity);
        }
    }
}
