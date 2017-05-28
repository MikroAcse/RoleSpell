package ru.mikroacse.rolespell.app.model.game.items.config;

import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.engine.config.Configuration;
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

    public void addItemConfig(String id, JsonValue config) {
        itemConfigs.put(id, parseItemConfig(config));
    }

    public ItemConfig getItemConfig(String id) {
        return itemConfigs.get(id);
    }

    public ItemConfig parseItemConfig(JsonValue config) {
        ItemConfig item;
        ItemConfig parent = null;

        String configParent = config.getString("parent", null);

        if (configParent != null) {
            parent = itemConfigs.get(configParent);

            item = parent.clone();
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
            item.parameters = new Configuration(config.get("parameters"));
        }

        return item;
    }
}
