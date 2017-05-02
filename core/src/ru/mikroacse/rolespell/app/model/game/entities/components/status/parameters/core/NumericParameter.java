package ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core;

import ru.mikroacse.engine.listeners.Listener;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.ParameterType;
import ru.mikroacse.engine.util.LimitedDouble;

/**
 * Created by MikroAcse on 09-Apr-17.
 */
public abstract class NumericParameter extends Parameter {
    protected LimitedDouble value;
    protected double speed;
    
    public NumericParameter(StatusComponent status, ParameterType type) {
        super(status, type);
        
        value = new LimitedDouble();
        value.setMin(0);
    }
    
    @Override
    public boolean update(float delta) {
        value.setValue(value.getValue() + speed * delta);
        
        return super.update(delta);
    }
    
    public LimitedDouble getValue() {
        return value;
    }
    
    public void setValue(double value) {
        this.value.setValue(value);
    }
    
    public double getCurrentValue() {
        return value.getValue();
    }
    
    public double getPercentage() {
        return value.getPercentage();
    }
    
    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
    
    }
}
