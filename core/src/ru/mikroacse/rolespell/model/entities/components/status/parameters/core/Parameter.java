package ru.mikroacse.rolespell.model.entities.components.status.parameters.core;

import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.ParameterType;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

import java.util.Observable;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public abstract class Parameter extends Observable {
    private StatusComponent status;
    private ParameterType type;

    public Parameter(StatusComponent status, ParameterType type) {
        this.status = status;
        this.type = type;
    }

    public abstract boolean update(float delta);

    public ParameterType getType() {
        return type;
    }

    public StatusComponent getStatus() {
        return status;
    }
}
