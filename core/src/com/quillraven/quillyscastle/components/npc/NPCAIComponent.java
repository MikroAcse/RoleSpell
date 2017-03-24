package com.quillraven.quillyscastle.components.npc;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.components.AIComponent;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.entity.Entity.Direction;
import com.quillraven.quillyscastle.entity.Entity.State;
import com.quillraven.quillyscastle.map.Map;

public class NPCAIComponent extends AIComponent {
    protected float stateTime;

    public NPCAIComponent() {
        stateTime = 2;
    }

    @Override
    public void receiveMessage(MessageType type, Object... args) {
        super.receiveMessage(type, args);

        switch (type) {
            case COLLISION_WITH_ENTITY:
            case COLLISION_WITH_MAP:
                if (state != State.IMMOBILE) {
                    // move for 3 seconds when colliding with something
                    //
                    // the physics component will try to move the entity in orthogonal direction
                    // of the colliding entity
                    state = State.WALKING;
                    stateTime = 3;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void update(Entity entity, GameWorld world, Map map, Camera camera, float deltaTime) {
        if (State.IMMOBILE != state) {
            stateTime -= deltaTime;
            if (stateTime <= 0) {
                // change state
                stateTime = MathUtils.random(2);
                State nextState = State.getRandom();
                if (State.WALKING == nextState) {
                    entity.sendMessage(MessageType.SET_STATE, State.WALKING, Direction.getRandom());
                } else {
                    entity.sendMessage(MessageType.SET_STATE, nextState);
                }
            }

        }
    }
}
