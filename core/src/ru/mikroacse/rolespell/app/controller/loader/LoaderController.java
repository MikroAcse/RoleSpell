package ru.mikroacse.rolespell.app.controller.loader;

import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.view.RendererListener;
import ru.mikroacse.rolespell.app.view.loader.LoaderRenderer;

/**
 * Created by MikroAcse on 29-Apr-17.
 */
public class LoaderController {
    private LoaderRenderer renderer;

    private RendererListener rendererListener;

    public LoaderController(LoaderRenderer renderer) {
        this.renderer = renderer;

        rendererListener = new RendererListener() {
            @Override
            public void onHidden() {
                RoleSpell.getScreenManager().setWaited();
            }
        };

        renderer.addListener(rendererListener);
    }

    public void update(float delta) {
        renderer.act(delta);

        if (renderer.isBusy()) {
            return;
        }

        if (RoleSpell.getAssetManager().isLoaded()) {
            renderer.hide();
        }
    }
}
