package ru.mikroacse.engine.actors;

/**
 * Created by MikroAcse on 24.07.2016.
 */
public interface MeasurableActor {
    /**
     * @return Real (visible) group width, considering its children.
     */
    float getRealWidth();

    /**
     * @return Real (visible) group height, considering its children.
     */
    float getRealHeight();
}
