package ru.mikroacse.rolespell.model.world.cells;

import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Position;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class CellChecker {
    public abstract boolean check(World world, int x, int y);

    public boolean check(World world, Position position) {
        return check(world, position.x, position.y);
    }
}
