package ru.mikroacse.engine.config.providers;

import java.util.Map;

/**
 * Created by MikroAcse on 24.06.2017.
 */
public interface ConfigurationProvider {
    Map<String, Object> get();
}
