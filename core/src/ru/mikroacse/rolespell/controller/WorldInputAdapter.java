package ru.mikroacse.rolespell.controller;

import com.badlogic.gdx.InputAdapter;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public class WorldInputAdapter extends InputAdapter {
    private boolean isJustTouched;
    private int mouseX;
    private int mouseY;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isJustTouched = true;

        mouseX = screenX;
        mouseY = screenY;
        return true;
    }

    public void update() {
        isJustTouched = false;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isJustTouched() {
        return isJustTouched;
    }
}
