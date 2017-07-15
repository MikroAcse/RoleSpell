package ru.mikroacse.rolespell.app.model.game.world.cells;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.model.game.world.WorldMap;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class PassableCellWeigher extends CellWeigher {
    private boolean checkEntities;

    public PassableCellWeigher(boolean checkEntities) {
        this.checkEntities = checkEntities;
    }

    @Override
    public double weigh(World world, int x, int y) {
        WorldMap map = world.getMap();

        if (!map.isPassable(x, y)) {
            return -1;
        }

        if (checkEntities) {
            Array<Entity> entities = world.getEntitiesAt(x, y);

            for (Entity entity : entities) {
                if (entity.hasParameter(Entity.Parameter.SOLID)) {
                    // TODO: 'weigh' entity?
                    return -1;
                }
            }
        }

        return world.getMap().getWeight(x, y);
    }
}
