package ru.mikroacse.rolespell.app.model.game.entities.components.controllers;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.Property;

/**
 * Created by Vitaly Rudenko on 20-May-17.
 */
public class MobListener implements MobController.Listener {
    @Override
    public void positionChanged(MobController controller, int prevX, int prevY, IntVector2 current) {

    }

    @Override
    public void originChanged(MobController controller, int prevX, int prevY, IntVector2 current) {

    }

    @Override
    public void died(MobController controller) {

    }

    @Override
    public void resurrected(MobController controller) {

    }

    @Override
    public void propertyUpdated(MobController controller, Property property, double previousValue, double currentValue) {

    }
}
