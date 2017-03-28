package ru.mikroacse.rolespell.model.entities.core;

import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public abstract class Entity {
    private EntityType type;

    public Entity(EntityType type) {
        this.type = type;

        initialize();
    }

    public Entity() {
        this(null);
    }

    private void initialize() {

    }

    public abstract void update(float delta, World world);

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }
}
