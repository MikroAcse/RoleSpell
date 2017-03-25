package ru.mikroacse.rolespell.model.entities.core;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public /*TODO:abstract*/ class Entity {
    public int x;
    public int y;

    private EntityType type;
    private LinkedList<Point> path;

    private float movingSpeed; // blocks per second
    private float movingUpdate;

    public Entity(int x, int y, EntityType type) {
        this.x = x;
        this.y = y;
        this.type = type;

        path = new LinkedList<>();

        movingUpdate = 0f;
        movingSpeed = 10f;
    }

    public Entity(int x, int y) {
        this(0, 0, null);
    }

    public Entity() {
        this(0, 0);
    }

    public void update(float delta) {
        if (!isPathEmpty()) {
            movingUpdate += movingSpeed * delta;

            Point point = null;
            while (movingUpdate >= 1f) {
                if (isPathEmpty())
                    break;

                point = removePathPoint();
                movingUpdate -= 1f;
            }

            if (point != null) {
                setPosition(point.x, point.y);
            }
        }
    }

    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addToPath(LinkedList<Point> path) {
        this.path.addAll(path);
    }

    public void addToPath(Point pathPoint) {
        path.add(pathPoint);
    }

    public void clearPath() {
        path.clear();
    }

    public Point removePathPoint() {
        if(isPathEmpty()) {
            return null;
        }

        return path.remove();
    }

    public boolean isPathEmpty() {
        return path.isEmpty();
    }

    public LinkedList<Point> getPath() {
        return path;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public EntityType getType() {
        return type;
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
