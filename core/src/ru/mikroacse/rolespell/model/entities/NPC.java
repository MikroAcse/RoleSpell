package ru.mikroacse.rolespell.model.entities;

import ru.mikroacse.rolespell.model.entities.ai.NpcAI;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.entities.core.EntityType;
import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public class Npc extends Entity {
    public Npc(int x, int y, World world) {
        super(x, y, EntityType.NPC, world);
    }

    public Npc() {
        this(0, 0, null);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    protected void initialize() {
        super.initialize();

        movingSpeed = 4f;

        ai = new NpcAI(this);
    }
}
