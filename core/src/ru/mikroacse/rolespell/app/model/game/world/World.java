package ru.mikroacse.rolespell.app.model.game.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.Player;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.pathfinding.GraphBuilder;
import ru.mikroacse.rolespell.app.model.game.pathfinding.PathFinder;
import ru.mikroacse.rolespell.app.model.game.pathfinding.graph.Graph;
import ru.mikroacse.rolespell.app.model.game.pathfinding.heuristic.ManhattanDistance;
import ru.mikroacse.rolespell.app.model.game.world.cells.CellChecker;
import ru.mikroacse.rolespell.app.model.game.world.cells.PassableCellChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MikroAcse on 23.03.2017.
 */
// TODO: refactor
public class World implements MovementComponent.Listener {
    private TiledMap map;
    private List<Entity> entities;
    
    private Listener listeners;
    
    private Player player;
    
    public World(TiledMap map) {
        this.map = map;
        listeners = ListenerSupportFactory.create(Listener.class);
        
        initialize();
    }
    
    private void initialize() {
        entities = new ArrayList<>();
        
        // TODO: make this less horrible and tryRouteTo to separate class
        for (MapObject mapObject : getLayer(Layer.SPAWNERS).getObjects()) {
            RectangleMapObject object = (RectangleMapObject) mapObject;
            
            Entity entity = EntityType.create(this, EntityType.valueOf(object.getName()));
            
            if (entity == null) {
                System.out.println("Couldn't create entity: " + object.getName());
                continue;
            }
            
            if (entity.getType() == EntityType.PLAYER) {
                player = (Player) entity;
            }
            
            int realX = (int) object.getRectangle().x;
            int realY = (int) object.getRectangle().y;
            
            MovementComponent movementComponent = entity.getComponent(MovementComponent.class);
            
            movementComponent.setBoth(new IntVector2(realX / getTileWidth(), realY / getTileHeight()));
            
            movementComponent.addListener(this);
            entities.add(entity);
        }
        
        for (Entity entity : entities) {
            if (entity.hasComponent(BehaviorAi.class)) {
                entity
                        .getComponent(BehaviorAi.class)
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
    
    @Override
    public void originChanged(MovementComponent movement, IntVector2 previous, IntVector2 current) {
    
    }
    
    @Override
    public void positionChanged(MovementComponent movement, IntVector2 previous, IntVector2 current) {
        listeners.entityMoved(movement.getEntity(), previous, current);
    }
    
    public LinkedList<IntVector2> getPath(IntVector2 from, IntVector2 to, int radius) {
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
    
    public List<IntVector2> getPassableCells(int x, int y, boolean checkEntities, int minRadius, int maxRadius,
                                             boolean inverse) {
        return getCells(
                Layer.META,
                new PassableCellChecker(checkEntities),
                x, y,
                minRadius, maxRadius,
                inverse);
    }
    
    /**
     * Rhombus search around given position (checks the position itself too).
     */
    // TODO: MAKE THIS BEAUTIFUL
    public List<IntVector2> getCells(Layer layer, CellChecker checker, int x, int y, int minRadius, int maxRadius,
                                     boolean inversed) {
        List<IntVector2> result = new ArrayList<>();
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
        
        if (inversed) {
            Collections.reverse(result);
        }
        
        return result;
    }
    
    public List<Entity> getEntitiesAt(int x, int y, int radius) {
        List<Entity> result = new ArrayList<>();
        
        for (Entity entity : entities) {
            MovementComponent movement = entity.getComponent(MovementComponent.class);
            
            if (movement.getPosition().distance(x, y) <= radius) {
                result.add(entity);
            }
        }
        
        return result;
    }
    
    public List<Entity> getEntitiesAt(int x, int y) {
        return getEntitiesAt(x, y, 0);
    }
    
    public List<Entity> getEntitiesAt(IntVector2 position, int radius) {
        return getEntitiesAt(position.x, position.y, radius);
    }
    
    public List<Entity> getEntitiesAt(IntVector2 position) {
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
    
    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void entityMoved(Entity entity, IntVector2 previous, IntVector2 current);
    }
}
