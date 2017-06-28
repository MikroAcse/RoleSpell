package ru.mikroacse.engine.config;

import ru.mikroacse.engine.config.providers.ConfigurationProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by MikroAcse on 24.06.2017.
 */
public class ConfigurationNode {
    private Map<String, Object> map;

    public ConfigurationNode(ConfigurationProvider provider) {
        this.map = provider.get();
    }

    public ConfigurationNode(Map<String, Object> map) {
        this.map = map;
    }

    public <T> T get(String key, Class<T> tClass) {
        if (map.containsKey(key)) {
            return (T) map.get(key);
        }

        // Trying to get the value recursively (node1.node2.key)
        String[] nodes = key.split("\\.");
        key = nodes[nodes.length - 1];

        Map<String, Object> current = map;
        for (int i = 0; i < nodes.length - 1 /* last value is a key */; i++) {
            Object next = current.get(nodes[i]);

            if (next != null && next instanceof Map) {
                current = (Map) next;
            } else {
                throw new ClassCastException("Not a node: " + next + " (key: " + nodes[i] + ")");
            }
        }

        if (current.containsKey(key)) {
            return (T) current.get(key);
        }

        throw new NullPointerException("Node doesn't have such key: " + key);
    }

    public Object get(String key) {
        return get(key, Object.class);
    }

    public boolean has(String key) {
        try {
            get(key);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public <T> T getOrDefault(String key, Class<T> tClass, T defaultValue) {
        try {
            return get(key, tClass);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Object getOrDefault(String key, Object defaultValue) {
        return getOrDefault(key, Object.class, defaultValue);
    }

    public ConfigurationNode getNode(String key) {
        return new ConfigurationNode(get(key, Map.class));
    }

    public ConfigurationNode getNodeOrNull(String key) {
        if (has(key)) {
            return getNode(key);
        }

        return null;
    }

    public boolean getBoolean(String key) {
        return get(key, boolean.class);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (has(key)) {
            return getBoolean(key);
        }

        return defaultValue;
    }

    public String getString(String key) {
        return get(key, String.class);
    }

    public String getString(String key, String defaultValue) {
        return getOrDefault(key, String.class, defaultValue);
    }

    public int getInt(String key) {
        return get(key, int.class);
    }

    public int getInt(String key, int defaultValue) {
        if (has(key)) {
            return getInt(key);
        }

        return defaultValue;
    }

    public float getFloat(String key) {
        return get(key, float.class);
    }

    public float getFloat(String key, float defaultValue) {
        if (has(key)) {
            return getFloat(key);
        }

        return defaultValue;
    }

    public double getDouble(String key) {
        return get(key, double.class);
    }

    public double getDouble(String key, double defaultValue) {
        if (has(key)) {
            return getDouble(key);
        }

        return defaultValue;
    }

    public <T> List<T> getList(String key) {
        return get(key, ArrayList.class);
    }

    public <T> List<T> getListOrNull(String key) {
        return has(key) ? getList(key) : null;
    }

    public List<ConfigurationNode> getNodeList(String key) {
        List<Map<String, Object>> list = getList(key);
        List<ConfigurationNode> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            result.add(new ConfigurationNode(list.get(i)));
        }

        return result;
    }

    public List<ConfigurationNode> getNodeListOrNull(String key) {
        try {
            return getNodeList(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
