package com.quillraven.quillyscastle.conversation;

public interface ConversationGraphObserver {
    void onConversationEvent(ConversationCommandEvent event, final ConversationGraph graph);

    public static enum ConversationCommandEvent {
        LOAD_STORE_INVENTORY,
        EXIT_CONVERSATION
    }
}
