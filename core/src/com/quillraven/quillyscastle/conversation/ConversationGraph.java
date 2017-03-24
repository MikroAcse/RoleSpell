package com.quillraven.quillyscastle.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ConversationGraph extends ConversationGraphSubject {
    private final static String TAG = ConversationGraph.class.getSimpleName();

    private ObjectMap<String, Conversation> conversations;
    private String currentConversationID;

    public ConversationGraph() {
        conversations = null;
        currentConversationID = null;
    }

    public ConversationGraph(ObjectMap<String, Conversation> conversations, String rootID) {
        super();
        setConversations(conversations);
        setCurrentConversation(rootID);
    }

    public void setConversations(ObjectMap<String, Conversation> conversations) {
        if (conversations == null || conversations.size == 0) {
            Gdx.app.error(TAG, "Conversationgraph cannot have missing or empty conversations");
            return;
        }

        this.conversations = conversations;
    }

    public void setCurrentConversation(String convID) {
        final Conversation conversation = getConversationByID(convID);
        if (conversation == null) {
            Gdx.app.debug(TAG, "Trying to set a conversation that does not exist: " + convID);
        }

        if (currentConversationID == null || currentConversationID.equals(convID) || isReachable(convID)) {
            // there is no current conversation yet or
            // conversation of given id is reachable then we can change to it
            this.currentConversationID = convID;
        } else {
            Gdx.app.debug(TAG, "Conversation " + convID + " is not reachable from current conversation " + currentConversationID);
        }
    }

    public Conversation getConversationByID(String ID) {
        if (!isValid(ID)) {
            Gdx.app.debug(TAG, "Conversation " + ID + " is not valid");
            return null;
        }

        return conversations.get(ID);
    }

    public boolean isValid(String conversationID) {
        return conversationID != null && conversations.get(conversationID) != null;
    }

    private boolean isReachable(String destinationID) {
        if (!isValid(currentConversationID) || !isValid(destinationID)) {
            return false;
        }

        // get choices from the current conversation
        final Array<ConversationChoice> choices = getCurrentChoices();
        if (choices == null || choices.size == 0) {
            return false;
        }

        // check if there is a choice from conversation source to conversation target
        for (ConversationChoice choice : choices) {
            if (choice.getDestinationID().equals(destinationID)) {
                return true;
            }
        }

        return false;
    }

    public Array<ConversationChoice> getCurrentChoices() {
        if (!isValid(currentConversationID)) {
            return null;
        }

        return conversations.get(currentConversationID).getChoices();
    }

    public String getCurrentConversationID() {
        return currentConversationID;
    }

    public void setCurrentConversationID(String currentConversationID) {
        this.currentConversationID = currentConversationID;
    }

    public String getCurrentConversationText() {
        return conversations.get(currentConversationID).getText();
    }

}
