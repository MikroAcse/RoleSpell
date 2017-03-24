package com.quillraven.quillyscastle.components.npc;

import com.quillraven.quillyscastle.components.GraphicsComponent;
import com.quillraven.quillyscastle.entity.Entity.Direction;
import com.quillraven.quillyscastle.entity.Entity.State;

public class NPCGraphicsComponent extends GraphicsComponent {
    protected boolean selected;

    @Override
    public void receiveMessage(MessageType type, Object... args) {
        super.receiveMessage(type, args);

        switch (type) {
            case SELECTED:
                selected = true;
                break;
            case DESELECTED:
                selected = false;
                break;
            case COLLISION_WITH_ENTITY:
                if (state != State.IMMOBILE) {
                    // try to move away in orthogonal direction to allow the entity triggering the collision
                    // to pass through this entity's position
                    final Direction directionOfCollidingEntity = (Direction) args[1];

                    if (!direction.isOrthogonal(directionOfCollidingEntity)) {
                        direction = direction.getOrthogonal(directionOfCollidingEntity);
                        state = State.WALKING;
                        updateCurrentAnimation();
                    }
                }
                break;
            case COLLISION_WITH_MAP:
                if (state != State.IMMOBILE) {
                    // turn around and move away from the map border
                    state = State.WALKING;
                    direction = direction.getOpposite();
                    updateCurrentAnimation();
                }
                break;
            default:
                break;
        }
    }
}
