package com.quillraven.quillyscastle.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.components.*;
import com.quillraven.quillyscastle.components.Component.MessageType;
import com.quillraven.quillyscastle.components.npc.NPCAIComponent;
import com.quillraven.quillyscastle.components.npc.NPCGraphicsComponent;
import com.quillraven.quillyscastle.components.npc.NPCInventoryComponent;
import com.quillraven.quillyscastle.components.npc.NPCPhysicsComponent;
import com.quillraven.quillyscastle.components.player.*;
import com.quillraven.quillyscastle.map.Map;

public class Entity {
    private static final String TAG = Entity.class.getSimpleName();
    private final EntityType type;
    private final EntityConfig config;
    private final Array<Component> components;
    private final AIComponent aiComponent;
    private final PhysicsComponent physicsComponent;
    private final GraphicsComponent graphicsComponent;
    private Entity(EntityType type, EntityConfig config, PhysicsComponent physicsComponent, GraphicsComponent graphicsComponent, AIComponent aiComponent) {
        this.type = type;
        this.config = config;

        this.physicsComponent = physicsComponent;
        this.graphicsComponent = graphicsComponent;
        this.aiComponent = aiComponent;

        components = new Array<Component>();
        components.add(this.physicsComponent);
        components.add(this.aiComponent);
        components.add(this.graphicsComponent);
    }

    /**
     * Creates an entity at the given location initializing it by loadings its animations,
     * AI state, direction, size and speed.
     *
     * @param type type of entity to be created
     * @param x    x-coordinate to spawn the entity
     * @param y    y-coordinate to spawn the entity
     * @return entity of type <b>type</b> at location <b>x</b> and <b>y</b>
     */
    public static Entity getEntity(EntityType type, float x, float y) {
        Entity result = null;

        EntityConfig config = EntityConfig.getEntityConfig(type);

        switch (type) {
            case PLAYER:
                result = new Entity(config.getEntityType(), config, new PlayerPhysicsComponent(), new PlayerGraphicsComponent(), new PlayerAIComponent());
                result.addComponent(new PlayerInputComponent());
                result.addComponent(new PlayerInventoryComponent());
                break;
            default:
                result = new Entity(config.getEntityType(), config, new NPCPhysicsComponent(), new NPCGraphicsComponent(), new NPCAIComponent());
                if (config.getInventory() != null) {
                    result.addComponent(new NPCInventoryComponent());
                }
                break;
        }

        result.sendMessage(MessageType.LOAD_ANIMATIONS, config);
        result.sendMessage(MessageType.SET_LOCATION, x, y);
        result.sendMessage(MessageType.SET_STATE, config.getState());
        result.sendMessage(MessageType.SET_DIRECTION, config.getDirection());
        result.sendMessage(MessageType.SET_SIZE, config.getAnimationWidth() * GameWorld.UNIT_SCALE, config.getAnimationHeight() * GameWorld.UNIT_SCALE);
        result.sendMessage(MessageType.SET_SPEED, config.getSpeed());
        if (config.getInventory() != null) {
            result.sendMessage(MessageType.LOAD_INVENTORY, config);
        }

        return result;
    }

    public void dispose() {
        for (int i = components.size - 1; i >= 0; --i) {
            components.get(i).dispose();
        }
        graphicsComponent.dispose();
        Gdx.app.debug(TAG, "disposed!");
    }

    public EntityType getType() {
        return type;
    }

    public EntityConfig getConfig() {
        return config;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void sendMessage(Component.MessageType type, Object... args) {
        // do not use the iterator loop here because update can call
        // the "sendMessage" method which results in a nested iterator exception
        for (int i = components.size - 1; i >= 0; --i) {
            components.get(i).receiveMessage(type, args);
        }
    }

    public void update(GameWorld world, Map map, Camera camera, float deltaTime) {
        // do not use the iterator loop here because update can call
        // the "sendMessage" method which results in a nested iterator exception
        for (int i = components.size - 1; i >= 0; --i) {
            components.get(i).update(this, world, map, camera, deltaTime);
        }
    }

    public void addObserver(ComponentObserver observer) {
        for (int i = components.size - 1; i >= 0; --i) {
            components.get(i).addObserver(observer);
        }
    }

    public void removeObserver(ComponentObserver observer) {
        for (int i = components.size - 1; i >= 0; --i) {
            components.get(i).removeObserver(observer);
        }
    }

    public void removeAllObservers() {
        for (int i = components.size - 1; i >= 0; --i) {
            components.get(i).removeAllObservers();
        }
    }

    public void render(final float interpolationValue, Batch batch, ShapeRenderer shapeRenderer) {
        graphicsComponent.render(this, interpolationValue, batch, shapeRenderer);
    }

    public Rectangle getCollisionBox() {
        return physicsComponent.getCollisionBox();
    }

    public Rectangle getBoundingBox() {
        return physicsComponent.getBoundingBox();
    }

    public Vector2 getSize() {
        return physicsComponent.getSize();
    }

    public <T extends Component> T getComponent(Class<T> type) {
        for (Component comp : components) {
            if (type.isInstance(comp)) {
                return type.cast(comp);
            }
        }

        return null;
    }

    public enum EntityType {
        PLAYER("scripts/entities.json"),
        TOWN_FOLK1("scripts/town_folk.json"),
        TOWN_FOLK2("scripts/town_folk.json"),
        TOWN_FOLK3("scripts/town_folk.json");

        private final String filePath;

        EntityType(String filePath) {
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    public enum State {
        IDLE,
        WALKING,
        IMMOBILE; // this one needs to be last

        public static State getRandom() {
            // ignore IMMOBILE state
            return State.values()[MathUtils.random(State.values().length - 2)];
        }
    }

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        public static Direction getRandom() {
            return Direction.values()[MathUtils.random(Direction.values().length - 1)];
        }

        public Direction getOpposite() {
            return Direction.values()[(this.ordinal() + 2) % Direction.values().length];
        }

        public boolean isOrthogonal(Direction toDirection) {
            switch (toDirection) {
                case UP:
                case DOWN:
                    return this == RIGHT || this == LEFT;
                case RIGHT:
                case LEFT:
                    return this == UP || this == DOWN;
                default:
                    return false;
            }
        }

        public Direction getOrthogonal(Direction ofDirection) {
            switch (ofDirection) {
                case UP:
                    return RIGHT;
                case RIGHT:
                    return DOWN;
                case DOWN:
                    return LEFT;
                case LEFT:
                    return UP;
                default:
                    return null;
            }
        }
    }

    public enum AnimationType {
        IDLE,
        WALK_UP,
        WALK_RIGHT,
        WALK_DOWN,
        WALK_LEFT,
        IMMOBILE
    }
}
