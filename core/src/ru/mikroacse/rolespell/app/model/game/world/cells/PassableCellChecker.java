package ru.mikroacse.rolespell.app.model.game.world.cells;

import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class PassableCellChecker extends CellChecker {
    private boolean checkEntities;
    
    public PassableCellChecker(boolean checkEntities) {
        this.checkEntities = checkEntities;
    }
    
    @Override
    public boolean check(World world, int x, int y) {
        if (checkEntities && !world.getEntitiesAt(x, y).isEmpty()) {
            return false;
        }
        
        return world.getMeta(x, y) != World.Meta.SOLID;
    }
}
