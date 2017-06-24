package ru.mikroacse.rolespell.parsers;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Timer;

import java.util.Map;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class TimerParser {
    public static Timer parse(Object config) {
        Interval interval = IntervalParser.parse(config);

        float speed = 1f;

        if (config instanceof ConfigurationNode) {
            speed = ((ConfigurationNode) config).getFloat("speed", speed);

        } else if (config instanceof Map) {

            speed = (float) ((Map) config).getOrDefault("speed", speed);
        }

        return new Timer(interval, speed);
    }
}
