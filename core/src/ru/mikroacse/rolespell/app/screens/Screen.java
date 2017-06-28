package ru.mikroacse.rolespell.app.screens;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class Screen implements com.badlogic.gdx.Screen {
    private boolean disposable;

    public Screen(boolean disposable) {
        this.disposable = disposable;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void suspend() {

    }

    public void restore() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public boolean isDisposable() {
        return disposable;
    }
}
