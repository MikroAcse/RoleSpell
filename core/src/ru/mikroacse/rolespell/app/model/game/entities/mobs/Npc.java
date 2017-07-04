package ru.mikroacse.rolespell.app.model.game.entities.mobs;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.CollisionAvoidingAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.DamageProperty;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.HealthProperty;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public class Npc extends Mob {
    private AttackAi attackAi;

    public Npc(World world, int x, int y) {
        super(EntityType.NPC, world, x, y, 4f);

        setParameters(EnumSet.of(Parameter.SOLID));

        getStatus().addProperty(new HealthProperty(getStatus(),
                new Interval(0, 100, 100),
                3));

        // TODO: this is bad
        getStatus().addProperty(new DamageProperty(
                getStatus(),
                new Interval(3.0, 10.0),
                1));

        // TODO: magic numbers everywhere

        CollisionAvoidingAi collisionAvoidingAi = new CollisionAvoidingAi(this, 1, 2, false);
        addComponent(collisionAvoidingAi);
    }
}
