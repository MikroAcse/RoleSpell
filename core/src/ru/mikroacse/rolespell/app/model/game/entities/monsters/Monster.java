package ru.mikroacse.rolespell.app.model.game.entities.monsters;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.CollisionAvoidingAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.DamageProperty;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.HealthProperty;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 11-May-17.
 */
public class Monster extends Entity {
    private CollisionAvoidingAi collisionAvoidingAi;
    private AttackAi attackAi;

    private PathMovementComponent movement;
    private StatusComponent status;

    public Monster(World world, String name, int x, int y) {
        super(EntityType.MONSTER, world, name);

        setParameters(EnumSet.of(Parameter.SOLID, Parameter.VULNERABLE));

        status = new StatusComponent(this);

        status.addParameter(new HealthProperty(status,
                new Interval(0, 100, 100),
                3));

        // TODO: this is bad
        status.addParameter(new DamageProperty(
                status,
                new Interval(20.0, 30.0),
                3,
                true));

        addComponent(status);

        // TODO: magic numbers everywhere

        movement = new PathMovementComponent(this, x, y, 2.5f);
        movement.setType(PathMovementComponent.UpdateType.POSITION);
        addComponent(movement);

        collisionAvoidingAi = new CollisionAvoidingAi(this, 1, 2, false);
        addComponent(collisionAvoidingAi);

        attackAi = new AttackAi(this, new Timer(new Interval(1.0, 1.0), true));
        addComponent(attackAi);

        attackAi.setTargetTypes(EnumSet.of(EntityType.PLAYER));

        attackAi.setTargetSelectors(BehaviorAi.TargetSelector.ALL);
    }

    @Override
    public void setPosition(int x, int y) {
        movement.setPosition(x, y);
    }

    @Override
    public IntVector2 getPosition() {
        return movement.getPosition();
    }

    @Override
    public void setOrigin(int x, int y) {
        movement.setOrigin(x, y);
    }

    @Override
    public IntVector2 getOrigin() {
        return movement.getOrigin();
    }
}
