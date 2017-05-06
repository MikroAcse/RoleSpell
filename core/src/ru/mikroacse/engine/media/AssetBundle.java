package ru.mikroacse.engine.media;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 29-Apr-17.
 */
public class AssetBundle<K> {
    private AssetManager manager;

    private Map<K, String> pathAliases;

    public AssetBundle() {
        manager = new AssetManager();
        manager.setLoader(ShaderProgram.class, new ShaderLoader(new InternalFileHandleResolver()));

        pathAliases = new HashMap<>();
    }

    public float getProgress() {
        return manager.getProgress();
    }

    public boolean isLoaded() {
        return manager.update();
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public <T> void loadAsset(K key, String path, Class<T> assetClass) {
        loadAsset(key, path, assetClass, null);
    }

    public <T> void loadAsset(K key, String path, Class<T> assetClass, AssetLoaderParameters<T> parameter) {
        manager.load(path, assetClass, parameter);
        pathAliases.put(key, path);
    }

    public <T> T getAsset(K key, Class<T> assetClass) {
        return manager.get(pathAliases.get(key), assetClass);
    }

    public <T extends Disposable> void unloadAssets(Class<T> assetClass) {
        Array<T> assets = new Array<>();
        manager.getAll(assetClass, assets);

        for (T asset : assets) {
            asset.dispose();
        }
    }

    public void dispose() {
        manager.dispose();
        pathAliases.clear();
    }

    public AssetManager getManager() {
        return manager;
    }
}