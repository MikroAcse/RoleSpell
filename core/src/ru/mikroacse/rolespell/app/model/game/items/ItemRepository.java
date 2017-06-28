package ru.mikroacse.rolespell.app.model.game.items;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;
import ru.mikroacse.rolespell.config.ConfigurationRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class ItemRepository extends ConfigurationRepository<String, ItemConfig> {
    private static ItemRepository instance = new ItemRepository();

    private ItemRepository() {
        super();
    }

    public static ItemRepository getInstance() {
        return instance;
    }
}
