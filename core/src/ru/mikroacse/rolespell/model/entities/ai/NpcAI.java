package ru.mikroacse.rolespell.model.entities.ai;

import ru.mikroacse.rolespell.model.entities.ai.core.EntityAI;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by MikroAcse on 26.03.2017.
 */
public class NpcAI extends EntityAI {
    private Random random;

    public NpcAI(Entity entity) {
        super(entity);

        updateInterval = 5f;
        random = new Random();
    }

    @Override
    public void update() {
        World world = entity.getWorld();
        int x = entity.getSupposedX();
        int y = entity.getSupposedY();

        ArrayList<Point> passableCells = world.getPassableCells(x, y, 0, 10, true);

        if(!passableCells.isEmpty()) {
            Point pos = passableCells.get(random.nextInt(passableCells.size()));

            LinkedList<Point> path = world.getPath(new Point(entity.x, entity.y), pos, 5);

            if(!path.isEmpty()) {
                entity.addToPath(path);
            }
        }
    }
}
