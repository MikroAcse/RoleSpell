package ru.mikroacse.rolespell;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.mikroacse.engine.tween.ActorAccessor;
import ru.mikroacse.engine.tween.TweenManager;
import ru.mikroacse.rolespell.config.Config;
import ru.mikroacse.rolespell.config.Lang;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.app.screens.ScreenManager;

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
        
        assetManager.loadBundle(AssetManager.Bundle.LOADER, true);
        
        assetManager.loadBundle(AssetManager.Bundle.GLOBAL);
        assetManager.loadBundle(AssetManager.Bundle.MENU);
        assetManager.loadBundle(AssetManager.Bundle.GAME);
        assetManager.finishLoading();
        
        screenManager.setWaitScene(ScreenManager.BundledScreen.GAME);
        screenManager.setScreen(ScreenManager.BundledScreen.LOADER);
    }
    
    @Override
    public void render() {
        super.render();
        tweenManager.update(Gdx.graphics.getDeltaTime());
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        screenManager.dispose();
        assetManager.unloadBundle(AssetManager.Bundle.GLOBAL);
    }
}
