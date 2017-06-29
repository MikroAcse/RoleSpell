package ru.mikroacse.rolespell.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.RecursiveConfigurationNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 29.06.2017.
 */
public class ConfigurationRepository<K, C extends RecursiveConfigurationNode<C>> {
    private Map<K, C> configurations;

    public ConfigurationRepository() {
        configurations = new HashMap<>();
    }

    public boolean contains(K key) {
        return configurations.containsKey(key);
    }

    public void add(K key, C config) {
        configurations.put(key, config);
    }

    public C get(K key) {
        return configurations.get(key);
    }
}
