package ru.mikroacse.rolespell.app.model.game.world.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.RecursiveConfigurationNode;

import java.util.Map;

/**
 * Created by ASUS on 29.06.2017.
 */
public class MapConfig extends RecursiveConfigurationNode<MapConfig> {
    public MapConfig(Map<String, Object> map, MapConfig parent) {
        super(map, parent);
    }

    public MapConfig(ConfigurationNode node, MapConfig parent) {
        super(node, parent);
    }

    public MapConfig(Map<String, Object> map) {
        super(map);
    }

    public MapConfig(ConfigurationNode node) {
        super(node);
    }
}
