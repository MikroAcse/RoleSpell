package ru.mikroacse.rolespell.app.view.loader;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Elastic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.mikroacse.engine.actors.AnimationActor;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.tween.ActorAccessor;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.util.AnimationUtil;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class LoaderRenderer extends Stage {
    private AnimationActor animation;

    private Listener listeners;

    private boolean busy;

    public LoaderRenderer() {
        super(new ScreenViewport());

        listeners = ListenerSupportFactory.create(Listener.class);

        animation = new AnimationActor(
                AnimationUtil.create(
                        AssetManager.Bundle.LOADER,
                        "loader",
                        false));

        // 20 frames per second TODO: magic number
        animation.setFrameDuration(1 / 20f);
        animation.setRepeatable(true);

        addActor(animation);

        busy = false;
    }

    public void addListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).addListener(listener);
    }

    public void removeListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<Listener>) listeners).clearListeners();
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.draw();
    }

    public void update() {
        animation.setScale(Math.round(getAnimationScale()));

        animation.setX((int) (getWidth() - animation.getRealWidth()) / 2);
        animation.setY((int) (getHeight() - animation.getRealHeight()) / 2);
    }

    public void show() {
        RoleSpell.getTweenManager().killTarget(animation);

        update();

        animation.getColor().a = 1f;

        Tween.from(animation, ActorAccessor.POSITION, 1.0f)
                .ease(Elastic.OUT)
                .target(getWidth() / 2, getHeight() / 2)
                .start(RoleSpell.getTweenManager());

        Tween.from(animation, ActorAccessor.ALPHA, 0.5f)
                .ease(Elastic.OUT)
                .target(0f)
                .start(RoleSpell.getTweenManager());

        Tween.from(animation, ActorAccessor.SCALE, 1.0f)
                .ease(Elastic.OUT)
                .target(0f)
                .setCallback((type, source) -> {
                    if (type == TweenCallback.COMPLETE) {
                        busy = false;
                    }
                })
                .start(RoleSpell.getTweenManager());

        busy = true;
    }

    public void hide() {
        RoleSpell.getTweenManager().killTarget(animation);

        Tween.to(animation, ActorAccessor.POSITION, 0.5f)
                .ease(Elastic.IN)
                .target(getWidth() / 2, getHeight() / 2)
                .start(RoleSpell.getTweenManager());

        Tween.to(animation, ActorAccessor.ALPHA, 0.5f)
                .ease(Elastic.IN)
                .target(0f)
                .start(RoleSpell.getTweenManager());

        Tween.to(animation, ActorAccessor.SCALE, 0.5f)
                .ease(Elastic.IN)
                .target(0f)
                .setCallback((type, source) -> {
                    if (type == TweenCallback.COMPLETE) {
                        busy = false;
                        listeners.onHidden();
                    }
                })
                .start(RoleSpell.getTweenManager());

        busy = true;
    }

    private float getAnimationScale() {
        return 8f * RoleSpell.getAssetManager().getScale();
    }

    public boolean isBusy() {
        return busy;
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void onHidden();
    }
}
