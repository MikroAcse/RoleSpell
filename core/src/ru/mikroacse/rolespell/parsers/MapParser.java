package ru.mikroacse.rolespell.parsers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.app.model.game.entities.mobs.Npc;
import ru.mikroacse.rolespell.app.model.game.entities.mobs.Player;
import ru.mikroacse.rolespell.app.model.game.entities.mobs.monsters.Monster;
import ru.mikroacse.rolespell.app.model.game.entities.mobs.monsters.Ogremagi;
import ru.mikroacse.rolespell.app.model.game.entities.objects.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.entities.objects.Portal;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.ItemRepository;
import ru.mikroacse.rolespell.app.model.game.world.Map;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 11-May-17.
 */
public class MapParser {
    // TODO: make not static?

    public static Array<Entity> getEntities(World world, Map map) {
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

    public static Array<Portal> getPortals(World world, Map map) {
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

        if (!properties.containsKey("id")) {
            return null;
        }

        String id = properties.get("id", String.class);
        boolean spawn = properties.get("spawn", false, Boolean.class);

        ConfigurationNode portalConfig = map.getConfig().extractNode("portals." + id);

        String destination = portalConfig.getString("destination");
        String portalId = portalConfig.getString("portal-id");
        String name = spawn ? null : portalConfig.getString("name");

        int x = (int) portalMapObject.getRectangle().x / map.getTileWidth();
        int y = (int) portalMapObject.getRectangle().y / map.getTileHeight();

        return new Portal(world, destination, portalId, spawn, name, x, y);
    }

    private static Entity parseEntity(RectangleMapObject entityMapObject, World world, Map map) {
        MapProperties properties = entityMapObject.getProperties();

        if (!properties.containsKey("id")) {
            return null;
        }

        String id = properties.get("id", String.class);

        ConfigurationNode config = map.getConfig().extractNode("spawners." + id);



        EntityConfig entityConfig = new EntityConfig(map.getConfig().extractNode("spawners." + id), );

        System.out.println(config.getMap());

        EntityType type = EntityType.valueOf(config.get("entity-type"));

        String name = config.get("name", null);

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
                Item item = ItemParser.parse(config.get("item"), ItemRepository.getInstance());

                entity = new DroppedItem<Item>(world, item, x, y);
                break;
        }

        if (entity != null) {
            if (config.has("inventory") && entity.hasComponent(InventoryComponent.class)) {
                Inventory inventory = entity.getComponent(InventoryComponent.class).getInventory();

                InventoryParser.parse(config.extractNodeList("inventory"), inventory);
            }

            entity.setId(id);
            entity.setConfig(config);
        }

        return entity;
    }
}
