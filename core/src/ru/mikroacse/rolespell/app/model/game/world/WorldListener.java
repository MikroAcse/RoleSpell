package ru.mikroacse.rolespell.app.model.game.world;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;

/**
 * Created by MikroAcse on 10-May-17.
 */
public class WorldListener implements World.Listener {
    @Override
    public void entityMoved(World world, Entity entity, int prevX, int prevY, IntVector2 current) {
        updated(world);
    }

    @Override
    public void entityAdded(World world, Entity entity) {
        updated(world);
    }

    @Override
    public void entityRemoved(World world, Entity entity) {
        updated(world);
    }

    public void updated(World world) {

    }
}
