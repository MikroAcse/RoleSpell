package ru.mikroacse.rolespell.model.entities.core;

import ru.mikroacse.rolespell.model.entities.Player;


/**
 * Created by MikroAcse on 24.03.2017.
 */
public enum EntityType {
    NPC(ru.mikroacse.rolespell.model.entities.NPC.class),
    BAT(Entity.class),
    SLIME(Entity.class),
    PLAYER(Player.class);

    private Class<? extends Entity> entityClass;

    EntityType(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
    }

    // TODO: looks bad
    public static Entity createEntity(EntityType type) {
        try {
            Entity entity = type.entityClass.newInstance();
            entity.setType(type);
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
