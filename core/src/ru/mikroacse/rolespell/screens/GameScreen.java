package ru.mikroacse.rolespell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import ru.mikroacse.rolespell.controller.GameController;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.rolespell.view.game.GameRenderer;


/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameScreen implements Screen {
    private GameRenderer renderer;
    private GameController controller;

    private GameModel model;

    @Override
    public void show() {
        model = new GameModel();
        model.setWorld(new World(new TmxMapLoader().load("data/maps/test_map.tmx")));

        renderer = new GameRenderer(model);
        controller = new GameController(renderer, model);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update(delta);
        renderer.render(delta);
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