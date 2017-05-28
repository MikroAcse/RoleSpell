package ru.mikroacse.rolespell.app.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.controller.game.GameController;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.controllers.MobController;
import ru.mikroacse.rolespell.app.model.game.entities.objects.Portal;
import ru.mikroacse.rolespell.app.model.game.world.Map;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.model.game.world.WorldListener;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameScreen implements Screen {
    private GameRenderer renderer;
    private GameController controller;

    private GameModel model;

    public GameScreen() {
        model = new GameModel();

        renderer = new GameRenderer(model);
        controller = new GameController(renderer, model);

        setWorld("eclipse-chambers", null);
    }

    // TODO: ???
    public void setWorld(String id, String portalId) {
        TiledMap map = RoleSpell.getAssetManager()
                .getBundle(AssetManager.Bundle.GAME)
                .getMap(id + "/map");

        World world = new World(new Map(map, id));

        model.setWorld(world);

        if (portalId != null) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.PORTAL) {
                    Portal portal = (Portal) entity;

                    if (portal.isSpawn() && portal.getId().equals(portalId)) {
                        model.getControllable().setPosition(portal.getPosition());
                        break;
                    }
                }
            }
        }

        world.addListener(new WorldListener() {
            @Override
            public void mobDied(World world, MobController controller) {
                System.out.println("died: " + controller.getEntity().getType());
            }
        });

        renderer.refreshWorld();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        renderer.draw();
    }

    @Override
    public void resize(int width, int height) {
        RoleSpell.getAssetManager().updateScale(width, height);

        renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public GameModel getModel() {
        return model;
    }
}