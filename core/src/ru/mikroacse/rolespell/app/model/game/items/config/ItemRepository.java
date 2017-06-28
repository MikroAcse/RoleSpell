package ru.mikroacse.rolespell.app.model.game.items.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.items.ItemType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class ItemRepository {
    private static ItemRepository instance = new ItemRepository();

    private Map<String, ItemConfig> itemConfigs;

    private ItemRepository() {
        itemConfigs = new HashMap<>();
    }

    public static ItemRepository getInstance() {
        return instance;
    }

    public void addItemConfig(String id, ConfigurationNode config) {
        itemConfigs.put(id, parseItemConfig(config));
    }

    public ItemConfig getItemConfig(String id) {
        return itemConfigs.get(id);
    }

    public ItemConfig parseItemConfig(ConfigurationNode config) {
        ItemConfig item;
        ItemConfig parent = null;

        String configParent = config.getString("parent", null);

        if (configParent != null) {
            parent = itemConfigs.get(configParent);

            item = parent.copy();
            item.parent = parent;
        } else {
            item = new ItemConfig();
        }

        if (config.has("name")) {
            item.name = config.getString("name");
        }

        if (config.has("texture")) {
            item.texture = config.getString("texture");
        }

        if (config.has("type")) {
            item.type = ItemType.valueOf(config.getString("type"));
        }

        if (config.has("pickable")) {
            item.pickable = config.getBoolean("pickable");
        }

        if (config.has("throwable")) {
            item.throwable = config.getBoolean("throwable");
        }

        if (config.has("parameters")) {
            item.parameters = config.getNode("parameters");
        }

        return item;
    }
}
