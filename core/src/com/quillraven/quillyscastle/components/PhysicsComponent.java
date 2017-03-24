package com.quillraven.quillyscastle.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.entity.Entity.Direction;
import com.quillraven.quillyscastle.entity.Entity.State;
import com.quillraven.quillyscastle.map.Map;

public abstract class PhysicsComponent extends ComponentSubject implements Component {
    private static final String TAG = PhysicsComponent.class.getSimpleName();

    protected State state;
    protected Rectangle collisionBox;
    protected Rectangle boundingBox;
    protected Vector2 size;
    protected float speed;
    protected Vector2 velocity;
    protected Direction direction;

    public PhysicsComponent() {
        size = new Vector2(1, 1);
        velocity = new Vector2(0, 0);
        boundingBox = new Rectangle(0, 0, 1, 1);
        collisionBox = new Rectangle(0, 0, 1, 0.2f);
        speed = 0f;
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "disposed!");
    }

    @Override
    public void receiveMessage(MessageType type, Object... args) {
        switch (type) {
            case SET_SPEED:
                this.speed = (Float) args[0];
                updateVelocity();
                break;
            case SET_SIZE:
                this.size.set((Float) args[0], (Float) args[1]);
                this.collisionBox.setSize(size.x, size.y * 0.2f);
                this.boundingBox.setSize(size.x, size.y);
                break;
            case SET_DIRECTION:
                this.direction = (Direction) args[0];
                updateVelocity();
                break;
            case SET_LOCATION:
                final Float x = (Float) args[0];
                final Float y = (Float) args[1];
                this.collisionBox.setPosition(x, y);
                this.boundingBox.setPosition(x, y);
                break;
            case SET_STATE:
                this.state = (State) args[0];
                updateVelocity();
                break;
            default:
                break;
        }
    }

    protected void updateVelocity() {
        if (state == null || direction == null) {
            // not yet initialized
            return;
        }

        switch (state) {
            case IDLE:
            case IMMOBILE:
                this.velocity.set(0, 0);
                break;
            case WALKING:
                switch (direction) {
                    case UP:
                        this.velocity.set(0, speed);
                        break;
                    case DOWN:
                        this.velocity.set(0, -speed);
                        break;
                    case LEFT:
                        this.velocity.set(-speed, 0);
                        break;
                    case RIGHT:
                        this.velocity.set(speed, 0);
                        break;
                }
                break;
        }
    }

    protected boolean isLocationPathable(Map map, Rectangle collisionBox) {
        if (!map.getBoundingBox().contains(collisionBox)) {
            return false;
        }

        for (MapObject object : map.getCollisionLayer().getObjects()) {
            if (object instanceof RectangleMapObject) {
                if (collisionBox.overlaps(((RectangleMapObject) object).getRectangle())) {
                    return false;
                }
            }
        }

        return true;
    }

    protected Entity getCollidingEntity(Entity entity, GameWorld world, Map map, Rectangle collisionBox) {
        final Array<Entity> entities = map.getEntities();
        for (int i = entities.size - 1; i >= 0; --i) {
            if (entity == entities.get(i)) {
                continue;
            }

            if (collisionBox.overlaps(entities.get(i).getCollisionBox())) {
                return entities.get(i);
            }
        }

        final Entity player = world.getPlayer();
        if (entity != player && collisionBox.overlaps(player.getCollisionBox())) {
            return player;
        }

        return null;
    }

    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public Vector2 getSize() {
        return size;
    }
}
