package ru.mikroacse.engine.media;

import ru.mikroacse.engine.util.IntVector2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 12.07.2016.
 */
public abstract class AssetBundleManager<K, B extends AssetBundle<?>> {
    private float scale;
    private IntVector2 initialSize;
    private Map<K, B> assets;

    public AssetBundleManager(int initialWidth, int initialHeight) {
        assets = new HashMap<>();
        initialSize = new IntVector2(initialWidth, initialHeight);
        scale = 1.0f;
    }

    public void updateScale(int width, int height) {
        scale = Math.min((float) width / initialSize.x, (float) height / initialSize.y);
    }

    public void addBundle(K key, B bundle) {
        assets.put(key, bundle);
    }

    public void loadBundle(K key) {
        loadBundle(key, false);
    }

    public abstract void loadBundle(K key, boolean sync);

    public void unloadBundle(K key) {
        B bundle = assets.remove(key);
        bundle.dispose();
    }

    public boolean isLoaded() {
        for (B sceneAssetBundle : assets.values()) {
            if (!sceneAssetBundle.isLoaded()) {
                return false;
            }
        }
        return true;
    }

    public boolean isLoaded(K key) {
        return assets.containsKey(key) && getBundle(key).isLoaded();
    }

    public void finishLoading(K key) {
        if (assets.containsKey(key)) {
            getBundle(key).finishLoading();
        }
    }

    public void finishLoading() {
        for (B sceneAssetBundle : assets.values()) {
            sceneAssetBundle.finishLoading();
        }
    }

    public B getBundle(K key) {
        return assets.get(key);
    }

    public float getScale() {
        return scale;
    }
}
