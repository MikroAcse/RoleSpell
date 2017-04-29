package ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters.core;

import ru.mikroacse.rolespell.screens.game.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters.ParameterType;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public abstract class Parameter {
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
