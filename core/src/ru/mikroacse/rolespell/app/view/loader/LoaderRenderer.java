package ru.mikroacse.rolespell.app.view.loader;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import ru.mikroacse.engine.tween.ActorAccessor;
import ru.mikroacse.rolespell.app.view.Renderer;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.getAssetManager;
import static ru.mikroacse.rolespell.RoleSpell.getTweenManager;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class LoaderRenderer extends Renderer {
    private Image background;
    private Image loaderCircle;

    public LoaderRenderer() {
        super();

        AssetBundle bundle = getAssetManager().getBundle(Bundle.LOADER);

        Texture backgroundTexture = bundle.getTexture("background");
        Texture loaderCircleTexture = bundle.getTexture("loader-circle");

        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        loaderCircleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = new Image(backgroundTexture);
        loaderCircle = new Image(loaderCircleTexture);

        loaderCircle.setOrigin(Align.center);

        addActor(background);
        addActor(loaderCircle);
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.draw();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        loaderCircle.rotateBy(-270 * delta);

        while (loaderCircle.getRotation() < -360) {
            loaderCircle.rotateBy(360);
        }
    }

    public void update() {
        loaderCircle.setX((int) (getWidth() / 2 - loaderCircle.getWidth() / 2));
        loaderCircle.setY((int) (getHeight() / 2 - loaderCircle.getHeight() / 2));

        loaderCircle.getColor().a = 0.5f;

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
                .start(getTweenManager());

        Tween.to(loaderCircle, ActorAccessor.ALPHA, 0.5f)
                .target(loaderCircle.getColor().a)
                .delay(0.25f)
                .setCallback((type, source) -> {
                    if (type == TweenCallback.COMPLETE) {
                        setBusy(false);
                        listeners.onShown();
                    }
                })
                .start(getTweenManager());

        background.getColor().a = 0f;
        loaderCircle.getColor().a = 0f;

        setBusy(true);
    }

    @Override
    public void hide() {
        super.hide();

        if (isBusy()) {
            return;
        }

        Tween.to(background, ActorAccessor.ALPHA, 0.25f)
                .target(0f)
                .start(getTweenManager());

        Tween.to(loaderCircle, ActorAccessor.ALPHA, 0.25f)
                .target(0f)
                .setCallback((type, source) -> {
                    if (type == TweenCallback.COMPLETE) {
                        setBusy(false);
                        listeners.onHidden();
                    }
                })
                .start(getTweenManager());

        setBusy(true);
    }
}
