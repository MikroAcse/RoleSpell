package ru.mikroacse.rolespell.app.view.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.HealthProperty;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.model.game.world.WorldListener;
import ru.mikroacse.rolespell.app.view.game.entities.EntityView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by MikroAcse on 09-May-17.
 */
public class WorldRenderer extends Group {
    private World world;

    private WorldListener worldListener;

    private MapRenderer mapRenderer;
    private PathRenderer pathRenderer;

    private Pool<EntityView> entityViewPool;
    private Pool<EntityName> entityNamePool;

    // TODO: separate?
    private Map<Entity, EntityView> entityViews;
    private Map<Entity, EntityName> entityNames;

    private CellSelector selector;

    private Entity observable;

    private float zoom;

    public WorldRenderer() {
        worldListener = new WorldListener() {
            @Override
            public void entityAdded(World world, Entity entity) {
                refreshEntities();
                updatePositions();
            }

            @Override
            public void entityRemoved(World world, Entity entity) {
                refreshEntities();
                updatePositions();
            }

            @Override
            public void positionChanged(World world, MovementComponent movement, int prevX, int prevY, IntVector2 current) {
                updatePositions();
            }
        };

        zoom = 1f;

        selector = new CellSelector();
        addActor(selector);

        entityViewPool = new Pool<EntityView>() {
            @Override
            protected EntityView newObject() {
                return new EntityView();
            }
        };
        entityNamePool = new Pool<EntityName>() {
            @Override
            protected EntityName newObject() {
                return new EntityName();
            }
        };

        pathRenderer = new PathRenderer(this);
        addActor(pathRenderer);

        entityViews = new HashMap<>();
        entityNames = new HashMap<>();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        updateEntities();
        updatePath();

        super.draw(batch, parentAlpha);
    }

    private void updateEntities() {
        for (Entity entity : world.getEntities()) {
            EntityView entityView = entityViews.get(entity);

            if (entityView == null) {
                continue;
            }

            // TODO: normal damage animation and handling
            StatusComponent status = entity.getComponent(StatusComponent.class);

            if (status != null) {
                HealthProperty health = status.getProperty(HealthProperty.class);

                if (System.currentTimeMillis() - health.getLastTimeDamaged() <= 400) {
                    entityView.setColor(Color.RED);
                } else {
                    entityView.setColor(Color.WHITE);
                }
            }
        }
    }

    private void updatePath() {
        PathMovementComponent pathMovement = observable.getComponent(PathMovementComponent.class);

        if (pathMovement != null) {
            pathRenderer.setPath(pathMovement.getPath());
        } else {
            pathRenderer.setPath(null);
        }
    }

    private void updatePositions() {
        for (Entity entity : world.getEntities()) {
            EntityView entityView = entityViews.get(entity);

            if (entityView == null) {
                continue;
            }

            if (entity.getType() == EntityType.DROPPED_ITEM) {
                entityView.toBack();
            }

            Vector2 mapPosition = mapRenderer.cellToMap(entity.getPosition());

            entityView.setPosition(mapPosition.x, mapPosition.y);

            EntityName entityName = entityNames.get(entity);

            if (entityName != null) {
                // TODO: magic numbers
                entityName.setX((int) (mapPosition.x + entityView.getWidth() / 2 - entityName.getRealWidth() / 2));
                entityName.setY((int) (mapPosition.y + entityView.getHeight() + 10));

                entityName.toFront();
            }
        }
    }

    private void refreshEntities() {
        Array<Entity> entities = world.getEntities();

        // remove unused views
        Iterator<Map.Entry<Entity, EntityView>> viewsIterator = entityViews.entrySet().iterator();

        while (viewsIterator.hasNext()) {
            Map.Entry<Entity, EntityView> entry = viewsIterator.next();

            Entity entity = entry.getKey();

            if (!world.hasEntity(entity)) {
                EntityView entityView = entityViews.get(entity);
                entityViewPool.free(entityView);

                viewsIterator.remove();
            }
        }

        // remove unused names
        Iterator<Map.Entry<Entity, EntityName>> namesIterator = entityNames.entrySet().iterator();

        while (namesIterator.hasNext()) {
            Map.Entry<Entity, EntityName> entry = namesIterator.next();
            Entity entity = entry.getKey();

            if (!world.hasEntity(entity)) {
                EntityName entityName = entityNames.get(entity);
                entityNamePool.free(entityName);

                namesIterator.remove();
            }
        }

        // add new views and names
        for (Entity entity : world.getEntities()) {
            // TODO: somehow exclude undrawable entity types
            if (entity.getType() == EntityType.PORTAL_SPAWN) {
                continue;
            }

            if (!entityViews.containsKey(entity)) {
                EntityView entityView = entityViewPool.obtain();
                entityView.setEntity(entity);

                entityViews.put(entity, entityView);

                addActor(entityView);
            }

            if (entity.getName() != null && !entityNames.containsKey(entity)) {
                EntityName entityName = entityNamePool.obtain();
                entityName.setEntity(entity);

                entityNames.put(entity, entityName);

                addActor(entityName);
            }
        }
    }

    public void moveCameraTo(Entity observable) {
        mapRenderer.updateCamera(observable);

        setPosition(-mapRenderer.getCameraX(), -mapRenderer.getCameraY());
    }

    public void moveCamera() {
        moveCameraTo(observable);
    }

    public void resizeViewport(int width, int height) {
        if(mapRenderer != null) {
            mapRenderer.resize(width, height);
        }
    }

    private void attachWorld(World world) {
        world.addListener(worldListener);

        mapRenderer = new MapRenderer(world.getMap());
        mapRenderer.setZoom(zoom);

        refreshEntities();
        updatePositions();
    }

    private void detachWorld(World world) {
        world.removeListener(worldListener);

        for (EntityName entityName : entityNames.values()) {
            entityNamePool.free(entityName);
        }

        entityNames.clear();

        for (EntityView entityView : entityViews.values()) {
            entityViewPool.free(entityView);
        }

        entityViewPool.clear();

        pathRenderer.clear();
    }

    public void setSelectorPosition(int x, int y) {
        Vector2 position = mapRenderer.cellToMap(x, y);
        selector.setPosition(position.x, position.y);
    }

    public void setSelectorVisible(boolean visible) {
        selector.setVisible(visible);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;

        if (mapRenderer != null) {
            mapRenderer.setZoom(zoom);
        }
        setScale(zoom);
    }

    public Entity getObservable() {
        return observable;
    }

    public void setObservable(Entity observable) {
        this.observable = observable;
    }

    public MapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        if (this.world != null) {
            detachWorld(this.world);
        }

        this.world = world;

        if (world != null) {
            attachWorld(world);
        }
    }
}
