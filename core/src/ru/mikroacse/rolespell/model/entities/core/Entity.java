package ru.mikroacse.rolespell.model.entities.core;

import ru.mikroacse.rolespell.model.entities.ai.core.EntityAI;
import ru.mikroacse.rolespell.model.world.World;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public /*TODO:abstract*/ class Entity {
    public int x;
    public int y;

    private Point supposedPosition;

    protected World world;

    protected EntityAI ai;

    protected EntityType type;
    protected LinkedList<Point> path;

    protected float movingSpeed; // blocks per second
    protected float time;

    public Entity(int x, int y, EntityType type, World world) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.world = world;

        initialize();
    }

    public Entity() {
        this(0, 0, null, null);
    }

    protected void initialize() {
        supposedPosition = new Point(x, y);

        path = new LinkedList<>();

        time = 0f;
        movingSpeed = 0f;
    }

    public void update(float delta) {
        if (isPathEmpty()) {
            if(ai != null) {
                ai.update(delta);
            }
        } else {
            time += movingSpeed * delta;

            Point point = null;
            while (time >= 1f) {
                if (isPathEmpty())
                    break;

                point = removePathPoint();
                time -= 1f;
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

    public void setSupposedPosition(int x, int y) {
        supposedPosition.setLocation(x, y);

        this.x = x;
        this.y = y;
    }

    public int getSupposedX() {
        return supposedPosition.x;
    }

    public int getSupposedY() {
        return supposedPosition.y;
    }

    public void setPath(LinkedList<Point> path) {
        this.path = path;
        time = 0f;
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

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "x=" + x +
                ", y=" + y +
                ", world=" + world +
                ", type=" + type +
                '}';
    }
}
