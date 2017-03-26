package ru.mikroacse.rolespell.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.entities.Npc;
import ru.mikroacse.rolespell.model.entities.Player;
import ru.mikroacse.rolespell.model.entities.core.Entity;

import java.awt.*;
import java.util.LinkedList;

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

        player = new Sprite(new Texture("data/player.png"));
        waypoint = new Sprite(new Texture("data/waypoint.png"));

        batch = new SpriteBatch();

        // TODO: zooming
    }

    public void render(float delta) {
        if(model.getWorld() == null) {
            return;
        }

        Point observablePosition = cellToMap(model.getObservable().x, model.getObservable().y);

        Vector2 cameraPos = new Vector2(observablePosition.x, observablePosition.y);

        cameraPos.x = Math.min(cameraPos.x, model.getWorld().getMapRealWidth() - camera.viewportWidth / 2f);
        cameraPos.y = Math.min(cameraPos.y, model.getWorld().getMapRealHeight() - camera.viewportHeight / 2f);

        cameraPos.x = Math.max(cameraPos.x, camera.viewportWidth / 2f);
        cameraPos.y = Math.max(cameraPos.y, camera.viewportHeight / 2f);

        camera.position.x += (cameraPos.x - camera.position.x) / 4f;
        camera.position.y += (cameraPos.y - camera.position.y) / 4f;

        camera.update();

        renderer.setView(camera);
        renderer.render(new int[]{0, 1, 2, 3});

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (Entity entity : model.getWorld().getEntities()) {
            Point entityPosition = cellToMap(entity.x, entity.y);

            if(entity instanceof Player) {
                batch.draw(new Texture("data/player.png"), entityPosition.x, entityPosition.y);
            }
            if(entity instanceof Npc) {
                batch.draw(new Texture("data/Npc.png"), entityPosition.x, entityPosition.y);
            }
        }

        LinkedList<Point> playerPath = model.getPlayer().getPath();

        if (!playerPath.isEmpty()) {
            Point waypointPosition = cellToMap(model.getWaypoint().x, model.getWaypoint().y);

            waypoint.setPosition(waypointPosition.x, waypointPosition.y);
            waypoint.draw(batch);

            for (int i = 0; i < playerPath.size() - 1; i++) {
                Point pathPoint = playerPath.get(i);
                Point realPoint = cellToMap(pathPoint.x, pathPoint.y);

                batch.draw(new Texture("data/path.png"), realPoint.x, realPoint.y);
            }
        }

        batch.end();

        renderer.render(new int[]{4, 5});
    }

    public Point globalToLocal(int x, int y) {
        y = (int) camera.viewportHeight - y;

        x += camera.position.x - camera.viewportWidth / 2f;
        y += camera.position.y - camera.viewportHeight / 2f;

        return new Point(x, y);
    }

    public Point cellToMap(int x, int y) {
        return new Point(
                x * model.getWorld().getMapTileWidth(),
                y * model.getWorld().getMapTileHeight());
    }

    public Point mapToCell(int x, int y) {
        return new Point(
                x / model.getWorld().getMapTileWidth(),
                y / model.getWorld().getMapTileHeight());
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
}
