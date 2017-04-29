package ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters;

import ru.mikroacse.rolespell.screens.game.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.screens.game.model.entities.components.status.parameters.core.Parameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public enum ParameterType {
    HEALTH,
    MANA,
    STAMINA,
    EXPERIENCE,
    DAMAGE;
    
    public static Parameter create(StatusComponent status, ParameterType type) {
        Parameter parameter = null;
        
        switch (type) {
            case HEALTH:
                parameter = new HealthParameter(status);
                break;
            case MANA:
                parameter = new ManaParameter(status);
                break;
            case STAMINA:
                parameter = new StaminaParameter(status);
                break;
            case EXPERIENCE:
                parameter = new ExperienceParameter(status);
                break;
            case DAMAGE:
                parameter = new DamageParameter(status);
                break;
        }
        
        return parameter;
    }
}
