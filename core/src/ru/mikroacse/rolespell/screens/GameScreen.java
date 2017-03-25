package ru.mikroacse.rolespell.screens;

/**
 * Created by MikroAcse on 22.03.2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import ru.mikroacse.rolespell.controller.WorldController;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.rolespell.view.WorldRenderer;

public class GameScreen implements Screen {
    private WorldRenderer renderer;
    private WorldController controller;

    private GameModel model;

    @Override
    public void show() {
        model = new GameModel();
        model.setWorld(new World(new TmxMapLoader().load("data/maps/test_map.tmx")));

        renderer = new WorldRenderer(model);
        controller = new WorldController(renderer, model);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(delta);
        controller.update(delta);
    }

    @Override
    public void resize(int width, int height) {
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