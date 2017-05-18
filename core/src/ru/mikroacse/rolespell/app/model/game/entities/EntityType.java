package ru.mikroacse.rolespell.app.model.game.entities;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public enum EntityType {
    // player
    PLAYER,

    // npc
    NPC,

    // monsters
    MONSTER,
    OGREMAGI,

    // objects
    DROPPED_ITEM,
    PORTAL;

    public static final EnumSet<EntityType> ALL = EnumSet.allOf(EntityType.class);
    public static final EnumSet<EntityType> NONE = EnumSet.noneOf(EntityType.class);
}
