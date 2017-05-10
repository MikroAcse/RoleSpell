package ru.mikroacse.rolespell.app.screens;

import com.badlogic.gdx.Screen;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.controller.loader.LoaderController;
import ru.mikroacse.rolespell.app.view.loader.LoaderRenderer;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class LoaderScreen implements Screen {
    private LoaderRenderer renderer;
    private LoaderController controller;

    public LoaderScreen() {
        renderer = new LoaderRenderer();
        controller = new LoaderController(renderer);
    }

    @Override
    public void show() {
        renderer.show();
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.draw();
    }

    @Override
    public void resize(int width, int height) {
        RoleSpell.getAssetManager().updateScale(width, height);

        renderer.getViewport().update(width, height, true);
        renderer.update();
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
