package ru.mikroacse.engine.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import ru.mikroacse.engine.actors.MeasurableActor;

/**
 * Created by MikroAcse on 10.07.2016.
 */
public class GroupUtil {
    /**
     * Returns actual width of group. Calculates only widths of actors (.getWidth() * .getScaleX()).
     */
    public static float getWidth(Group group) {
        SnapshotArray<Actor> children = group.getChildren();
        float minX = 0;
        float maxX = 0;
        for (int i = 0; i < children.size; i++) {
            Actor child = children.get(i);
            float childMinX = child.getX();
            float childMaxX;

            if (!child.isVisible()) {
                continue;
            }

            if (child instanceof MeasurableActor) {
                childMaxX = childMinX + ((MeasurableActor) child).getRealWidth();
            } else if (child instanceof Group) {
                childMaxX = childMinX + GroupUtil.getWidth((Group) child);
            } else {
                childMaxX = childMinX + child.getWidth() * child.getScaleX();
            }

            if (childMinX < minX) {
                minX = childMinX;
            }
            if (childMaxX > maxX) {
                maxX = childMaxX;
            }
        }

        return (Math.abs(minX) + Math.abs(maxX)) * group.getScaleX();
    }

    /**
     * Returns actual height of group. Calculates only heights of actors (.getHeight() * .getScaleY()).
     */
    public static float getHeight(Group group) {
        SnapshotArray<Actor> children = group.getChildren();
        float minY = 0;
        float maxY = 0;
        for (int i = 0; i < children.size; i++) {
            Actor child = children.get(i);
            float childMinY = child.getY();
            float childMaxY;

            if (!child.isVisible()) {
                continue;
            }

            if (child instanceof MeasurableActor) {
                childMaxY = childMinY + ((MeasurableActor) child).getRealHeight();
            } else if (child instanceof Group) {
                childMaxY = childMinY + GroupUtil.getHeight((Group) child);
            } else {
                childMaxY = childMinY + child.getHeight() * child.getScaleY();
            }

            if (childMinY < minY) {
                minY = childMinY;
            }
            if (childMaxY > maxY) {
                maxY = childMaxY;
            }
        }

        return (Math.abs(minY) + Math.abs(maxY)) * group.getScaleY();
    }
}
