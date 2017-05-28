package ru.mikroacse.rolespell.parsers;

import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Timer;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class TimerParser {
    public static Timer parse(JsonValue config) {
        Interval interval = IntervalParser.parse(config);

        boolean randomized = config.getBoolean("randomized", false);
        float speed = config.getFloat("speed", 1f);

        return new Timer(interval, randomized, speed);
    }
}
