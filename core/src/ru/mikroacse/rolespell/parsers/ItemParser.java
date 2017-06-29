package ru.mikroacse.rolespell.parsers;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.ItemType;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;
import ru.mikroacse.rolespell.app.model.game.items.ItemRepository;
import ru.mikroacse.rolespell.app.model.game.items.weapons.Weapon;

import java.util.Map;

/**
 * Created by Vitaly Rudenko on 25-May-17.
 */
public class ItemParser {
    public static Item parse(Object value, ItemRepository repository) {
        ItemConfig itemConfig;

        if (value instanceof String) {
            itemConfig = repository.get((String) value);

        } else if (value instanceof Map) {
            Map map = (Map) value;

            itemConfig = new ItemConfig(map, (String) map.get("parent"));

        } else if (value instanceof ConfigurationNode) {
            ConfigurationNode node = (ConfigurationNode) value;

            itemConfig = new ItemConfig(node.getMap(), node.get("parent", null));
        } else {
            throw new IllegalArgumentException("Can parse only strings or nodes.");
        }

        return create(itemConfig);
    }

    public static Item create(ItemConfig config) {
        Item item = ItemType.create(config.getType(ItemType.ITEM));

        item.setConfig(config);

        return item;
    }
}
