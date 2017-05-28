package ru.mikroacse.rolespell.config;

import ru.mikroacse.engine.config.Configuration;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 29-Apr-17.
 */
public final class Config extends Configuration {
    public Config() {
        super(AssetManager.ASSETS_DIRECTORY + "config.json");
    }
}
