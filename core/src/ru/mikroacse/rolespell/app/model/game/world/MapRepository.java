package ru.mikroacse.rolespell.app.model.game.world;

import ru.mikroacse.rolespell.app.model.game.world.config.MapConfig;
import ru.mikroacse.rolespell.config.ConfigurationRepository;

/**
 * Created by MikroAcse on 29.06.2017.
 */
public class MapRepository extends ConfigurationRepository<String, MapConfig> {
    public static final MapRepository instance = new MapRepository();
}
