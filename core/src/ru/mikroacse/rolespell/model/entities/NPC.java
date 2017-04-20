package ru.mikroacse.rolespell.model.entities;

import com.badlogic.gdx.graphics.Texture;
import ru.mikroacse.rolespell.model.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.model.entities.components.ai.CollisionAvoidingAi;
import ru.mikroacse.rolespell.model.entities.components.ai.behaviors.AttackBehavior;
import ru.mikroacse.rolespell.model.entities.components.ai.behaviors.SeekBehavior;
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
    private BehaviorAi behaviorAi;
    private CollisionAvoidingAi collisionAvoidingAi;

    private PathMovementComponent movement;
    private TextureDrawableComponent drawable;
    private StatusComponent status;

    public Npc(World world, int x, int y) {
        super(EntityType.NPC, world);

        status = new StatusComponent(this);

        status.addParameter(new HealthParameter(status));

        // TODO: this is bad
        status.addParameter(new DamageParameter(
                status,
                new LimitedDouble(3.0, 10.0),
                2,
                true) {
            @Override
            public boolean bump(Entity entity) {
                if (entity instanceof Player) {
                    return super.bump(entity);
                }
                return false;
            }
        });

        addComponent(status);

        // TODO: magic numbers everywhere

        movement = new PathMovementComponent(this, x, y, 4f);
        movement.setType(PathMovementComponent.UpdateType.POSITION);
        addComponent(movement);

        collisionAvoidingAi = new CollisionAvoidingAi(this, 1, 2, false);
        addComponent(collisionAvoidingAi);

        behaviorAi = new BehaviorAi(this, 20);
        addComponent(behaviorAi);

        /*behaviorAi.addBehavior(
                new WanderBehavior(
                        Priority.LOW,
                        WanderBehavior.Guide.POSITION,
                        2,
                        5,
                        new Interval(new LimitedDouble(4.0, 14.0), true)
                )
        );*/

        behaviorAi.addBehavior(
                new SeekBehavior(
                        Priority.NORMAL,
                        new Interval(2.0),
                        5
                )
        );

        behaviorAi.addBehavior(
                new AttackBehavior(
                        Priority.NORMAL,
                        new Interval(new LimitedDouble(3.0, 7.0), true)
                )
        );

        // TODO: Is it supposed to be in the View?!
        drawable = new TextureDrawableComponent(this, new Texture("data/npc.png"));
        addComponent(drawable);
    }

    public Npc(World world) {
        this(world, 0, 0);
    }

    @Override
    public void dispose() {
        behaviorAi.dispose();
        collisionAvoidingAi.dispose();

        movement.dispose();
        drawable.dispose();
        status.dispose();
    }
}
