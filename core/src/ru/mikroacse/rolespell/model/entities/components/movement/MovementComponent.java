package ru.mikroacse.rolespell.model.entities.components.movement;

import ru.mikroacse.rolespell.model.entities.components.core.UpdatableComponent;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Position;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class MovementComponent implements UpdatableComponent {
    private Position position;
    private Position origin;

    private double speed; // blocks per second
    private double time;

    public MovementComponent(int x, int y, double speed) {
        this.speed = speed;

        origin = new Position(x, y);
        position = origin.clone();

        time = 0.0;
    }

    public MovementComponent() {
        this(0, 0, 1.0);
    }

    @Override
    public boolean update(Entity entity, World world, float delta) {
        time += speed * delta;

        while (time >= 1.0) {
            move(entity, world);

            time -= 1.0;
        }

        return true;
    }

    public abstract void move(Entity entity, World world);

    public void validate(World world) {
        world.validatePosition(origin);
        world.validatePosition(position);
    }

    public void reset() {
        time = 0.0;
    }

    public void teleportToOrigin() {
        position.set(origin);
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        setPosition(position.x, position.y);
    }

    public void setOrigin(int x, int y) {
        origin.set(x, y);
    }

    public Position getOrigin() {
        return origin;
    }

    public void setOrigin(Position origin) {
        setOrigin(origin.x, origin.y);
    }

    public void setBoth(int x, int y) {
        origin.set(x, y);
        teleportToOrigin();
    }

    public void setBoth(Position origin) {
        setBoth(origin.x, origin.y);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
