package ru.mikroacse.rolespell.app.model.game.entities.objects;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 29.06.2017.
 */
public class PortalSpawn extends Entity {
    private MovementComponent movement;

    public PortalSpawn(World world, int x, int y) {
        super(EntityType.PORTAL_SPAWN, world);

        movement = new MovementComponent(this, x, y, 0);
        addComponent(movement);
    }

    @Override
    public void setPosition(int x, int y) {
        movement.setPosition(x, y);
    }

    @Override
    public IntVector2 getPosition() {
        return movement.getPosition();
    }

    @Override
    public void setOrigin(int x, int y) {
        movement.setOrigin(x, y);
    }

    @Override
    public IntVector2 getOrigin() {
        return movement.getOrigin();
    }
}
