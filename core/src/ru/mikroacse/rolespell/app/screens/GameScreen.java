package ru.mikroacse.rolespell.app.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.controller.game.GameController;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.world.World;
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

        TiledMap map = RoleSpell.getAssetManager()
                .getBundle(AssetManager.Bundle.GAME)
                .getMap("rottenville");

        World world = new World(map);

        model.setWorld(world);

        renderer = new GameRenderer(model);
        controller = new GameController(renderer, model);
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
}