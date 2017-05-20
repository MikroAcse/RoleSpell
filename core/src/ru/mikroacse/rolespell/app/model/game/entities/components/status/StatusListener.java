package ru.mikroacse.rolespell.app.model.game.entities.components.status;

import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.Property;

/**
 * Created by Vitaly Rudenko on 20-May-17.
 */
public class StatusListener implements StatusComponent.Listener {
    @Override
    public void propertyUpdated(StatusComponent status, Property property, double previousValue, double currentValue) {
        updated(status);
    }

    @Override
    public void propertyAdded(StatusComponent status, Property property) {
        updated(status);
    }

    @Override
    public void propertyRemoved(StatusComponent status, Property property) {
        updated(status);
    }

    public void updated(StatusComponent status) {

    }
}
