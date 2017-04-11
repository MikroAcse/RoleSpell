package ru.mikroacse.rolespell.model.entities.components.status;

import ru.mikroacse.rolespell.model.entities.components.core.Component;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.core.Parameter;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.ParameterType;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikroAcse on 03.04.2017.
 */
// TODO: separate statues for flexibility[, love and trust]
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

    public void add(Parameter parameter) {
        parameters.add(parameter);
    }

    public Parameter remove(int index) {
        return parameters.remove(index);
    }

    public boolean remove(Parameter parameter) {
        return parameters.remove(parameter);
    }

    public Parameter remove(ParameterType type) {
        for (Parameter parameter : parameters) {
            if (parameter.getType() == type) {
                return parameter;
            }
        }
        return null;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public void dispose() {

    }
}
