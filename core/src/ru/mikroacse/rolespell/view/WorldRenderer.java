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
import ru.mikroacse.rolespell.model.entities.components.drawable.DrawableComponent;
import ru.mikroacse.rolespell.model.entities.core.DrawableEntity;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.MovableEntity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Position;

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
        if (model.getWorld() == null) {
            return;
        }

        World world = model.getWorld();
        MovableEntity observable = model.getObservable();

        Position observablePosition = cellToMap(observable.getMovementComponent().getPosition());

        Vector2 cameraPos = new Vector2(observablePosition.x, observablePosition.y);

        cameraPos.x = Math.min(cameraPos.x, world.getRealWidth() - camera.viewportWidth / 2f);
        cameraPos.y = Math.min(cameraPos.y, world.getRealHeight() - camera.viewportHeight / 2f);

        cameraPos.x = Math.max(cameraPos.x, camera.viewportWidth / 2f);
        cameraPos.y = Math.max(cameraPos.y, camera.viewportHeight / 2f);

        camera.position.x += (cameraPos.x - camera.position.x) / 4f;
        camera.position.y += (cameraPos.y - camera.position.y) / 4f;

        camera.update();

        renderer.setView(camera);
        renderer.render(new int[]{0, 1, 2, 3});

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (Entity entity : world.getEntities()) {
            if (entity instanceof DrawableEntity) {
                DrawableComponent drawableComponent = ((DrawableEntity) entity).getDrawableComponent();

                drawableComponent.draw(entity, world, batch);
            }
        }

        // TODO: beautify
        LinkedList<Position> playerPath = model.getPlayer().getMovementComponent().getPath();

        if (!playerPath.isEmpty()) {
            Position waypointPosition = cellToMap(model.getWaypoint().x, model.getWaypoint().y);

            waypoint.setPosition(waypointPosition.x, waypointPosition.y);
            waypoint.draw(batch);

            for (int i = 0; i < playerPath.size() - 1; i++) {
                Position pathPoint = playerPath.get(i);
                Position realPoint = cellToMap(pathPoint.x, pathPoint.y);

                batch.draw(new Texture("data/path.png"), realPoint.x, realPoint.y);
            }
        }

        batch.end();

        renderer.render(new int[]{4, 5});
    }

    public Position globalToLocal(int x, int y) {
        y = (int) camera.viewportHeight - y;

        x += camera.position.x - camera.viewportWidth / 2f;
        y += camera.position.y - camera.viewportHeight / 2f;

        return new Position(x, y);
    }

    // TODO: bad method names

    public Position cellToMap(int x, int y) {
        return new Position(
                x * model.getWorld().getTileWidth(),
                y * model.getWorld().getTileHeight());
    }

    public Position cellToMap(Position position) {
        return cellToMap(position.x, position.y);
    }

    public Position mapToCell(int x, int y) {
        return new Position(
                x / model.getWorld().getTileWidth(),
                y / model.getWorld().getTileHeight());
    }

    public Position mapToCell(Position position) {
        return mapToCell(position.x, position.y);
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
}
