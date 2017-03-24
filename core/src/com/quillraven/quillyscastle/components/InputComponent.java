package com.quillraven.quillyscastle.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.QuillysCastleGame.ScreenType;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.map.Map;
import com.quillraven.quillyscastle.utils.Utils;

public abstract class InputComponent extends ComponentSubject implements Component, InputProcessor {
    private static final String TAG = InputComponent.class.getSimpleName();
    protected final ObjectMap<Keys, Boolean> keyPressedMap;
    protected Keys lastPressed;
    protected Vector3 mouseLocation;
    protected boolean inputChanged;
    public InputComponent() {
        this.keyPressedMap = new ObjectMap<Keys, Boolean>();
        for (Keys k : Keys.values()) {
            keyPressedMap.put(k, false);
        }
        mouseLocation = new Vector3();
        inputChanged = false;
        lastPressed = null;
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "disposed!");
    }

    @Override
    public void update(Entity entity, GameWorld world, Map map, Camera camera, float deltaTime) {
        if (inputChanged) {
            processInput(entity, world, map, camera);
            inputChanged = false;
        }
    }

    protected abstract void processInput(Entity entity, GameWorld world, Map map, Camera camera);

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                lastPressed = Keys.LEFT;
                keyPressedMap.put(Keys.LEFT, true);
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                lastPressed = Keys.RIGHT;
                keyPressedMap.put(Keys.RIGHT, true);
                break;
            case Input.Keys.UP:
            case Input.Keys.W:
                lastPressed = Keys.UP;
                keyPressedMap.put(Keys.UP, true);
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                lastPressed = Keys.DOWN;
                keyPressedMap.put(Keys.DOWN, true);
                break;
            case Input.Keys.ESCAPE:
                Utils.changeScreen(ScreenType.MAIN_MENU);
                break;
            case Input.Keys.Q:
                Gdx.app.exit();
                break;
        }

        inputChanged = true;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                keyPressedMap.put(Keys.LEFT, false);
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                keyPressedMap.put(Keys.RIGHT, false);
                break;
            case Input.Keys.UP:
            case Input.Keys.W:
                keyPressedMap.put(Keys.UP, false);
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                keyPressedMap.put(Keys.DOWN, false);
                break;
        }

        inputChanged = true;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Buttons.LEFT == button) {
            keyPressedMap.put(Keys.SELECT, true);
        } else {
            keyPressedMap.put(Keys.DESELECT, true);
        }
        mouseLocation.set(screenX, screenY, 0);

        inputChanged = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (Buttons.LEFT == button) {
            keyPressedMap.put(Keys.SELECT, false);
        } else {
            keyPressedMap.put(Keys.DESELECT, false);
        }
        mouseLocation.set(screenX, screenY, 0);

        inputChanged = true;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    protected enum Keys {
        LEFT,
        DOWN,
        RIGHT,
        UP,
        SELECT,
        DESELECT
    }
}
