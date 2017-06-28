package ru.mikroacse.rolespell.app.view.settings;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Quint;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.actors.MeasurableActor;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.tween.ActorAccessor;
import ru.mikroacse.engine.util.GroupUtil;
import ru.mikroacse.rolespell.app.model.settings.SettingsAction;
import ru.mikroacse.rolespell.app.view.Renderer;
import ru.mikroacse.rolespell.app.view.settings.ui.LabeledSlider;
import ru.mikroacse.rolespell.app.view.settings.ui.SettingsButton;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.*;

/**
 * Created by Vitaly Rudenko on 06-Jun-17.
 */
public class SettingsRenderer extends Renderer {
    private Array<Actor> controls;

    private Image background;
    private SettingsButton backButton;
    private Group controlsContainer;

    private ActionListener actionListeners;

    public SettingsRenderer() {
        super();

        actionListeners = ListenerSupportFactory.create(ActionListener.class);

        Texture backgroundTexture = bundle(Bundle.SETTINGS).getTexture("background");
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = new Image(backgroundTexture);

        LabeledSlider musicSlider = createSlider("music_slider.label", SettingsAction.MUSIC);
        LabeledSlider soundsSlider = createSlider("sounds_slider.label", SettingsAction.SOUNDS);
        backButton = createButton("go_to_menu", SettingsAction.GO_TO_MENU);

        // TODO: universal listener
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actionListeners.onAction(SettingsAction.GO_TO_MENU);
            }
        });

        controls = new Array<>();
        controls.addAll(musicSlider, soundsSlider);
        controls.reverse();

        controlsContainer = new Group();
        controlsContainer.addActor(soundsSlider);
        controlsContainer.addActor(musicSlider);

        addActor(background);
        addActor(controlsContainer);
        addActor(backButton);
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.draw();
    }

    @Override
    public void update() {
        super.update();

        float scaleX = getWidth() / background.getWidth();
        float scaleY = getHeight() / background.getHeight();
        background.setScale(Math.max(scaleX, scaleY));

        background.setX((int) (getWidth() / 2 - background.getWidth() * background.getScaleX() / 2));
        background.setY((int) (getHeight() / 2 - background.getHeight() * background.getScaleY() / 2));

        float y = 0;
        for (int i = 0; i < controls.size; i++) {
            Actor actor = controls.get(i);

            if (actor instanceof MeasurableActor) {
                float width = ((MeasurableActor) actor).getRealWidth();
                float height = ((MeasurableActor) actor).getRealHeight();

                actor.setX((int) (-width));
                actor.setY((int) y);

                // TODO: magic number (offset between controls)
                y += height + 20;
            }
        }

        controlsContainer.setX((int) (getWidth() / 2 + GroupUtil.getWidth(controlsContainer) / 2));
        controlsContainer.setY((int) (getHeight() / 2 - y / 2));

        backButton.setX((int) (getWidth() / 2 - backButton.getRealWidth() / 2));
        // TODO: magic number (offset between controls and back button)
        backButton.setY((int) (controlsContainer.getY() - backButton.getRealHeight() - 20));
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

        Tween.to(controlsContainer, ActorAccessor.Y, 1f)
                .target(controlsContainer.getY())
                .ease(Quint.OUT)
                .start(tweens());

        Tween.to(backButton, ActorAccessor.Y, 1f)
                .target(backButton.getY())
                .setCallback((type, source) -> {
                    if (type == TweenCallback.COMPLETE) {
                        setBusy(false);
                        listeners.onShown();
                    }
                })
                .delay(0.15f)
                .ease(Quint.OUT)
                .start(tweens());

        background.getColor().a = 0f;
        controlsContainer.setY(-GroupUtil.getHeight(controlsContainer));
        backButton.setY(-backButton.getRealHeight());

        setBusy(true);
    }

    @Override
    public void hide() {
        super.hide();

        Tween.to(controlsContainer, ActorAccessor.Y, 0.4f)
                .target(getHeight())
                .ease(Quint.IN)
                .start(tweens());

        Tween.to(backButton, ActorAccessor.Y, 0.4f)
                .target(getHeight())
                .ease(Quint.IN)
                .delay(0.1f)
                .start(tweens());

        Tween.to(background, ActorAccessor.ALPHA, 0.4f)
                .target(0f)
                .delay(0.3f)
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

    private SettingsButton createButton(String labelKey, SettingsAction action) {
        return new SettingsButton(lang().get(Bundle.SETTINGS, labelKey), action);
    }

    private LabeledSlider createSlider(String labelKey, SettingsAction action) {
        // TODO: magic numbers
        LabeledSlider slider = new LabeledSlider();
        slider.setLabel(lang().get(Bundle.SETTINGS, labelKey));

        slider.setWidth(300f);
        slider.setHeight(40f);

        return slider;
    }

    public interface ActionListener extends ru.mikroacse.engine.listeners.Listener {
        void onAction(SettingsAction action);
    }
}
