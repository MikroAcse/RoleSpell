package com.quillraven.quillyscastle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.quillraven.quillyscastle.components.Component.MessageType;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.entity.Entity.EntityType;
import com.quillraven.quillyscastle.map.Map;
import com.quillraven.quillyscastle.map.MapManager;
import com.quillraven.quillyscastle.map.MapManager.MapType;
import com.quillraven.quillyscastle.map.MapObserver;
import com.quillraven.quillyscastle.profile.ProfileManager;
import com.quillraven.quillyscastle.profile.ProfileObserver;

public class GameWorld implements ProfileObserver, MapObserver {
    private final static String TAG = GameWorld.class.getSimpleName();

    private final static int PIXELS_PER_WORLD_UNIT = 32;
    public final static float UNIT_SCALE = 1f / PIXELS_PER_WORLD_UNIT;

    private final Entity player;
    private final OrthographicCamera camera;
    private Array<Entity> entities;
    private Map currentMap;

    public GameWorld() {
        this.camera = new OrthographicCamera();

        player = Entity.getEntity(EntityType.PLAYER, 0, 0);

        ProfileManager.getInstance().addObserver(this);
        MapManager.getInstance().addObserver(this);
    }

    public void dispose() {
        // dispose maps and map entities
        MapManager.getInstance().dispose();
        // dispose entities
        player.dispose();

        Gdx.app.debug(TAG, "disposed!");
    }

    public void update(float deltaTime) {
        player.update(this, currentMap, camera, deltaTime);

        for (Entity entity : entities) {
            entity.update(this, currentMap, camera, deltaTime);
        }
    }

    public Entity getPlayer() {
        return player;
    }

    public Array<Entity> getEntities() {
        return entities;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void onSave(ProfileManager manager) {
        manager.setProperty("currentMap", currentMap.getType().toString());
        manager.setProperty("playerX", player.getCollisionBox().x);
        manager.setProperty("playerY", player.getCollisionBox().y);
    }

    @Override
    public void onLoad(ProfileManager manager) {
        final String mapToLoad = manager.getProperty("currentMap", String.class);
        Float playerX = manager.getProperty("playerX", Float.class);
        Float playerY = manager.getProperty("playerY", Float.class);

        if (mapToLoad == null || mapToLoad.trim().isEmpty()) {
            // there was no map saved -> load default TOWN map
            MapManager.getInstance().setMap(MapType.TOWN);
        } else {
            MapManager.getInstance().setMap(MapType.valueOf(mapToLoad));
        }

        if (playerX == null || playerY == null) {
            // there was no entities location saved -> user map default start location
            Vector2 playerStartLoc = currentMap.getStartLocation();
            playerX = playerStartLoc.x;
            playerY = playerStartLoc.y;
        }

        player.sendMessage(MessageType.SET_LOCATION, playerX, playerY);
    }

    @Override
    public void onMapChanged(Map currentMap, Map newMap) {
        this.currentMap = newMap;
        entities = this.currentMap.getEntities();

        final Vector2 startLocation = this.currentMap.getStartLocation();
        player.sendMessage(MessageType.SET_LOCATION, startLocation.x, startLocation.y);
    }
}
