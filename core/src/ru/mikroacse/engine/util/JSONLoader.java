package ru.mikroacse.engine.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import static com.badlogic.gdx.Gdx.files;

/**
 * Created by MikroAcse on 08.07.2016.
 */
public class JSONLoader {

    /**
     * Loads json file and returns parsed JsonValue.
     */
    public static JsonValue load(String filename) {
        FileHandle fileHandle = files.internal(filename);
        String data = fileHandle.readString();
        return new JsonReader().parse(data);
    }
}
