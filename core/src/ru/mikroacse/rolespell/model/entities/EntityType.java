package ru.mikroacse.rolespell.model.entities;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public enum EntityType {
    NPC(Entity.class),
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
            return type.entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
