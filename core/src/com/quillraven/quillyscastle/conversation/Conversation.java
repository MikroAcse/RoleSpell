package com.quillraven.quillyscastle.conversation;

import com.badlogic.gdx.utils.Array;

public class Conversation {
    private String text;
    private Array<ConversationChoice> choices;

    public Conversation() {
        text = null;
        choices = null;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Array<ConversationChoice> getChoices() {
        return choices;
    }

    public void setChoices(Array<ConversationChoice> choices) {
        this.choices = choices;
    }
}
