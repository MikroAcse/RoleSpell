package ru.mikroacse.rolespell.app.model.game.world.cells;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class CellChecker {
    public abstract boolean check(World world, int x, int y);
    
    public boolean check(World world, IntVector2 position) {
        return check(world, position.x, position.y);
    }
}
