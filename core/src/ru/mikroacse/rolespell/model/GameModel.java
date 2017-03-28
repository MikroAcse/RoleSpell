package ru.mikroacse.rolespell.model;

import ru.mikroacse.rolespell.model.entities.Player;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.MovableEntity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Position;

import java.util.List;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameModel {
    private Position waypoint;

    private MovableEntity observable;
    private World world;

    public GameModel() {
        waypoint = new Position(0, 0);
    }

    private void initializeWorld() {
        observable = world.getPlayer();
    }

    public void update(float delta) {
        List<Entity> entities = world.getEntities();

        for (Entity entity : entities) {
            entity.update(delta, world);
        }
    }

    public Player getPlayer() {
        return world.getPlayer();
    }

    public MovableEntity getObservable() {
        return observable;
    }

    public void setObservable(MovableEntity observable) {
        this.observable = observable;
    }

    public Position getWaypoint() {
        return waypoint;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;

        initializeWorld();
    }
}
