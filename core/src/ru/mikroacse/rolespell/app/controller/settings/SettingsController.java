package ru.mikroacse.rolespell.app.controller.settings;

import com.badlogic.gdx.Input;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.controller.Controller;
import ru.mikroacse.rolespell.app.controller.shared.InputAdapter;
import ru.mikroacse.rolespell.app.model.settings.SettingsAction;
import ru.mikroacse.rolespell.app.screens.ScreenManager.BundledScreen;
import ru.mikroacse.rolespell.app.view.RendererListener;
import ru.mikroacse.rolespell.app.view.settings.SettingsRenderer;

import static ru.mikroacse.rolespell.RoleSpell.getScreenManager;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class SettingsController extends Controller {
    private SettingsRenderer renderer;

    private RendererListener rendererListener;

    private SettingsRenderer.ActionListener actionListener;

    private SettingsAction action;

    public SettingsController(SettingsRenderer renderer) {
        this.renderer = renderer;

        rendererListener = new RendererListener() {
            @Override
            public void onHidden() {
                if(action == null) {
                    System.err.println("null action at settings");
                    return;
                }

                switch (action) {
                    case GO_TO_MENU:
                        getScreenManager().setScreen(BundledScreen.MENU);
                        break;
                }

                action = null;
            }
        };

        actionListener = new SettingsRenderer.ActionListener() {
            @Override
            public void onAction(SettingsAction action) {
                SettingsController.this.action = action;

                renderer.hide();
            }
        };

        renderer.addListener(rendererListener);
        renderer.addListener(actionListener);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        InputAdapter input = InputAdapter.getInstance();

        // logic
        if(input.getButton(Input.Keys.ESCAPE).justReleased) {
            action = SettingsAction.GO_TO_MENU;

            renderer.hide();
        }

        input.update();
    }
}
