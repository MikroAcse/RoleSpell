package ru.mikroacse.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by MikroAcse on 08.07.2016.
 */
@Deprecated
public class JsonLoader {
    /**
     * Loads json file and returns parsed JsonValue.
     */
    public static JsonValue load(FileHandle fileHandle) {
        String data = fileHandle.readString();
        return new JsonReader().parse(data);
    }

    /**
     * Loads json file and returns parsed JsonValue.
     */
    public static JsonValue load(String path) {
        return load(Gdx.files.internal(path));
    }
}
