package ru.mikroacse.rolespell.app.model.game.entities.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.RecursiveConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.entities.EntityRepository;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;

import java.util.Map;

/**
 * Created by MikroAcse on 24.06.2017.
 */
public class EntityConfig extends RecursiveConfigurationNode<EntityConfig> {
    private String parent;

    public EntityConfig(Map<String, Object> map, String parent) {
        super(map);

        this.parent = parent;
    }

    public double getPickupDistance(double defaultValue) {
        return get("pickup_distance", defaultValue);
    }

    public String getName(String defaultValue) {
        return get("name", defaultValue);
    }

    public EntityType getType(EntityType defaultValue) {
        String type = get("type", null);

        System.out.println("get type: " + type + " " + defaultValue + " " + get("type", null) + " " + getParent() + " " + parent);

        return type != null? EntityType.valueOf(type) : defaultValue;
    }

    public boolean isShared(boolean defaultValue) {
        return get("shared", defaultValue);
    }

    @Override
    public EntityConfig getParent() {
        return EntityRepository.instance().get(parent);
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }
}
