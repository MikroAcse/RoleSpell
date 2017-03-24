package com.quillraven.quillyscastle.conversation;

import com.badlogic.gdx.utils.Array;
import com.quillraven.quillyscastle.conversation.ConversationGraphObserver.ConversationCommandEvent;

public abstract class ConversationGraphSubject {
    private final Array<ConversationGraphObserver> observers;

    protected ConversationGraphSubject() {
        observers = new Array<ConversationGraphObserver>();
    }

    public void addObserver(ConversationGraphObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ConversationGraphObserver observer) {
        observers.removeValue(observer, true);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    public void fireConversationEvent(ConversationCommandEvent event, final ConversationGraph graph) {
        for (ConversationGraphObserver observer : observers) {
            observer.onConversationEvent(event, graph);
        }
    }
}
