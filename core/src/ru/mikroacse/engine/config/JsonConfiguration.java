package ru.mikroacse.engine.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.engine.util.JsonLoader;

/**
 * Created by MikroAcse on 08.07.2016.
 */
@Deprecated
public class JsonConfiguration {
    private JsonValue json;

    public JsonConfiguration() {
        json = new JsonValue(JsonValue.ValueType.object);
    }

    public JsonConfiguration(JsonValue json) {
        this.json = json;
    }

    public JsonConfiguration(FileHandle fileHandle) {
        json = JsonLoader.load(fileHandle);
    }

    public JsonConfiguration(String path) {
        this(Gdx.files.internal(path));
    }

    public String getString(String key, String defaultValue) {
        JsonValue node = getNode(key);
        if (node == null) {
            return defaultValue;
        }
        return node.asString();
    }

    public String getString(String key) {
        return getNode(key).asString();
    }

    public void setString(String key, String value) {
        getNode(key).set(value);
    }

    public int getInt(String key, int defaultValue) {
        JsonValue node = getNode(key);
        if (node == null) {
            return defaultValue;
        }
        return node.asInt();
    }

    public int getInt(String key) {
        return getNode(key).asInt();
    }

    public void setInt(String key, int value) {
        getNode(key).set(value, String.valueOf(value));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        JsonValue node = getNode(key);
        if (node == null) {
            return defaultValue;
        }
        return node.asBoolean();
    }

    public boolean getBoolean(String key) {
        return getNode(key).asBoolean();
    }

    public void setBoolean(String key, boolean value) {
        getNode(key).set(value);
    }

    public float getFloat(String key, float defaultValue) {
        JsonValue node = getNode(key);
        if (node == null) {
            return defaultValue;
        }
        return node.asFloat();
    }

    public float getFloat(String key) {
        return getNode(key).asFloat();
    }

    public void setFloat(String key, float value) {
        getNode(key).set(value, String.valueOf(value));
    }

    public double getDouble(String key, double defaultValue) {
        JsonValue node = getNode(key);
        if (node == null) {
            return defaultValue;
        }
        return node.asDouble();
    }

    public double getDouble(String key) {
        return getNode(key).asDouble();
    }

    public void setDouble(String key, double value) {
        getNode(key).set(value, String.valueOf(value));
    }

    public int getColor(String key, int defaultValue) {
        String value = getString(key);
        if (value == null) {
            return defaultValue;
        }
        value = value.substring(2);
        return Integer.parseInt(value, 16);
    }

    public int getColor(String key) {
        return getColor(key, 0);
    }

    /**
     * Parses node path (i.e. node1.subnode2.somevalue) and returns the node.
     */
    public JsonValue getNode(String nodePath) {
        String[] keys = nodePath.split("\\.");

        JsonValue node = json;

        for (String key : keys) {
            if (node == null) {
                return null;
            }
            node = node.get(key);
        }

        return node;
    }

    public boolean has(String key) {
        return getNode(key) != null;
    }

    /**
     * Parses node path (i.e. node1.subnode2.somevalue) and returns the node as new Configuration instance.
     */

    public JsonConfiguration extractNode(String nodePath) {
        JsonValue node = getNode(nodePath);

        if (node != null) {
            return new JsonConfiguration(node);
        }

        return null;
    }

    public JsonValue json() {
        return json;
    }
}