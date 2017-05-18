package ru.mikroacse.rolespell.app.model.game.world.cells;

import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class CellWeigher {
    public abstract double weigh(World world, int x, int y);
}
