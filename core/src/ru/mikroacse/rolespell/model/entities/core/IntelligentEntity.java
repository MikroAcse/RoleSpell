package ru.mikroacse.rolespell.model.entities.core;

import ru.mikroacse.rolespell.model.entities.components.ai.AiComponent;

import java.util.ArrayList;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public interface IntelligentEntity {
    ArrayList<AiComponent> getAi();
}
