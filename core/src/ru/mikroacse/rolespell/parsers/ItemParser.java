package ru.mikroacse.rolespell.parsers;

import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.ItemType;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemRepository;

/**
 * Created by Vitaly Rudenko on 25-May-17.
 */
public class ItemParser {
    public static Item parse(JsonValue config, ItemRepository repository) {
        ItemConfig itemConfig;

        if (config.isString()) {
            itemConfig = repository.getItemConfig(config.asString());
        } else {
            itemConfig = repository.parseItemConfig(config);
        }

        return ItemType.create(itemConfig);
    }
}
