package ru.mikroacse.rolespell.app.model.game.entities.objects;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by Vitaly Rudenko on 17-May-17.
 */
public class Portal extends Entity {
    private MovementComponent movement;

    private String spawn;
    private String destination;

    public Portal(World world, String destination, String spawn, int x, int y) {
        super(EntityType.PORTAL, world);

        this.destination = destination;
        this.spawn = spawn;

        movement = new MovementComponent(this, x, y, 0);
        addComponent(movement);
    }

    public Portal(World world, int x, int y) {
        this(world, null, null, x, y);
    }

    @Override
    public void setConfig(EntityConfig config) {
        super.setConfig(config);

        destination = config.get("destination", destination);
        spawn = config.get("spawn", spawn);
    }

    public String getSpawn() {
        return spawn;
    }

    public String getDestination() {
        return destination;
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

    @Override
    public String toString() {
        return "Portal{" +
                "spawn='" + spawn + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
