package ru.mikroacse.rolespell.model;

import ru.mikroacse.rolespell.model.entities.Player;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

import java.awt.*;
import java.util.List;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameModel {
    private Point waypoint;

    private Entity observable;
    private World world;

    public GameModel() {
        waypoint = new Point(0, 0);
    }

    private void initializeWorld() {
        observable = world.getPlayer();
    }

    public void update(float delta) {
        List<Entity> entities = world.getEntities();

        for (Entity entity : entities) {
            entity.update(delta);
        }
    }

    public boolean moveEntityBy(Entity entity, int dx, int dy) {
        int newX = entity.x + dx;
        int newY = entity.y + dy;

        if (world.getMeta(newX, newY) == World.Meta.SOLID) {
            return false;
        }

        entity.x += dx;
        entity.y += dy;

        entity.x = Math.max(entity.x, 0);
        entity.y = Math.max(entity.y, 0);

        entity.x = Math.min(entity.x, world.getMapWidth() - 1);
        entity.y = Math.min(entity.y, world.getMapHeight() - 1);

        return true;
    }

    public boolean movePlayerBy(int dx, int dy) {
        return moveEntityBy(getPlayer(), dx, dy);
    }

    public Player getPlayer() {
        return world.getPlayer();
    }

    public Entity getObservable() {
        return observable;
    }

    public Point getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(int x, int y) {
        waypoint.setLocation(x, y);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;

        initializeWorld();
    }
}
