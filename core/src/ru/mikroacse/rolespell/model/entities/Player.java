package ru.mikroacse.rolespell.model.entities;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.EntityType;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class Player extends Entity {
    public Player(int x, int y) {
        super(x, y, EntityType.PLAYER);
    }

    public Player() {
        this(0, 0);
    }
}
