package ru.mikroacse.rolespell.app.model.game.entities.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.RecursiveConfigurationNode;

/**
 * Created by MikroAcse on 24.06.2017.
 */
public class EntityConfig extends RecursiveConfigurationNode<EntityConfig> {
    // default values
    private static final double PICKUP_DISTANCE = 2;

    public EntityConfig(ConfigurationNode node, EntityConfig parent) {
        super(node, parent);
    }

    public double getPickupDistance() {
        return get("pickup_distance", PICKUP_DISTANCE);
    }
}
