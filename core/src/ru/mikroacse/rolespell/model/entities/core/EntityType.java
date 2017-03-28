package ru.mikroacse.rolespell.model.entities.core;

import ru.mikroacse.rolespell.model.entities.Npc;
import ru.mikroacse.rolespell.model.entities.Player;


/**
 * Created by MikroAcse on 24.03.2017.
 */
// TODO: maybe, factory?
public enum EntityType {
    NPC,
    BAT,
    SLIME,
    PLAYER;

    public static Entity create(EntityType type) {
        Entity entity = null;

        switch (type) {
            case NPC:
                entity = new Npc();
                break;
            case PLAYER:
                entity = new Player();
                break;
        }

        return entity;
    }
}
