package ru.mikroacse.rolespell.config;

import com.badlogic.gdx.Gdx;
import ru.mikroacse.engine.config.Language;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.media.Bundle;

/**
 * Created by MikroAcse on 29-Apr-17.
 */
public class Lang extends Language {
    public Lang() {
        Gdx.app.log("LOADING", "loading languages");

        load(AssetManager.ASSETS_DIRECTORY + "languages/bundle");
    }

    public String get(Bundle bundle, String key, Object... args) {
        return super.get(bundle.getName() + "." + key, args);
    }
}
