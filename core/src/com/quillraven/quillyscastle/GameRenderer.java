package com.quillraven.quillyscastle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.map.Map;
import com.quillraven.quillyscastle.map.MapManager;
import com.quillraven.quillyscastle.map.MapObserver;

public class GameRenderer implements MapObserver {
    private static final String TAG = GameRenderer.class.getSimpleName();

    private static final int[] BACKGROUND_LAYERS = {0, 1};
    private static final int[] FOREGROUND_LAYERS = {2, 3};
    private static final float VISIBLE_TILES_X_AXIS = 32f;

    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final GameWorld world;
    private final Entity player;
    private final Rectangle playerBoundingBox;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Batch batch;
    private Vector2 previousPlayerPosition;

    public GameRenderer(GameWorld world) {
        camera = world.getCamera();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();
        MapManager.getInstance().addObserver(this);

        this.world = world;
        player = world.getPlayer();
        playerBoundingBox = player.getBoundingBox();
        previousPlayerPosition = new Vector2();
    }

    public void dispose() {
        shapeRenderer.dispose();
        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
        Gdx.app.debug(TAG, "disposed!");
    }

    public void render(final float interpolationValue) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set( // interpolate position
                playerBoundingBox.x * interpolationValue + previousPlayerPosition.x * (1 - interpolationValue), // position x
                playerBoundingBox.y * interpolationValue + previousPlayerPosition.y * (1 - interpolationValue), // position y
                0);
        camera.update();
        mapRenderer.setView(camera);

        batch.setColor(1, 1, 1, 1);
        mapRenderer.render(BACKGROUND_LAYERS);

        batch.begin();
        for (Entity entity : world.getEntities()) {
            entity.render(interpolationValue, batch, shapeRenderer);
        }
        player.render(interpolationValue, batch, shapeRenderer);
        batch.end();

        batch.setColor(1, 1, 1, 1);
        mapRenderer.render(FOREGROUND_LAYERS);

        playerBoundingBox.getPosition(previousPlayerPosition);
    }

    public void resize(int width, int height) {
        float visibleTiles = width * GameWorld.UNIT_SCALE;
        camera.viewportWidth = visibleTiles >= VISIBLE_TILES_X_AXIS ? VISIBLE_TILES_X_AXIS : visibleTiles;
        camera.viewportHeight = camera.viewportWidth * height / width;
        camera.update();

        Gdx.app.debug(TAG, "Resizing window: " + width + " x " + height);
        Gdx.app.debug(TAG, "Resizing viewport: " + camera.viewportWidth + " x " + camera.viewportHeight);
    }

    @Override
    public void onMapChanged(Map currentMap, Map newMap) {
        if (mapRenderer == null) {
            mapRenderer = new OrthogonalTiledMapRenderer(newMap.getTiledMap(), GameWorld.UNIT_SCALE);
            batch = mapRenderer.getBatch();
        } else {
            mapRenderer.setMap(newMap.getTiledMap());
        }

        Gdx.app.debug(TAG, "Map changed!");
    }
}
