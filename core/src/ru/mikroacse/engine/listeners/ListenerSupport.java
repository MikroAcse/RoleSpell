package ru.mikroacse.engine.listeners;

/**
 * Created by MikroAcse on 29.07.2016.
 */
public interface ListenerSupport<T extends Listener> {
    void addListener(T listener);

    void removeListener(T listener);

    void clearListeners();
}
