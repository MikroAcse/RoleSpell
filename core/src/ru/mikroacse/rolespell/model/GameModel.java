package ru.mikroacse.rolespell.model;

import ru.mikroacse.rolespell.model.entities.Player;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

import java.util.List;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameModel {
    private Entity observable;
    private World world;

    public GameModel() {

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

    public Player getPlayer() {
        return world.getPlayer();
    }

    public Entity getObservable() {
        return observable;
    }

    public void setObservable(Entity observable) {
        this.observable = observable;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;

        initializeWorld();
    }
}
