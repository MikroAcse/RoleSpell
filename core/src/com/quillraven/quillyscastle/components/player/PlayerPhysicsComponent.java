package com.quillraven.quillyscastle.components.player;

import com.badlogic.gdx.graphics.Camera;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.components.PhysicsComponent;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.map.Map;
import com.quillraven.quillyscastle.map.MapManager;
import com.quillraven.quillyscastle.map.Portal;

public class PlayerPhysicsComponent extends PhysicsComponent {
    @Override
    public void update(Entity entity, GameWorld world, Map map, Camera camera, float deltaTime) {
        if (!velocity.isZero()) {
            updateLocation(entity, world, map, deltaTime);
        }

        checkPortalActivation(entity, world, map);
    }

    private void updateLocation(Entity entity, GameWorld world, Map map, float deltaTime) {
        final float origX = collisionBox.x;
        final float origY = collisionBox.y;

        if (isLocationPathable(map, collisionBox.setPosition(origX + velocity.x * deltaTime, origY + velocity.y * deltaTime))) {
            final Entity collisionEntity = getCollidingEntity(entity, world, map, collisionBox);

            if (collisionEntity != null) {
                // new position is blocked by entity -> reset position
                collisionBox.setPosition(origX, origY);
                // inform colliding entity -> if it is a non-immobile npc it will try to move away in orthogonal direction
                collisionEntity.sendMessage(MessageType.COLLISION_WITH_ENTITY, entity.getType(), direction);
            } else {
                // no collision -> update position
                entity.sendMessage(MessageType.SET_LOCATION, collisionBox.x, collisionBox.y);
            }
        } else {
            // collision with map -> reset position
            collisionBox.setPosition(origX, origY);
        }
    }

    private void checkPortalActivation(Entity entity, GameWorld world, Map map) {
        Portal activatedPortal = null;
        for (Portal portal : map.getPortals()) {
            if (collisionBox.overlaps(portal.getBoundingBox())) {
                activatedPortal = portal;
                break;
            }
        }

        if (activatedPortal != null) {
            MapManager.getInstance().setMap(activatedPortal.getTargetMap());
            entity.sendMessage(MessageType.SET_LOCATION, activatedPortal.getTargetX(), activatedPortal.getTargetY());
        }
    }
}
