package ru.mikroacse.rolespell.app.model.game.world;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.controllers.MobController;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.Property;

/**
 * Created by MikroAcse on 10-May-17.
 */
public class WorldListener implements World.Listener {
    @Override
    public void mobDied(World world, MobController controller) {
        updated(world);
    }

    @Override
    public void mobResurrected(World world, MobController controller) {
        updated(world);
    }

    @Override
    public void propertyUpdated(World world, StatusComponent status, Property property, double previousValue, double currentValue) {
        updated(world);
    }

    @Override
    public void positionChanged(World world, MovementComponent movement, int prevX, int prevY, IntVector2 current) {
        updated(world);
    }

    @Override
    public void originChanged(World world, MovementComponent movement, int prevX, int prevY, IntVector2 current) {
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
