package ru.mikroacse.rolespell.app.model.game.entities.objects;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by Vitaly Rudenko on 17-May-17.
 */
public class Portal extends Entity {
    private MovementComponent movement;

    private String destination;
    private String id;

    private boolean spawn;

    public Portal(World world, String destination, String id, boolean spawn, String name, int x, int y) {
        super(EntityType.PORTAL, world, name);

        this.id = id;
        this.destination = destination;
        this.spawn = spawn;

        movement = new MovementComponent(this, x, y, 0);
        addComponent(movement);
    }

    public Portal(World world, String destination, String id, boolean spawn, String name) {
        this(world, destination, id, spawn, name, 0, 0);
    }

    public String getDestination() {
        return destination;
    }

    public String getId() {
        return id;
    }

    public boolean isSpawn() {
        return spawn;
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
