package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.rolespell.app.model.game.entities.mobs.Npc;
import ru.mikroacse.rolespell.app.model.game.entities.mobs.Player;
import ru.mikroacse.rolespell.app.model.game.entities.mobs.monsters.Devil;
import ru.mikroacse.rolespell.app.model.game.entities.mobs.monsters.Ogremagi;
import ru.mikroacse.rolespell.app.model.game.entities.objects.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.entities.objects.Portal;
import ru.mikroacse.rolespell.app.model.game.entities.objects.PortalSpawn;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public enum EntityType {
    ENTITY,

    // player
    PLAYER,

    // npc
    NPC,

    // monsters
    DEVIL, OGREMAGI,

    // objects
    DROPPED_ITEM,
    PORTAL, PORTAL_SPAWN;

    public static final EnumSet<EntityType> ALL = EnumSet.allOf(EntityType.class);
    public static final EnumSet<EntityType> NONE = EnumSet.noneOf(EntityType.class);

    public static Entity create(EntityType type, World world, int x, int y) throws IllegalArgumentException {
        switch (type) {
            case ENTITY:
                throw new IllegalArgumentException("Cannot create an item without a type");
            case NPC:
                return new Npc(world, x, y);
            case DEVIL:
                return new Devil(world, x, y);
            case OGREMAGI:
                return new Ogremagi(world, x, y);
            case PLAYER:
                return new Player(world, x, y);
            case DROPPED_ITEM:
                return new DroppedItem(world, x, y);
            case PORTAL:
                return new Portal(world, x, y);
            case PORTAL_SPAWN:
                return new PortalSpawn(world, x, y);
            default:
                throw new IllegalArgumentException("Cannot automatically create an entity of this type");
        }
    }
}
