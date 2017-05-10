package ru.mikroacse.rolespell.app.view.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.Map;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 02-May-17.
 */
// TODO: make this Group, and separate map renderer from world renderer
public class MapRenderer {
    private final OrthographicCamera camera;

    private OrthogonalTiledMapRenderer mapRenderer;
    private Map map;

    public MapRenderer(Map map) {
        this.map = map;

        mapRenderer = new OrthogonalTiledMapRenderer(map.getMap());
        camera = new OrthographicCamera();
    }

    public void updateCamera(Entity observable) {
        MovementComponent observableMovement = observable.getComponent(MovementComponent.class);

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
    }

    public void drawBottomLayers() {
        renderLayers(new Map.Layer[]{
                Map.Layer.BACKGROUND,
                Map.Layer.LAYOUT,
                Map.Layer.BUILDINGS,
                Map.Layer.BUILDINGS_DECOR,
                Map.Layer.BOTTOM,
                Map.Layer.OBJECTS});

        /*batch.begin();
        batch.setProjectionMatrix(camera.combined);


        for (Entity entity : map.getEntities()) {
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

        PathMovementComponent pathMovement = viewEntity.getComponent(PathMovementComponent.class);
        if (pathMovement != null && !pathMovement.isPathEmpty()) {
            Array<IntVector2> path = pathMovement.getPath();

            for (int i = 0; i < path.size; i++) {
                IntVector2 position = path.get(i);
                Vector2 mapPosition = cellToMap(position.x, position.y);

                if (i < path.size - 1) {
                    batch.draw(pathTexture, mapPosition.x, mapPosition.y);
                } else {
                    batch.draw(waypoint, mapPosition.x, mapPosition.y);
                }
            }
        }

        batch.end();*/
    }

    public void drawTopLayers() {
        renderLayers(new Map.Layer[]{
                Map.Layer.ADDITIONAL,
                Map.Layer.BUILDINGS_TOP,
                Map.Layer.ROOFS,
                Map.Layer.TOP});

        /*batch.begin();

        selector.draw(batch, 1f);

        batch.end();*/
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    private void renderLayers(Map.Layer[] layers) {
        mapRenderer.getBatch().begin();

        for (Map.Layer layer : layers) {
            mapRenderer.renderTileLayer(map.getTileLayer(layer));
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
                x * map.getTileWidth(),
                y * map.getTileHeight());
    }

    // TODO: bad method names

    public Vector2 cellToMap(IntVector2 position) {
        return cellToMap(position.x, position.y);
    }

    public IntVector2 mapToCell(float x, float y) {
        return new IntVector2(
                (int) (x / map.getTileWidth()),
                (int) (y / map.getTileHeight())
        );
    }

    public IntVector2 mapToCell(Vector2 position) {
        return mapToCell(position.x, position.y);
    }

    public float getCameraX() {
        return camera.position.x / camera.zoom - getCameraWidth();
    }

    public float getCameraY() {
        return camera.position.y / camera.zoom - getCameraHeight();
    }

    public float getCameraWidth() {
        return camera.viewportWidth * camera.zoom;
    }

    public float getCameraHeight() {
        return camera.viewportHeight * camera.zoom;
    }

    public float getWidth() {
        return map.getWidth() * map.getTileWidth();
    }

    public float getHeight() {
        return map.getHeight() * map.getTileHeight();
    }

    public float getZoom() {
        return 1 / camera.zoom;
    }

    public void setZoom(float zoom) {
        camera.zoom = 1 / zoom;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }
}
