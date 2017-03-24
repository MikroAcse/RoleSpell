package com.quillraven.quillyscastle.components.player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.components.ComponentObserver.ComponentEvent;
import com.quillraven.quillyscastle.components.InputComponent;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.entity.Entity.State;
import com.quillraven.quillyscastle.map.Map;

public class PlayerInputComponent extends InputComponent {
    @Override
    public void receiveMessage(MessageType type, Object... args) {
    }

    @Override
    protected void processInput(Entity entity, GameWorld world, Map map, Camera camera) {
        if (lastPressed != null) {
            if (lastPressed == Keys.LEFT) {
                processLeftPressed(entity);
            } else if (lastPressed == Keys.RIGHT) {
                processRightPressed(entity);
            } else if (lastPressed == Keys.UP) {
                processUpPressed(entity);
            } else if (lastPressed == Keys.DOWN) {
                processDownPressed(entity);
            }

            lastPressed = null;
        } else {
            if (keyPressedMap.get(Keys.LEFT)) {
                processLeftPressed(entity);
            } else if (keyPressedMap.get(Keys.RIGHT)) {
                processRightPressed(entity);
            } else if (keyPressedMap.get(Keys.UP)) {
                processUpPressed(entity);
            } else if (keyPressedMap.get(Keys.DOWN)) {
                processDownPressed(entity);
            } else {
                processNothingPressed(entity);
            }
        }

        if (keyPressedMap.get(Keys.SELECT)) {
            processSelection(entity, map, camera, true);
        } else if (keyPressedMap.get(Keys.DESELECT)) {
            processSelection(entity, map, camera, false);
        }
    }

    private void processLeftPressed(Entity entity) {
        entity.sendMessage(MessageType.SET_STATE, State.WALKING);
        entity.sendMessage(MessageType.SET_DIRECTION, Entity.Direction.LEFT);
    }

    private void processRightPressed(Entity entity) {
        entity.sendMessage(MessageType.SET_STATE, State.WALKING);
        entity.sendMessage(MessageType.SET_DIRECTION, Entity.Direction.RIGHT);
    }

    private void processUpPressed(Entity entity) {
        entity.sendMessage(MessageType.SET_STATE, State.WALKING);
        entity.sendMessage(MessageType.SET_DIRECTION, Entity.Direction.UP);
    }

    private void processDownPressed(Entity entity) {
        entity.sendMessage(MessageType.SET_STATE, State.WALKING);
        entity.sendMessage(MessageType.SET_DIRECTION, Entity.Direction.DOWN);
    }

    private void processNothingPressed(Entity entity) {
        entity.sendMessage(MessageType.SET_STATE, State.IDLE);
    }

    private void processSelection(Entity entity, Map map, Camera camera, boolean selected) {
        camera.unproject(mouseLocation);

        Entity selectedEntity = getEntity(entity, map);
        if (selectedEntity != null) {
            selectedEntity.sendMessage(selected ? MessageType.SELECTED : MessageType.DESELECTED);
            if (selected && selectedEntity.getConfig().getConversationConfigPath() != null) {
                map.setCurrentSelectedEntity(selectedEntity);
                fireComponentEvent(ComponentEvent.LOAD_CONVERSATION, selectedEntity.getConfig().getConversationConfigPath(), selectedEntity.getType());
            }
        }
    }

    private Entity getEntity(Entity entity, Map map) {
        Array<Entity> entities = map.getEntities();
        for (int i = entities.size - 1; i >= 0; --i) {
            final Rectangle box = entities.get(i).getBoundingBox();
            Rectangle playerBox = entity.getBoundingBox();
            final float a = playerBox.x - box.x;
            final float b = playerBox.y - box.y;
            final float distance2 = (a * a + b * b);

            if (box.contains(mouseLocation.x, mouseLocation.y) && distance2 < 1.5) {
                return entities.get(i);
            }
        }

        return null;
    }
}
