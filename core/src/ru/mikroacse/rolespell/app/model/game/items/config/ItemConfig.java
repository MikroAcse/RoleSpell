package ru.mikroacse.rolespell.app.model.game.items.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.RecursiveConfigurationNode;
import ru.mikroacse.engine.config.providers.ConfigurationProvider;
import ru.mikroacse.rolespell.app.model.game.items.ItemType;

import java.util.Map;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class ItemConfig extends RecursiveConfigurationNode<ItemConfig> {
    public ItemConfig(ConfigurationNode node, ItemConfig parent) {
        super(node, parent);
    }

    public String getName() {
        return get("type", null);
    }

    public ItemType getType() {
        String type = get("type", null);

        return type != null? ItemType.valueOf(type) : ItemType.NONE;
    }

    public String getTexture() {
        return get("texture", null);
    }

    public boolean isThrowable() {
        return get("throwable", false);
    }

    public boolean isPickable() {
        return get("pickable", false);
    }
}