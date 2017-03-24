package com.quillraven.quillyscastle.components.npc;

import com.badlogic.gdx.graphics.Camera;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.components.PhysicsComponent;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.entity.Entity.Direction;
import com.quillraven.quillyscastle.entity.Entity.State;
import com.quillraven.quillyscastle.map.Map;

public class NPCPhysicsComponent extends PhysicsComponent {
    @Override
    public void receiveMessage(MessageType type, Object... args) {
        super.receiveMessage(type, args);

        switch (type) {
            case COLLISION_WITH_ENTITY:
                if (state != State.IMMOBILE) {
                    // try to move away in orthogonal direction to allow the entity triggering the collision
                    // to pass through this entity's position
                    final Direction directionOfCollidingEntity = (Direction) args[1];

                    if (!direction.isOrthogonal(directionOfCollidingEntity)) {
                        direction = direction.getOrthogonal(directionOfCollidingEntity);
                        state = State.WALKING;
                        updateVelocity();
                    }
                }
                break;
            case COLLISION_WITH_MAP:
                if (state != State.IMMOBILE) {
                    // turn around and move away from the map border
                    direction = direction.getOpposite();
                    state = State.WALKING;
                    updateVelocity();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void update(Entity entity, GameWorld world, Map map, Camera camera, float deltaTime) {
        if (!velocity.isZero()) {
            updateLocation(entity, world, map, deltaTime);
        }
    }

    private void updateLocation(Entity entity, GameWorld world, Map map, float deltaTime) {
        final float origX = collisionBox.x;
        final float origY = collisionBox.y;

        if (isLocationPathable(map, collisionBox.setPosition(origX + velocity.x * deltaTime, origY + velocity.y * deltaTime))) {
            final Entity collisionEntity = getCollidingEntity(entity, world, map, collisionBox);

            if (collisionEntity != null) {
                // new position is blocked by entity -> reset position
                collisionBox.setPosition(origX, origY);
                collisionEntity.sendMessage(MessageType.COLLISION_WITH_ENTITY, entity.getType(), direction);
            } else {
                // no collision -> update position
                entity.sendMessage(MessageType.SET_LOCATION, collisionBox.x, collisionBox.y);
            }
        } else {
            // collision with map -> reset position
            collisionBox.setPosition(origX, origY);
            entity.sendMessage(MessageType.COLLISION_WITH_MAP);
        }
    }
}
