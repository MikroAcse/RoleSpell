package ru.mikroacse.rolespell.model.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import ru.mikroacse.rolespell.model.entities.Player;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.EntityType;
import ru.mikroacse.rolespell.model.entities.core.MovableEntity;
import ru.mikroacse.rolespell.model.pathfinding.GraphBuilder;
import ru.mikroacse.rolespell.model.pathfinding.PathFinder;
import ru.mikroacse.rolespell.model.pathfinding.graph.Graph;
import ru.mikroacse.rolespell.model.pathfinding.heuristic.ManhattanDistance;
import ru.mikroacse.rolespell.model.world.cells.CellChecker;
import ru.mikroacse.rolespell.model.world.cells.PassableCellChecker;
import ru.mikroacse.util.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MikroAcse on 23.03.2017.
 */
// TODO: refactor
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

        // TODO: make this less horrible and move to separate util class
        for (MapObject mapObject : getLayer(Layer.SPAWNERS).getObjects()) {
            RectangleMapObject object = (RectangleMapObject) mapObject;
            Entity entity = EntityType.create(EntityType.valueOf(object.getName()));

            if (entity == null) {
                System.out.println("Couldn't create entity: " + object.getName());
                continue;
            }

            if (entity.getType() == EntityType.PLAYER) {
                player = (Player) entity;
            }

            int realX = (int) object.getRectangle().x;
            int realY = (int) object.getRectangle().y;

            if (entity instanceof MovableEntity) {
                MovementComponent movementComponent = ((MovableEntity) entity).getMovement();

                movementComponent
                        .setBoth(realX / getTileWidth(), realY / getTileHeight());
            }

            entities.add(entity);
        }
    }

    public LinkedList<Position> getPath(Position from, Position to, int radius) {
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

        // TODO: use actual map instead of generating Graph every time
        Graph graph = GraphBuilder.fromWorld(this, rect);

        return pathFinder.convertPathToCells(
                pathFinder.getPath(
                        graph,
                        (int) (rect.height * (from.x - rect.x) + (from.y - rect.y)),
                        (int) (rect.height * (to.x - rect.x) + (to.y - rect.y))
                ));
    }

    public Position getCellPosition(float x, float y) {
        return new Position(
                (int) (x / getTileWidth()),
                (int) (y / getTileHeight()));
    }

    public double getWeight(int x, int y) {
        return getWeight(getMeta(x, y));
    }

    public double getWeight(Meta meta) {
        switch (meta) {
            case PATH:
                return 0.5;
            default:
                return 1;
        }
    }

    public Meta getMeta(int x, int y) {
        TiledMapTileLayer.Cell cell = getCell(Layer.META, x, y);

        if (cell == null) {
            return Meta.EMPTY;
        }

        return Meta.valueOf((String) cell.getTile().getProperties().get("type"));
    }

    public Meta getMeta(Position position) {
        return getMeta(position.x, position.y);
    }

    public TiledMapTileLayer.Cell getCell(Layer layer, int x, int y) {
        return getTileLayer(layer).getCell(x, y);
    }

    // TODO: new PassableCellChecker instance every time (fix?)

    public boolean isPassable(int x, int y, boolean checkEntities) {
        return new PassableCellChecker(checkEntities).check(this, x, y);
    }

    public boolean isPassable(Position position, boolean checkEntities) {
        return isPassable(position.x, position.y, checkEntities);
    }

    public ArrayList<Position> getPassableCells(int x, int y, boolean checkEntities, int minRadius, int maxRadius, boolean inverse) {
        return getCells(
                Layer.META,
                new PassableCellChecker(checkEntities),
                x,
                y,
                minRadius,
                maxRadius,
                inverse);
    }

    /**
     * Rhombus search around given position (checks the position itself too).
     */
    // TODO: MAKE THIS BEAUTIFUL
    public ArrayList<Position> getCells(Layer layer, CellChecker checker, int x, int y, int minRadius, int maxRadius, boolean inversed) {
        ArrayList<Position> result = new ArrayList<>();
        int radius = inversed ? maxRadius : minRadius;

        while (inversed ? radius >= minRadius : radius <= maxRadius) {
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    if (Math.abs(i) + Math.abs(j) != radius) {
                        continue;
                    }

                    int cellX = x + i;
                    int cellY = y + j;

                    if (!isValidPosition(cellX, cellY))
                        continue;

                    TiledMapTileLayer.Cell cell = getCell(layer, cellX, cellY);

                    if (checker.check(this, cellX, cellY))
                        result.add(new Position(cellX, cellY));
                }
            }

            if (inversed) {
                radius--;
            } else {
                radius++;
            }
        }

        return result;
    }

    public ArrayList<Entity> getEntitiesAt(int x, int y) {
        ArrayList<Entity> result = new ArrayList<>();

        for (Entity entity : entities) {
            if(entity instanceof MovableEntity) {
                MovementComponent movement = ((MovableEntity) entity).getMovement();

                if(movement.getPosition().equals(x, y)) {
                    result.add(entity);
                }
            }
        }

        return result;
    }

    public ArrayList<Entity> getEntitiesAt(Position position) {
        return getEntitiesAt(position.x, position.y);
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    public boolean isValidPosition(Position position) {
        return isValidPosition(position.x, position.y);
    }

    public void validatePosition(Position position) {
        if (!isValidPosition(position)) {
            position.limit(0, 0, getWidth() - 1, getHeight() - 1);
        }
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

    public MapLayer getLayer(Layer layer) {
        return map.getLayers().get(layer.name());
    }

    public TiledMapTileLayer getTileLayer(World.Layer layer) {
        return (TiledMapTileLayer) getLayer(layer);
    }

    public int getWidth() {
        return getTileLayer(World.Layer.BACKGROUND).getWidth();
    }

    public int getHeight() {
        return getTileLayer(World.Layer.BACKGROUND).getHeight();
    }

    public int getTileWidth() {
        return (int) getTileLayer(World.Layer.BACKGROUND).getTileWidth();
    }

    public int getTileHeight() {
        return (int) getTileLayer(World.Layer.BACKGROUND).getTileHeight();
    }

    public int getRealWidth() {
        return getWidth() * getTileWidth();
    }

    public int getRealHeight() {
        return getHeight() * getTileHeight();
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
}
