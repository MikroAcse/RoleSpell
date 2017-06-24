package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.engine.config.JsonConfiguration;

/**
 * Created by MikroAcse on 24.06.2017.
 */
public class EntityConfig {
    public static final String PICKUP_DISTANCE = "pickup_distance";

    private JsonConfiguration config;

    public EntityConfig(JsonConfiguration config) {
        this.config = config;
    }

    public JsonConfiguration get() {
        return config;
    }

    public void set(JsonConfiguration config) {
        this.config = config;
    }
}
