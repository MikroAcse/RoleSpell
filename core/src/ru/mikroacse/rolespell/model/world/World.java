package ru.mikroacse.rolespell.model.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import ru.mikroacse.rolespell.model.entities.Player;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.EntityType;
import ru.mikroacse.rolespell.model.pathfinding.GraphBuilder;
import ru.mikroacse.rolespell.model.pathfinding.PathFinder;
import ru.mikroacse.rolespell.model.pathfinding.graph.Graph;
import ru.mikroacse.rolespell.model.pathfinding.heuristic.ManhattanDistance;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public class World {
    private TiledMap map;
    private List<Entity> entities;

    private Player player;

    public World(TiledMap map) {
        this.map = map;

        initialize();
    }

    public World(String mapFileName) {
        this(new TmxMapLoader().load(mapFileName));
    }

    private void initialize() {
        entities = new ArrayList<>();

        // TODO: :/
        for (MapObject mapObject : getMapLayer(Layer.SPAWNERS).getObjects()) {
            RectangleMapObject object = (RectangleMapObject) mapObject;
            Entity entity = EntityType.createEntity(EntityType.valueOf(object.getName()));

            if(entity == null) {
                System.out.println("Null entity: " + object.getName());
                continue;
            }

            if(entity.getType() == EntityType.PLAYER) {
                player = (Player) entity;
            }

            // TODO: is this ok? It's for AI
            entity.setWorld(this);

            int realX = (int) object.getRectangle().x;
            int realY = (int) object.getRectangle().y;

            entity.setSupposedPosition(realX / getMapTileWidth(), realY / getMapTileHeight());

            System.out.println(entity);

            entities.add(entity);
        }
    }

    public LinkedList<Point> getPath(Point from, Point to, int radius) {
        int minX = Math.min(from.x, to.x);
        int minY = Math.min(from.y, to.y);

        int maxX = Math.max(from.x, to.x);
        int maxY = Math.max(from.y, to.y);

        int width = maxX - minX + 1;
        int height = maxY - minY + 1;

        Rectangle rect = new Rectangle(
                Math.max(minX - radius, 0),
                Math.max(minY - radius, 0),
                width + radius * 2,
                height + radius * 2);


        PathFinder pathFinder = new PathFinder(new ManhattanDistance(getWeight(World.Meta.PATH)));

        Graph graph = GraphBuilder.fromWorld(this, rect);

        LinkedList<Point> path = pathFinder.convertPathToCells(
                pathFinder.getPath(
                        graph,
                        (int) (rect.height * (from.x - rect.x) + (from.y - rect.y)),
                        (int) (rect.height * (to.x - rect.x) + (to.y - rect.y))
                ));

        return path;
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

    public ArrayList<Point> getPassableCells(int x, int y, int minRadius, int maxRadius, boolean inverse) {
        return getCells(
                Layer.META,
                (cell, cellX, cellY) -> getMeta(cellX, cellY) != Meta.SOLID,
                x,
                y,
                minRadius,
                maxRadius,
                inverse);
    }

    /**
     * Rhombus search around given position (checks the position itself too).
     */
    public ArrayList<Point> getCells(Layer layer, CellChecker checker, int x, int y, int minRadius, int maxRadius, boolean inversed) {
        ArrayList<Point> result = new ArrayList<>();
        int radius = inversed? maxRadius : minRadius;

        while (inversed? radius >= minRadius : radius <= maxRadius) {
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    if(Math.abs(i) + Math.abs(j) != radius) {
                        continue;
                    }

                    int cellX = x + i;
                    int cellY = y + j;

                    if (!isValidCoordinates(cellX, cellY))
                        continue;

                    TiledMapTileLayer.Cell cell = getCell(layer, cellX, cellY);

                    if (checker.check(cell, cellX, cellY))
                        result.add(new Point(cellX, cellY));
                    }
            }

            if(inversed) {
                radius--;
            } else {
                radius++;
            }
        }

        return result;
    }

    public boolean isTraversable(int x, int y) {
        return getMeta(x, y) != Meta.SOLID;
    }

    public boolean isValidCoordinates(int x, int y) {
        return x >= 0 && y >= 0 && x < getMapWidth() && y < getMapHeight();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public boolean addEntity(Entity entity) {
        return entities.add(entity);
    }

    public boolean removeEntity(Entity entity) {
        return entities.remove(entity);
    }

    public Player getPlayer() {
        return player;
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

    public int getMapTileWidth() {
        return (int) getMapTileLayer(World.Layer.BACKGROUND).getTileWidth();
    }

    public int getMapTileHeight() {
        return (int) getMapTileLayer(World.Layer.BACKGROUND).getTileHeight();
    }

    public int getMapRealWidth() {
        return getMapWidth() * getMapTileWidth();
    }

    public int getMapRealHeight() {
        return getMapHeight() * getMapTileHeight();
    }

    @Override
    public String toString() {
        return "World{" +
                "map=" + map +
                '}';
    }

    public enum Layer {
        SPAWNERS, // object layer with entities/player spawn locations
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

    public interface CellChecker {
        boolean check(TiledMapTileLayer.Cell cell, int x, int y);
    }
}
