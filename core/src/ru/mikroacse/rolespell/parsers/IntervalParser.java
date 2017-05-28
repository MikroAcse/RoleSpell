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
            interval = new Interval(config.getDouble("min"), config.getDouble("max"));

            if (config.has("value")) {
                interval.setValue(config.getDouble("value"));
            }
        }

        return interval;
    }
}
