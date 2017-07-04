package ru.mikroacse.rolespell.app.controller.loader;

import ru.mikroacse.rolespell.app.view.RendererListener;
import ru.mikroacse.rolespell.app.view.loader.LoaderRenderer;

import static ru.mikroacse.rolespell.RoleSpell.assets;
import static ru.mikroacse.rolespell.RoleSpell.screens;

/**
 * Created by MikroAcse on 29-Apr-17.
 */
public class LoaderController {
    private LoaderRenderer renderer;

    public LoaderController(LoaderRenderer renderer) {
        this.renderer = renderer;

        RendererListener rendererListener = new RendererListener() {
            @Override
            public void onHidden() {
                screens().setWaited();
            }
        };

        renderer.addListener(rendererListener);
    }

    public void update(float delta) {
        renderer.act(delta);

        if (renderer.isBusy()) {
            return;
        }

        if (assets().isLoaded()) {
            renderer.hide();
        }
    }
}
