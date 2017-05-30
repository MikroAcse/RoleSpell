package ru.mikroacse.rolespell.parsers;

import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.engine.util.Interval;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class IntervalParser {
    public static Interval parse(JsonValue config) {
        Interval interval;

        if (config.isNumber()) {
            interval = new Interval(config.asDouble());
        } else {
            double min = config.getDouble("min");
            double max = config.getDouble("max");
            double value = config.getDouble("value", min);

            boolean randomized = config.getBoolean("randomized", !config.has("value"));

            interval = new Interval(min, max, value, randomized);
        }

        return interval;
    }
}
