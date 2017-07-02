package ru.mikroacse.rolespell.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.controller.game.GameController;
import ru.mikroacse.rolespell.app.controller.shared.InputAdapter;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityRepository;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.controllers.MobController;
import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.app.model.game.entities.objects.Portal;
import ru.mikroacse.rolespell.app.model.game.entities.objects.PortalSpawn;
import ru.mikroacse.rolespell.app.model.game.items.ItemRepository;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;
import ru.mikroacse.rolespell.app.model.game.world.Map;
import ru.mikroacse.rolespell.app.model.game.world.MapRepository;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.model.game.world.WorldListener;
import ru.mikroacse.rolespell.app.model.game.world.config.MapConfig;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;
import ru.mikroacse.rolespell.media.Bundle;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import static ru.mikroacse.rolespell.RoleSpell.assets;
import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameScreen extends Screen {
    private GameRenderer renderer;
    private GameController controller;

    private GameModel model;

    public GameScreen() {
        super(false);

        model = new GameModel();

        renderer = new GameRenderer(model);

        controller = new GameController(renderer, model);

        // TODO: Move it somewhere else

        // item defaults
        ConfigurationNode items = bundle(Bundle.GAME).getConfig("items");

        for (String key : items.getMap().keySet()) {
            ItemConfig itemConfig = new ItemConfig(items.get(key), items.get(key + ".parent", null));

            ItemRepository.instance().add(key, itemConfig);
        }

        // map defaults
        ConfigurationNode mapDefaults = bundle(Bundle.GAME).getConfig("maps/map-defaults");

        MapConfig mapConfig = new MapConfig(mapDefaults);

        MapRepository.instance().add("map-defaults", mapConfig);

        parseMapConfig(mapConfig);

        setWorld("eclipse-chambers", null);
    }

    @Override
    public void restore() {
        RoleSpell.hideMouse();

        Gdx.input.setInputProcessor(InputAdapter.getInstance());
    }

    // TODO: Bad.
    public void parseMapConfig(MapConfig mapConfig) {
        ConfigurationNode items = mapConfig.getItems();

        if (items != null) {
            for (String key : items.getMap().keySet()) {
                ItemConfig itemConfig = new ItemConfig(items.get(key), items.get(key + ".parent", null));

                ItemRepository.instance().add(key, itemConfig);
            }
        }

        Set<Entry<String, Object>> entries = new HashSet<>();

        ConfigurationNode entities = mapConfig.getEntities();
        ConfigurationNode portals = mapConfig.getPortals();

        if (entities != null) {
            entries.addAll(entities.getMap().entrySet());
        }

        if (portals != null) {
            entries.addAll(portals.getMap().entrySet());
        }

        for (Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            java.util.Map<String, Object> value = (java.util.Map) entry.getValue();

            EntityConfig entityConfig = new EntityConfig(value, (String) value.get("parent"));

            EntityRepository.instance().add(key, entityConfig);
        }
    }

    // TODO: ???!!!
    public void setWorld(String id, String portalId) {
        TiledMap tiledMap = bundle(Bundle.GAME).getMap(id + "/map");

        MapConfig mapConfig;

        if (MapRepository.instance().contains(id)) {
            mapConfig = MapRepository.instance().get(id);
        } else {
            ConfigurationNode node = bundle(Bundle.GAME).getConfig("maps/" + id);
            mapConfig = new MapConfig(node, node.get("parent", null));
            MapRepository.instance().add(id, mapConfig);
        }

        parseMapConfig(mapConfig);

        Map map = new Map(tiledMap);

        map.setId(id);
        map.setConfig(mapConfig);

        World world = new World(map);

        model.setWorld(world);

        if (portalId != null) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.PORTAL_SPAWN) {
                    PortalSpawn portalSpawn = (PortalSpawn) entity;

                    if (portalSpawn.getId().equals(portalId)) {
                        model.getControllable().setPosition(portalSpawn.getPosition());
                        break;
                    }
                }
            }
        }

        world.addListener(new WorldListener() {
            @Override
            public void mobDied(World world, MobController controller) {
                System.out.println("died: " + controller.getEntity().getType());
            }
        });

        renderer.refreshWorld();
    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        renderer.act(delta);
        renderer.draw();
    }

    @Override
    public void resize(int width, int height) {
        assets().updateScale(width, height);

        renderer.resize(width, height);
    }

    public GameModel getModel() {
        return model;
    }
}