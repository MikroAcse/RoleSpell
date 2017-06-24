package ru.mikroacse.rolespell.parsers;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemRepository;

import java.util.List;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class InventoryParser {
    public static void parse(List<ConfigurationNode> itemConfigs, Inventory inventory) {
        for (ConfigurationNode itemConfig : itemConfigs) {
            if (itemConfig.has("chance")) {
                double chance = itemConfig.getDouble("chance");

                if (Math.random() > chance) {
                    continue;
                }
            }

            Item item = ItemParser.parse(itemConfig.get("item"), ItemRepository.getInstance());

            if (itemConfig.has("index")) {
                inventory.getItems().setItem(itemConfig.getInt("index"), item);
            } else {
                inventory.getItems().addItem(item);
            }

            if (itemConfig.has("hotbar")) {
                inventory.getHotbar().setItem(itemConfig.getInt("hotbar"), item);
            }
        }
    }
}
