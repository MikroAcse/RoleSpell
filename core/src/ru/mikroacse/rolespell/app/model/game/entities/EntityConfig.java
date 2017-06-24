package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.engine.config.ConfigurationNode;

/**
 * Created by MikroAcse on 24.06.2017.
 */
public class EntityConfig {
    public static final String PICKUP_DISTANCE = "pickup_distance";

    private ConfigurationNode config;

    public EntityConfig(ConfigurationNode config) {
        this.config = config;
    }

    public ConfigurationNode get() {
        return config;
    }

    public void set(ConfigurationNode config) {
        this.config = config;
    }
}
