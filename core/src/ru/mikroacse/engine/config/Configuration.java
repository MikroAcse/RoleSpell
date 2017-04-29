package ru.mikroacse.engine.config;

import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.engine.utils.JSONLoader;

/**
 * Created by MikroAcse on 08.07.2016.
 */
public class Configuration {
    protected JsonValue config;
    
    public Configuration() {
    
    }
    
    public Configuration(JsonValue config) {
        this.config = config;
    }
    
    public Configuration(String path) {
        load(path);
    }
    
    public void load(String path) {
        config = JSONLoader.load(path);
    }
    
    public String getString(String key, String defaultValue) {
        JsonValue node = getNode(key);
        if (node == null) {
            return defaultValue;
        }
        return node.asString();
    }
    
    public String getString(String key) {
        return getString(key, null);
    }
    
    public int getInt(String key, int defaultValue) {
        JsonValue node = getNode(key);
        if (node == null) {
            return defaultValue;
        }
        return node.asInt();
    }
    
    public int getInt(String key) {
        return getInt(key, 0);
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }
    
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    
    public float getFloat(String key, float defaultValue) {
        return config.getFloat(key, defaultValue);
    }
    
    public float getFloat(String key) {
        return getFloat(key, 0);
    }
    
    public int getColor(String key, int defaultValue) {
        String value = getString(key, null);
        if (value == null) {
            return defaultValue;
        }
        value = value.substring(2);
        return Integer.parseInt(value, 16);
    }
    
    public int getColor(String key) {
        return getColor(key, 0);
    }
    
    protected JsonValue getNode(String nodePath) {
        String[] keys = nodePath.split("\\.");
        
        JsonValue node = config;
        
        for (String key : keys) {
            if (node == null) {
                return null;
            }
            node = node.get(key);
        }
        
        return node;
    }
}
