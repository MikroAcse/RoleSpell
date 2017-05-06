package ru.mikroacse.rolespell.app.view.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.HealthParameter;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;

import java.util.Iterator;
import java.util.List;

/**
 * Created by MikroAcse on 02-May-17.
 */
public class WorldRenderer {
    private final OrthographicCamera camera;

    private Texture waypoint;
    private Texture pathTexture;

    private AssetBundle bundle;

    private OrthogonalTiledMapRenderer mapRenderer;
    private World world;

    private int zoom;

    public WorldRenderer(World world) {
        this.world = world;

        mapRenderer = new OrthogonalTiledMapRenderer(world.getMap());
        camera = new OrthographicCamera();

        bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);

        waypoint = bundle.getTexture("path/waypoint");
        pathTexture = bundle.getTexture("path/path");
    }

    public void draw(Entity viewEntity, Batch batch) {
        if (world == null) {
            return;
        }

        MovementComponent observableMovement = viewEntity.getComponent(MovementComponent.class);

        Vector2 observablePosition = cellToMap(observableMovement.getPosition());

        Vector2 cameraPosition = observablePosition.cpy();

        cameraPosition.x = Math.max(cameraPosition.x, getCameraWidth() / 2);
        cameraPosition.y = Math.max(cameraPosition.y, getCameraHeight() / 2);

        cameraPosition.x = Math.min(cameraPosition.x, getWidth() - getCameraWidth() / 2);
        cameraPosition.y = Math.min(cameraPosition.y, getHeight() - getCameraHeight() / 2);

        // TODO: magic number, smoothing camera movement
        camera.position.x += (cameraPosition.x - camera.position.x) / 4f;
        camera.position.y += (cameraPosition.y - camera.position.y) / 4f;

        camera.update();

        mapRenderer.setView(camera);
        renderLayers(new World.Layer[]{
                World.Layer.BACKGROUND,
                World.Layer.LAYOUT,
                World.Layer.BUILDINGS,
                World.Layer.BUILDINGS_DECOR,
                World.Layer.BOTTOM,
                World.Layer.OBJECTS});

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (Entity entity : world.getEntities()) {
            MovementComponent movement = entity.getComponent(MovementComponent.class);

            IntVector2 position = movement.getPosition();
            Vector2 mapPosition = cellToMap(position);

            Texture texture = null;

            // TODO: separate entity drawer
            switch (entity.getType()) {
                case NPC:
                    texture = bundle.getTexture("entities/npc");
                    break;
                case PLAYER:
                    texture = bundle.getTexture("entities/player");
                    break;
                case DROPPED_ITEM:
                    texture = bundle.getTexture("items/weapons/wooden-sword");
                    break;
            }

            if (texture != null) {
                // TODO: normal damage animation and handling
                StatusComponent status = entity.getComponent(StatusComponent.class);

                if (status != null) {
                    HealthParameter health = status.getParameter(HealthParameter.class);

                    if (System.currentTimeMillis() - health.getLastTimeDamaged() <= 400) {
                        batch.setColor(Color.RED);
                    }
                }

                batch.draw(texture, mapPosition.x, mapPosition.y);
                batch.setColor(Color.WHITE);
            }
        }

        // TODO: beautify
        if (viewEntity.hasComponent(PathMovementComponent.class)) {
            List<IntVector2> path = ((PathMovementComponent) observableMovement).getPath();

            if (!path.isEmpty()) {
                Iterator<IntVector2> it = path.iterator();

                while (it.hasNext()) {
                    IntVector2 position = it.next();
                    Vector2 mapPosition = cellToMap(position.x, position.y);

                    if (it.hasNext()) {
                        batch.draw(pathTexture, mapPosition.x, mapPosition.y);
                    } else {
                        batch.draw(waypoint, mapPosition.x, mapPosition.y);
                    }
                }
            }
        }

        batch.end();

        renderLayers(new World.Layer[]{
                World.Layer.ADDITIONAL,
                World.Layer.ROOFS,
                World.Layer.TOP});
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    private void renderLayers(World.Layer[] layers) {
        mapRenderer.getBatch().begin();

        for (World.Layer layer : layers) {
            mapRenderer.renderTileLayer(world.getTileLayer(layer));
        }

        mapRenderer.getBatch().end();
    }

    public IntVector2 stageToCell(float x, float y) {
        return mapToCell(stageToMap(x, y));
    }

    public Vector2 stageToMap(float x, float y) {
        x *= camera.zoom;
        y *= camera.zoom;

        x += camera.position.x - getCameraWidth() / 2f;
        y += camera.position.y - getCameraHeight() / 2f;

        return new Vector2(x, y);
    }

    public Vector2 cellToMap(int x, int y) {
        return new Vector2(
                x * world.getTileWidth(),
                y * world.getTileHeight());
    }

    // TODO: bad method names

    public Vector2 cellToMap(IntVector2 position) {
        return cellToMap(position.x, position.y);
    }

    public IntVector2 mapToCell(float x, float y) {
        return new IntVector2(
                (int) (x / world.getTileWidth()),
                (int) (y / world.getTileHeight())
        );
    }

    public IntVector2 mapToCell(Vector2 position) {
        return mapToCell(position.x, position.y);
    }

    private float getCameraWidth() {
        return camera.viewportWidth * camera.zoom;
    }

    private float getCameraHeight() {
        return camera.viewportHeight * camera.zoom;
    }

    public float getWidth() {
        return world.getWidth() * world.getTileWidth() / camera.zoom;
    }

    public float getHeight() {
        return world.getHeight() * world.getTileHeight() / camera.zoom;
    }

    public float getZoom() {
        return 1 / camera.zoom;
    }

    public void setZoom(float zoom) {
        camera.zoom = 1 / zoom;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
