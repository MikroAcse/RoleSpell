package ru.mikroacse.rolespell.app.view.menu;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import aurelienribon.tweenengine.equations.Quint;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.AbstractListener;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.tween.ActorAccessor;
import ru.mikroacse.engine.util.GroupUtil;
import ru.mikroacse.rolespell.app.model.menu.MenuAction;
import ru.mikroacse.rolespell.app.view.Renderer;
import ru.mikroacse.rolespell.app.view.menu.ui.MenuButton;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.*;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class MenuRenderer extends Renderer {
    private Image logo;

    private Image background;

    private Array<MenuButton> buttons;
    private Group buttonGroup;

    private ActionListener actionListeners;

    public MenuRenderer() {
        super();

        actionListeners = ListenerSupportFactory.create(ActionListener.class);

        logo = new Image(bundle(Bundle.MENU).getTexture("logo"));

        MenuButton newGameButton = createButton("new_game_button.label", MenuAction.NEW_GAME);
        MenuButton settingsButton = createButton("settings_button.label", MenuAction.SETTINGS);
        MenuButton exitButton = createButton("exit_button.label", MenuAction.EXIT);

        buttonGroup = new Group();

        buttonGroup.addActor(newGameButton);
        buttonGroup.addActor(settingsButton);
        buttonGroup.addActor(exitButton);

        buttons = new Array<>();

        buttons.addAll(newGameButton, settingsButton, exitButton);
        buttons.reverse();

        // TODO: universal listener
        for (MenuButton button : buttons) {
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    actionListeners.onAction(button.getAction());
                }
            });
        }

        Texture backgroundTexture = bundle(Bundle.MENU).getTexture("background");
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = new Image(backgroundTexture);

        addActor(background);
        addActor(logo);
        addActor(buttonGroup);
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.draw();
    }

    public void update() {
        int y = 0;

        for (MenuButton button : buttons) {
            if (y > 0) {
                // TODO: magic number (button offset)
                y += 15;
            }

            button.setX((int) (-button.getRealWidth() / 2));
            button.setY(y);

            y += button.getRealHeight();
        }

        logo.setX((int) (getWidth() / 2 - logo.getWidth() / 2));
        logo.setY((int) (getHeight() - logo.getHeight()));

        buttonGroup.setX((int) (getWidth() / 2));
        buttonGroup.setY((int) ((getHeight() * 3 / 4) / 2 - y / 2));

        float scaleX = getWidth() / background.getWidth();
        float scaleY = getHeight() / background.getHeight();
        background.setScale(Math.max(scaleX, scaleY));

        background.setX((int) (getWidth() / 2 - background.getWidth() * background.getScaleX() / 2));
        background.setY((int) (getHeight() / 2 - background.getHeight() * background.getScaleY() / 2));
    }

    @Override
    public void show() {
        super.show();

        if (isBusy()) {
            return;
        }

        update();

        Tween.to(background, ActorAccessor.ALPHA, 0.5f)
                .target(1f)
                .start(tweens());

        Tween.to(logo, ActorAccessor.Y, 1f)
                .target(logo.getY())
                .ease(Expo.OUT)
                .start(tweens());

        Tween.to(buttonGroup, ActorAccessor.Y, 1f)
                .target(buttonGroup.getY())
                .setCallback((type, source) -> {
                    if (type == TweenCallback.COMPLETE) {
                        setBusy(false);
                        listeners.onShown();
                    }
                })
                .delay(0.2f)
                .ease(Expo.OUT)
                .start(tweens());

        logo.setY(-logo.getHeight());

        background.getColor().a = 0f;

        buttonGroup.setY(-GroupUtil.getHeight(buttonGroup));

        setBusy(true);
    }

    @Override
    public void hide() {
        super.hide();

        if (isBusy()) {
            return;
        }

        Tween.to(logo, ActorAccessor.Y, 0.4f)
                .target(getHeight())
                .ease(Quint.IN)
                .start(tweens());

        Tween.to(buttonGroup, ActorAccessor.Y, 0.4f)
                .target(getHeight())
                .delay(0.1f)
                .ease(Quint.IN)
                .start(tweens());

        Tween.to(background, ActorAccessor.ALPHA, 0.4f)
                .target(0f)
                .delay(0.2f)
                .setCallback((type, source) -> {
                    if (type == TweenCallback.COMPLETE) {
                        setBusy(false);
                        listeners.onHidden();
                    }
                })
                .start(tweens());

        setBusy(true);
    }

    public void addListener(ActionListener listener) {
        ((ListenerSupport<ActionListener>) actionListeners).addListener(listener);
    }

    public void removeListener(ActionListener listener) {
        ((ListenerSupport<ActionListener>) actionListeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<ActionListener>) actionListeners).clearListeners();
    }

    private MenuButton createButton(String labelKey, MenuAction action) {
        return new MenuButton(lang().get(Bundle.MENU, labelKey), action);
    }

    public Array<MenuButton> getButtons() {
        return buttons;
    }

    public interface ActionListener extends AbstractListener {
        void onAction(MenuAction action);
    }
}
