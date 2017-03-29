package ru.mikroacse.rolespell.model.entities.components.movement;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.MovableEntity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Position;

import java.util.LinkedList;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class PathMovementComponent extends MovementComponent {
    private LinkedList<Position> path;
    private UpdateType type;

    public PathMovementComponent(int x, int y, double speed) {
        super(x, y, speed);

        path = new LinkedList<>();
        type = UpdateType.BOTH;
    }

    @Override
    public void move(Entity entity, World world) {
        if (path.isEmpty()) {
            return;
        }

        Position position = nextPosition();

        switch (type) {
            case ORIGIN:
                setOrigin(position);
                break;
            case CURRENT:
                setPosition(position);
                break;
            case BOTH:
                setBoth(position);
                break;
        }

        validate(world);
    }

    public boolean moveTo(MovableEntity entity, World world, Position destination, int pathFindRadius) {
        LinkedList<Position> newPath = world.getPath(getPosition(), destination, pathFindRadius);

        // > 1 because first element might be entity's current position
        if (newPath.size() > 1) {
            // remove first, because it's equal to entity's current position
            newPath.remove(0);
            setPath(newPath);
            return true;
        }

        return false;
    }

    public LinkedList<Position> getPath() {
        return path;
    }

    public void setPath(LinkedList<Position> path) {
        this.path = path;
    }

    public void addToPath(LinkedList<Position> path) {
        this.path.addAll(path);
    }

    public void addToPath(Position pathPos) {
        path.add(pathPos);
    }

    public void clearPath() {
        path.clear();
    }

    public Position nextPosition() {
        return path.poll();
    }

    public boolean isPathEmpty() {
        return path.isEmpty();
    }

    public UpdateType getType() {
        return type;
    }

    public void setType(UpdateType type) {
        this.type = type;
    }

    public enum UpdateType {
        ORIGIN,
        CURRENT,
        BOTH
    }
}
