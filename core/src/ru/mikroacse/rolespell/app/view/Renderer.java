package ru.mikroacse.rolespell.app.view;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public abstract class Renderer extends com.badlogic.gdx.scenes.scene2d.Stage {
    private boolean busy;

    protected Listener listeners;

    public Renderer() {
        super(new ScreenViewport());

        listeners = ListenerSupportFactory.create(Listener.class);
    }

    public void addListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).addListener(listener);
    }

    public void removeListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<Listener>) listeners).clearListeners();
    }

    public void update() {

    }

    public void show() {

    }

    public void hide() {

    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);

        update();
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void onHidden();
        void onShown();
    }
}
