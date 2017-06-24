package ru.mikroacse.rolespell.app.model.game.items.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.items.ItemType;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class ItemConfig {
    ItemConfig parent;

    String name;
    ItemType type;
    String texture;

    boolean throwable;
    boolean pickable;

    ConfigurationNode parameters;

    ItemConfig() {

    }

    /**
     * Looks through item config and its parents for configuration node,
     * which contains needed key.
     *
     * @return Configuration node, which contains needed key.
     */
    public ConfigurationNode find(String key) {
        ItemConfig config = this;

        while (config.getParameters() != null && !config.getParameters().has(key)) {
            config = config.parent;

            if (config == null) {
                return null;
            }
        }

        return config.getParameters();
    }

    public Object get(String key) {
        return find(key).get(key);
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public String getTexture() {
        return texture;
    }

    public boolean isThrowable() {
        return throwable;
    }

    public boolean isPickable() {
        return pickable;
    }

    public ConfigurationNode getParameters() {
        return parameters;
    }

    public ItemConfig getParent() {
        return parent;
    }

    public ItemConfig copy() {
        ItemConfig result = new ItemConfig();

        result.name = name;
        result.type = type;
        result.texture = texture;
        result.throwable = throwable;
        result.pickable = pickable;
        result.parent = parent;
        result.parameters = parameters;

        return result;
    }
}