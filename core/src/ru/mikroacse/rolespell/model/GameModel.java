package ru.mikroacse.rolespell.model;

import ru.mikroacse.rolespell.model.entities.Player;
import ru.mikroacse.rolespell.model.world.World;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameModel {
    public boolean isMoving;
    private Point waypoint;

    private LinkedList<Point> path;

    private Player player;
    private World world;

    public GameModel() {
        player = new Player(0, 0);
        waypoint = new Point(0, 0);

        path = new LinkedList<>();
    }

    public void movePlayer(int dx, int dy) {
        int newX = player.x + dx;
        int newY = player.y + dy;

        if (world.getMeta(newX, newY) == World.Meta.SOLID) {
            return;
        }

        player.x += dx;
        player.y += dy;

        player.x = Math.max(player.x, 0);
        player.y = Math.max(player.y, 0);

        player.x = Math.min(player.x, world.getMapWidth() - 1);
        player.y = Math.min(player.y, world.getMapHeight() - 1);
    }

    public Player getPlayer() {
        return player;
    }

    public void addPath(LinkedList<Point> path) {
        this.path.addAll(path);
    }

    public void clearPath() {
        this.path.clear();
    }

    public LinkedList<Point> getPath() {
        return path;
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
    }
}
