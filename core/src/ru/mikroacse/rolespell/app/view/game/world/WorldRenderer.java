package ru.mikroacse.rolespell.app.view.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.HealthParameter;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.model.game.world.WorldListener;
import ru.mikroacse.rolespell.app.view.game.entities.EntityView;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 09-May-17.
 */
public class WorldRenderer extends Group {
    private World world;

    private WorldListener worldListener;

    private MapRenderer mapRenderer;
    private PathRenderer pathRenderer;

    private Pool<EntityView> entityViewPool;
    private Array<EntityView> entityViews;

    private AssetBundle bundle;
    private CellSelector selector;

    private Entity observable;

    private float zoom;

    public WorldRenderer(World world) {
        this.world = world;

        worldListener = new WorldListener() {
            @Override
            public void entityAdded(World world, Entity entity) {
                updateEntities();
                updatePositions();
            }

            @Override
            public void entityRemoved(World world, Entity entity) {
                updateEntities();
            }

            @Override
            public void entityMoved(World world, Entity entity, IntVector2 previous, IntVector2 current) {
                updatePositions();
            }
        };

        // TODO: update listener on world change, also update map and other things
        world.addListener(worldListener);

        mapRenderer = new MapRenderer(world.getMap());
        zoom = 1f;

        // TODO: remove
        EntityName name = new EntityName("Test");
        //addActor(name);
        name.setPosition(100, 100);
        name.setSize(200, 100);

        bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);

        selector = new CellSelector();
        addActor(selector);

        entityViewPool = new Pool<EntityView>() {
            @Override
            protected EntityView newObject() {
                return new EntityView();
            }
        };

        pathRenderer = new PathRenderer(this);
        addActor(pathRenderer);

        entityViews = new Array<>();

        updateEntities();
        updatePositions();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (EntityView entityView : entityViews) {
            Entity entity = entityView.getEntity();

            // TODO: normal damage animation and handling
            StatusComponent status = entity.getComponent(StatusComponent.class);

            if (status != null) {
                HealthParameter health = status.getParameter(HealthParameter.class);

                if (System.currentTimeMillis() - health.getLastTimeDamaged() <= 400) {
                    entityView.setColor(Color.RED);
                } else {
                    entityView.setColor(Color.WHITE);
                }
            }
        }

        PathMovementComponent pathMovement = observable.getComponent(PathMovementComponent.class);
        if (pathMovement != null) {
            pathRenderer.setPath(pathMovement.getPath());
        } else {
            pathRenderer.setPath(null);
        }

        super.draw(batch, parentAlpha);
    }

    public void updatePositions() {
        for (EntityView entityView : entityViews) {
            Entity entity = entityView.getEntity();

            MovementComponent movement = entity.getComponent(MovementComponent.class);

            IntVector2 position = movement.getPosition();
            Vector2 mapPosition = mapRenderer.cellToMap(position);

            entityView.setPosition(mapPosition.x, mapPosition.y);
        }
    }

    public void updateEntities() {
        Array<Entity> entities = world.getEntities();
        int size = entities.size;

        while (size != entityViews.size) {
            if(size > entityViews.size) {
                entityViews.add(entityViewPool.obtain());
            } else {
                entityViewPool.free(entityViews.pop());
            }
        }

        for (int i = 0; i < entityViews.size; i++) {
            EntityView entityView = entityViews.get(i);

            entityView.setEntity(entities.get(i));

            addActor(entityView);
        }
    }

    public void updateCamera(Entity observable) {
        mapRenderer.updateCamera(observable);

        setPosition(-mapRenderer.getCameraX(), -mapRenderer.getCameraY());
    }

    public void resizeViewport(int width, int height) {
        mapRenderer.resize(width, height);
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

        mapRenderer.setZoom(zoom);
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
}
