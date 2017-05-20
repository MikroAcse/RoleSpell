package ru.mikroacse.rolespell.app.model.game.world;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.mobs.Player;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementListener;
import ru.mikroacse.rolespell.app.model.game.pathfinding.GraphBuilder;
import ru.mikroacse.rolespell.app.model.game.pathfinding.PathFinder;
import ru.mikroacse.rolespell.app.model.game.pathfinding.graph.Graph;
import ru.mikroacse.rolespell.app.model.game.pathfinding.heuristic.ManhattanDistance;
import ru.mikroacse.rolespell.app.model.game.world.cells.CellWeigher;
import ru.mikroacse.rolespell.app.model.game.world.cells.PassableCellWeigher;

/**
 * Created by MikroAcse on 23.03.2017.
 */
// TODO: refactor
public class World {
    private GameModel gameModel;

    private Map map;
    private Array<Entity> entities;

    private Listener listeners;

    private MovementListener movementListener;

    private Player player;

    public World(GameModel gameModel, Map map) {
        this.gameModel = gameModel;
        this.map = map;

        listeners = ListenerSupportFactory.create(Listener.class);

        movementListener = new MovementListener() {
            @Override
            public void positionChanged(MovementComponent movement, int prevX, int prevY, IntVector2 current) {
                listeners.entityMoved(World.this, movement.getEntity(), prevX, prevY, current);
            }
        };

        initialize();
    }

    private void initialize() {
        entities = new Array<>();

        entities.addAll(MapParser.parseEntities(this, map));
        entities.addAll(MapParser.parsePortals(this, map));

        for (Entity entity : entities) {
            if(entity.getType() == EntityType.PLAYER) {
                if(player != null) {
                    System.out.println("World: player entity already exists!");
                }

                player = (Player) entity;
            }

            MovementComponent movement = entity.getComponent(MovementComponent.class);

            movement.addListener(movementListener);
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

        // TODO: don't create new path finder and cell checker every time
        PathFinder pathFinder = new PathFinder(new ManhattanDistance(map.getWeight(Map.Meta.PATH)));

        CellWeigher cellWeigher = new PassableCellWeigher(true) {
            @Override
            public double weigh(World world, int x, int y) {
                if(from.equals(x, y)) { // don't check 'from' point
                    return 0;
                }
                return super.weigh(world, x, y);
            }
        };

        // TODO: use actual map instead of generating Graph every time (?) or optimize it somehow
        Graph graph = GraphBuilder.fromWorld(this, rect, cellWeigher);

        return pathFinder.getPath(
                graph,
                (int) (rect.height * (from.x - rect.x) + (from.y - rect.y)),
                (int) (rect.height * (to.x - rect.x) + (to.y - rect.y)));
    }

    // TODO: new PassableCellChecker instance every time (fix?)
    public boolean isPassable(int x, int y, boolean checkEntities) {
        return new PassableCellWeigher(checkEntities).weigh(this, x, y) != Double.POSITIVE_INFINITY;
    }

    public boolean isPassable(IntVector2 position, boolean checkEntities) {
        return isPassable(position.x, position.y, checkEntities);
    }

    public Array<IntVector2> getPassableCells(int x, int y, boolean checkEntities, int minRadius, int maxRadius,
                                              boolean reverse) {
        return getCells(
                Map.Layer.META,
                new PassableCellWeigher(checkEntities),
                x, y,
                minRadius, maxRadius,
                reverse);
    }

    /**
     * Rhombus search around given position (checks the position itself too).
     * TODO: make this circular
     */
    // TODO: MAKE THIS BEAUTIFUL
    public Array<IntVector2> getCells(Map.Layer layer, CellWeigher checker, int x, int y, int minRadius, int maxRadius,
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

                    if (!map.isValidPosition(cellX, cellY)) {
                        continue;
                    }

                    TiledMapTileLayer.Cell cell = map.getCell(layer, cellX, cellY);

                    if (checker.weigh(this, cellX, cellY) != Double.POSITIVE_INFINITY) {
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
            if (entity.getPosition().distance(x, y) <= radius) {
                result.add(entity);
            }
        }

        return result;
    }

    /**
     * @return First found entity at position.
     */
    public Entity getEntityAt(int x, int y) {
        for (Entity entity : entities) {
            if (entity.getPosition().equals(x, y)) {
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

    public Array<Entity> getEntities() {
        return entities;
    }

    public boolean hasEntity(Entity entity) {
        return entities.contains(entity, true);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);

        listeners.entityAdded(this, entity);
    }

    public boolean removeEntity(Entity entity) {
        if(entities.removeValue(entity, true)) {
            listeners.entityRemoved(this, entity);
            return true;
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "World{" +
                "map=" + map +
                '}';
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void entityMoved(World world, Entity entity, int prevX, int prevY, IntVector2 current);
        void entityAdded(World world, Entity entity);
        void entityRemoved(World world, Entity entity);
    }
}
