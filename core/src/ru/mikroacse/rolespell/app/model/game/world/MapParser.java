package ru.mikroacse.rolespell.app.model.game.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.engine.config.Configuration;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.*;
import ru.mikroacse.rolespell.app.model.game.entities.monsters.Monster;
import ru.mikroacse.rolespell.app.model.game.entities.monsters.Ogremagi;
import ru.mikroacse.rolespell.app.model.game.entities.objects.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.entities.objects.Portal;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.weapons.WoodenSword;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 11-May-17.
 */
public class MapParser {
    // TODO: make not static?

    public static Array<Entity> parseEntities(World world, Map map) {
        Array<Entity> entities = new Array<>();

        for (MapObject mapObject : map.getLayer(Map.Layer.SPAWNERS).getObjects()) {
            RectangleMapObject object = (RectangleMapObject) mapObject;

            Entity entity = parseEntity(object, world, map);

            if (entity == null) {
                continue;
            }

            entities.add(entity);
        }

        return entities;
    }

    public static Array<Portal> parsePortals(World world, Map map) {
        Array<Portal> portals = new Array<>();

        for (MapObject mapObject : map.getLayer(Map.Layer.PORTALS).getObjects()) {
            RectangleMapObject object = (RectangleMapObject) mapObject;

            Portal portal = parsePortal(object, world, map);

            if (portal == null) {
                continue;
            }

            portals.add(portal);
        }

        return portals;
    }

    private static Portal parsePortal(RectangleMapObject portalMapObject, World world, Map map) {
        MapProperties properties = portalMapObject.getProperties();

        if(!properties.containsKey("id")) {
            return null;
        }

        String id = properties.get("id", String.class);
        boolean spawn = properties.get("spawn", false, Boolean.class);

        Configuration portalConfig = new Configuration(map.getConfig().getNode("portals." + id));

        String destination = portalConfig.getString("destination");
        String portalId = portalConfig.getString("portal-id");
        String name = spawn? null : portalConfig.getString("name");

        int x = (int) portalMapObject.getRectangle().x / map.getTileWidth();
        int y = (int) portalMapObject.getRectangle().y / map.getTileHeight();

        return new Portal(world, destination, portalId, spawn, name, x, y);
    }

    private static Entity parseEntity(RectangleMapObject entityMapObject, World world, Map map) {
        MapProperties properties = entityMapObject.getProperties();

        if(!properties.containsKey("id")) {
            return null;
        }

        String id = properties.get("id", String.class);

        Configuration config = new Configuration(map.getConfig().getNode("spawners." + id));

        EntityType type = EntityType.valueOf(config.getString("entity-type"));

        String name = config.getString("name", null);

        int x = (int) entityMapObject.getRectangle().x / map.getTileWidth();
        int y = (int) entityMapObject.getRectangle().y / map.getTileHeight();

        Entity entity = null;

        switch (type) {
            case NPC:
                entity = new Npc(world, name, x, y);
                break;
            case MONSTER:
                entity = new Monster(world, name, x, y);
                break;
            case OGREMAGI:
                entity = new Ogremagi(world, name, x, y);
                break;
            case PLAYER:
                entity = new Player(world, x, y);
                break;
            case DROPPED_ITEM:
                Item item = parseItem(config.extractNode("item"));

                entity = new DroppedItem<Item>(world, item, x, y);
                break;
        }

        if(entity != null) {
            entity.setConfig(config);
        }

        return entity;
    }

    private static Item parseItem(Configuration itemConfig) {
        String name = itemConfig.getString("name");

        // TODO: item parsing

        return new WoodenSword();
    }
}
