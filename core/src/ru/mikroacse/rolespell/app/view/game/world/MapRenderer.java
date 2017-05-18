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
        Vector2 observablePosition = cellToMap(observable.getPosition());

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
    }

    public void drawTopLayers() {
        renderLayers(new Map.Layer[]{
                Map.Layer.ADDITIONAL,
                Map.Layer.BUILDINGS_TOP,
                Map.Layer.ROOFS,
                Map.Layer.TOP});
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

    public Vector2 cellToMap(IntVector2 position) {
        return cellToMap(position.x, position.y);
    }

    public IntVector2 mapToCell(float x, float y) {
        return new IntVector2(
                (int) (x / map.getTileWidth()),
                (int) (y / map.getTileHeight()));
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
}
