package ru.mikroacse.rolespell.app.view.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by MikroAcse on 10-May-17.
 */
public class PathRenderer extends Group {
    private Pool<PathPoint> pathPointPool;

    private Array<PathPoint> pathPoints;
    private Image waypoint;

    private WorldRenderer worldRenderer;

    public PathRenderer(WorldRenderer worldRenderer) {
        this.worldRenderer = worldRenderer;

        // TODO: magic numbers (maybe use max path distance)
        pathPointPool = new Pool<PathPoint>(0, 20) {
            @Override
            protected PathPoint newObject() {
                return new PathPoint();
            }
        };

        pathPoints = new Array<>();

        waypoint = new Image(bundle(Bundle.GAME).getTexture("ui/waypoint"));
    }

    public void setPath(Array<IntVector2> path) {
        if (path == null || path.size == 0) {
            clear();
            return;
        }

        int size = path.size - 1;

        while (size != pathPoints.size) {
            if (size > pathPoints.size) {
                pathPoints.add(pathPointPool.obtain());
            } else {
                pathPointPool.free(pathPoints.pop());
            }
        }

        MapRenderer mapRenderer = worldRenderer.getMapRenderer();

        for (int i = 0; i < pathPoints.size; i++) {
            PathPoint pathPoint = pathPoints.get(i);

            Vector2 mapPosition = mapRenderer.cellToMap(path.get(i));

            pathPoint.setPosition(mapPosition.x, mapPosition.y);

            addActor(pathPoint);
        }

        // waypoint (last path point)
        Vector2 mapPosition = mapRenderer.cellToMap(path.peek());

        waypoint.setPosition(mapPosition.x, mapPosition.y);

        addActor(waypoint);
    }

    public void clear() {
        clearChildren();

        if (pathPoints.size > 0) {
            pathPointPool.freeAll(pathPoints);

            pathPoints.clear();
        }
    }

    private class PathPoint extends Image implements Pool.Poolable {
        public PathPoint() {
            super(bundle(Bundle.GAME).getTexture("ui/path"));
        }

        @Override
        public void reset() {
            this.remove();
        }
    }
}
