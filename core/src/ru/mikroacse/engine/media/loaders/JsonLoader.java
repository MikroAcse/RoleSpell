package ru.mikroacse.engine.media.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Vitaly Rudenko on 17-May-17.
 */
public class JsonLoader extends AsynchronousAssetLoader<JsonValue, JsonLoader.JsonParameter> {
    private JsonReader jsonReader;
    private JsonValue jsonValue;

    public JsonLoader(FileHandleResolver resolver) {
        super(resolver);

        jsonReader = new JsonReader();
    }

    @Override
    public void loadAsync(AssetManager assetManager, String fileName, FileHandle fileHandle, JsonParameter jsonParameter) {
        jsonValue = jsonReader.parse(fileHandle);
    }

    @Override
    public JsonValue loadSync(AssetManager assetManager, String fileName, FileHandle fileHandle, JsonParameter jsonParameter) {
        return jsonValue;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle fileHandle, JsonParameter jsonParameter) {
        return null;
    }

    class JsonParameter extends AssetLoaderParameters<JsonValue> {

    }
}
