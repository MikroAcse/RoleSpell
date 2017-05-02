package ru.mikroacse.engine.actors;

/**
 * Created by MikroAcse on 24.07.2016.
 */
// TODO: lame class name
public interface RealActor {
    /**
     * @return Real (visible) group width, considering its children.
     */
    float getRealWidth();
    
    /**
     * @return Real (visible) group height, considering its children.
     */
   float getRealHeight();
}
