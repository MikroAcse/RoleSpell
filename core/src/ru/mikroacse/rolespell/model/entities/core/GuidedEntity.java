package ru.mikroacse.rolespell.model.entities.core;

import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public interface GuidedEntity extends MovableEntity {
    PathMovementComponent getMovementComponent();
}
