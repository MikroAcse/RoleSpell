package ru.mikroacse.rolespell.app.model.game.entities.components.status.properties;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class ExperienceProperty extends Property {
    public ExperienceProperty(StatusComponent status, Interval interval) {
        super(status, PropertyType.EXPERIENCE, interval);
    }
}
