package ru.mikroacse.engine.config.providers;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * Created by MikroAcse on 24.06.2017.
 */
public class YamlProvider implements ConfigurationProvider {
    private Map<String, Object> map;

    public YamlProvider(Reader reader) {
        map = (Map) new Yaml().load(reader);
    }

    public YamlProvider(String yaml) {
        map = (Map) new Yaml().load(yaml);
    }

    public YamlProvider(InputStream stream) {
        map = (Map) new Yaml().load(stream);
    }

    public Map<String, Object> get() {
        return map;
    }
}
