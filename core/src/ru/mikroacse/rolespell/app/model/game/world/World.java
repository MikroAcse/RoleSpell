package ru.mikroacse.rolespell.app.model.game.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.Player;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementListener;
import ru.mikroacse.rolespell.app.model.game.pathfinding.GraphBuilder;
import ru.mikroacse.rolespell.app.model.game.pathfinding.PathFinder;
import ru.mikroacse.rolespell.app.model.game.pathfinding.graph.Graph;
import ru.mikroacse.rolespell.app.model.game.pathfinding.heuristic.ManhattanDistance;
import ru.mikroacse.rolespell.app.model.game.world.cells.CellChecker;
import ru.mikroacse.rolespell.app.model.game.world.cells.PassableCellChecker;

/**
 * Created by MikroAcse on 23.03.2017.
 */
// TODO: refactor
public class World {
    // TODO: separate map parser
    private TiledMap map;
    private Array<Entity> entities;

    private Listener listeners;

    private MovementListener movementListener;

    private Player player;

    public World(TiledMap map) {
        this.map = map;
        listeners = ListenerSupportFactory.create(Listener.class);

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("META");

        movementListener = new MovementListener() {
            @Override
            public void positionChanged(MovementComponent movement, IntVector2 previous, IntVector2 current) {
                listeners.entityMoved(movement.getEntity(), previous, current);
            }
        };

        initialize();
    }

    private void initialize() {
        entities = new Array<>();

        // TODO: make this less horrible and tryRouteTo to separate class
        for (MapObject mapObject : getLayer(Layer.SPAWNERS).getObjects()) {
            RectangleMapObject object = (RectangleMapObject) mapObject;

            String entityType = object.getProperties().get("type").toString();
            Entity entity = EntityType.create(this, EntityType.valueOf(entityType));

            if (entity == null) {
                System.out.println("Couldn't create entity: " + entityType);
                continue;
            }

            if (entity.getType() == EntityType.PLAYER) {
                player = (Player) entity;
            }

            int realX = (int) object.getRectangle().x;
            int realY = (int) object.getRectangle().y;

            MovementComponent movementComponent = entity.getComponent(MovementComponent.class);

            movementComponent.setBoth(new IntVector2(realX / getTileWidth(), realY / getTileHeight()));

            movementComponent.addListener(movementListener);
            entities.add(entity);
        }

        // TODO: remove
        for (Entity entity : entities) {
            if (entity.hasComponent(AttackAi.class)) {
                entity.getComponent(AttackAi.class)
                        .addTarget(player);
            }
        }
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

    public Array<IntVector2> getPath(IntVector2 from, IntVector2 to, int radius) {
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

        return pathFinder.getPath(
                graph,
                (int) (rect.height * (from.x - rect.x) + (from.y - rect.y)),
                (int) (rect.height * (to.x - rect.x) + (to.y - rect.y))
        );
    }

    public IntVector2 getCellPosition(float x, float y) {
        return new IntVector2(
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

    public Meta getMeta(IntVector2 position) {
        return getMeta(position.x, position.y);
    }

    public TiledMapTileLayer.Cell getCell(Layer layer, int x, int y) {
        return getTileLayer(layer).getCell(x, y);
    }

    // TODO: new PassableCellChecker instance every time (fix?)

    public boolean isPassable(int x, int y, boolean checkEntities) {
        return new PassableCellChecker(checkEntities).check(this, x, y);
    }

    public boolean isPassable(IntVector2 position, boolean checkEntities) {
        return isPassable(position.x, position.y, checkEntities);
    }

    public Array<IntVector2> getPassableCells(int x, int y, boolean checkEntities, int minRadius, int maxRadius,
                                              boolean reverse) {
        return getCells(
                Layer.META,
                new PassableCellChecker(checkEntities),
                x, y,
                minRadius, maxRadius,
                reverse);
    }

    /**
     * Rhombus search around given position (checks the position itself too).
     * TODO: make this circular
     */
    // TODO: MAKE THIS BEAUTIFUL
    public Array<IntVector2> getCells(Layer layer, CellChecker checker, int x, int y, int minRadius, int maxRadius,
                                      boolean reverse) {
        Array<IntVector2> result = new Array<>();
        int radius = minRadius;

        while (radius <= maxRadius) {
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    if (Math.abs(i) + Math.abs(j) != radius) {
                        continue;
                    }

                    int cellX = x + i;
                    int cellY = y + j;

                    if (!isValidPosition(cellX, cellY)) {
                        continue;
                    }

                    TiledMapTileLayer.Cell cell = getCell(layer, cellX, cellY);

                    if (checker.check(this, cellX, cellY)) {
                        result.add(new IntVector2(cellX, cellY));
                    }
                }
            }

            radius++;
        }

        if (reverse) {
            result.reverse();
        }

        return result;
    }

    public Array<Entity> getEntitiesAt(int x, int y, double radius) {
        Array<Entity> result = new Array<>();

        for (Entity entity : entities) {
            MovementComponent movement = entity.getComponent(MovementComponent.class);

            if (movement.getPosition().distance(x, y) <= radius) {
                result.add(entity);
            }
        }

        return result;
    }

    /**
     * @return First found entity at position
     */
    public Entity getEntityAt(int x, int y) {
        for (Entity entity : entities) {
            MovementComponent movement = entity.getComponent(MovementComponent.class);

            if (movement.getPosition().equals(x, y)) {
                return entity;
            }
        }

        return null;
    }

    /**
     * @return First found entity at position
     */
    public Entity getEntityAt(IntVector2 position) {
        return getEntityAt(position.x, position.y);
    }

    public Array<Entity> getEntitiesAt(int x, int y) {
        return getEntitiesAt(x, y, 0);
    }

    public Array<Entity> getEntitiesAt(IntVector2 position, int radius) {
        return getEntitiesAt(position.x, position.y, radius);
    }

    public Array<Entity> getEntitiesAt(IntVector2 position) {
        return getEntitiesAt(position.x, position.y);
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

    public Array<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public boolean removeEntity(Entity entity) {
        return entities.removeValue(entity, true);
    }

    public Player getPlayer() {
        return player;
    }

    public TiledMap getMap() {
        return map;
    }

    public int getLayerIndex(Layer layer) {
        return map.getLayers().getIndex(layer.name());
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

    public enum Layer {
        SPAWNERS, // object layer with entities/player spawn locations
        TELEPORTS, // object layer with portals spawn locations
        META, // meta layer for collisions/etc markup
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

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void entityMoved(Entity entity, IntVector2 previous, IntVector2 current);
    }
}
