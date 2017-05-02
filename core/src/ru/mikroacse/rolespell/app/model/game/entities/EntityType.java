package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public enum EntityType {
    NPC,
    BAT,
    SLIME,
    PLAYER,
    DROPPED_ITEM;
    
    public static Entity create(World world, EntityType type) {
        switch (type) {
            case NPC:
                return new Npc(world);
            case PLAYER:
                return new Player(world);
        }
        
        return null;
    }
}
