package com.quillraven.quillyscastle.components;

import com.badlogic.gdx.graphics.Camera;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.map.Map;

public interface Component {
    public void dispose();

    public void receiveMessage(MessageType type, Object... args);

    public void update(Entity entity, GameWorld world, Map map, Camera camera, float deltaTime);

    public void addObserver(ComponentObserver conversationObserver);

    public void removeObserver(ComponentObserver conversationObserver);

    public void removeAllObservers();

    public enum MessageType {
        LOAD_ANIMATIONS,
        SET_LOCATION,
        SET_STATE,
        SET_DIRECTION,
        SET_SIZE,
        SET_SPEED,
        COLLISION_WITH_MAP,
        COLLISION_WITH_ENTITY,
        SELECTED,
        DESELECTED,
        LOAD_INVENTORY,
        MOVE_ITEM,
        INIT_PLAYER_HUD,
        SELL_ITEM,
        BUY_ITEM
    }
}
