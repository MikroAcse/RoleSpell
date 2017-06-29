package ru.mikroacse.engine.config;

import ru.mikroacse.engine.config.providers.ConfigurationProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable recursive configuration based on UnmodifiableMap.
 */
public abstract class RecursiveConfigurationNode<P extends RecursiveConfigurationNode<P>> extends ConfigurationNode {
    public RecursiveConfigurationNode(Map<String, Object> map) {
        super(map);
    }

    public RecursiveConfigurationNode(ConfigurationProvider provider) {
        super(provider);
    }

    public RecursiveConfigurationNode(ConfigurationNode node) {
        super(node);
    }

    public RecursiveConfigurationNode(RecursiveConfigurationNode<P> node) {
        super(node);
    }

    /**
     * @return Returns a value or getParent().get(key), if the value is not present.
     */
    @Override
    public <T> T get(String key) {
        try {
            return super.get(key);
        } catch (Exception e) {
            if (hasParent()) {
                return getParent().get(key);
            } else {
                throw e;
            }
        }
    }

    public abstract boolean hasParent();

    public abstract P getParent();
}
