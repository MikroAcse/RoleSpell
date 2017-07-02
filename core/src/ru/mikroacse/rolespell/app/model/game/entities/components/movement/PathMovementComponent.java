package ru.mikroacse.rolespell.app.model.game.entities.components.movement;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class PathMovementComponent extends MovementComponent {
    private Priority priority;
    private Array<IntVector2> path;

    private Listener listeners;

    public PathMovementComponent(Entity entity, int x, int y, float speed) {
        super(entity, x, y, speed);

        listeners = ListenerSupportFactory.create(Listener.class);

        path = new Array<>();

        priority = Priority.NEVER;
    }

    @Override
    public boolean action() {
        if (path.size == 0) {
            priority = Priority.NEVER;
            return false;
        }

        moveTo(path.removeIndex(0), getType());
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
    public IntVector2 tryRouteTo(IntVector2 destination, Priority priority, int searchRadius, int maxDistance, int minRadius, int maxRadius) {
        if (priority.getValue() < this.priority.getValue()) {
            return null;
        }

        destination.shorten(getPosition(), maxDistance);

        World world = getEntity().getWorld();

        // checking for passable cells
        if (world.getMap().isValidPosition(destination)) {
            IntVector2 newDestination = null;

            // TODO: magic number
            maxRadius = Math.min(maxRadius, 15);

            for (int i = minRadius; i <= maxRadius; i++) {
                Array<IntVector2> passableCells = world.getPassableCells(
                        destination.x, destination.y,
                        false,
                        i, i,
                        false);

                if (passableCells.size != 0) {
                    // nearest cell to the entity
                    passableCells.sort((o1, o2) -> {
                        double d1 = o1.distance(getPosition());
                        double d2 = o2.distance(getPosition());

                        return Double.compare(d1, d2);
                    });

                    newDestination = passableCells.get(0);
                    break;
                }
            }

            destination = newDestination;
        }


        if (destination == null) {
            return null;
        }

        // TODO: commented because path can change because of entities, but destination would be the same
        /*if (getPosition().equals(destination)) {
            return destination;
        }*/

        Array<IntVector2> newPath = world.getPath(getPosition(), destination, searchRadius);

        // > 1 because first element is actually entity current position
        if (newPath.size > 1) {
            // first is equal to the entity current position
            newPath.removeIndex(0);
            setPath(newPath);

            this.priority = priority;
            return destination;
        }

        return null;
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

    public Array<IntVector2> getPath() {
        return path;
    }

    public void setPath(Array<IntVector2> path) {
        this.path = path;
        listeners.pathChanged(this, Listener.Event.PATH_SET, path);
    }

    public void addToPath(Array<IntVector2> path) {
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
        return path.size == 0;
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
        void pathChanged(PathMovementComponent movement, Event event, Array<IntVector2> path);

        enum Event {
            PATH_SET, PATH_CLEARED, PATH_NEXT, PATH_ADDED
        }
    }
}
