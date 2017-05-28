package ru.mikroacse.rolespell.media;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by MikroAcse on 29-Apr-17.
 */
public class AssetBundle extends ru.mikroacse.engine.media.AssetBundle<String> {
    private TextureParameter textureParameter;
    private BitmapFontParameter fontParameter;

    public AssetBundle() {
        super();

        getManager().setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        textureParameter = new TextureParameter();
        textureParameter.genMipMaps = false;
        textureParameter.minFilter = Texture.TextureFilter.Linear;
        textureParameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;

        fontParameter = new BitmapFontParameter();
        fontParameter.flip = false;
        fontParameter.genMipMaps = true;
        fontParameter.minFilter = Texture.TextureFilter.MipMapLinearNearest;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
    }

    @Override
    public <T> void loadAsset(String key, String path, Class<T> assetClass, AssetLoaderParameters<T> parameter) {
        String assetKey = key + assetClass.getSimpleName();

        if (parameter == null) {
            if (assetClass == BitmapFont.class) {
                super.loadAsset(assetKey, path, BitmapFont.class, fontParameter);
                return;
            }

            if (assetClass == Texture.class) {
                super.loadAsset(assetKey, path, Texture.class, textureParameter);
                return;
            }
        }

        super.loadAsset(assetKey, path, assetClass, parameter);
    }

    @Override
    public <T> void loadAsset(String key, String path, Class<T> assetClass) {
        loadAsset(key, path, assetClass, null);
    }

    @Override
    public <T> T getAsset(String key, Class<T> assetClass) {
        String assetKey = key + assetClass.getSimpleName();

        return super.getAsset(assetKey, assetClass);
    }

    public JsonValue getConfig(String name) {
        return getAsset(name, JsonValue.class);
    }

    public Sound getSound(String name) {
        return getAsset(name, Sound.class);
    }

    public Music getMusic(String name) {
        return getAsset(name, Music.class);
    }

    public Texture getTexture(String name) {
        return getAsset(name, Texture.class);
    }

    public TextureAtlas getAtlas(String name) {
        return getAsset(name, TextureAtlas.class);
    }

    public TiledMap getMap(String name) {
        return getAsset(name, TiledMap.class);
    }

    public BitmapFont getFont(String name) {
        return getAsset(name, BitmapFont.class);
    }

    public TextureParameter getTextureParameter() {
        return textureParameter;
    }

    public BitmapFontParameter getFontParameter() {
        return fontParameter;
    }
}
