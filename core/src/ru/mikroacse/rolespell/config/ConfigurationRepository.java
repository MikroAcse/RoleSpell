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

    public void addConfig(K key, C config) {
        configurations.put(key, config);
    }

    public C getConfig(K key) {
        return configurations.get(key);
    }
}
