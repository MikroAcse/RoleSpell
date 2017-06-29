package ru.mikroacse.rolespell.app.model.game.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.world.config.MapConfig;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by MikroAcse on 09-May-17.
 */
public class Map {
    private String id;

    private String name;

    private TiledMap map;
    private MapConfig config;

    public Map(TiledMap map) {
        this.map = map;
    }

    public void setConfig(MapConfig config) {
        this.config = config;

        this.name = config.getName(name);
    }

    public ConfigurationNode getConfig() {
        return config;
    }

    public IntVector2 getCellPosition(float x, float y) {
        return new IntVector2(
                (int) (x / getTileWidth()),
                (int) (y / getTileHeight()));
    }

    public double getWeight(int x, int y) {
        return getWeight(getMeta(x, y));
    }

    public double getWeight(Map.Meta meta) {
        return config.getWeight(meta, 0);
    }

    public boolean isPassable(int x, int y) {
        return isPassable(getMeta(x, y));
    }

    public boolean isPassable(Map.Meta meta) {
        return config.isPassable(meta, false);
    }

    public Map.Meta getMeta(int x, int y) {
        TiledMapTileLayer.Cell cell = getCell(Map.Layer.META, x, y);

        if (cell == null) {
            return Map.Meta.EMPTY;
        }

        return Map.Meta.valueOf((String) cell.getTile().getProperties().get("type"));
    }

    public Map.Meta getMeta(IntVector2 position) {
        return getMeta(position.x, position.y);
    }

    public TiledMapTileLayer.Cell getCell(Map.Layer layer, int x, int y) {
        return getTileLayer(layer).getCell(x, y);
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    public boolean isValidPosition(IntVector2 position) {
        return isValidPosition(position.x, position.y);
    }

    public void validatePosition(IntVector2 position) {
        if (!isValidPosition(position)) {
            position.limit(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    public int getLayerIndex(Layer layer) {
        return map.getLayers().getIndex(layer.name());
    }

    public MapLayer getLayer(Layer layer) {
        return map.getLayers().get(layer.name());
    }

    public TiledMapTileLayer getTileLayer(Layer layer) {
        return (TiledMapTileLayer) getLayer(layer);
    }

    public int getWidth() {
        return getTileLayer(Layer.BACKGROUND).getWidth();
    }

    public int getHeight() {
        return getTileLayer(Layer.BACKGROUND).getHeight();
    }

    public int getTileWidth() {
        return (int) getTileLayer(Layer.BACKGROUND).getTileWidth();
    }

    public int getTileHeight() {
        return (int) getTileLayer(Layer.BACKGROUND).getTileHeight();
    }

    public TiledMap getMap() {
        return map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Map{" +
                "id='" + id + '\'' +
                '}';
    }

    public enum Layer {
        ENTITIES, // object layer with entities/player spawn locations
        PORTALS, // object layer with portals spawn locations
        META, // objects layer for collisions/etc markup
        TOP,
        ROOFS,
        BUILDINGS_TOP,
        ADDITIONAL,
        OBJECTS,
        BOTTOM,
        BUILDINGS_DECOR,
        BUILDINGS,
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
