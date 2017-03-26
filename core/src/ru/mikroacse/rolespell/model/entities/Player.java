package ru.mikroacse.rolespell.model.entities;

import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.EntityType;
import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class Player extends Entity {
    public Player(int x, int y, World world) {
        super(x, y, EntityType.PLAYER, world);
    }

    public Player() {
        this(0, 0, null);
    }

    @Override
    protected void initialize() {
        super.initialize();

        movingSpeed = 10f;
    }
}
