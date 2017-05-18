package ru.mikroacse.rolespell.app.model.game.entities.components.status.properties;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.core.NumericProperty;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class ManaProperty extends NumericProperty {
    public ManaProperty(StatusComponent status, Interval interval, double speed) {
        super(status, PropertyType.MANA, interval, speed);
    }
}
