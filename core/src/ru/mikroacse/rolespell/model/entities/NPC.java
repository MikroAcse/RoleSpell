package ru.mikroacse.rolespell.model.entities;

import com.badlogic.gdx.graphics.Texture;
import ru.mikroacse.rolespell.model.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.model.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.model.entities.components.ai.CollisionAvoidingAi;
import ru.mikroacse.rolespell.model.entities.components.ai.behaviors.SeekBehavior;
import ru.mikroacse.rolespell.model.entities.components.ai.behaviors.WanderBehavior;
import ru.mikroacse.rolespell.model.entities.components.drawable.TextureDrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.DamageParameter;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.HealthParameter;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.LimitedDouble;
import ru.mikroacse.util.Priority;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public class Npc extends Entity {
    private RandomMovementAi randomMovementAi;
    private CollisionAvoidingAi collisionAvoidingAi;
    private AttackAi attackAi;

    private PathMovementComponent movement;
    private TextureDrawableComponent drawable;
    private StatusComponent status;

    public Npc(World world, int x, int y) {
        super(EntityType.NPC, world);


        status = new StatusComponent(this);

        status.add(new HealthParameter(status));

        DamageParameter damage = new DamageParameter(status, new LimitedDouble(5), 1, 2);
        status.add(damage);

        addComponent(status);

        // TODO: magic numbers everywhere

        movement = new PathMovementComponent(this, x, y, 4f);
        movement.setType(PathMovementComponent.UpdateType.POSITION);
        addComponent(movement);

        collisionAvoidingAi = new CollisionAvoidingAi(this, 1, 2, false);
        addComponent(collisionAvoidingAi);

        BehaviorAi behaviorAi = new BehaviorAi(this, 3, 20);
        addComponent(behaviorAi);

        behaviorAi.addBehavior(
                new WanderBehavior(
                        Priority.LOW,
                        WanderBehavior.Guide.POSITION,
                        2, 5,
                        new Interval(new LimitedDouble(4.0, 14.0), true)
                )
        );

        behaviorAi.addBehavior(new SeekBehavior(Priority.NORMAL, new Interval(1.0)));

        // TODO: Is it supposed to be in the View?!
        drawable = new TextureDrawableComponent(this, new Texture("data/npc.png"));
        addComponent(drawable);
    }

    public Npc(World world) {
        this(world, 0, 0);
    }

    @Override
    public void dispose() {
        randomMovementAi.dispose();
        collisionAvoidingAi.dispose();

        movement.dispose();
        drawable.dispose();
        status.dispose();
    }
}
