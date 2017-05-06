package ru.mikroacse.rolespell.app.model.game.items;

/**
 * Created by MikroAcse on 27-Apr-17.
 */
public abstract class Item {
    protected String name;

    protected boolean throwable;
    protected boolean pickable;
    protected boolean stackable;

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
}
