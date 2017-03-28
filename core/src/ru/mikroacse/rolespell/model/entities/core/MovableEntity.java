package ru.mikroacse.rolespell.model.entities.core;

import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public interface MovableEntity {
    MovementComponent getMovementComponent();
}
