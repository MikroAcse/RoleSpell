package ru.mikroacse.engine.config;

import ru.mikroacse.engine.config.providers.ConfigurationProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable recursive configuration based on UnmodifiableMap.
 */
public class RecursiveConfigurationNode<P extends RecursiveConfigurationNode<P>> extends ConfigurationNode {
    private P parent;

    public RecursiveConfigurationNode(Map<String, Object> map, P parent) {
        super(map);

        this.parent = parent;
    }

    public RecursiveConfigurationNode(ConfigurationProvider provider, P parent) {
        super(provider);

        this.parent = parent;
    }

    public RecursiveConfigurationNode(ConfigurationNode node, P parent) {
        super(node);

        this.parent = parent;
    }

    public RecursiveConfigurationNode(Map<String, Object> map) {
        this(map, null);
    }

    public RecursiveConfigurationNode(ConfigurationProvider provider) {
        this(provider, null);
    }

    public RecursiveConfigurationNode(ConfigurationNode node) {
        this(node, null);
    }

    public RecursiveConfigurationNode(RecursiveConfigurationNode<P> node) {
        this(node, node.getParent());
    }

    /**
     * @return Returns a value or parent.get(key), if it's null.
     */
    @Override
    public <T> T get(String key) {
        try {
            return super.get(key);
        } catch (Exception e) {
            if (parent != null) {
                return parent.get(key);
            } else {
                throw e;
            }
        }
    }

    public P getParent() {
        return parent;
    }
}
