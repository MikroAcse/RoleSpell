package ru.mikroacse.rolespell.view.game;

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
import ru.mikroacse.rolespell.model.entities.components.drawable.TextureDrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.rolespell.view.game.ui.StatusHUD;
import ru.mikroacse.util.Position;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameRenderer {
    private GameModel model;

    private TiledMapRenderer renderer;
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Sprite player;
    private Sprite waypoint;

    // TODO: asset manager
    private Texture pathTexture;

    private StatusHUD statusHUD;

    public GameRenderer(GameModel model) {
        this.model = model;

        renderer = new OrthogonalTiledMapRenderer(model.getWorld().getMap());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        player = new Sprite(new Texture("data/player.png"));
        waypoint = new Sprite(new Texture("data/waypoint.png"));

        pathTexture = new Texture("data/path.png");

        batch = new SpriteBatch();

        statusHUD = new StatusHUD();

        // TODO: zooming
    }

    public void render(float delta) {
        if (model.getWorld() == null) {
            return;
        }

        World world = model.getWorld();
        Entity observable = model.getObservable();
        MovementComponent observableMovement = observable.getComponent(MovementComponent.class);

        Position observablePosition = cellToMap(observableMovement.getPosition());

        Vector2 cameraPos = new Vector2(observablePosition.x, observablePosition.y);

        cameraPos.x = Math.min(cameraPos.x, world.getRealWidth() - camera.viewportWidth / 2f);
        cameraPos.y = Math.min(cameraPos.y, world.getRealHeight() - camera.viewportHeight / 2f);

        cameraPos.x = Math.max(cameraPos.x, camera.viewportWidth / 2f);
        cameraPos.y = Math.max(cameraPos.y, camera.viewportHeight / 2f);

        camera.position.x += (cameraPos.x - camera.position.x) / 4f;
        camera.position.y += (cameraPos.y - camera.position.y) / 4f;

        camera.update();

        renderer.setView(camera);
        renderer.render(new int[]{0, 1, 2, 3}); // TODO: magic

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (Entity entity : world.getEntities()) {
            DrawableComponent drawable = entity.getComponent(DrawableComponent.class);
            MovementComponent movement = entity.getComponent(MovementComponent.class);

            Position position = movement.getPosition();
            position = cellToMap(position);

            if (drawable instanceof TextureDrawableComponent) {
                Texture texture = ((TextureDrawableComponent) drawable).getTexture();

                batch.draw(texture, position.x, position.y);
            }
        }

        // TODO: beautify
        if (observable.hasComponent(PathMovementComponent.class)) {
            LinkedList<Position> path = ((PathMovementComponent) observableMovement).getPath();

            if (!path.isEmpty()) {
                Iterator<Position> it = path.iterator();

                while (it.hasNext()) {
                    Position position = it.next();
                    Position mapPosition = cellToMap(position.x, position.y);

                    if (it.hasNext()) {
                        batch.draw(pathTexture, mapPosition.x, mapPosition.y);
                    } else {
                        batch.draw(waypoint, mapPosition.x, mapPosition.y);
                    }
                }
            }
        }

        // TODO: doesn't work when uncommented
        //renderer.render(new int[]{4, 5}); // TODO: magic

        statusHUD.draw(
                observable.getComponent(StatusComponent.class),
                batch,
                camera.position.x - camera.viewportWidth / 2 + 5,
                camera.position.y - camera.viewportHeight / 2 + 5);

        batch.end();
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
