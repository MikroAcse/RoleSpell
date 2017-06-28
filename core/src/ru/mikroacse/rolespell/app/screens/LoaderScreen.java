package ru.mikroacse.rolespell.app.screens;

import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.controller.loader.LoaderController;
import ru.mikroacse.rolespell.app.view.loader.LoaderRenderer;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class LoaderScreen extends Screen {
    private LoaderRenderer renderer;
    private LoaderController controller;

    public LoaderScreen() {
        super(false);

        renderer = new LoaderRenderer();
        controller = new LoaderController(renderer);
    }

    @Override
    public void restore() {
        renderer.show();

        RoleSpell.showMouse();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        controller.update(delta);

        renderer.act(delta);
        renderer.draw();
    }

    @Override
    public void resize(int width, int height) {
        RoleSpell.assets().updateScale(width, height);

        renderer.getViewport().update(width, height, true);
        renderer.update();
    }
}
