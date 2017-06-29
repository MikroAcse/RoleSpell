package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.config.ConfigurationRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 29.06.2017.
 */
public class EntityRepository extends ConfigurationRepository<String, EntityConfig> {
    private static EntityRepository instance = new EntityRepository();

    public static EntityRepository instance() {
        return instance;
    }
}
