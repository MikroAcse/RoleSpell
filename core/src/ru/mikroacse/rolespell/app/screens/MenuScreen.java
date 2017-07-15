package ru.mikroacse.rolespell.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.controller.menu.MenuController;
import ru.mikroacse.rolespell.app.controller.shared.InputAdapter;
import ru.mikroacse.rolespell.app.view.menu.MenuRenderer;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class MenuScreen extends Screen {
    private MenuRenderer renderer;
    private MenuController controller;

    private InputMultiplexer input;

    public MenuScreen() {
        super(false);

        renderer = new MenuRenderer();

        controller = new MenuController(renderer);

        input = new InputMultiplexer();
        input.addProcessor(renderer);
        input.addProcessor(InputAdapter.instance);
    }

    @Override
    public void restore() {
        renderer.show();

        RoleSpell.showMouse();

        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void suspend() {
        renderer.hide();

        Gdx.input.setInputProcessor(null);
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
        super.resize(width, height);

        renderer.resize(width, height);
    }
}
