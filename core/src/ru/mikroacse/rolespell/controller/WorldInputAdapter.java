package ru.mikroacse.rolespell.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public class WorldInputAdapter extends InputAdapter {
    private boolean isJustTouched;
    private int touchMouseX;
    private int touchMouseY;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isJustTouched = true;

        touchMouseX = screenX;
        touchMouseY = screenY;
        return true;
    }

    public void update() {
        isJustTouched = false;
    }

    public int getMouseX() {
        return Gdx.input.getX();
    }

    public int getMouseY() {
        return Gdx.input.getY();
    }

    public int getTouchMouseX() {
        return touchMouseX;
    }

    public int getTouchMouseY() {
        return touchMouseY;
    }

    public boolean isJustTouched() {
        return isJustTouched;
    }
}
