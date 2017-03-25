package ru.mikroacse.rolespell.model.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import ru.mikroacse.rolespell.model.entities.Entity;
import ru.mikroacse.rolespell.model.entities.EntityType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public class World {
    private TiledMap map;
    private List<Entity> entities;

    public World(TiledMap map) {
        this.map = map;

        initialize();
    }

    public World(String mapFileName) {
        this(new TmxMapLoader().load(mapFileName));
    }

    private void initialize() {
        entities = new ArrayList<>();

        Iterator<MapObject> it = getMapLayer(Layer.SPAWNERS).getObjects().iterator();

        // TODO: :/
        while (it.hasNext()) {
            RectangleMapObject object = (RectangleMapObject) it.next();
            Entity entity = EntityType.createEntity(EntityType.valueOf(object.getName()));
            entity.type = EntityType.valueOf(object.getName());

            entity.setPosition((int) object.getRectangle().x, (int) object.getRectangle().y);

            entities.add(entity);
        }
    }

    public Point getCellPosition(float x, float y) {
        return new Point(
                (int) (x / getMapTileWidth()),
                (int) (y / getMapTileHeight()));
    }

    public float getWeight(int x, int y) {
        return getWeight(getMeta(x, y));
    }

    public float getWeight(Meta meta) {
        switch (meta) {
            case PATH:
                return 0.5f;
            default:
                return 1f;
        }
    }

    public Meta getMeta(int x, int y) {
        TiledMapTileLayer.Cell cell = getCell(Layer.META, x, y);

        if (cell == null) {
            return Meta.EMPTY;
        }

        return Meta.valueOf((String) cell.getTile().getProperties().get("type"));
    }

    public TiledMapTileLayer.Cell getCell(Layer layer, int x, int y) {
        return getMapTileLayer(layer).getCell(x, y);
    }

    public Point getNearestEmptyCell(Layer layer, int x, int y, int maxRadius) {
        int radius = 0;

        while (radius <= maxRadius) {
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    if (i == 0 || i == radius || j == 0 || j == radius) {
                        int cellX = x + i;
                        int cellY = y + j;

                        if (!isValidCoordinates(cellX, cellY))
                            continue;

                        TiledMapTileLayer.Cell cell = getCell(layer, cellX, cellY);

                        if (cell == null)
                            return new Point(cellX, cellY);
                    }
                }
            }

            radius++;
        }

        return null;
    }

    public boolean isTraversable(int x, int y) {
        return getMeta(x, y) != Meta.SOLID;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public boolean isValidCoordinates(int x, int y) {
        return x >= 0 && y >= 0 && x < getMapWidth() && y < getMapHeight();
    }

    public TiledMap getMap() {
        return map;
    }

    public MapLayer getMapLayer(Layer layer) {
        return map.getLayers().get(layer.name());
    }

    public TiledMapTileLayer getMapTileLayer(World.Layer layer) {
        return (TiledMapTileLayer) getMapLayer(layer);
    }

    public int getMapWidth() {
        return getMapTileLayer(World.Layer.BACKGROUND).getWidth();
    }

    public int getMapHeight() {
        return getMapTileLayer(World.Layer.BACKGROUND).getHeight();
    }

    public float getMapTileWidth() {
        return getMapTileLayer(World.Layer.BACKGROUND).getTileWidth();
    }

    public float getMapTileHeight() {
        return getMapTileLayer(World.Layer.BACKGROUND).getTileHeight();
    }

    public float getMapRealWidth() {
        return getMapWidth() * getMapTileWidth();
    }

    public float getMapRealHeight() {
        return getMapHeight() * getMapTileHeight();
    }

    public enum Layer {
        SPAWNERS, // object layer with entities/entities spawn locations
        META, // meta layer for collisions markup
        TOP,
        ADDITIONAL,
        OBJECTS,
        BOTTOM,
        LAYOUT,
        BACKGROUND
    }

    public enum Meta {
        SOLID,
        WATER,
        PATH,
        EMPTY
    }
}
