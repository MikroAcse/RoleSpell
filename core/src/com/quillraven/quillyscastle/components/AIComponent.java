package com.quillraven.quillyscastle.components;

import com.badlogic.gdx.Gdx;
import com.quillraven.quillyscastle.entity.Entity.State;

public abstract class AIComponent extends ComponentSubject implements Component {
    private static final String TAG = AIComponent.class.getSimpleName();

    protected State state;

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "disposed!");
    }

    @Override
    public void receiveMessage(MessageType type, Object... args) {
        switch (type) {
            case SET_STATE:
                this.state = (State) args[0];
                break;
            default:
                break;
        }
    }
}
