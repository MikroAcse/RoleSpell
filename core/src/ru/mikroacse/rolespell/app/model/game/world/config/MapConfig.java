package ru.mikroacse.rolespell.app.model.game.world.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.RecursiveConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.app.model.game.world.WorldMap.Meta;
import ru.mikroacse.rolespell.app.model.game.world.MapRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ASUS on 29.06.2017.
 */
public class MapConfig extends RecursiveConfigurationNode<MapConfig> {
    private String parent;

    public MapConfig(Map<String, Object> map, String parent) {
        super(map);

        this.parent = parent;
    }

    public MapConfig(ConfigurationNode node, String parent) {
        super(node);

        this.parent = parent;
    }

    public MapConfig(Map<String, Object> map) {
        super(map);
    }

    public MapConfig(ConfigurationNode node) {
        super(node);
    }

    public String getName(String defaultValue) {
        return get("name", defaultValue);
    }

    public Set<String> getEntities() {
        return keySet("entities");
    }

    public Set<String> getPortals() {
        return keySet("portals");
    }

    public Set<String> getItems() {
        return keySet("items");
    }

    public double getWeight(Meta meta, double defaultValue) {
        return get("meta." + meta.name() + ".weight", defaultValue);
    }

    public boolean isPassable(Meta meta, boolean defaultValue) {
        return get("meta." + meta.name() + ".passable", defaultValue);
    }

    @Override
    public MapConfig getParent() {
        return MapRepository.instance.get(parent);
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }
}
