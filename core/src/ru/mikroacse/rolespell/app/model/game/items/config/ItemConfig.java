package ru.mikroacse.rolespell.app.model.game.items.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.RecursiveConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.items.ItemRepository;
import ru.mikroacse.rolespell.app.model.game.items.ItemType;

import java.util.Map;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class ItemConfig extends RecursiveConfigurationNode<ItemConfig> {
    private String parent;

    public ItemConfig(Map<String, Object> map, String parent) {
        super(map);

        this.parent = parent;
    }

    public ItemConfig(Map<String, Object> map) {
        super(map);
    }

    public ItemConfig(ConfigurationNode node) {
        super(node);
    }

    public String getName(String defaultValue) {
        return get("name", defaultValue);
    }

    public ItemType getType(ItemType defaultValue) {
        String type = get("type", null);

        return type != null? ItemType.valueOf(type) : defaultValue;
    }

    public String getTexture(String defaultValue) {
        return get("texture", defaultValue);
    }

    public boolean isThrowable(boolean defaultValue) {
        return get("throwable", defaultValue);
    }

    public boolean isPickable(boolean defaultValue) {
        return get("pickable", defaultValue);
    }

    @Override
    public ItemConfig getParent() {
        return ItemRepository.instance().get(parent);
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }
}