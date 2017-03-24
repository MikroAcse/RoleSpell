package com.quillraven.quillyscastle.components;

public interface ComponentObserver {
    void onComponentEvent(ComponentEvent event, final Object... args);

    public static enum ComponentEvent {
        LOAD_CONVERSATION,
        SHOW_CONVERSATION,
        HIDE_CONVERSATION,
        LOAD_INVENTORY,
        INVENTORY_SLOT_UPDATED,
        GOLD_UPDATED,
        LOAD_STORE,
        NOT_ENOUGH_GOLD,
        NOT_ENOUGH_INVENTORY_SPACE
    }
}
