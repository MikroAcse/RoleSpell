package ru.mikroacse.rolespell.model.entities.core;

import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public abstract class Entity implements DrawableEntity, MovableEntity {
    private EntityType type;

    public Entity(EntityType type) {
        this.type = type;
    }

    public Entity() {
        this(null);
    }

    public abstract void update(float delta, World world);

    public abstract void dispose();

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }
}
