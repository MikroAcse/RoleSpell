package ru.mikroacse.rolespell.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.world.World;

import java.awt.*;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class WorldRenderer {
    private GameModel model;

    private TiledMapRenderer renderer;
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Sprite player;
    private Sprite waypoint;

    public WorldRenderer(GameModel model) {
        this.model = model;

        renderer = new OrthogonalTiledMapRenderer(model.getWorld().getMap());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(model.getWorld().getMap().getTileSets().getTile(0));
        model.getWorld().getMapTileLayer(World.Layer.OBJECTS).setCell(0, 0, cell);
        model.getWorld().getMapTileLayer(World.Layer.OBJECTS).setCell(10, 10, cell);

        player = new Sprite(new Texture("data/player.png"));
        waypoint = new Sprite(new Texture("data/waypoint.png"));

        batch = new SpriteBatch();
    }

    public void render(float delta) {
        Point playerPosition = mapToReal(model.getPlayer().x, model.getPlayer().y);

        camera.position.x = 0;
        camera.position.y = 0;

        camera.position.x += playerPosition.x;
        camera.position.y += playerPosition.y;

        camera.position.x = Math.min(camera.position.x, model.getWorld().getMapRealWidth() - camera.viewportWidth / 2f);
        camera.position.y = Math.min(camera.position.y, model.getWorld().getMapRealHeight() - camera.viewportHeight / 2f);

        camera.position.x = Math.max(camera.position.x, camera.viewportWidth / 2f);
        camera.position.y = Math.max(camera.position.y, camera.viewportHeight / 2f);

        camera.update();

        renderer.setView(camera);
        renderer.render(new int[]{0, 1, 2, 3});

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        player.setPosition(playerPosition.x, playerPosition.y);
        player.draw(batch);

        if (model.isMoving) {
            Point waypointPosition = mapToReal(model.getWaypoint().x, model.getWaypoint().y);

            waypoint.setPosition(waypointPosition.x, waypointPosition.y);
            waypoint.draw(batch);
        }

        batch.end();

        renderer.render(new int[]{4});
    }

    public Point globalToLocal(int x, int y) {
        y = (int) camera.viewportHeight - y;

        x += camera.position.x - camera.viewportWidth / 2f;
        y += camera.position.y - camera.viewportHeight / 2f;

        return new Point(x, y);
    }

    public Point mapToReal(int x, int y) {
        return new Point(
                (int) (x * model.getWorld().getMapTileWidth()),
                (int) (y * model.getWorld().getMapTileHeight()));
    }

    public Point realToMap(int x, int y) {
        return new Point(
                (int) (x / model.getWorld().getMapTileWidth()),
                (int) (y / model.getWorld().getMapTileHeight()));
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
}
