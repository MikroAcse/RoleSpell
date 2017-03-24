package com.quillraven.quillyscastle.conversation;

import com.quillraven.quillyscastle.conversation.ConversationGraphObserver.ConversationCommandEvent;

public class ConversationChoice {
    private String destinationID;
    private String text;
    private ConversationCommandEvent conversationCommandEvent;

    public ConversationChoice() {
        destinationID = text = null;
        conversationCommandEvent = null;
    }

    public String getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(String destinationID) {
        this.destinationID = destinationID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ConversationCommandEvent getConversationCommandEvent() {
        return conversationCommandEvent;
    }

    public void setConversationCommandEvent(ConversationCommandEvent conversationCommandEvent) {
        this.conversationCommandEvent = conversationCommandEvent;
    }

    @Override
    public String toString() {
        // this text is displayed in the ConversationUI List widget
        return text;
    }
}
