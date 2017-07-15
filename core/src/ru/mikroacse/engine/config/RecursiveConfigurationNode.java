package ru.mikroacse.engine.config;

import ru.mikroacse.engine.config.providers.ConfigurationProvider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Immutable recursive configuration based on UnmodifiableMap.
 */
public abstract class RecursiveConfigurationNode<P extends RecursiveConfigurationNode<P>> extends ConfigurationNode {
    // TODO: beautify / move to ConfigurationNode maybe?
    private boolean useCache = true;
    private Map<String, Object> cache = new HashMap<>();

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
        Object result;

        if(useCache && cache.containsKey(key)) {
            result = cache.get(key);

            if(result != null) {
                return (T) result;
            }

            throw new NullPointerException("Node doesn't have such key: " + key);
        }

        try {
            result = super.get(key);
        } catch (Exception e) {
            if (hasParent()) {
                result = getParent().get(key);
            } else {
                if(useCache) {
                    cache.put(key, null);
                }
                throw e;
            }
        }

        if(useCache) {
            cache.put(key, result);
        }

        return (T) result;
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        if(useCache && cache.containsKey(key) && cache.get(key) == null) {
            return defaultValue;
        }

        try {
            return get(key);
        } catch (Exception e) {
            if(useCache) {
                cache.put(key, null);
            }
            return defaultValue;
        }
    }

    public Set<String> keySet(String nodeKey) {
        Set<String> entities = new HashSet<>();

        RecursiveConfigurationNode<P> config = this;

        while (config != null) {
            Map<String, Object> node = config.get(nodeKey, null);

            if(node != null) {
                entities.addAll(((Map) node).keySet());
            }

            config = config.getParent();
        }

        return entities;
    }

    public abstract boolean hasParent();

    public abstract P getParent();
}
