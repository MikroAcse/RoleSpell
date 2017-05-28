package ru.mikroacse.rolespell.app.model.game.items;

import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;

/**
 * Created by MikroAcse on 27-Apr-17.
 */
public abstract class Item {
    private ItemConfig config;

    private String name;
    private ItemType type;

    private boolean throwable;
    private boolean pickable;

    public Item(ItemConfig config) {
        this.config = config;

        name = config.getName();
        type = config.getType();

        throwable = config.isThrowable();
        pickable = config.isPickable();

        configure(config);
    }

    protected void configure(ItemConfig config) {

    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public boolean isThrowable() {
        return throwable;
    }

    public boolean isPickable() {
        return pickable;
    }

    public ItemConfig getConfig() {
        return config;
    }
}
