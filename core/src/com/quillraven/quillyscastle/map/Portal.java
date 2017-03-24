package com.quillraven.quillyscastle.map;

import com.badlogic.gdx.math.Rectangle;
import com.quillraven.quillyscastle.map.MapManager.MapType;

public class Portal {
    private final Rectangle boundingBox;
    private final MapType targetMap;
    private final float targetX;
    private final float targetY;
    private boolean active;

    public Portal(Rectangle boundingBox, MapType targetMap, int targetX, int targetY) {
        this.boundingBox = boundingBox;
        this.targetMap = targetMap;
        this.targetX = targetX;
        this.targetY = targetY;
        this.active = true;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public MapType getTargetMap() {
        return targetMap;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
