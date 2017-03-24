package com.quillraven.quillyscastle.components;

import com.badlogic.gdx.utils.Array;
import com.quillraven.quillyscastle.components.ComponentObserver.ComponentEvent;

public class ComponentSubject {
    private Array<ComponentObserver> observers;

    public ComponentSubject() {
        observers = new Array<ComponentObserver>();
    }

    public void addObserver(ComponentObserver conversationObserver) {
        observers.add(conversationObserver);
    }

    public void removeObserver(ComponentObserver conversationObserver) {
        observers.removeValue(conversationObserver, true);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    protected void fireComponentEvent(ComponentEvent event, final Object... args) {
        for (ComponentObserver observer : observers) {
            observer.onComponentEvent(event, args);
        }
    }
}
