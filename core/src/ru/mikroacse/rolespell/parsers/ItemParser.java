package ru.mikroacse.rolespell.parsers;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.ItemType;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;
import ru.mikroacse.rolespell.app.model.game.items.ItemRepository;

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
            itemConfig = repository.add(new ConfigurationNode((Map) value));

        } else if (value instanceof ConfigurationNode) {
            itemConfig = repository.add((ConfigurationNode) value);

        } else {
            throw new IllegalArgumentException("Can parse only strings or nodes.");
        }

        return ItemType.create(itemConfig);
    }
}
