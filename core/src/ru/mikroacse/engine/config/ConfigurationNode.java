package ru.mikroacse.engine.config;

import ru.mikroacse.engine.config.providers.ConfigurationProvider;

import java.util.*;

/**
 * Immutable configuration based on UnmodifiableMap.
 */
public class ConfigurationNode {
    private Map<String, Object> map;

    public ConfigurationNode(Map<String, Object> map) {
        this.map = Collections.unmodifiableMap(map);
    }

    public ConfigurationNode(ConfigurationProvider provider) {
        this(provider.get());
    }

    public ConfigurationNode(ConfigurationNode node) {
        this(node.getMap());
    }

    public <T> T get(String key) throws ClassCastException, NullPointerException {
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

    public <T> T get(String key, T defaultValue) {
        try {
            return get(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean contains(String key) {
        try {
            get(key);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public ConfigurationNode extractNode(String key) {
        return new ConfigurationNode((Map) get(key));
    }

    public ConfigurationNode extractNodeOrNull(String key) {
        if (contains(key)) {
            return extractNode(key);
        }

        return null;
    }

    public List<ConfigurationNode> extractNodeList(String key) {
        List<Map<String, Object>> list = get(key);
        List<ConfigurationNode> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            result.add(new ConfigurationNode(list.get(i)));
        }

        return result;
    }

    public List<ConfigurationNode> extractNodeListOrNull(String key) {
        try {
            return extractNodeList(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "ConfigurationNode" + map;
    }
}
