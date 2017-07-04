package ru.mikroacse.rolespell.app.model.game.entities.mobs.monsters;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.CollisionAvoidingAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.DamageProperty;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.HealthProperty;
import ru.mikroacse.rolespell.app.model.game.entities.mobs.Mob;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 11-May-17.
 */
public class Ogremagi extends Mob {

    public Ogremagi(World world, int x, int y) {
        super(EntityType.OGREMAGI, world, x, y, 1f);

        getParameters().add(Parameter.VULNERABLE);

        getStatus().addProperty(new HealthProperty(getStatus(),
                new Interval(0, 400, 400),
                3));

        // TODO: this is bad
        getStatus().addProperty(new DamageProperty(
                getStatus(),
                new Interval(10.0, 20.0),
                1));

        // TODO: magic numbers everywhere

        CollisionAvoidingAi collisionAvoidingAi = new CollisionAvoidingAi(this, 1, 2, false);
        addComponent(collisionAvoidingAi);

        AttackAi attackAi = new AttackAi(this, new Timer(new Interval(1.0, 3.0)));
        addComponent(attackAi);

        attackAi.setTargetTypes(EnumSet.of(EntityType.PLAYER));

        attackAi.setTargetSelectors(BehaviorAi.TargetSelector.ALL);
    }
}
