package ru.mikroacse.rolespell.parsers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityRepository;
import ru.mikroacse.rolespell.app.model.game.world.Map;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 11-May-17.
 */
public class MapParser {
    // TODO: make not static?

    public static Array<Entity> getEntities(World world, Map map, Map.Layer layer) {
        Array<Entity> entities = new Array<>();

        for (MapObject mapObject : map.getLayer(layer).getObjects()) {
            RectangleMapObject object = (RectangleMapObject) mapObject;

            int x = (int) object.getRectangle().x / map.getTileWidth();
            int y = (int) object.getRectangle().y / map.getTileHeight();

            Entity entity = createEntity(object, world, x, y);

            System.out.println("GG " + entity);
            if (entity != null) {
                entities.add(entity);
            }
        }

        return entities;
    }

    private static Entity createEntity(RectangleMapObject entityMapObject, World world, int x, int y) {
        MapProperties properties = entityMapObject.getProperties();

        String id = properties.get("id", entityMapObject.getName(), String.class);

        if(id == null) {
            return null;
        }

        System.out.println("Creating " + id);
        Entity entity = EntityParser.create(EntityRepository.instance().get(id), world, x, y);

        System.out.println("BB entity");

        if(entity != null) {
            entity.setId(id);
        }

        return entity;
    }
}
