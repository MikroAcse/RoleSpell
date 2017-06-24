package ru.mikroacse.rolespell.parsers;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.util.Interval;

import java.util.Map;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class IntervalParser {
    public static Interval parse(Object value) {
        if (value instanceof Number) {
            return new Interval(((Number) value).doubleValue());
        }

        ConfigurationNode node;

        if (value instanceof ConfigurationNode) {
            node = (ConfigurationNode) value;
        } else if (value instanceof Map) {
            node = new ConfigurationNode((Map) value);
        } else {
            throw new IllegalArgumentException("Can only parse numbers or nodes");
        }

        double min = node.getDouble("min");
        double max = node.getDouble("max");
        double val = node.getDouble("value", min);

        boolean randomized = node.getBoolean("randomized", !node.has("value"));

        return new Interval(min, max, val, randomized);
    }
}
