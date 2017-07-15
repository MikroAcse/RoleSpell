package ru.mikroacse.rolespell.app.model.game.world;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.listeners.AbstractListener;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityRepository;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.controllers.MobController;
import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.app.model.game.entities.objects.PortalSpawn;
import ru.mikroacse.rolespell.app.model.game.items.ItemRepository;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;
import ru.mikroacse.rolespell.app.model.game.world.config.MapConfig;
import ru.mikroacse.rolespell.media.Bundle;

import java.util.*;

import static ru.mikroacse.rolespell.RoleSpell.bundle;

public class WorldManager {
    public static final WorldManager instance = new WorldManager();

    private Listener listeners;

    private Array<Entity> sharedEntities;
    private HashMap<String, World> worlds;

    private String currentId;

    private WorldManager() {
        listeners = ListenerSupportFactory.create(Listener.class);

        sharedEntities = new Array<>();
        worlds = new HashMap<>();

        currentId = null;
    }

    /**
     * @param id New world's id.
     * @return Previous world's id.
     */
    public String setWorld(String id) {
        String prevId = currentId;

        if(prevId != null) {
            detachWorld(prevId);
        }

        currentId = id;

        if(currentId != null) {
            attachWorld(currentId);
        }

        listeners.worldChanged(prevId, currentId);

        return prevId;
    }

    // TODO: Bad?
    public static void parseMapConfig(MapConfig mapConfig) {
        Set<String> items = mapConfig.getItems();

        if (items != null) {
            for (String key : items) {
                ItemConfig itemConfig = new ItemConfig((Map) mapConfig.get("items." + key), mapConfig.get("items." + key + ".parent", null));

                ItemRepository.instance.add(key, itemConfig);
            }
        }

        Set<String> entities = mapConfig.getEntities();
        Set<String> portals = mapConfig.getPortals();

        for (String entity : entities) {
            Map<String, Object> value = mapConfig.get("entities." + entity);

            EntityConfig entityConfig = new EntityConfig(value, (String) value.get("parent"));

            EntityRepository.instance.add(entity, entityConfig);
        }

        for (String portal : portals) {
            Map<String, Object> value = mapConfig.get("portals." + portal);

            EntityConfig entityConfig = new EntityConfig(value, (String) value.get("parent"));

            EntityRepository.instance.add(portal, entityConfig);
        }
    }

    public boolean hasSharedEntity(String id) {
        for (Entity sharedEntity : sharedEntities) {
            if(sharedEntity.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public Entity takeSharedEntity(String id) {
        for (Entity sharedEntity : sharedEntities) {
            if(sharedEntity.getId().equals(id)) {
                sharedEntities.removeValue(sharedEntity, true);

                return sharedEntity;
            }
        }

        return null;
    }

    public void addListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).addListener(listener);
    }

    public void removeListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<Listener>) listeners).clearListeners();
    }

    private void attachWorld(String id) {
        MapConfig mapConfig;

        if (MapRepository.instance.contains(id)) {
            mapConfig = MapRepository.instance.get(id);
        } else {
            ConfigurationNode node = bundle(Bundle.GAME).getConfig("maps/" + id);
            mapConfig = new MapConfig(node, node.get("parent", null));
            MapRepository.instance.add(id, mapConfig);
        }

        parseMapConfig(mapConfig);

        World world = getWorld(id);

        if(world != null) {
            System.out.println("Already existing world");

            MapConfig config = world.getMap().getConfig();
            Set<String> entities = config.getEntities();

            for (String entityId : entities) {
                EntityConfig entityConfig = EntityRepository.instance.get(entityId);

                System.out.println("Entity: " + entityId + " : " + entityConfig.isShared(false));

                if(entityConfig.isShared(false)) {
                    System.out.println("Taking shared entity: " + hasSharedEntity(entityId));

                    world.addEntity(takeSharedEntity(entityId));
                }
            }
        } else {
            TiledMap tiledMap = bundle(Bundle.GAME).getMap(id + "/map");

            WorldMap map = new WorldMap(tiledMap);

            map.setId(id);
            map.setConfig(mapConfig);

            worlds.put(id, new World(map));
        }
    }

    private void detachWorld(String id) {
        World world = getWorld(id);
        Array<Entity> entities = world.getEntities();

        for (int i = entities.size - 1; i >= 0; i--) {
            Entity entity = entities.get(i);

            if(entity.isShared()) {
                entity.remove();

                sharedEntities.add(entity);

                System.out.println("Added entity " + entity + " to cache");
            }
        }
    }

    public World getWorld(String id) {
        return worlds.get(id);
    }

    public String getCurrentWorldId() {
        return currentId;
    }

    public World getCurrentWorld() {
        return worlds.get(currentId);
    }

    public interface Listener extends AbstractListener {
        void worldChanged(String prevId, String currentId);
    }
}
