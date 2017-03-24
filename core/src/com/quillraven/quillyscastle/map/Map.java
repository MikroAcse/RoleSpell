package com.quillraven.quillyscastle.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.entity.Entity.EntityType;
import com.quillraven.quillyscastle.map.MapManager.MapType;

public class Map {
    private final static String TAG = Map.class.getSimpleName();

    private final static String MAP_COLLISION_LAYER = "Collision";
    private final static String MAP_SPAWNS_LAYER = "Spawns";
    private final static String MAP_PORTALS_LAYER = "Portals";
    private static final String START_LOCATION = "START_LOC";

    private final MapType type;

    private final TiledMap tiledMap;
    private final Array<Entity> entities;
    private final Array<Portal> portals;
    private final MapLayer collisionLayer;
    private final MapLayer spawnsLayer;
    private final Vector2 tileSize;
    private final Rectangle boundingBox;
    private final Vector2 startLocation;
    private Entity currentSelectedEntity;

    public Map(MapType type, TiledMap map) {
        this.type = type;
        this.tiledMap = map;

        this.tileSize = new Vector2( // params
                map.getProperties().get("tilewidth", Integer.class) * GameWorld.UNIT_SCALE, // width
                map.getProperties().get("tileheight", Integer.class) * GameWorld.UNIT_SCALE // height
        );
        this.boundingBox = new Rectangle( // params
                0, 0, // position
                map.getProperties().get("width", Integer.class) * tileSize.x, // width
                map.getProperties().get("height", Integer.class) * tileSize.y // height
        );

        entities = new Array<Entity>();
        portals = new Array<Portal>();
        startLocation = new Vector2(-1, -1);

        collisionLayer = map.getLayers().get(MAP_COLLISION_LAYER);
        parseCollisionLayer();

        parsePortalLayer(map.getLayers().get(MAP_PORTALS_LAYER));

        spawnsLayer = map.getLayers().get(MAP_SPAWNS_LAYER);
        parseSpawnLayer();

        if (startLocation.x == -1) {
            Gdx.app.error(TAG, type + " does not have a start location within Tiled!");
        }
    }

    public void dispose() {
        tiledMap.dispose();
        for (Entity entity : entities) {
            entity.dispose();
        }
        Gdx.app.debug(TAG, type + " disposed!");
    }

    private void parseCollisionLayer() {
        if (collisionLayer == null) {
            Gdx.app.debug(TAG, "No collision layer!");
            return;
        }

        for (MapObject object : collisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                rect.set( // update Tiled editor real values with our game world values
                        rect.x * GameWorld.UNIT_SCALE, // scale x
                        rect.y * GameWorld.UNIT_SCALE, // scale y
                        rect.width * GameWorld.UNIT_SCALE, // scale width
                        rect.height * GameWorld.UNIT_SCALE // scale height
                );
            }
        }
    }

    private void parsePortalLayer(MapLayer layer) {
        if (layer == null) {
            Gdx.app.debug(TAG, "No portal layer!");
            return;
        }

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                final Rectangle rect = ((RectangleMapObject) object).getRectangle();

                if (START_LOCATION.equals(object.getName())) {
                    rect.getPosition(startLocation);
                    startLocation.scl(GameWorld.UNIT_SCALE);
                } else {
                    portals.add(new Portal(
                            rect.set( // update Tiled editor real values with our game world values
                                    rect.x * GameWorld.UNIT_SCALE, // scale x
                                    rect.y * GameWorld.UNIT_SCALE, // scale y
                                    rect.width * GameWorld.UNIT_SCALE, // scale width
                                    rect.height * GameWorld.UNIT_SCALE), // scale height,
                            MapType.valueOf(object.getName()), // target map
                            (int) (Integer.parseInt(object.getProperties().get("targetX", String.class)) * tileSize.x), // target tile index x
                            (int) (Integer.parseInt(object.getProperties().get("targetY", String.class)) * tileSize.y)) // target tile index y
                    );
                }
            }
        }
    }

    private void parseSpawnLayer() {
        if (spawnsLayer == null) {
            Gdx.app.debug(TAG, "No spawn layer!");
            return;
        }

        for (MapObject object : spawnsLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                entities.add(Entity.getEntity( // params
                        EntityType.valueOf(object.getName()), // entity type
                        ((RectangleMapObject) object).getRectangle().x * GameWorld.UNIT_SCALE, // start x
                        ((RectangleMapObject) object).getRectangle().y * GameWorld.UNIT_SCALE // start y
                ));
            }
        }
    }

    public Array<Entity> getEntities() {
        return entities;
    }

    public Vector2 getTileSize() {
        return tileSize;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public MapLayer getCollisionLayer() {
        return collisionLayer;
    }

    public Array<Portal> getPortals() {
        return portals;
    }

    public MapType getType() {
        return type;
    }

    public Vector2 getStartLocation() {
        return startLocation;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Entity getCurrentSelectedEntity() {
        return currentSelectedEntity;
    }

    public void setCurrentSelectedEntity(Entity currentSelectedEntity) {
        this.currentSelectedEntity = currentSelectedEntity;
    }
}
