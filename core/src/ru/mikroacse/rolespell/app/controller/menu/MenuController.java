package ru.mikroacse.rolespell.app.controller.menu;

import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.menu.MenuAction;
import ru.mikroacse.rolespell.app.screens.ScreenManager.BundledScreen;
import ru.mikroacse.rolespell.app.view.RendererListener;
import ru.mikroacse.rolespell.app.view.menu.MenuRenderer;

import static ru.mikroacse.rolespell.RoleSpell.screens;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class MenuController {

    private MenuAction action;

    public MenuController(MenuRenderer renderer) {
        MenuRenderer renderer1 = renderer;

        RendererListener rendererListener = new RendererListener() {
            @Override
            public void onHidden() {
                if (action == null) {
                    System.err.println("null action at menu");
                    return;
                }

                switch (action) {
                    case NEW_GAME:
                        screens().setScreen(BundledScreen.GAME);
                        break;
                    case SETTINGS:
                        screens().setScreen(BundledScreen.SETTINGS);
                        break;
                    case EXIT:
                        RoleSpell.exit();
                        break;
                }

                action = null;
            }
        };

        MenuRenderer.ActionListener actionListener = action -> {
            MenuController.this.action = action;

            renderer.hide();
        };

        renderer.addListener(rendererListener);
        renderer.addListener(actionListener);
    }

    public void update(float delta) {

    }
}
