package ru.mikroacse.rolespell.app.controller.settings;

import com.badlogic.gdx.Input;
import ru.mikroacse.rolespell.app.controller.Controller;
import ru.mikroacse.rolespell.app.controller.shared.InputAdapter;
import ru.mikroacse.rolespell.app.model.settings.SettingsAction;
import ru.mikroacse.rolespell.app.screens.ScreenManager;
import ru.mikroacse.rolespell.app.screens.ScreenManager.BundledScreen;
import ru.mikroacse.rolespell.app.view.RendererListener;
import ru.mikroacse.rolespell.app.view.settings.SettingsRenderer;

import static ru.mikroacse.rolespell.RoleSpell.screens;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class SettingsController extends Controller {
    private SettingsRenderer renderer;

    private SettingsAction action;

    public SettingsController(SettingsRenderer renderer) {
        this.renderer = renderer;

        RendererListener rendererListener = new RendererListener() {
            @Override
            public void onHidden() {
                if (action == null) {
                    System.err.println("null action at settings");
                    return;
                }

                switch (action) {
                    case GO_TO_MENU:
                        screens().setScreen(BundledScreen.MENU);
                        break;
                }

                action = null;
            }
        };

        SettingsRenderer.ActionListener actionListener = action -> {
            SettingsController.this.action = action;

            renderer.hide();
        };

        renderer.addListener(rendererListener);
        renderer.addListener(actionListener);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        InputAdapter input = InputAdapter.getInstance();

        // logic
        if (input.getButton(Input.Keys.ESCAPE).justReleased) {
            action = SettingsAction.GO_TO_MENU;

            renderer.hide();
        }

        input.update();
    }
}
