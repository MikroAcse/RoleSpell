package ru.mikroacse.rolespell.app.model.game.items;

import ru.mikroacse.engine.config.Configuration;

/**
 * Created by MikroAcse on 27-Apr-17.
 */
public abstract class Item {
    private Configuration config;

    private String name;

    private boolean throwable;
    private boolean pickable;
    private boolean stackable;

    public Item(String name, boolean throwable, boolean pickable, boolean stackable) {
        this.name = name;
        this.throwable = throwable;
        this.pickable = pickable;
        this.stackable = stackable;
    }

    public String getName() {
        return name;
    }

    public boolean isThrowable() {
        return throwable;
    }

    public boolean isPickable() {
        return pickable;
    }

    public boolean isStackable() {
        return stackable;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }
}
