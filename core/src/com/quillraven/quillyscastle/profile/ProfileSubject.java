package com.quillraven.quillyscastle.profile;

import com.badlogic.gdx.utils.Array;

public abstract class ProfileSubject {
    private final Array<ProfileObserver> observers;

    protected ProfileSubject() {
        observers = new Array<ProfileObserver>();
    }

    public void addObserver(ProfileObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ProfileObserver observer) {
        observers.removeValue(observer, true);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    protected void fireSave(ProfileManager manager) {
        for (ProfileObserver observer : observers) {
            observer.onSave(manager);
        }
    }

    protected void fireLoad(ProfileManager manager) {
        for (ProfileObserver observer : observers) {
            observer.onLoad(manager);
        }
    }
}
