package ru.mikroacse.rolespell.app.model.game.entities.components.movement;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class PathMovementComponent extends MovementComponent {
    private Priority priority;
    private List<IntVector2> path;

    private Listener listeners;

    public PathMovementComponent(Entity entity, int x, int y, float speed) {
        super(entity, x, y, speed);

        listeners = ListenerSupportFactory.create(Listener.class);

        path = new ArrayList<>();

        priority = Priority.NEVER;
    }

    @Override
    public boolean action() {
        if (path.isEmpty()) {
            priority = Priority.NEVER;
            return false;
        }

        moveTo(path.remove(0), getType());
        listeners.pathChanged(this, Listener.Event.PATH_NEXT, path);
        return true;
    }

    /**
     * Finds nearest path to the destination point and sets.
     *
     * @param priority     Priority of route.
     *                     If current path's priority is bigger, movement cancels.
     * @param searchRadius Radius of map's 'cut' offset.
     * @param maxDistance  Maximum distance between current entity position and destination
     *                     If actual distance is bigger, path is being shortened.
     */
    // TODO: move path finder to separate class
    public boolean tryRouteTo(IntVector2 destination, Priority priority, int searchRadius, int maxDistance) {
        if (priority.getValue() < this.priority.getValue()) {
            return false;
        }

        destination.shorten(getPosition(), maxDistance);

        if (getPosition().equals(destination)) {
            return false;
        }

        World world = getEntity().getWorld();
        LinkedList<IntVector2> newPath = world.getPath(getPosition(), destination, searchRadius);

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

    public boolean tryRouteTo(List<IntVector2> destinations, Priority priority, int searchRadius, int maxDistance) {
        for (IntVector2 destination : destinations) {
            if (tryRouteTo(destination, priority, searchRadius, maxDistance)) {
                return true;
            }
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

    public List<IntVector2> getPath() {
        return path;
    }

    public void setPath(List<IntVector2> path) {
        this.path = path;
        listeners.pathChanged(this, Listener.Event.PATH_SET, path);
    }

    public void addToPath(List<IntVector2> path) {
        this.path.addAll(path);
        listeners.pathChanged(this, Listener.Event.PATH_ADDED, path);
    }

    public void addToPath(IntVector2 position) {
        path.add(position);
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

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void pathChanged(PathMovementComponent movement, Event event, List<IntVector2> path);

        enum Event {
            PATH_SET, PATH_CLEARED, PATH_NEXT, PATH_ADDED
        }
    }
}
