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
import ru.mikroacse.rolespell.config.AppConfig;
import ru.mikroacse.rolespell.config.AppLang;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.media.Bundle;

import java.io.FileNotFoundException;

public class RoleSpell extends Game {
    private static TweenManager tweenManager;
    private static AssetManager assetManager;
    private static ScreenManager screenManager;
    private static AppConfig config;
    private static AppLang lang;

    public static TweenManager tweens() {
        return tweenManager;
    }

    public static AssetManager assets() {
        return assetManager;
    }

    public static AssetBundle bundle(Bundle bundle) {
        return assetManager.getBundle(bundle);
    }

    public static ScreenManager screens() {
        return screenManager;
    }

    public static AppConfig config() {
        return config;
    }

    public static AppLang lang() {
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

        try {
            config = new AppConfig();
        } catch (FileNotFoundException e) {
            System.err.println("App config not found!");
        }

        lang = new AppLang();

        // TODO: magic numbers (initial screen size)
        assetManager = new AssetManager(1280, 720);

        screenManager = new ScreenManager(this);

        assetManager.loadBundle(Bundle.GLOBAL);
        assetManager.finishLoading();

        assetManager.loadBundle(Bundle.SETTINGS);

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
        assetManager.unloadBundle(Bundle.GLOBAL);
    }
}
