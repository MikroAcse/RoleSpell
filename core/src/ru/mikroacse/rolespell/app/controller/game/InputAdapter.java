package ru.mikroacse.rolespell.app.controller.game;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public class InputAdapter extends com.badlogic.gdx.InputAdapter {
    public static final int CLICK_TIMEOUT = 250;

    private Map<Integer, Button> buttons;

    public InputAdapter() {
        buttons = new HashMap<>();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        pressButton(button);

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        releaseButton(button);

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        pressButton(keycode);

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        releaseButton(keycode);

        return true;
    }

    public void update() {
        for (Map.Entry<Integer, Button> entry : buttons.entrySet()) {
            Button button = entry.getValue();

            button.justPressed = false;
            button.justReleased = false;

            if (System.currentTimeMillis() - button.lastTimePressed > CLICK_TIMEOUT) {
                button.taps = 0;
            }
        }
    }

    public Button getButton(int code) {
        if (!buttons.containsKey(code)) {
            buttons.put(code, new Button(code));
        }

        return buttons.get(code);
    }

    private void pressButton(int code) {
        Button button = getButton(code);

        button.taps++;
        button.isDown = true;
        button.justPressed = true;
        button.justReleased = false;
        button.lastTimePressed = System.currentTimeMillis();
    }

    private void releaseButton(int code) {
        Button button = getButton(code);

        button.isDown = false;
        button.justPressed = false;
        button.justReleased = true;
        button.lastTimeReleased = System.currentTimeMillis();
    }

    public int getMouseX() {
        return Gdx.input.getX();
    }

    public int getMouseY() {
        return Gdx.graphics.getHeight() - Gdx.input.getY();
    }

    public class Button {
        public int code;

        public int taps = 0;
        public long lastTimePressed = 0;
        public long lastTimeReleased = 0;

        public boolean justPressed = false;
        public boolean justReleased = false;
        public boolean isDown = false;

        public Button(int code) {
            this.code = code;
        }
    }
}
