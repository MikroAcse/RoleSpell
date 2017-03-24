package com.quillraven.quillyscastle.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.utils.Utils;

public class MapManager extends MapSubject {
    private static final String TAG = MapManager.class.getSimpleName();
    private static MapManager instance;
    private final ObjectMap<MapType, Map> mapCache;
    private Map currentMap;
    private MapManager() {
        mapCache = new ObjectMap<MapType, Map>();
    }

    public static final MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }

        return instance;
    }

    public void setMap(MapType type) {
        Map nextMap = mapCache.get(type);

        if (nextMap == null) {
            nextMap = loadMap(type);
            mapCache.put(type, nextMap);
        }

        fireMapChanged(currentMap, nextMap);
        currentMap = nextMap;
        Gdx.app.debug(TAG, "Map changed: " + type);
    }

    private Map loadMap(MapType type) {
        final String filePath = type.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            Gdx.app.debug(TAG, "Trying to load a map without filePath");
            return null;
        }

        Utils.loadMapAsset(filePath);
        if (Utils.isAssetLoaded(filePath)) {
            return new Map(type, Utils.getMapAsset(filePath));
        } else {
            Gdx.app.debug(TAG, "Map not loaded: " + filePath);
            return null;
        }
    }

    /**
     * disposes all loaded maps (including the internal {@link TiledMap} tiledmaps) and
     * its entities
     */
    public void dispose() {
        for (Map map : mapCache.values()) {
            map.dispose();
        }
        Gdx.app.debug(TAG, "disposed!");
    }

    public Entity getSelectedEntity() {
        return currentMap.getCurrentSelectedEntity();
    }

    public void setSelectedEntity(Entity entity) {
        currentMap.setCurrentSelectedEntity(entity);
    }

    public enum MapType {
        TOWN("maps/town.tmx"),
        OVERWORLD("maps/overworld.tmx"),
        CASTLE("maps/castle.tmx");

        private final String filePath;

        MapType(String filePath) {
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }
    }
}
