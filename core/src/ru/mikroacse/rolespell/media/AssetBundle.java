package ru.mikroacse.rolespell.media;

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
    public <T> void loadAsset(String key, String path, Class<T> assetClass) {
        if (assetClass == BitmapFont.class) {
            loadAsset(key, path, BitmapFont.class, fontParameter);
            return;
        }

        if (assetClass == Texture.class) {
            loadAsset(key, path, Texture.class, textureParameter);
            return;
        }

        super.loadAsset(key, path, assetClass);
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
