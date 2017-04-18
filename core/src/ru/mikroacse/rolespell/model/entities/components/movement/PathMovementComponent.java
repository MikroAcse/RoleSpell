package ru.mikroacse.rolespell.model.entities.components.movement;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Position;
import ru.mikroacse.util.Priority;
import ru.mikroacse.util.listeners.ListenerSupport;
import ru.mikroacse.util.listeners.ListenerSupportFactory;

import java.util.LinkedList;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class PathMovementComponent extends MovementComponent {
    private Priority priority;
    private LinkedList<Position> path;

    private Listener listeners;

    public PathMovementComponent(Entity entity, int x, int y, float speed) {
        super(entity, x, y, speed);

        listeners = ListenerSupportFactory.create(Listener.class);

        path = new LinkedList<>();

        priority = Priority.NEVER;
    }

    @Override
    public boolean action() {
        if (path.isEmpty()) {
            priority = Priority.NEVER;
            return false;
        }

        moveTo(path.poll(), getType());
        listeners.pathChanged(this, Listener.Event.PATH_NEXT, path);
        return true;
    }

    /**
     * Finds nearest path to the destination point and sets.
     *
     * @param priority       Priority of route.
     *                       If current path's priority is bigger, movement cancels.
     * @param pathFindRadius Radius of map's 'cut' offset.
     * @param maxDistance    Maximum distance between current entity position and destination
     *                       If actual distance is bigger, path is being shortened.
     */
    // TODO: move path finder to separate class
    public boolean routeTo(Position destination, Priority priority, int pathFindRadius, int maxDistance) {
        if (priority.getValue() < this.priority.getValue()) {
            return false;
        }

        destination.shorten(getPosition(), maxDistance);

        World world = getEntity().getWorld();
        LinkedList<Position> newPath = world.getPath(getPosition(), destination, pathFindRadius);

        // > 1 because first element is actually entity current position
        if (newPath.size() > 1) {
            // first is equal to the entity current position
            newPath.removeFirst();
            setPath(newPath);

            this.priority = priority;
            return true;
        }

        return false;
    }

    public void addListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).addListener(listener);
    }

    public void removeListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<Listener>) listeners).clearListeners();
    }

    public LinkedList<Position> getPath() {
        return path;
    }

    public void setPath(LinkedList<Position> path) {
        this.path = path;
        listeners.pathChanged(this, Listener.Event.PATH_SET, path);
    }

    public void addToPath(LinkedList<Position> path) {
        this.path.addAll(path);
        listeners.pathChanged(this, Listener.Event.PATH_ADDED, path);
    }

    public void addToPath(Position pathPos) {
        path.add(pathPos);
        listeners.pathChanged(this, Listener.Event.PATH_ADDED, path);

    }

    public void clearPath() {
        path.clear();
        listeners.pathChanged(this, Listener.Event.PATH_CLEARED, path);
    }

    public boolean isPathEmpty() {
        return path.isEmpty();
    }

    /**
     * @return Current path priority.
     */
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public interface Listener extends ru.mikroacse.util.listeners.Listener {
        public void pathChanged(PathMovementComponent movement, Event event, LinkedList<Position> path);

        ;

        public enum Event {
            PATH_SET, PATH_CLEARED, PATH_NEXT, PATH_ADDED
        }
    }
}
