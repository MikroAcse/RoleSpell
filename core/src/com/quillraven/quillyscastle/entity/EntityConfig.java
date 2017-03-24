package com.quillraven.quillyscastle.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.quillraven.quillyscastle.entity.Entity.AnimationType;
import com.quillraven.quillyscastle.entity.Entity.Direction;
import com.quillraven.quillyscastle.entity.Entity.EntityType;
import com.quillraven.quillyscastle.entity.Entity.State;
import com.quillraven.quillyscastle.inventory.ItemLocation;
import com.quillraven.quillyscastle.utils.Utils;

public class EntityConfig {
    private final static ObjectMap<EntityType, EntityConfig> configCache = new ObjectMap<EntityType, EntityConfig>();

    private EntityType entityType;
    private State state;
    private Direction direction;
    private float speed;
    private String conversationConfigPath;
    private int animationWidth;
    private int animationHeight;
    private String charAtlasID;
    private Array<AnimationConfig> animationConfig;
    private Array<ItemLocation> inventory;

    private EntityConfig() {
        animationConfig = null;
        inventory = null;

        state = State.IDLE;
        direction = Direction.DOWN;
    }

    public static EntityConfig getEntityConfig(EntityType type) {
        EntityConfig result = configCache.get(type);

        if (result == null) {
            // type not loaded yet -> load it
            Object fromJson = Utils.fromJson(null, Gdx.files.internal(type.getFilePath()));

            if (fromJson instanceof Array<?>) {
                // multiple entity types defined within one file
                // -> load each type at once
                for (Object val : (Array<?>) fromJson) {
                    EntityConfig cfg = Utils.readJsonValue(EntityConfig.class, (JsonValue) val);
                    EntityType cfgEntityType = cfg.getEntityType();
                    configCache.put(cfgEntityType, cfg);

                    if (cfgEntityType == type) {
                        result = cfg;
                    }
                }
            } else {
                // only one entity type defined -> load it
                result = Utils.readJsonValue(EntityConfig.class, (JsonValue) fromJson);
                configCache.put(type, result);
            }
        }

        return result;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Entity.Direction getDirection() {
        return direction;
    }

    public void setDirection(Entity.Direction direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getConversationConfigPath() {
        return conversationConfigPath;
    }

    public void setConversationConfigPath(String conversationConfigPath) {
        this.conversationConfigPath = conversationConfigPath;
    }

    public Entity.State getState() {
        return state;
    }

    public void setState(Entity.State state) {
        this.state = state;
    }

    public int getAnimationWidth() {
        return animationWidth;
    }

    public void setAnimationWidth(int animationWidth) {
        this.animationWidth = animationWidth;
    }

    public int getAnimationHeight() {
        return animationHeight;
    }

    public void setAnimationHeight(int animationHeight) {
        this.animationHeight = animationHeight;
    }

    public String getCharAtlasID() {
        return charAtlasID;
    }

    public void setCharAtlasID(String charAtlasID) {
        this.charAtlasID = charAtlasID;
    }

    public Array<AnimationConfig> getAnimationConfig() {
        return animationConfig;
    }

    public void setAnimationConfig(Array<AnimationConfig> animationConfigs) {
        this.animationConfig = animationConfigs;
    }

    public Array<ItemLocation> getInventory() {
        return inventory;
    }

    public void setInventory(Array<ItemLocation> inventory) {
        this.inventory = inventory;
    }

    public static class AnimationConfig {
        private AnimationType animationType;
        private float frameDuration;
        private Array<GridPoint2> gridPoints;

        public AnimationConfig() {
            frameDuration = 1;
            animationType = AnimationType.IDLE;
            gridPoints = null;
        }

        public AnimationType getAnimationType() {
            return animationType;
        }

        public void setAnimationType(AnimationType animationType) {
            this.animationType = animationType;
        }

        public float getFrameDuration() {
            return frameDuration;
        }

        public void setFrameDuration(float frameDuration) {
            this.frameDuration = frameDuration;
        }

        public Array<GridPoint2> getGridPoints() {
            return gridPoints;
        }

        public void setGridPoints(Array<GridPoint2> gridPoints) {
            this.gridPoints = gridPoints;
        }
    }
}
