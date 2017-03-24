package com.quillraven.quillyscastle.map;

import com.badlogic.gdx.utils.Array;

public abstract class MapSubject {
    private final Array<MapObserver> observers;

    protected MapSubject() {
        observers = new Array<MapObserver>();
    }

    public void addObserver(MapObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MapObserver observer) {
        observers.removeValue(observer, true);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    protected void fireMapChanged(Map currentMap, Map newMap) {
        for (MapObserver observer : observers) {
            observer.onMapChanged(currentMap, newMap);
        }
    }
}
