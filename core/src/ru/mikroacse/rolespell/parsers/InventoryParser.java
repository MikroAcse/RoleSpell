package ru.mikroacse.rolespell.parsers;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.ItemRepository;

import java.util.List;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
class InventoryParser {
    static void parse(List<ConfigurationNode> itemConfigs, Inventory inventory) {
        for (ConfigurationNode itemConfig : itemConfigs) {
            if (itemConfig.contains("chance")) {
                double chance = itemConfig.get("chance");

                if (Math.random() > chance) {
                    continue;
                }
            }

            Item item = ItemParser.parse(itemConfig.get("item"), ItemRepository.instance);

            if (itemConfig.contains("index")) {
                inventory.getItems().setItem(itemConfig.get("index"), item);
            } else {
                inventory.getItems().addItem(item);
            }

            if (itemConfig.contains("hotbar")) {
                inventory.getHotbar().setItem(itemConfig.get("hotbar"), item);
            }
        }
    }
}
