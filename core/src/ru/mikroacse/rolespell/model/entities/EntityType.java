package ru.mikroacse.rolespell.model.entities;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;


/**
 * Created by MikroAcse on 24.03.2017.
 */
public enum EntityType {
    NPC,
    BAT,
    SLIME,
    PLAYER;

    public static Entity create(World world, EntityType type) {
        Entity entity = null;

        switch (type) {
            case NPC:
                entity = new Npc(world);
                break;
            case PLAYER:
                entity = new Player(world);
                break;
        }

        return entity;
    }
}
