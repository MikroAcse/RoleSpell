package ru.mikroacse.rolespell.app.model.game.entities.components.controllers;

import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusListener;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.HealthProperty;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.Property;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.PropertyType;

/**
 * Created by Vitaly Rudenko on 19-May-17.
 */
public class MobController extends Component {
    private Status status;

    private StatusListener statusListener;

    private Property.Listener healthListener;

    public MobController(Entity entity) {
        super(entity, true);

        status = Status.ALIVE;
    }

    @Override
    protected void initListeners() {
        statusListener = new StatusListener() {
            @Override
            public void propertyUpdated(StatusComponent status, Property property, double previousValue, double currentValue) {
                if(property.getType() == PropertyType.HEALTH) {
                    if(isAlive() && currentValue <= 0) {
                        die();
                    }
                }
            }
        };
    }

    public void die() {
        System.out.println(getEntity().getType() + " said: Im fuckin dead now.");
    }

    public boolean isAlive() {
        return status == Status.ALIVE;
    }

    public boolean isDead() {
        return status == Status.DEAD;
    }

    @Override
    protected void attachEntity(Entity entity) {
        super.attachEntity(entity);

        StatusComponent status = entity.getComponent(StatusComponent.class);

        status.addListener(statusListener);
    }

    @Override
    protected void detachEntity(Entity entity) {
        super.detachEntity(entity);

        StatusComponent status = entity.getComponent(StatusComponent.class);

        status.removeListener(statusListener);
    }

    public enum Status {
        ALIVE,
        DEAD
    }
}
