package ru.mikroacse.rolespell.app.controller.menu;

import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.menu.MenuAction;
import ru.mikroacse.rolespell.app.screens.ScreenManager.BundledScreen;
import ru.mikroacse.rolespell.app.view.RendererListener;
import ru.mikroacse.rolespell.app.view.menu.MenuRenderer;

import static ru.mikroacse.rolespell.RoleSpell.getScreenManager;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class MenuController {
    private MenuRenderer renderer;

    private RendererListener rendererListener;
    private MenuRenderer.ActionListener actionListener;

    private MenuAction action;

    public MenuController(MenuRenderer renderer) {
        this.renderer = renderer;

        rendererListener = new RendererListener() {
            @Override
            public void onHidden() {
                if(action == null) {
                    System.err.println("null action at menu");
                    return;
                }

                switch (action) {
                    case NEW_GAME:
                        getScreenManager().setScreen(BundledScreen.GAME);
                        break;
                    case SETTINGS:
                        getScreenManager().setScreen(BundledScreen.SETTINGS);
                        break;
                    case EXIT:
                        RoleSpell.exit();
                        break;
                }

                action = null;
            }
        };

        actionListener = new MenuRenderer.ActionListener() {
            @Override
            public void onAction(MenuAction action) {;
                MenuController.this.action = action;

                renderer.hide();
            }
        };

        renderer.addListener(rendererListener);
        renderer.addListener(actionListener);
    }

    public void update(float delta) {

    }
}
