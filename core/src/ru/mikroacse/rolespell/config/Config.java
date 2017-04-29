package ru.mikroacse.rolespell.config;

import com.badlogic.gdx.Gdx;
import ru.mikroacse.engine.config.Configuration;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 29-Apr-17.
 */
public class Config extends Configuration {
    public Config() {
        Gdx.app.log("LOADING", "loading config");
        
        load(AssetManager.ASSETS_DIRECTORY + "config.json");
    }
    
    public String getFont(AssetManager.Bundle bundle, String key) {
        key = String.format("bundles.%s.fonts.%s", bundle.getName(), key);
        
        return getString(key);
    }
    
    public int getAnimationFps() {
        return getInt("animationFps");
    }
    
    public float getAnimationFrameDuration() {
        return 1f / getAnimationFps();
    }
}
