package ru.mikroacse.rolespell.screens.game.model.entities.components.status;

import ru.mikroacse.rolespell.screens.game.model.entities.components.Component;
import ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters.core.Parameter;
import ru.mikroacse.rolespell.screens.game.model.entities.core.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikroAcse on 03.04.2017.
 */
// TODO: separate statuses for flexibility
public class StatusComponent extends Component {
    private List<Parameter> parameters;
    
    public StatusComponent(Entity entity) {
        super(entity);
        
        parameters = new ArrayList<>();
    }
    
    @Override
    public boolean update(float delta) {
        boolean updated = false;
        for (Parameter parameter : parameters) {
            updated |= parameter.update(delta);
        }
        
        return updated;
    }
    
    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }
    
    public boolean removeParameter(Parameter parameter) {
        return parameters.remove(parameter);
    }
    
    /**
     * @return True if status has at least one parameter of given class.
     */
    public boolean hasParameter(Class<? extends Component> parameterClass) {
        for (Parameter parameter : parameters) {
            if (parameterClass.isInstance(parameter)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return First matching status parameter of given class.
     */
    public <T extends Parameter> T getParameter(Class<T> parameterClass) {
        for (Parameter parameter : parameters) {
            if (parameterClass.isInstance(parameter)) {
                return (T) parameter;
            }
        }
        return null;
    }
    
    /**
     * @return All status parameters of given class.
     */
    public <T extends Parameter> List<T> getParameters(Class<T> parameterClass) {
        List<T> result = new ArrayList<T>();
        
        for (Parameter parameter : parameters) {
            if (parameterClass.isInstance(parameter)) {
                result.add((T) parameter);
            }
        }
        
        return result;
    }
    
    public List<Parameter> getParameters() {
        return parameters;
    }
    
    @Override
    public void dispose() {
    
    }
}
