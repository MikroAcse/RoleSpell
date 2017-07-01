package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.config.ConfigurationRepository;

/**
 * Created by MikroAcse on 29.06.2017.
 */
public class EntityRepository extends ConfigurationRepository<String, EntityConfig> {
    private static EntityRepository instance = new EntityRepository();

    public static EntityRepository instance() {
        return instance;
    }
}
