package ru.mikroacse.rolespell.model.world.cells;

import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class PassableCellChecker extends CellChecker {
    @Override
    public boolean check(World world, int x, int y) {
        return world.getMeta(x, y) != World.Meta.SOLID;
    }
}
