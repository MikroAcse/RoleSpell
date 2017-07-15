package ru.mikroacse.rolespell.parsers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityRepository;
import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.app.model.game.world.WorldManager;
import ru.mikroacse.rolespell.app.model.game.world.WorldMap;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 11-May-17.
 */
public class MapParser {
    // TODO: make not static?

    public static Array<Entity> getEntities(World world, WorldMap map, WorldMap.Layer layer) {
        Array<Entity> entities = new Array<>();

        for (MapObject mapObject : map.getLayer(layer).getObjects()) {
            RectangleMapObject object = (RectangleMapObject) mapObject;

            int x = (int) object.getRectangle().x / map.getTileWidth();
            int y = (int) object.getRectangle().y / map.getTileHeight();

            Entity entity = createEntity(object, world, x, y);

            if (entity != null) {
                entities.add(entity);
            }
        }

        return entities;
    }

    private static Entity createEntity(RectangleMapObject entityMapObject, World world, int x, int y) {
        MapProperties properties = entityMapObject.getProperties();

        String id = properties.get("id", entityMapObject.getName(), String.class);

        if (id == null) {
            return null;
        }

        EntityConfig config = EntityRepository.instance.get(id);
        Entity entity = null;

        if(config.isShared(false)) {
            entity = WorldManager.instance.takeSharedEntity(id);

            if(entity != null) {
                entity.setWorld(world);
                entity.setPosition(x, y);

                return entity;
            }
        }

        entity = EntityParser.create(config, world, x, y);

        if (entity != null) {
            entity.setId(id);
        }

        return entity;
    }
}
