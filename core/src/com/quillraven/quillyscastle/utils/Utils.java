package com.quillraven.quillyscastle.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.quillraven.quillyscastle.QuillysCastleGame;
import com.quillraven.quillyscastle.QuillysCastleGame.ScreenType;

public final class Utils {
    public final static TextureAtlas CHARACTERS_TEXTURE_ATLAS = new TextureAtlas("characters/characters.atlas");
    public final static TextureAtlas ITEMS_TEXTURE_ATLAS = new TextureAtlas("items/items.atlas");
    public final static TextureAtlas UI_TEXTURE_ATLAS = new TextureAtlas("ui/uiskin.atlas");
    public final static Skin UI_SKIN = new Skin(Gdx.files.internal("ui/uiskin.json"), UI_TEXTURE_ATLAS);
    private final static String TAG = Utils.class.getSimpleName();
    private final static AssetManager assetManager = new AssetManager();
    private final static InternalFileHandleResolver fileResolver = new InternalFileHandleResolver();
    private final static Json json = new Json();

    public static void changeScreen(ScreenType screenType) {
        QuillysCastleGame game = (QuillysCastleGame) Gdx.app.getApplicationListener();

        game.setScreen(game.getScreen(screenType));
    }

    public static String toJson(Object object) {
        return json.toJson(object);
    }

    public static <T> T fromJson(Class<T> type, String jsonString) {
        return json.fromJson(type, jsonString);
    }

    public static <T> T fromJson(Class<T> type, FileHandle file) {
        return json.fromJson(type, file);
    }

    public static <T> T readJsonValue(Class<T> type, JsonValue value) {
        return json.readValue(type, value);
    }

    public static String prettyPrintJson(String jsonString) {
        return json.prettyPrint(jsonString);
    }

    private static <T, P extends AssetLoaderParameters<T>> void loadAsset(String filePath, Class<T> type, AssetLoader<T, P> loader) {
        if (filePath == null || filePath.trim().isEmpty()) {
            Gdx.app.debug(TAG, "Trying to load a " + type.getSimpleName() + " with empty filePath");
            return;
        }

        if (fileResolver.resolve(filePath).exists()) {
            assetManager.setLoader(type, loader);
            assetManager.load(filePath, type);

            assetManager.finishLoading();
            Gdx.app.debug(TAG, type.getSimpleName() + " loaded: " + filePath);
        } else {
            Gdx.app.debug(TAG, type.getSimpleName() + " does not exist: " + filePath);
        }
    }

    private static <T> T getAsset(String filePath, Class<T> type) {
        T result = null;

        if (assetManager.isLoaded(filePath)) {
            result = assetManager.get(filePath, type);
        } else {
            Gdx.app.debug(TAG, type.getSimpleName() + " is not loaded: " + filePath);
        }

        return result;
    }

    public static void loadMapAsset(String filePath) {
        loadAsset(filePath, TiledMap.class, new TmxMapLoader(fileResolver));
    }

    public static TiledMap getMapAsset(String filePath) {
        return getAsset(filePath, TiledMap.class);
    }

    public static void loadTextureAsset(String filePath) {
        loadAsset(filePath, Texture.class, new TextureLoader(fileResolver));
    }

    public static Texture getTextureAsset(String filePath) {
        return getAsset(filePath, Texture.class);
    }

    public static boolean isAssetLoaded(String filePath) {
        return assetManager.isLoaded(filePath);
    }

    public static void unloadAsset(String filePath) {
        if (isAssetLoaded(filePath)) {
            assetManager.unload(filePath);
            Gdx.app.debug(TAG, "Unloaded: " + filePath);
        }
    }

    public static int numberAssetsQueued() {
        return assetManager.getQueuedAssets();
    }

    public static boolean isAssetLoadCompleted() {
        return assetManager.getProgress() == 1;
    }

    public static boolean updatedAssetLoading() {
        return assetManager.update();
    }

    public static void dispose() {
        assetManager.dispose();
        UI_SKIN.dispose();
        UI_TEXTURE_ATLAS.dispose();
        CHARACTERS_TEXTURE_ATLAS.dispose();
        ITEMS_TEXTURE_ATLAS.dispose();
    }
}
