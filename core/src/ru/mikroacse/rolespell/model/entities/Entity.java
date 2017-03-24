package ru.mikroacse.rolespell.model.entities;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public /*TODO:abstract*/ class Entity {
    public int x;
    public int y;
    public EntityType type;

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Entity() {
        this(0, 0);
    }

    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "x=" + x +
                ", y=" + y +
                ", type=" + type +
                '}';
    }
}
