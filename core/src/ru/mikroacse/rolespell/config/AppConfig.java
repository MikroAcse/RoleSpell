package ru.mikroacse.rolespell.config;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.providers.YamlProvider;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by MikroAcse on 29-Apr-17.
 */
public final class AppConfig extends ConfigurationNode {
    public AppConfig() throws FileNotFoundException {
        super(new YamlProvider(new FileReader("config.yaml")));
    }
}
