package ru.mikroacse.rolespell.app.model.game.items;

import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;
import ru.mikroacse.rolespell.app.model.game.items.weapons.Weapon;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public enum ItemType {
    WEAPON;

    public static Item create(ItemConfig config) {
        switch (config.getType()) {
            case WEAPON:
                return new Weapon(config);
        }

        return null;
    }
}
