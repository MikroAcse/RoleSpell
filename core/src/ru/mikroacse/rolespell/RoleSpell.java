package ru.mikroacse.rolespell;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.mikroacse.engine.tween.ActorAccessor;
import ru.mikroacse.engine.tween.TweenManager;
import ru.mikroacse.rolespell.app.screens.ScreenManager;
import ru.mikroacse.rolespell.config.Config;
import ru.mikroacse.rolespell.config.Lang;
import ru.mikroacse.rolespell.media.AssetManager;

public class RoleSpell extends Game {
    private static TweenManager tweenManager;
    private static AssetManager assetManager;
    private static ScreenManager screenManager;
    private static Config config;
    private static Lang lang;

    public static TweenManager getTweenManager() {
        return tweenManager;
    }

    public static AssetManager getAssetManager() {
        return assetManager;
    }

    public static ScreenManager getScreenManager() {
        return screenManager;
    }

    public static Config getConfig() {
        return config;
    }

    public static Lang getLang() {
        return lang;
    }

    public static void showMouse() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

    public static void hideMouse() {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(new Color(1, 1, 1, 0));
        pm.fill();

        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));

        pm.dispose();
    }

    public static void exit() {
        Gdx.app.exit();
    }

    @Override
    public void create() {
        Tween.registerAccessor(Actor.class, new ActorAccessor());
        Tween.setCombinedAttributesLimit(4);

        tweenManager = new TweenManager();

        config = new Config();

        lang = new Lang();

        // TODO: magic numbers (initial screen size)
        assetManager = new AssetManager(1280, 720);

        screenManager = new ScreenManager(this);

        assetManager.loadBundle(AssetManager.Bundle.GLOBAL);
        assetManager.finishLoading();

        assetManager.loadBundle(AssetManager.Bundle.SETTINGS);

        screenManager.setScreen(ScreenManager.BundledScreen.GAME);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();

        tweenManager.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        if (assetManager != null) {
            assetManager.updateScale(width, height);
        }

        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();

        screenManager.dispose();
        assetManager.unloadBundle(AssetManager.Bundle.GLOBAL);
    }
}
