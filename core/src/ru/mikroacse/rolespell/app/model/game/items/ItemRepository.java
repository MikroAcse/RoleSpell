package ru.mikroacse.rolespell.app.model.game.items;

import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;
import ru.mikroacse.rolespell.config.ConfigurationRepository;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class ItemRepository extends ConfigurationRepository<String, ItemConfig> {
    public static final ItemRepository instance = new ItemRepository();
}
