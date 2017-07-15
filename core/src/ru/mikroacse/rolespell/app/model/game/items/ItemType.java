package ru.mikroacse.rolespell.app.model.game.items;

import ru.mikroacse.rolespell.app.model.game.items.weapons.Weapon;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public enum ItemType {
    ITEM,

    WEAPON;

    public static Item create(ItemType type) {
        switch (type) {
            case ITEM:
                throw new IllegalArgumentException("Cannot create an item without a type");
            case WEAPON:
                return new Weapon();
            default:
                throw new IllegalArgumentException("Cannot automatically create an item of this type");
        }
    }
}
